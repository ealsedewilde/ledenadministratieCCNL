package nl.ealse.ccnl.service;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.ledenadministratie.model.dao.SettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
    dao.save(setting);
  }

  @Transactional
  public void save(Setting setting, String oldId) {
    String newId = setting.getId();
    if (!newId.equals(oldId)) {
      Optional<Setting> old = dao.findById(oldId);
      if (old.isPresent()) {
        dao.delete(old.get());
      }
    }
    dao.save(setting);
  }

  public void delete(Setting setting) {
    dao.delete(setting);
  }


}
