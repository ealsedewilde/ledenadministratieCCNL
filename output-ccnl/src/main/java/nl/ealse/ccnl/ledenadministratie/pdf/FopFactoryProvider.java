package nl.ealse.ccnl.ledenadministratie.pdf;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.configuration.Configuration;
import org.apache.fop.configuration.DefaultConfigurationBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

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
    Resource fopConf = new ClassPathResource("fop.xconf");
    try {
      log.info(fopConf.getURI().toString());
      FopFactoryBuilder factoryBuilder =
          new FopFactoryBuilder(fopConf.getURI(), new CustomResourceResolver(fopConf.getURI()));
      DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
      Configuration cfg = cfgBuilder.build(fopConf.getInputStream());
      fopFactory = factoryBuilder.setConfiguration(cfg).build();
    } catch (Exception e) {
      log.error("Could not configure the FopFactry", e);
      throw new ExceptionInInitializerError(e);
    }
  }

}
