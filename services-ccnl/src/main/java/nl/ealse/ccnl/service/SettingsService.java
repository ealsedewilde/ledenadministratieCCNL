package nl.ealse.ccnl.service;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseProperties;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.ledenadministratie.model.dao.SettingRepository;
import nl.ealse.ccnl.ledenadministratie.util.TransactionUtil;

@Slf4j
public class SettingsService {
  
  @Getter
  private static SettingsService instance = new SettingsService();

  private final SettingRepository dao;

  private SettingsService() {
    log.info("Service created");
    this.dao = SettingRepository.getInstance();
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
    DatabaseProperties.reload();
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
      DatabaseProperties.reload();
    });
  }

  public void delete(Setting setting) {
    dao.delete(setting);
  }


}
