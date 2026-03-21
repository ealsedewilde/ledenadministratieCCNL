package nl.ealse.ccnl.ledenadministratie.dd;

import java.io.File;
import java.util.List;
import java.util.Set;
import nl.ealse.ccnl.ledenadministratie.model.Member;

/**
 * Context met alle gegevens die nodig zijn voor het aanmaken van het incassobestand.
 */
public record SepaIncassoContext(List<String> messages, File controlExcelFile, List<Member> members,
    Set<Integer> sepaNumbers) {

}
