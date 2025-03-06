package nl.ealse.ccnl.dd.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.Optional;
import nl.ealse.ccnl.ledenadministratie.dao.SettingRepository;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigAmountEntry;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.service.SepaDirectDebitService;
import nl.ealse.ccnl.service.SettingsService;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatProperty;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatPropertyKey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DirectDebitAmountServiceTest {
  
  private static SettingRepository dao;
  
  private static SettingsService settingService;
  private static SepaDirectDebitService sddService;
  private static DirectDebitAmountService sut;
  
  private static Setting setting;
  
  @Test
  void saveFlatProperty() {
    DDConfigAmountEntry entry = new DDConfigAmountEntry();
    entry.setValue(BigDecimal.valueOf(3250, 2));
    entry.setDescription("incassobedrag");
    FlatProperty prop = new FlatProperty(FlatPropertyKey.DD_AMOUNT, entry);
    sut.saveFlatProperty(prop);
    verify(dao).save(setting);
  }
  
  @Test
  void saveSetting() {
    sut.saveSetting(setting);
    verify(sddService).saveProperty(any(FlatProperty.class));
  }
  
  @BeforeAll
  static void initConfig() {
    sddService = mock(SepaDirectDebitService.class);
    dao = mock(SettingRepository.class);
    
    setting = new Setting();
    setting.setDescription("incassobedrag");
    setting.setSettingsGroup("ccnl.contributie");
    setting.setKey("incasso");
    setting.setValue("€ 32,50");
    setting.prePersist();
    when(dao.findById("ccnl.contributie.incasso")).thenReturn(Optional.of(setting));
    settingService = new SettingsService(dao);
    sut = new DirectDebitAmountService(sddService, settingService);
  }


}
