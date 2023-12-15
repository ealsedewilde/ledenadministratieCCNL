package nl.ealse.ccnl.ledenadministratie.config;

import java.util.List;
import java.util.Properties;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.ledenadministratie.model.dao.SettingRepository;

public class DefaultDatabasePropertiesProvider implements DatabasePropertiesProvider{

  @Override
  public Properties getProperties() {
    SettingRepository dao = SettingRepository.getInstance();
    List<Setting> settings = dao.findByOrderBySettingsGroupAscKeyAsc();
    Properties properties = new Properties();
    settings.forEach(setting -> properties.put(setting.getId(), setting.getValue()));
    return properties;
  }

}
