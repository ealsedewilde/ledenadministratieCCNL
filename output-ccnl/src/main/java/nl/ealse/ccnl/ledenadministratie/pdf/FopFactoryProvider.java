package nl.ealse.ccnl.ledenadministratie.pdf;

import java.io.File;
import java.net.URI;
import java.net.URL;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.configuration.Configuration;
import org.apache.fop.configuration.DefaultConfigurationBuilder;

/**
 * Handles all the difficulties in configuring a {@link FopFactory}.
 *
 * @author ealse
 *
 */
@Slf4j
@UtilityClass
public class FopFactoryProvider {

  /**
   * The fully configured FopFactory.
   */
  @Getter
  private final FopFactory fopFactory;

  static {
    // The resource is on the classpath of the output-ccnl module
    URL fopConf = FopFactoryProvider.class.getResource("/fop.xconf");
    log.info("FOP conf found: " + new File(fopConf.getFile()).exists());
    
    try {
      URI basUri = getBaseUri(fopConf.toURI());
      log.info(fopConf.toURI().toString());
      FopFactoryBuilder factoryBuilder =
          new FopFactoryBuilder(basUri, new CustomResourceResolver());
      DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
      Configuration cfg = cfgBuilder.build(fopConf.openStream());
      fopFactory = factoryBuilder.setConfiguration(cfg).build();
    } catch (Exception e) {
      log.error("Could not configure the FopFactry", e);
      throw new ExceptionInInitializerError(e);
    }
  }
  
  public URI getBaseUri(URI configUri) {
    String base = configUri.toString();
    int ix = base.lastIndexOf('/');
    if (ix != -1) {
      base = base.substring(0, ++ix);
    }
    return URI.create(base);
  }


}
