package nl.ealse.ccnl.event.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import nl.ealse.ccnl.control.menu.ChoiceGroup;
import nl.ealse.ccnl.control.menu.MenuChoice;

/**
 * Event target definition.
 * Only one of the attributes eventClass, menuChoice, choiceGroup is allowed.
 * All attributes are optional. The target method event parameter must be present when no
 * attributes are defined.
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventListener {

  /**
   * The event class to match by the {@link EventPublisher}.
   * This is only relevant when the target method has no event parameter.
   * @return
   */
  Class<?> eventClass() default Object.class;

  /**
   * The MenuChoice to match by the {@link EventPublisher}.
   * @return
   */
  MenuChoice menuChoice() default MenuChoice.LOGO;

  /**
   * The ChoiceGroup to match by the {@link EventPublisher}.
   * @return
   */
ChoiceGroup choiceGroup() default ChoiceGroup.UNKNOWN;

  /**
   * Defined at the target method that shows the default (logo) page.
   * Applicable when the MenuChoice has a target without a fxml page. 
   * @return
   */
  boolean command() default false;

}
