package nl.ealse.ccnl.test;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.javafx.FXMLLoadException;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.FXMLNodeMap;
import nl.ealse.javafx.PageId;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Non caching FXML loading for unit-tests.
 * @author ealse
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
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
    Callback<Class<?>, Object> controllerFactory = param -> {return mock(param);};
    doReturn(controllerFactory).when(springContext).getBean(isA(Class.class));
    fnm = new TestFxmlNodeMap(springContext);
    pc = spy(new PageController(fnm));
    //  the main BorderPane is not loaded, so any actions using it will fail.
    doNothing().when(pc).setActivePage(isA(PageName.class));
    doNothing().when(pc).showMessage(isA(String.class));
    doNothing().when(pc).showErrorMessage(isA(String.class));
    doNothing().when(pc).showPermanentMessage(isA(String.class));
    doNothing().when(pc).activateLogoPage();
    try {
      FieldUtils.writeField(fnm, "fxmlDirectory", FXML_DIR, true);
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
  
  /** Non caching variant
   * 
   */
  private static class TestFxmlNodeMap extends FXMLNodeMap {

    public TestFxmlNodeMap(ApplicationContext springContext) {
      super(springContext);
    }
    
    @Override
    public Parent get(PageId id, Object controller) throws FXMLMissingException {
      return FXMLNodeMap.getPage(id.getFxmlName(), null, controller);
    }
    
  }


}
