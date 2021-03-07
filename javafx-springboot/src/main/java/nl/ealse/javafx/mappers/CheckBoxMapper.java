package nl.ealse.javafx.mappers;

import javafx.scene.control.CheckBox;

public class CheckBoxMapper implements PropertyMapper<CheckBox, Boolean> {

  @Override
  public Boolean getPropertyFromJavaFx(CheckBox javaFx) {
    return javaFx.isSelected();
  }

  @Override
  public void mapPropertyToJavaFx(Boolean modelProperty, CheckBox javaFx) {
    javaFx.setSelected(modelProperty);

  }

}
