package nl.ealse.ccnl.ledenadministratie.model.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Slf4j
class SettingsRepositoryTest extends JpaTestBase {

  @Autowired
  private EntityManager em;

  @Autowired
  private SettingRepository dao;

  @Test
  void settingsTest() {
    List<Setting> settings = dao.findByOrderBySettingsGroupAscKeyAsc();
    Assertions.assertEquals(13, settings.size());
  }

  @Test
  void prepersisttest() {
    Setting s = new Setting();
    s.setSettingsGroup("test");
    s.setKey("key");
    s.setDescription("foo");
    s.setValue("value");
    dao.save(s);
    Assertions.assertEquals("test.key", s.getId());

  }

  @BeforeEach
  public void init() {
    // em.getTransaction().begin();
    Resource initSql = new ClassPathResource("init.sql");
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(initSql.getInputStream()))) {
      String line = reader.readLine();
      while (line != null) {
        line = line.trim();
        Query q = em.createNativeQuery(line);
        q.executeUpdate();
        line = reader.readLine();
      }
      // em.getTransaction().commit();
    } catch (Exception e) {
      log.error("failed to load", e);
    }
  }



}
