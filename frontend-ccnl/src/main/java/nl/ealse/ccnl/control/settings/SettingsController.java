package nl.ealse.ccnl.control.settings;

import javafx.fxml.FXML;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.dd.service.DirectDebitAmountService;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.event.support.EventPublisher;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.service.SettingsService;

public class SettingsController extends SettingsView {

  private final PageController pageController;

  private final SettingsService service;
  
  private final DirectDebitAmountService ddaService;

  @FXML
  private TableView<Setting> tableView;

  public SettingsController(PageController pageController, SettingsService service, DirectDebitAmountService ddaService) {
    this.pageController = pageController;
    this.service = service;
    this.ddaService = ddaService;
  }

  @FXML
  void save() {
    if (valid()) {
      Setting setting = new Setting();
      setting.setKey(getKey().getText());
      setting.setSettingsGroup(getGroup().getText());
      setting.setValue(getValue().getText());
      setting.setDescription(getDescription().getText());
      service.save(setting);
      refresh("Instelling is opgeslagen");
    }
  }

  public void update(Setting selectedSetting, String oldId) {
    service.save(selectedSetting, oldId);
    ddaService.saveSetting(selectedSetting);
    refresh("Instelling is bijgewerkt");
  }

  public void delete(Setting selectedSettings) {
    service.delete(selectedSettings);
    refresh("Instelling is verwijderd");
  }

  private void refresh(String message) {
    pageController.showMessage(message);
    tableView.getItems().clear();
    tableView.getItems().addAll(service.findByOrderBySettingsGroupAscKeyAsc());
  }

  @FXML
  void editSetting(MouseEvent event) {
    event.consume();
    @SuppressWarnings("unchecked")
    TableRow<Setting> row = (TableRow<Setting>) event.getSource();
    Setting setting = row.getItem();
    SettingSelectionEvent se = new SettingSelectionEvent(this, setting);
    EventPublisher.publishEvent(se);
  }

  @EventListener(menuChoice = MenuChoice.SETTINGS)
  public void findSettings(MenuChoiceEvent event) {
    pageController.setActivePage(PageName.SETTINGS);
    tableView.getItems().clear();
    tableView.getItems().addAll(service.findByOrderBySettingsGroupAscKeyAsc());
  }

}
