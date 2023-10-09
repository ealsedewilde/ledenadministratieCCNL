package nl.ealse.ccnl.control.settings;

import javafx.fxml.FXML;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.service.SettingsService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class SettingsController extends SettingsView {

  private final ApplicationEventPublisher eventPublisher;

  private final PageController pageController;

  private final SettingsService service;

  @FXML
  private TableView<Setting> tableView;

  public SettingsController(SettingsService service, PageController pageController,
      ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
    this.pageController = pageController;
    this.service = service;
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
      refresh("Instelling is opgeslagen; actief na herstart");
    }
  }

  public void update(Setting selectedSettings, String oldId) {
    service.save(selectedSettings, oldId);
    refresh("Instelling is bijgewerkt; actief na herstart");
  }

  public void delete(Setting selectedSettings) {
    service.delete(selectedSettings);
    refresh("Instelling is verwijderd; actief na herstart");
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
    eventPublisher.publishEvent(se);
  }

  @EventListener(condition = "#event.name('SETTINGS')")
  public void findSettings(MenuChoiceEvent event) {
    pageController.setActivePage(PageName.SETTINGS);
    tableView.getItems().clear();
    tableView.getItems().addAll(service.findByOrderBySettingsGroupAscKeyAsc());
  }

}
