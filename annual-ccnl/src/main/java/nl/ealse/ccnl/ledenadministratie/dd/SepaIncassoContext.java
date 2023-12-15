package nl.ealse.ccnl.ledenadministratie.dd;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class SepaIncassoContext {
  
  private final List<String> messages = new ArrayList<>();


}
