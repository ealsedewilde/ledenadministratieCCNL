package nl.ealse.ccnl.control.settings;

import jakarta.annotation.PostConstruct;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nl.ealse.ccnl.MainStage;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.javafx.FXMLLoaderBean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class SettingsEdit extends SettingsView {

  private final SettingsController controller;

  private Setting selectedSettings;
  
  private Stage editStage;


  public SettingsEdit(SettingsController parentController) {
    this.controller = parentController;
  }
  
  @PostConstruct
  void setup() {
    editStage = new Stage();
    editStage.initModality(Modality.APPLICATION_MODAL);
    editStage.setTitle("Instelling wijzigen");
    editStage.getIcons().add(MainStage.getIcon());
    editStage.initOwner(MainStage.getStage());
    Parent p = FXMLLoaderBean.getPage("settings/settingsEdit", this);
    Scene dialogScene = new Scene(p, 1105, 400);
    editStage.setScene(dialogScene);
  }

  @FXML
  void update() {
    if (valid()) {
      selectedSettings.setKey(getKey().getText());
      selectedSettings.setValue(getValue().getText());
      selectedSettings.setDescription(getDescription().getText());
      selectedSettings.setSettingsGroup(getGroup().getText());
      String oldId = selectedSettings.getId();
      controller.update(selectedSettings, oldId);
      editStage.close();
    }
  }

  @FXML
  void delete() {
    controller.delete(selectedSettings);
    editStage.close();
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
    if (!editStage.isShowing()) {
      editStage.show();
    }

  }

}
