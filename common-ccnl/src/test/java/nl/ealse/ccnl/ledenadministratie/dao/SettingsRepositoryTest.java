package nl.ealse.ccnl.ledenadministratie.dao;

import java.util.List;
import nl.ealse.ccnl.ledenadministratie.dao.SettingRepository;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SettingsRepositoryTest {

  private SettingRepository dao = SettingRepository.getInstance();

  @Test
  void prepersisttest() {
    Setting s = new Setting();
    s.setSettingsGroup("test");
    s.setKey("key");
    s.setDescription("foo");
    s.setValue("value");
    s.prePersist();
    dao.save(s);
    Assertions.assertEquals("test.key", s.getId());
    List<Setting> settings = dao.findByOrderBySettingsGroupAscKeyAsc();
    Assertions.assertEquals(1, settings.size());
    dao.delete(s);
  }

}
