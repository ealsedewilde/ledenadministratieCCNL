package nl.ealse.ccnl.service;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseProperties;
import nl.ealse.ccnl.ledenadministratie.dao.SettingRepository;
import nl.ealse.ccnl.ledenadministratie.dao.util.TransactionUtil;
import nl.ealse.ccnl.ledenadministratie.model.Setting;

@Slf4j
public class SettingsService {

  private final SettingRepository dao;

  public SettingsService(SettingRepository dao) {
    log.info("Service created");
    this.dao = dao;
  }

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
    DatabaseProperties.initialize();
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
      DatabaseProperties.initialize();
    });
  }

  public void delete(Setting setting) {
    dao.delete(setting);
  }


}
