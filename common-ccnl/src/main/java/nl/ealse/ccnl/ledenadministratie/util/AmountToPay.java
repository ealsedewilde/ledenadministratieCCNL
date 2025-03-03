package nl.ealse.ccnl.ledenadministratie.util;

import java.math.BigDecimal;
import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseProperties;

/**
 * The due amount of  membership fee.
 */
@UtilityClass
public class AmountToPay {
  
  private String DEFAULT_AMOUNT_EURO = DatabaseProperties.getProperty("ccnl.contributie.overboeken");
  private String DEFAULT_AMOUNT_STRING = DEFAULT_AMOUNT_EURO.replace("€", "").trim();
  private BigDecimal DEFAULT_AMOUNT = BigDecimal.valueOf(AmountFormatter.parse(DEFAULT_AMOUNT_STRING));
  
  public String amountToPayDefault() {
    return DEFAULT_AMOUNT_STRING;
  }
  
  
  public String amountToPayAsString(BigDecimal amountPaid) {
    if (BigDecimal.ZERO.compareTo(amountPaid) == 0) {
      return DEFAULT_AMOUNT_EURO;
    }
    return AmountFormatter.format(DEFAULT_AMOUNT.subtract(amountPaid));
  }

}
