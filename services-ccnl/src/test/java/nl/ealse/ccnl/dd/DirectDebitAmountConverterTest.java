package nl.ealse.ccnl.dd;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigAmountEntry;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatProperty;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatPropertyKey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DirectDebitAmountConverterTest {
  
  private static EntityManager em;
  
  private static Setting setting;
  
  @Test
  void saveFlatProperty() {
    DDConfigAmountEntry entry = new DDConfigAmountEntry();
    entry.setValue(BigDecimal.valueOf(3250, 2));
    entry.setDescription("incassobedrag");
    FlatProperty prop = new FlatProperty(FlatPropertyKey.DD_AMOUNT, entry);
    DirectDebitAmountConverter.saveFlatProperty(prop);
    verify(em).merge(any(Setting.class));
  }
  
  @Test
  void saveSetting() {
    DirectDebitAmountConverter.saveSetting(setting);
    verify(em).merge(any(DirectDebitConfig.class));
  }
  
  @BeforeAll
  static void initConfig() {
    em = EntityManagerProvider.getEntityManager();
    setting = new Setting();
    setting.setDescription("incassobedrag");
    setting.setSettingsGroup("ccnl.contributie");
    setting.setKey("incasso");
    setting.setValue("€ 32,50");
    setting.prePersist();
    when(em.find(Setting.class, "ccnl.contributie.incasso")).thenReturn(setting);
  }


}
