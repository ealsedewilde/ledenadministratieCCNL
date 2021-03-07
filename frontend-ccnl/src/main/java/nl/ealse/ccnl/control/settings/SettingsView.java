package nl.ealse.ccnl.control.settings;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;

@Getter
public class SettingsView {

  @FXML
  private TextField key;

  @FXML
  private Label keyE;

  @FXML
  private TextField value;

  @FXML
  private Label valueE;

  @FXML
  private TextField group;

  @FXML
  private TextField description;

  public boolean valid() {
    boolean valid = true;
    if (key.getText() == null || key.getText().isBlank()) {
      keyE.setVisible(true);
      valid = false;
    } else {
      keyE.setVisible(false);
    }
    if (value.getText() == null || value.getText().isBlank()) {
      valueE.setVisible(true);
      valid = false;
    } else {
      valueE.setVisible(false);
    }
    return valid;
  }

}
