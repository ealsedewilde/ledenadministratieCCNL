package nl.ealse.ccnl.ledenadministratie.payment;

import java.text.ParseException;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.util.AmountFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class MemberShipFee {
  
  private final double incasso;
  private final double overboeken;
  
  public MemberShipFee(@Value("${ccnl.contributie.incasso}") String incasso, @Value("${ccnl.contributie.overboeken}") String overboeken) {
    this.incasso = toDouble(incasso);
    this.overboeken = toDouble(overboeken);
  }
  
  private double toDouble(String bedrag) {
    try {
      return AmountFormatter.parse(bedrag);
    } catch (ParseException e) {
      throw new PaymentException("ongeldig bedrag " + bedrag, e);
    }
    
  }

}
