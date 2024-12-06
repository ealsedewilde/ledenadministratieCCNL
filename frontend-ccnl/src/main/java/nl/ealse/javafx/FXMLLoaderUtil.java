package nl.ealse.javafx;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.util.Callback;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ioc.ComponentProviderUtil;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationProperties;

/**
 * Utility for loading FXML.
 *
 * @author ealse
 *
 */
@Slf4j
@UtilityClass
public class FXMLLoaderUtil {

  private final String FXML_TYPE = ".fxml";

  private final ClassLoader CLASSLOADER = FXMLLoaderUtil.class.getClassLoader();

  private final String FXML_DIRECTORY = ApplicationProperties.getProperty("fxml.dir");
  
  private final Callback<Class<?>, Object> CONTROLLER_FACTORY = clazz -> {
    try { 
      return ComponentProviderUtil.getComponent(clazz);
    } catch (Exception e) {
      String msg = "failed to get controller";
      log.error(msg, e);
      throw new FXMLLoadException(msg, e);
    }
  };
  

  /**
   * Lookup an initialized FXML page.
   *
   * @param String fxmlName - unique id of the page
   * @param controller - the controller object fxml binding.
   * @return Parent of the page
   */
  public Parent getPage(String fxmlName, Object controller) {
    FXMLLoader loader = new FXMLLoader();
    loader.setController(controller);
    return loadFXML(fxmlName, loader);
  }

  public Parent getPage(String fxmlName) {
    FXMLLoader loader = new FXMLLoader();
    loader.setControllerFactory(CONTROLLER_FACTORY);
    return loadFXML(fxmlName, loader);
  }

  /**
   * Loading of the FXML from the classpath.
   *
   * @param fxmlName - of the page to retrieve
   * @param loader - for loading fxml
   * @return
   */
  private Parent loadFXML(String fxmlName,FXMLLoader loader) {
    String fqFxmlName = new StringBuilder(FXML_DIRECTORY).append(fxmlName).append(FXML_TYPE).toString();
    try {
      URL fxmlUrl = CLASSLOADER.getResource(fqFxmlName);
      if (fxmlUrl != null) {
        loader.setLocation(fxmlUrl);
        log.info(fqFxmlName);
        return loader.load();
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
