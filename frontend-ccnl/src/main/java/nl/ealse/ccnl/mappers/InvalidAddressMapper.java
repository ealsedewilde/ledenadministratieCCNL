package nl.ealse.ccnl.mappers;

import javafx.scene.control.Label;
import nl.ealse.javafx.mappers.PropertyMapper;

public class InvalidAddressMapper implements PropertyMapper<Label, Boolean> {

  private static final String TEXT = "(Let op: Adres is onjuist)";

  @Override
  public Boolean getPropertyFromJavaFx(Label javaFx) {
    return Boolean.valueOf(TEXT.equals(javaFx.getText()));
  }

  @Override
  public void mapPropertyToJavaFx(Boolean modelProperty, Label javaFx) {
    if (modelProperty.booleanValue()) {
      javaFx.setText(TEXT);
    } else {
      javaFx.setText("");
    }
  }

}
