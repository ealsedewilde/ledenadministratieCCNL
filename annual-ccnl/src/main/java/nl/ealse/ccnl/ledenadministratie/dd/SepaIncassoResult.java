package nl.ealse.ccnl.ledenadministratie.dd;

import java.util.List;
import lombok.Data;

@Data
public class SepaIncassoResult {
  
  private final int numberOfTransactions;
  private final List<String> messages;


}
