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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.menu.ChoiceGroup;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.Index;
import org.jboss.jandex.IndexReader;
import org.jboss.jandex.MethodInfo;
import org.jboss.jandex.MethodParameterInfo;
import org.jboss.jandex.Type;

/**
 *
 */
@Slf4j
public class EventProcessor {

  @Getter
  private static final EventProcessor instance = new EventProcessor();

  private final Map<MenuChoice, EventContext> menuChoiceMapping = new EnumMap<>(MenuChoice.class);
  private final Map<ChoiceGroup, EventContext> choiceGroupMapping =
      new EnumMap<>(ChoiceGroup.class);

  /**
   * The key of this Map is the class of the event.
   */
  private final Map<String, EventContext> eventClassMapping = new HashMap<>();
  private final List<EventContext> commandMapping = new ArrayList<>();

  private EventProcessor() {}

  /**
   *
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

  private void processAnnotation(AnnotationInstance annotation) {
    MethodInfo method = annotation.target().asMethod();
    ClassInfo classInfo = method.declaringClass();
    String className = classInfo.name().toString();
    String methodName = method.name();
    EventContext eventContext = new EventContext(className, methodName);

    List<MethodParameterInfo> parms = method.parameters();
    if (!parms.isEmpty()) {
      MethodParameterInfo mpi = parms.get(0);
      Type parmType = mpi.type();
      String parmName = parmType.name().toString();
      eventContext.setParametersClassName(parmName);
    }

    List<AnnotationValue> values = annotation.values();
    values.forEach(value -> {
      switch (value.name()) {
        case "command":
          commandMapping.add(eventContext);
          break;
        case "eventClass":
          Type eventType = value.asClass();
          eventClassMapping.put(eventType.name().toString(), eventContext);
          break;
        case "choiceGroup":
          ChoiceGroup choiceGroup = ChoiceGroup.valueOf(value.asEnum());
          choiceGroupMapping.put(choiceGroup, eventContext);
          break;
        case "menuChoice":
        default:
          MenuChoice menuChoice = MenuChoice.valueOf(value.asEnum());
          menuChoiceMapping.put(menuChoice, eventContext);
          break;
      }

    });
    if (values.isEmpty()) {
      eventClassMapping.put(eventContext.getParametersClassName(), eventContext);
    }

  }

  /**
   *
   * @param event
   */
  public void processEvent(Object event) {
    if (event instanceof MenuChoiceEvent mce) {
      MenuChoice choice = mce.getMenuChoice();
      if (choice.isCommand()) {
        commandMapping.forEach(eventContext -> invokeMethod(event, eventContext));
      }
      if (mce.hasGroup()) {
        EventContext eventContext = choiceGroupMapping.get(choice.getGroup());
        invokeMethod(event, eventContext);
      } else {
        EventContext eventContext = menuChoiceMapping.get(choice);
        invokeMethod(event, eventContext);
      }
    } else {
      Class<?> eventClass = event.getClass();
      EventContext eventContext = eventClassMapping.get(eventClass.getName());
      invokeMethod(event, eventContext);
    }

  }

  private void invokeMethod(Object event, EventContext eventContext) {
    Object target = eventContext.getTargetObject(event);
    Method method = eventContext.getTargetMethod();
    try {
      if (eventContext.getParametersClassName() != null) {
        method.invoke(target, event);
      } else {
        method.invoke(target);
      }
    } catch (IllegalAccessException | InvocationTargetException e) {
      log.error("Unable to invoke targetMethod for event", e);
    }
  }

  @Getter
  @Setter
  private static class EventContext {

    private final String targetClassName;
    private final String targetMethodName;
    private String parametersClassName;

    @Setter(AccessLevel.NONE)
    private Object targetObject;

    @Setter(AccessLevel.NONE)
    private Method targetMethod;

    private EventContext(String targetClassName, String targetMethodName) {
      this.targetClassName = targetClassName;
      this.targetMethodName = targetMethodName;
    }

    public Object getTargetObject(Object event) {
      if (targetObject == null) {
        initializeTarget(event);
      }
      return targetObject;
    }

    public Method getTargetMethod() {
      return targetMethod;
    }

    private void initializeTarget(Object event) {
      try {
        Class<?> targetClass = Class.forName(targetClassName);
        targetObject = ApplicationContext.getComponent(targetClass);
        if (getParametersClassName() != null) {
          targetMethod = targetClass.getMethod(targetMethodName, event.getClass());
        } else {
          targetMethod = targetClass.getMethod(targetMethodName);
        }
      } catch (Exception e) {
        log.error("Unable to instantiate event target", e);
      }
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("targetClassName: ").append(targetClassName);
      sb.append(" targetMethodName: ").append(targetMethodName);
      sb.append(" parametersClassName: ").append(parametersClassName);
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
