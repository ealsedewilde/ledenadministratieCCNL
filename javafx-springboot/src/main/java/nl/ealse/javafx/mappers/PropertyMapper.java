package nl.ealse.javafx.mappers;

import javafx.scene.control.Control;

/**
 * All PropertyMapper implementatiions must umplement this interface.
 * 
 * @author ealse
 *
 * @param <F> - the javafx control in the form
 * @param <M> - the property in the model
 */
public interface PropertyMapper<F extends Control, M> {

  M getPropertyFromJavaFx(F javaFx);

  void mapPropertyToJavaFx(M modelProperty, F javaFx);



}
