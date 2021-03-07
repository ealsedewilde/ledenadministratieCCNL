package nl.ealse.ccnl.test.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@UtilityClass
public class ExcelPropertiesFactory {
  
  public CCNLColumnProperties newExcelProperties() {
    ConfigurableEnvironment environment = new StandardEnvironment();
    MutablePropertySources propertySources = environment.getPropertySources();
    Properties props = new Properties();
    Map<String, Object> map = new HashMap<>();
    propertySources.addFirst(new MapPropertySource("excel.properties", map));
    Resource r = new ClassPathResource("excel.properties");
    try {
      props.load(r.getInputStream());
      props.forEach((key, value) -> map.put((String) key, value));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new CCNLColumnProperties(environment);

  }


}
