package nl.ealse.ccnl.ledenadministratie.dd;

import java.util.List;

public record SepaIncassoResult(int numberOfTransactions, List<String> messages) {

}
