package nl.ealse.javafx;

import java.io.IOException;
import java.net.URL;
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

  private final ApplicationContext springContext; // NOSONAR

  public FXMLNodeMap(ApplicationContext springContext) {
    this.springContext = springContext;
  }

  /**
   * Lookup an initialized FXML page.
   * 
   * @throws FXMLMissingException - when page not found
   */
  public Parent get(PageId id) throws FXMLMissingException {
    Parent page = FXML_PAGES.get(id.getPagekey());
    if (page == null) {
      page = getFXML(id);
      if (page == null) {
        log.error("Unknown page " + id.getFxmlName());
        throw new FXMLMissingException("Unknown page", id.getFxmlName());
      }
    }
    return page;
  }

  /**
   * Manage the FXML caching.
   * 
   * @param id - of the page to retrieve
   * @return
   */
  private Parent getFXML(PageId id) {
    Resource r = new ClassPathResource(fxmlDirectory + id.getFxmlName());
    try {
      return processPath(id.getPagekey(), r.getURL());
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Actually loading of the FXML.
   * 
   * @param fxmlPath - file path of the page to load
   * @return
   */
  private Parent processPath(String key, URL fxmlPath) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(fxmlPath);
      // Take Spring managed bean as the fx:controller
      fxmlLoader.setControllerFactory(springContext::getBean);
      log.info(fxmlLoader.getLocation().toString());
      Parent page = fxmlLoader.load();
      FXML_PAGES.put(key, page);
      return page;
    } catch (IOException e) {
      log.error("error loading fxml", e);
      throw new FXMLLoadException("error loading fxml", e);
    }
  }

}
