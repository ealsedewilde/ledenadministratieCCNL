package nl.ealse.ccnl.ledenadministratie.dd;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;

@Data
public class SepaIncassoContext {
  
  private final IncassoProperties incassoProperties;
  private final CCNLColumnProperties excelProperties;
  private final List<String> messages = new ArrayList<>();


}
