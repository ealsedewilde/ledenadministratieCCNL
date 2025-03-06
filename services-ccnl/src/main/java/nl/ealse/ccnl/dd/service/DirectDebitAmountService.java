package nl.ealse.ccnl.dd.service;

import java.math.BigDecimal;
import java.util.Optional;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoProperties;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigAmountEntry;
import nl.ealse.ccnl.ledenadministratie.util.AmountFormatter;
import nl.ealse.ccnl.service.SepaDirectDebitService;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatProperty;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatPropertyKey;
import nl.ealse.ccnl.service.SettingsService;

public class DirectDebitAmountService {
  
  private final SepaDirectDebitService ddService;
  
  private final SettingsService settingService;
  
  public DirectDebitAmountService(SepaDirectDebitService ddService, SettingsService settingService) {
    this.ddService = ddService;
    this.settingService = settingService;
    
  }
  
  public void saveFlatProperty(FlatProperty property) {
    if (FlatPropertyKey.DD_AMOUNT == property.getFpk()) {
      Optional<Setting> result = settingService.getSetting(Optional.of("ccnl.contributie"), "incasso");
      if (result.isPresent()) {
        Setting incassoSetting = result.get();
        incassoSetting.setValue(property.getValue());
        incassoSetting.setDescription(property.getDescription());
        settingService.save(incassoSetting, incassoSetting.getId());
      }
    }
    
  }
  
  public void saveSetting(Setting setting) {
    if ("ccnl.contributie.incasso".equals(setting.getId())) {
      DDConfigAmountEntry entry = IncassoProperties.getProperties().getDirectDebitAmount();
      BigDecimal amount = BigDecimal.valueOf(AmountFormatter.parse(setting.getValue()));
      entry.setValue(amount);
      entry.setDescription(setting.getDescription());
      ddService.saveProperty(new FlatProperty(FlatPropertyKey.DD_AMOUNT, entry));
    }
    
  }

}
