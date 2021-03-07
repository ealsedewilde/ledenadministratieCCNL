package nl.ealse.ccnl.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.ledenadministratie.model.dao.SettingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettingsServiceTest {
  
  @Mock
  private SettingRepository dao;
  
  @InjectMocks
  private SettingsService sut;
  
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
    sut.save(setting, oldId);
    verify(dao).delete(setting);
  }

}
