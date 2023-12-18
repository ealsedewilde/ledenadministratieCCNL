package nl.ealse.ccnl.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.Optional;
import nl.ealse.ccnl.ledenadministratie.dao.SettingRepository;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.test.MockProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SettingsServiceTest {
  
  private static SettingRepository dao;
  
  private static SettingsService sut;
  
  @Test
  void getSettingTest() {
    Optional<String> settingsGroup = Optional.of("group");
    String key = "key";
    sut.getSetting(settingsGroup, key);
    verify(dao, never()).findById(key);
  }
  
  @Test
  void saveTest() {
    Setting setting = new Setting();
    setting.setKey("newId");
    String oldId = "oldId";
    Optional<Setting> old = Optional.of(setting);
    when(dao.findById(oldId)).thenReturn(old);
    setting.prePersist(); // simulate @PrePersist, @PreUpdate
    sut.save(setting, oldId);
    verify(dao).save(setting);
  }
  
  @BeforeAll
  static void setup() {
    EntityManager em = EntityManagerProvider.getEntityManager();
    EntityTransaction t = mock(EntityTransaction.class);
    when(em.getTransaction()).thenReturn(t);
    dao = MockProvider.mock(SettingRepository.class);
    sut = SettingsService.getInstance();
  }

}
