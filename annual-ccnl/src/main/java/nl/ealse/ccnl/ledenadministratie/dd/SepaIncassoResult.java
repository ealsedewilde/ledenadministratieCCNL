package nl.ealse.ccnl.ledenadministratie.dd;

import java.util.List;

/**
 * log met de meldingen die zijn aangemaakt bij het genereren van het incassobestand.
 */
public record SepaIncassoResult(int numberOfTransactions, List<String> messages) {

}
