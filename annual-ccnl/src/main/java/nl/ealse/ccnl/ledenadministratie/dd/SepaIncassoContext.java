package nl.ealse.ccnl.ledenadministratie.dd;

import java.io.File;
import java.util.List;
import java.util.Set;
import nl.ealse.ccnl.ledenadministratie.model.Member;

public record SepaIncassoContext(List<String> messages, File controlExcelFile, List<Member> members,
    Set<Integer> sepaNumbers) {

}
