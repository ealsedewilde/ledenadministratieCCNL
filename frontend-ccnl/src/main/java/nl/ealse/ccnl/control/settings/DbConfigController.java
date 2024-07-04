package nl.ealse.ccnl.control.settings;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import nl.ealse.ccnl.MainStage;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.database.config.BaseDbConfigurator;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.javafx.FXMLLoaderUtil;

public class DbConfigController extends BaseDbConfigurator{
  
  private final PageController pageController;

   public DbConfigController(PageController pageController) {
    this.pageController = pageController;
    getConfigStage().initOwner(MainStage.getStage());
    getConfigStage().initModality(Modality.APPLICATION_MODAL);
 }

  /**
   * Reconfigure the database location.
   *
   * @param event
   */
  @EventListener(menuChoice = MenuChoice.DB_CONFIG)
  public void dbConfig(MenuChoiceEvent event) {
    openDialog();
  }

  @Override
  protected Parent loadFxml(String fxmlName) {
    return FXMLLoaderUtil.getPage(fxmlName, this);
  }

  @Override
  protected Image getStageIcon() {
    return MainStage.getIcon();
  }

  @Override
  protected void nextAction() {
    getConfigStage().close();
    pageController.showMessage("Database locatie is aangepast; actief na herstart");
  }

}
