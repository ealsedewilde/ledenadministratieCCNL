package nl.ealse.ccnl.dd;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.dao.util.TransactionUtil;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigAmountEntry;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.ledenadministratie.util.AmountFormatter;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatProperty;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatPropertyKey;

/**
 * Synchronize the direct debit amount.
 * The direct debit amount is configured twice.
 * <ol>
 * <li>In setting "ccnl.contributie.incasso"</li>
 * <li>In the properties for generating the direct debit file</li>
 * </ol>
 * This converter should be used to keep both in sync.
 */
@UtilityClass
public class DirectDebitAmountConverter {
  
  private final String ID = "ccnl.contributie.incasso";
  
  /**
   * Synchronize the setting "ccnl.contributie.incasso".
   *
   * @param property - the direct debit amount property
   */
  public void saveFlatProperty(FlatProperty property) {
    if (FlatPropertyKey.DD_AMOUNT == property.getFpk()) {
      final EntityManager em = ApplicationContext.getEntityManagerProvider().getEntityManager();
      Setting incassoSetting = em.find(Setting.class, ID);
      incassoSetting.setValue(property.getValue());
      persist(incassoSetting);
    }
    
  }
  
  /**
   * Synchronize the direct debit amount property.
   *
   * @param setting - setting "ccnl.contributie.incasso"
   */
  public void saveSetting(Setting setting) {
    if (ID.equals(setting.getId())) {
      DirectDebitConfig config = ApplicationContext.getIncassoProperties();
      DDConfigAmountEntry entry = config.getDirectDebitAmount();
      BigDecimal amount = BigDecimal.valueOf(AmountFormatter.parse(setting.getValue()));
      entry.setValue(amount);
      persist(config);
    }
    
  }
  
  private void persist(Object entity) {
    final EntityManager em = ApplicationContext.getEntityManagerProvider().getEntityManager();
    TransactionUtil.inTransction(() -> em.merge(entity));
  }

}
