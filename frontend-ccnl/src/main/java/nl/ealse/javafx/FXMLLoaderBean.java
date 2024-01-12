package nl.ealse.javafx;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationProperties;

/**
 * Spring bean for loading FXML.
 * <p>
 * This bean is the only eargerly loaded bean of the application !
 * </p>
 *
 * @author ealse
 *
 */
@Slf4j
public class FXMLLoaderBean {

  private static final String FXML_TYPE = ".fxml";

  private static final FXMLLoaderBean instance = new FXMLLoaderBean();

  private final String fxmlDirectory = "/" + ApplicationProperties.getProperty("fxml.dir");

  private final FXMLLoader fxmlLoader = new FXMLLoader();

  public FXMLLoaderBean() {
    fxmlLoader.setControllerFactory(clazz -> {
      try {
        Method m = clazz.getMethod("getInstance");
        return m.invoke(null);
      } catch (Exception e) {
        String msg = "failed to get controller";
        log.error(msg, e);
        throw new FXMLLoadException(msg, e);
      }
    });
  }

  /**
   * Lookup an initialized FXML page.
   *
   * @param String fxmlName - unique id of the page
   * @param controller - the controller object fxml binding.
   * @return Parent of the page
   */
  public static Parent getPage(String fxmlName, Object controller) {
    return instance.loadFXML(fxmlName, controller);
  }

  public static Parent getPage(String fxmlName) {
    return instance.loadFXML(fxmlName, null);
  }

  /**
   * Loading of the FXML from the classpath.
   *
   * @param fxmlName - of the page to retrieve
   * @param controller - optional controller
   * @return
   */
  private Parent loadFXML(String fxmlName, Object controller) {
    String fqFxmlName = fxmlDirectory + fxmlName + FXML_TYPE;
    try {
      URL fxmlUrl = getClass().getResource(fqFxmlName);
      if (fxmlUrl != null) {
        fxmlLoader.setLocation(getClass().getResource(fqFxmlName));
        log.info(fxmlLoader.getLocation().toString());
        fxmlLoader.setRoot(null);
        fxmlLoader.setController(controller);
        return fxmlLoader.load();
      } else {
        log.error("Unknown page " + fqFxmlName);
        Parent errorLabel = new Label("Unknown page: " + fqFxmlName);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 30; -fx-font-weight: bold;");
        return errorLabel;
      }
    } catch (IOException e) {
      log.error("error loading fxml", e);
      throw new FXMLLoadException("error loading fxml", e);
    }
  }

}
