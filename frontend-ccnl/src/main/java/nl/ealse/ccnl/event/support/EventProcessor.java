package nl.ealse.ccnl.event.support;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.menu.ChoiceGroup;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.config.ConfigurationException;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.Index;
import org.jboss.jandex.IndexReader;
import org.jboss.jandex.MethodInfo;
import org.jboss.jandex.MethodParameterInfo;
import org.jboss.jandex.Type;

/**
 * Processor that dispatches an event to its target.
 */
@Slf4j
public class EventProcessor {

  @Getter
  private static final EventProcessor instance = new EventProcessor();

  /**
   * Register of Event targets for a {@link MenuChoice}
   */
  private final Map<MenuChoice, TargetDefinition> menuChoiceMapping = new EnumMap<>(MenuChoice.class);
  
  /**
   * Register of Event targets for a {@link ChoiceGroup}
   */
  private final Map<ChoiceGroup, TargetDefinition> choiceGroupMapping =
      new EnumMap<>(ChoiceGroup.class);

  /**
   * Register of Event targets for a Class.
   * The key of this Map is the class (as string) of the event.
   */
  private final Map<String, TargetDefinition> eventClassMapping = new HashMap<>();
  
  /**
   * Register of Event targets that trigger the display of the logo page.
   */
  private final List<TargetDefinition> commandMapping = new ArrayList<>();

  private EventProcessor() {}

  /**
   * Build the complete Event target registry.
   */
  public void initialize() {
    try (InputStream input = getClass().getResourceAsStream("/META-INF/jandex.idx")) {
      IndexReader reader = new IndexReader(input);
      Index index = reader.read();
      Collection<AnnotationInstance> annotations = index.getAnnotations(EventListener.class);
      annotations.forEach(this::processAnnotation);
    } catch (IOException e) {
      log.error("Unable to initialize", e);
    }
  }

  /**
   * Register one Event target.
   * @param annotation
   */
  private void processAnnotation(AnnotationInstance annotation) {
    TargetDefinition tagetDefinition = new TargetDefinition(annotation);

    // inspect the attributes of the EventListener annotation
    List<AnnotationValue> values = annotation.values();
    values.forEach(value -> {
      switch (value.name()) {
        case "command":
          commandMapping.add(tagetDefinition);
          break;
        case "eventClass":
          Type eventType = value.asClass();
          eventClassMapping.put(eventType.name().toString(), tagetDefinition);
          break;
        case "choiceGroup":
          ChoiceGroup choiceGroup = ChoiceGroup.valueOf(value.asEnum());
          choiceGroupMapping.put(choiceGroup, tagetDefinition);
          break;
        case "menuChoice":
        default:
          MenuChoice menuChoice = MenuChoice.valueOf(value.asEnum());
          menuChoiceMapping.put(menuChoice, tagetDefinition);
          break;
      }

    });
    // Handle EventListener annotation with no attribute
    if (values.isEmpty()) {
      String parameterClassName = tagetDefinition.getParameterClassName();
      if (parameterClassName != null) {
        eventClassMapping.put(parameterClassName, tagetDefinition);
      } else {
        throw new ConfigurationException("missing eventClass attribute");
      }
    }

  }


  /**
   * Dispatch an event to its target.
   * @param event
   */
  public void processEvent(Object event) {
    if (event instanceof MenuChoiceEvent mce) {
      MenuChoice choice = mce.getMenuChoice();
      if (choice.isCommand()) {
        commandMapping.forEach(targetDefinition -> invokeMethod(event, targetDefinition));
      }
      if (mce.hasGroup()) {
        TargetDefinition targetDefinition = choiceGroupMapping.get(choice.getGroup());
        invokeMethod(event, targetDefinition);
      } else {
        TargetDefinition targetDefinition = menuChoiceMapping.get(choice);
        invokeMethod(event, targetDefinition);
      }
    } else {
      Class<?> eventClass = event.getClass();
      TargetDefinition targetDefinition = eventClassMapping.get(eventClass.getName());
      invokeMethod(event, targetDefinition);
    }

  }

  private void invokeMethod(Object event, TargetDefinition targetDefinition) {
    Object target = targetDefinition.getTargetObject(event);
    Method method = targetDefinition.getTargetMethod();
    try {
      if (targetDefinition.isWithParameter()) {
        method.invoke(target, event);
      } else {
        method.invoke(target);
      }
    } catch (IllegalAccessException | InvocationTargetException e) {
      log.error("Unable to invoke targetMethod for event", e);
    }
  }

  /**
   * Definition for event's target location.
   * It lazily initializes target class and target method.
   * Only a limited target locations are hit in a average user session.
   * So initialization will only be done when there is a hit of an incoming event.
   */
  private static class TargetDefinition {

    private final AnnotationInstance annotation;
    
    /**
     * Indication whether the target method has a (event) parameter.
     */
    @Getter
    private boolean withParameter;

    private Object targetObject;

    @Getter
    private Method targetMethod;

    private TargetDefinition(AnnotationInstance annotation) {
      this.annotation = annotation;
    }

    public Object getTargetObject(Object event) {
      if (targetObject == null) {
        initializeTarget(event);
      }
      return targetObject;
    }

    /**
     * Initialization is executes the first time an event hits this target.
     * @param event
     */
    private void initializeTarget(Object event) {
      MethodInfo method = annotation.target().asMethod();
      try {
        Class<?> targetClass = Class.forName(method.declaringClass().name().toString());
        targetObject = ApplicationContext.getComponent(targetClass);
        if (!method.parameters().isEmpty()) {
          targetMethod = targetClass.getMethod(method.name(), event.getClass());
          withParameter = true;
        } else {
          targetMethod = targetClass.getMethod(method.name());
        }
      } catch (Exception e) {
        log.error("Unable to instantiate event target", e);
      }
    }
    
    public String getParameterClassName() {
      MethodInfo method = annotation.target().asMethod();
      List<MethodParameterInfo> parms = method.parameters();
      if (!parms.isEmpty()) {
        MethodParameterInfo mpi = parms.get(0);
        Type parmType = mpi.type();
        return parmType.name().toString();
      }
      return null;
    }

    @Override
    public String toString() {
      MethodInfo method = annotation.target().asMethod();
      StringBuilder sb = new StringBuilder();
      sb.append("targetClassName: ").append(method.declaringClass().name().toString());
      sb.append(" targetMethodName: ").append(method.name());
      sb.append(" targetMethod parameterClassName: ").append(getParameterClassName());
      return sb.toString();
    }


  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("menuChoiceMapping").append(menuChoiceMapping.toString());
    sb.append("\nchoiceGroupMapping").append(choiceGroupMapping.toString());
    sb.append("\ncommandMapping").append(commandMapping.toString());
    sb.append("\neventClassMapping").append(eventClassMapping.toString());
    return sb.toString();
  }

}
