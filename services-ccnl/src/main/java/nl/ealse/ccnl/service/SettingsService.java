package nl.ealse.ccnl.service;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.dao.SettingRepository;
import nl.ealse.ccnl.ledenadministratie.dao.util.TransactionUtil;
import nl.ealse.ccnl.ledenadministratie.model.Setting;

@Slf4j
@AllArgsConstructor
public class SettingsService {
  {log.info("Service created");}

  private final SettingRepository dao;
  
  public List<Setting> findByOrderBySettingsGroupAscKeyAsc() {
    return dao.findByOrderBySettingsGroupAscKeyAsc();
  }

  public Optional<Setting> getSetting(Optional<String> settingsGroup, String key) {
    if (settingsGroup.isPresent()) {
      StringJoiner sj = new StringJoiner(".");
      sj.add(settingsGroup.get()).add(key);
      return dao.findById(sj.toString());
    }
    return dao.findById(key);
  }

  public void save(Setting setting) {
    setting.prePersist();
    dao.save(setting);
    ApplicationContext.reloadPreferences();
  }

  public void save(Setting setting, String oldId) {
    setting.prePersist();
    String newId = setting.getId();
    TransactionUtil.inTransction(() -> {
      if (!newId.equals(oldId)) {
        Optional<Setting> old = dao.findById(oldId);
        if (old.isPresent()) {
          dao.delete(old.get());
        }
      }
      dao.save(setting);
      ApplicationContext.reloadPreferences();
    });
  }

  public void delete(Setting setting) {
    dao.delete(setting);
  }


}
