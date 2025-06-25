package nl.ealse.ccnl.ledenadministratie.payment;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.util.AmountFormatter;

@UtilityClass
public class MemberShipFee {
  
  @Getter
  private final double incasso;
  @Getter
  private final double overboeken;
  
  static {
    incasso = toDouble(ApplicationContext.getPreference("ccnl.contributie.incasso"));
    overboeken = toDouble(ApplicationContext.getPreference("ccnl.contributie.overboeken"));
  }
  
  private double toDouble(String bedrag) {
    try {
      return AmountFormatter.parse(bedrag);
    } catch (NumberFormatException e) {
      throw new PaymentException("ongeldig bedrag " + bedrag, e);
    }
    
  }

}
