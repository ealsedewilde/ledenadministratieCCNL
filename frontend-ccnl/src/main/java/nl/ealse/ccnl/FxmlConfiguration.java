package nl.ealse.ccnl;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration of the location of the fxml files.
 */
@Configuration
@PropertySource("classpath:fxml.properties")
public class FxmlConfiguration {

}
