package nl.ealse.javafx;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Utility that is capable of loading image-files from the classpath.
 * 
 * @author ealse
 *
 */
@Slf4j
@UtilityClass
public class ImagesMap {

  private static final String IMAGE_DIR = "images/";

  /**
   * Cache of loaded images.
   */
  private static final Map<String, Image> IMAGE_MAP = new HashMap<>();

  /**
   * Retrieve an image from the classpath.
   * @param key - key of the desired image
   * @return
   */
  public Image get(String key) {
    Image image = IMAGE_MAP.get(key);
    if (image == null) {
      Resource r = new ClassPathResource(IMAGE_DIR + key);
      try (InputStream is = r.getInputStream()) {
        IMAGE_MAP.put(key, new Image(is));
      } catch (IOException e) {
        String msg = "error loading image " + key;
        log.error(msg, e);
        throw new FXMLLoadException(msg, e);
      }
    }
    return IMAGE_MAP.get(key);
  }

}
