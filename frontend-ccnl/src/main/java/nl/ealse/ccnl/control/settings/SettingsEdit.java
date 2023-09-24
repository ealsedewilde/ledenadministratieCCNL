package nl.ealse.ccnl.control.settings;

import javafx.fxml.FXML;
import lombok.Getter;
import lombok.Setter;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class SettingsEdit extends SettingsView {

  @Getter
  private final SettingsController controller;

  @Setter
  private Setting selectedSettings;

  public SettingsEdit(SettingsController parentController) {
    this.controller = parentController;
  }

  @FXML
  void update() {
    if (valid()) {
      String oldId = selectedSettings.getId();
      selectedSettings.setKey(getKey().getText());
      selectedSettings.setValue(getValue().getText());
      selectedSettings.setDescription(getDescription().getText());
      selectedSettings.setSettingsGroup(getGroup().getText());
      controller.update(selectedSettings, oldId);
    }
  }

  @FXML
  void delete() {
    controller.delete(selectedSettings);
  }

  @FXML
  void reset() {
    getKey().setText(selectedSettings.getKey());
    getValue().setText(selectedSettings.getValue());
    getGroup().setText(selectedSettings.getSettingsGroup());
    getDescription().setText(selectedSettings.getDescription());

    getKeyE().setVisible(false);
    getValueE().setVisible(false);
  }

  @EventListener
  public void onApplicationEvent(SettingSelectionEvent event) {
    selectedSettings = event.getSelectedSetting();
    reset();
  }

}
