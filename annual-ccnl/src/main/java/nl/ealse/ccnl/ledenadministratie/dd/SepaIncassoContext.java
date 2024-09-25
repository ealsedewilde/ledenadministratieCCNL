package nl.ealse.ccnl.ledenadministratie.dd;

import java.io.File;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;

public record SepaIncassoContext(List<String> messages, File controlExcelFile, List<Member> members,
    List<Integer> sepaNumbers) {

}
