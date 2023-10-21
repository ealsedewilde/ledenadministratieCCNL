package nl.ealse.javafx;

import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Spring bean for loading FXML.
 * <p>
 * This bean is the only eargerly loaded bean of the application !
 * </p>
 * 
 * @author ealse
 *
 */
@Component
@Lazy(false)
@Slf4j
public class FXMLLoaderBean {

  private static final String FXML_TYPE = ".fxml";

  @Value("${fxml.dir}")
  private String fxmlDirectory;

  private static FXMLLoaderBean instance;

  private final ApplicationContext springContext; // NOSONAR

  public FXMLLoaderBean(ApplicationContext springContext) {
    this.springContext = springContext;
    setInstance(this);
  }

  private static void setInstance(FXMLLoaderBean fnm) {
    instance = fnm;
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
    Resource r = new ClassPathResource(fqFxmlName);
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(r.getURL());
      if (controller == null) {
        // Take Spring managed bean as the fx:controller
        fxmlLoader.setControllerFactory(getControllerFactory());
      } else {
        fxmlLoader.setController(controller);
      }
      log.info(fxmlLoader.getLocation().toString());
      return fxmlLoader.load();
    } catch (FileNotFoundException e) {
      log.error("Unknown page " + fqFxmlName);
      Parent errorLabel = new Label("Unknown page: " + fqFxmlName);
      errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 30; -fx-font-weight: bold;");
      return errorLabel;
    } catch (IOException e) {
      log.error("error loading fxml", e);
      throw new FXMLLoadException("error loading fxml", e);
    }
  }

  protected Callback<Class<?>, Object> getControllerFactory() {
    return springContext::getBean;
  }

}
