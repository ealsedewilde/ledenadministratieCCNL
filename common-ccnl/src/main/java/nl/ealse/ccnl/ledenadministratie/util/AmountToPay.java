package nl.ealse.ccnl.ledenadministratie.util;

import java.math.BigDecimal;
import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseProperties;

/**
 * The due amount of  membership fee.
 */
@UtilityClass
public class AmountToPay {
  
  public String amountToPayDefault() {
    // do not cache the setting; it might be changed by the user.
    return DatabaseProperties.getProperty("ccnl.contributie.overboeken").replace("€", "").trim();
  }
  
  public String amountToPayAsString(BigDecimal amountPaid) {
    if (BigDecimal.ZERO.compareTo(amountPaid) == 0) {
      return DatabaseProperties.getProperty("ccnl.contributie.overboeken");
    }
    BigDecimal defaultAmount = BigDecimal.valueOf(AmountFormatter.parse(amountToPayDefault()));
    return AmountFormatter.format(defaultAmount.subtract(amountPaid));
  }

}
