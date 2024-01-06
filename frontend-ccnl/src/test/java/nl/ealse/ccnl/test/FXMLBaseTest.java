package nl.ealse.ccnl.test;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.support.EventProcessor;
import nl.ealse.javafx.FXMLLoadException;

/**
 * Non caching FXML loading for unit-tests.
 *
 * @author ealse
 *
 * @param <T>
 */
@Slf4j
public abstract class FXMLBaseTest extends FXBase {

  private static final String FXML_DIR = "/fxml/";
  
  static {
    EventProcessor.getInstance().initialize();
  }

  private static final PageController pc = MockProvider.mock(PageController.class);

  /**
   * Return a mockito spy. Use it when working with a form.
   *
   * @return
   */
  protected PageController getPageController() {
    return pc;
  }

  /**
   * Loading a form without caching.
   *
   * @param controller
   * @param pageName
   * @return
   * @throws FXMLMissingException
   */
  protected Parent getPageWithFxController(Object controller, PageName pageName) {
    return getPage(controller, pageName, true);
  }

  /**
   * Loading a form without caching.
   *
   * @param controller
   * @param pageName
   * @return
   * @throws FXMLMissingException
   */
  protected Parent getPageWithoutFxController(Object controller, PageName pageName) {
    return getPage(controller, pageName, false);
  }

  /**
   * Loading a form without caching.
   *
   * @param controller
   * @param pageName
   * @return
   * @throws FXMLMissingException
   */
  private Parent getPage(Object controller, PageName pageName, boolean factory) {
    URL pageUrl = getClass().getResource(FXML_DIR + pageName.getFxmlName() + ".fxml");
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(pageUrl);
      if (factory) {
        fxmlLoader.setControllerFactory(param -> {
          return controller;
        });
      } else {
        fxmlLoader.setController(controller);
      }
      //log.info(fxmlLoader.getLocation().toString());
      Parent page = fxmlLoader.load();
      return page;
    } catch (IOException e) {
      log.error("error loading fxml", e);
      throw new FXMLLoadException("error loading fxml", e);
    }
  }



}
