package nl.ealse.ccnl.control.settings;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nl.ealse.ccnl.MainStage;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.javafx.FXMLLoaderUtil;

public class SettingsEdit extends SettingsView {

  private final SettingsController controller;

  private Setting selectedSetting;

  private Stage editStage;

  public SettingsEdit(SettingsController controller) {
    this.controller = controller;
    setup();
  }

  private void setup() {
    editStage = new Stage();
    editStage.initModality(Modality.APPLICATION_MODAL);
    editStage.setTitle("Instelling wijzigen");
    editStage.getIcons().add(MainStage.getIcon());
    editStage.initOwner(MainStage.getStage());
    Parent p = FXMLLoaderUtil.getPage("settings/settingsEdit", this);
    Scene dialogScene = new Scene(p, 1105, 400);
    editStage.setScene(dialogScene);
  }

  @FXML
  void update() {
    if (valid()) {
      selectedSetting.setKey(getKey().getText());
      selectedSetting.setValue(getValue().getText());
      selectedSetting.setDescription(getDescription().getText());
      selectedSetting.setSettingsGroup(getGroup().getText());
      String oldId = selectedSetting.getId();
      controller.update(selectedSetting, oldId);
      editStage.close();
    }
  }

  @FXML
  void delete() {
    controller.delete(selectedSetting);
    editStage.close();
  }

  @FXML
  void reset() {
    getKey().setText(selectedSetting.getKey());
    getValue().setText(selectedSetting.getValue());
    getGroup().setText(selectedSetting.getSettingsGroup());
    getDescription().setText(selectedSetting.getDescription());

    getKeyE().setVisible(false);
    getValueE().setVisible(false);
  }

  @EventListener
  public void onApplicationEvent(SettingSelectionEvent event) {
    selectedSetting = event.getSelectedSetting();
    reset();
    if (!editStage.isShowing()) {
      editStage.show();
    }

  }

}
