package nl.ealse.ccnl.test;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import java.io.IOException;
import java.lang.reflect.Field;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.javafx.FXMLLoadException;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.FXMLNodeMap;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Non caching FXML loading for unit-tests.
 * @author ealse
 *
 * @param <T>
 */
@Slf4j
public abstract class FXMLBaseTest<T extends Object> extends FXBase {

  private static final String FXML_DIR = "fxml/";
  
  private static ApplicationContext springContext = mock(ApplicationContext.class);
  private static FXMLNodeMap fnm;
  private static PageController pc;
  
  /**
   * Setup a environment for loading forms.
   */
  static {
    fnm = new FXMLNodeMap(springContext);
    pc = spy(new PageController(fnm));
    //  the main BorderPane is not loaded, so any actions using it will fail.
    doNothing().when(pc).setActivePage(isA(PageName.class));
    doNothing().when(pc).showMessage(isA(String.class));
    doNothing().when(pc).showErrorMessage(isA(String.class));
    doNothing().when(pc).showPermanentMessage(isA(String.class));
    doNothing().when(pc).activateLogoPage();
    try {
      Field f = FXMLNodeMap.class.getDeclaredField("fxmlDirectory");
      f.setAccessible(true);
      f.set(fnm, FXML_DIR);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Return a mockito spy.
   * Use it when working with a form.
   * @return
   */
  protected PageController getPageController() {
    return pc;
  }
  
  protected ApplicationContext getSpringContext() {
    return springContext;
  }

  /**
   * Loading a form without caching.
   * @param controller
   * @param pageName
   * @return
   * @throws FXMLMissingException
   */
  protected Parent getPageWithFxController(T controller, PageName pageName) throws FXMLMissingException {
    return getPage(controller, pageName, true);
  }

  /**
   * Loading a form without caching.
   * @param controller
   * @param pageName
   * @return
   * @throws FXMLMissingException
   */
  protected Parent getPageWithoutFxController(T controller, PageName pageName) throws FXMLMissingException {
    return getPage(controller, pageName, false);
  }

  /**
   * Loading a form without caching.
   * @param controller
   * @param pageName
   * @return
   * @throws FXMLMissingException
   */
  private Parent getPage(T controller, PageName pageName, boolean factory) throws FXMLMissingException {
    Resource r = new ClassPathResource(FXML_DIR + pageName.getId().getFxmlName());
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(r.getURL());
      if (factory) {
        fxmlLoader.setControllerFactory(param -> {return controller;});
      } else {
        fxmlLoader.setController(controller);
      }
      log.info(fxmlLoader.getLocation().toString());
      Parent page = fxmlLoader.load();
      return page;
    } catch (IOException e) {
      log.error("error loading fxml", e);
      throw new FXMLLoadException("error loading fxml", e);
    }
  }


}
