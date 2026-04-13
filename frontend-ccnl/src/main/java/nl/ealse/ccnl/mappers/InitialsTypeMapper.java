package nl.ealse.ccnl.mappers;

import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import nl.ealse.ccnl.ledenadministratie.model.InitialsType;
import nl.ealse.javafx.mappers.PropertyMapper;
import nl.ealse.javafx.mapping.MappingException;

/**
 * Map the selected button from a ToggleGroup. This mapper has been bound to only one of the
 * RadioButtons of the ToggleGroup.
 */
public class InitialsTypeMapper implements PropertyMapper<RadioButton, InitialsType> {

  /**
   * Map the value of the selected RadioButton to the model.
   */
  @Override
  public InitialsType getPropertyFromJavaFx(RadioButton button) {
    for (Toggle t : button.getToggleGroup().getToggles()) {
      if (t.isSelected()) {
        return InitialsType.valueOf(((RadioButton) t).getId());
      }
    }
    // This should never happen.
    throw new MappingException("Unknown initials type:  " + button.getId());
  }

  /**
   * Set the bound RadioButton according the property in the model.
   */
  @Override
  public void mapPropertyToJavaFx(InitialsType modelProperty, RadioButton button) {
    InitialsType buttonType = InitialsType.valueOf(button.getId());
    button.setSelected(buttonType == modelProperty);
  }

}
