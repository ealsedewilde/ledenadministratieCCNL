package nl.ealse.ccnl.test;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.address.AddressController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.javafx.FXMLLoadException;
import nl.ealse.javafx.FXMLMissingException;
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
  
  private static final AddressController addressController = new AddressController();

  protected Parent getPage(T controller, PageName pageName) throws FXMLMissingException {
    Resource r = new ClassPathResource(FXML_DIR + pageName.getId().getFxmlName());
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(r.getURL());
      fxmlLoader.setControllerFactory(parm -> {
        if ("AddressController".equals(parm.getSimpleName())) {
          return addressController;
        }
        return controller;
      });
      log.info(fxmlLoader.getLocation().toString());
      Parent page = fxmlLoader.load();
      return page;
    } catch (IOException e) {
      log.error("error loading fxml", e);
      throw new FXMLLoadException("error loading fxml", e);
    }
  }


}
