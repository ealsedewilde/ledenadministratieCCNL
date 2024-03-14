package nl.ealse.ccnl.ledenadministratie.dd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import nl.ealse.ccnl.ledenadministratie.model.Member;

@Data
public class SepaIncassoContext {
  
  private final List<String> messages = new ArrayList<>();
  private final File controlExcelFile;
  private final List<Member> members;
  private final List<Integer> sepaNumbers;


}
