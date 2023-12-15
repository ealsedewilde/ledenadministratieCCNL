package nl.ealse.ccnl.event.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import nl.ealse.ccnl.control.menu.ChoiceGroup;
import nl.ealse.ccnl.control.menu.MenuChoice;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventListener {
  
  Class<?> eventClass() default Object.class;
  
  MenuChoice menuChoice() default MenuChoice.LOGO;
  
  ChoiceGroup choiceGroup() default ChoiceGroup.UNKNOWN;
  
  boolean command() default false;

}
