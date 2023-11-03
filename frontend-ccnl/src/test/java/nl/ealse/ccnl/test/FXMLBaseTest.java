package nl.ealse.ccnl.test;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import java.io.IOException;
import java.lang.reflect.Field;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.MainStage;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.control.menu.PageReference;
import nl.ealse.javafx.FXMLLoadException;
import nl.ealse.javafx.FXMLLoaderBean;
import nl.ealse.javafx.ImagesMap;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Non caching FXML loading for unit-tests.
 *
 * @author ealse
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
@Slf4j
public abstract class FXMLBaseTest extends FXBase {

  private static final String FXML_DIR = "fxml/";

  private static ApplicationContext springContext = mock(ApplicationContext.class);
  private static PageController pc;
  private static Callback<Class<?>, Object> controllerFactory;

  /**
   * Setup a environment for loading forms.
   */
  static {
    controllerFactory = param -> {
      if (PageController.class.isAssignableFrom(param)) {
        return pc;
      }
      return mock(param);
    };
    doReturn(controllerFactory).when(springContext).getBean(isA(Class.class));
    new TestfxmlLoaderBean(springContext);
    pc = spy(new PageController());
    // the main BorderPane is not loaded, so any actions using it will fail.
    doNothing().when(pc).setActivePage(isA(PageReference.class));
    doNothing().when(pc).showMessage(isA(String.class));
    doNothing().when(pc).showErrorMessage(isA(String.class));
    doNothing().when(pc).showPermanentMessage(isA(String.class));
    doNothing().when(pc).activateLogoPage();
    initializeMainStage();
  }
  
  private static void initializeMainStage() {
    Field icon = FieldUtils.getField(MainStage.class, "icon", true);
    try {
      icon.set(null, ImagesMap.get("citroen.png"));
    } catch (IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  /**
   * Return a mockito spy. Use it when working with a form.
   *
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
    Resource r = new ClassPathResource(FXML_DIR + pageName.getFxmlName() + ".fxml");
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(r.getURL());
      if (factory) {
        fxmlLoader.setControllerFactory(param -> {
          return controller;
        });
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


  /**
   * Variant with custom ControllerFactory.
   */
  private static class TestfxmlLoaderBean extends FXMLLoaderBean {

    public TestfxmlLoaderBean(ApplicationContext springContext) {
      super(springContext);
      initialize();
    }

    private void initialize() {
      try {
        FieldUtils.writeField(this, "fxmlDirectory", FXML_DIR, true);
        FXMLLoader fxmlLoader = (FXMLLoader) FieldUtils.readField(this, "fxmlLoader", true);
        fxmlLoader.setControllerFactory(controllerFactory);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }



}
