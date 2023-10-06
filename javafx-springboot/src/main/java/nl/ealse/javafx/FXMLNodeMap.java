package nl.ealse.javafx;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * FXML cache.
 * <p>
 * The FXML is lazily loaded. Almost pages contain a reference to a Spring Boot controller. If
 * Spring is also configured for lazy loading, such a controller gets loaded as the FXML gets
 * loaded.
 * </p>
 * <p>
 * Important: Every FXML only gets loaded once.
 * </p>
 * 
 * @author ealse
 *
 */
@Component
@Slf4j
public class FXMLNodeMap {

  @Value("${fxml.dir}")
  private String fxmlDirectory;

  /**
   * Cache for all loaded pages.
   */
  private static final Map<String, Parent> FXML_PAGES = new HashMap<>();

  private static FXMLNodeMap instance ;

  private final ApplicationContext springContext; // NOSONAR

  public FXMLNodeMap(ApplicationContext springContext) {
    this.springContext = springContext;
    setInstance(this);
  }
  
  private static void setInstance(FXMLNodeMap fnm) {
    instance = fnm;
  }

  /**
   * Get the page for fxml with fx:root.
   * <p>
   * Used by non Spring components.
   * </p?
   * 
   * @param id - unique id of the page
   * @param root - the root object for the fxml
   * @param controller - the controller object fxml binding.
   * @return Parent of the page
   * @throws FXMLMissingException - when page not found
   */

  public static Parent getPage(PageId id, Object root, Object controller)
      throws FXMLMissingException {
    return instance.getFXML(id, root, controller);
  }

  /**
   * Lookup an initialized FXML page.
   * 
   * @param id - unique id of the page
   * @param controller - the controller object fxml binding.
   * @return Parent of the page
   * @throws FXMLMissingException - when page not found
   */
  public Parent get(PageId id, Object controller) throws FXMLMissingException {
    Parent page = FXML_PAGES.get(id.getPagekey());
    if (page == null) {
      page = getFXML(id, null, controller);
      FXML_PAGES.put(id.getPagekey(), page);
    }
    return page;
  }

  /**
   * Loading of the FXML from the classpath.
   * 
   * @param id - of the page to retrieve
   * @param root - optional root object for the fxml
   * @return
   * @throws FXMLMissingException 
   */
  private Parent getFXML(PageId id, Object root, Object controller) throws FXMLMissingException {
    Resource r = new ClassPathResource(fxmlDirectory + id.getFxmlName());
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(r.getURL());
      fxmlLoader.setRoot(root);
      if (controller == null) {
        // Take Spring managed bean as the fx:controller
        fxmlLoader.setControllerFactory(springContext::getBean);
      } else {
        fxmlLoader.setController(controller);
      }
      log.info(fxmlLoader.getLocation().toString());
      return fxmlLoader.load();
    } catch (FileNotFoundException e) {
      log.error("Unknown page " + id.getFxmlName());
      throw new FXMLMissingException("Unknown page", id.getFxmlName());
    } catch (IOException e) {
      log.error("error loading fxml", e);
      throw new FXMLLoadException("error loading fxml", e);
    }
  }

}
