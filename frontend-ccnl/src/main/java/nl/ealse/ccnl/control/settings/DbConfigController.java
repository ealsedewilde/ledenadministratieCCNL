package nl.ealse.ccnl.control.settings;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nl.ealse.ccnl.MainStage;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.database.config.BaseDbConfigurator;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.javafx.FXMLLoaderBean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class DbConfigController extends BaseDbConfigurator{
  
  private final PageController pageController;

  public DbConfigController(PageController pageController) {
    super(new Stage());
    this.pageController = pageController;
  }

  /**
   * Reconfigure the database location.
   *
   * @param event
   */
  @EventListener(condition = "#event.name('DB_CONFIG')")
  public void dbConfig(MenuChoiceEvent event) {
    if (!getStage().isShowing()) {
      getStage().initOwner(MainStage.getStage());
      getStage().initModality(Modality.APPLICATION_MODAL);
    }
    openDialog();
  }

  @Override
  protected Parent loadFxml(String fxmlName) {
    return FXMLLoaderBean.getPage(fxmlName, this);
  }

  @Override
  protected Image getStageIcon() {
    return MainStage.getIcon();
  }

  @Override
  protected void nextAction() {
    getStage().close();
    pageController.showMessage("Database locatie is aangepast; actief na herstart");
  }

}
