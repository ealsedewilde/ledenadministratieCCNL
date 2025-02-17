package nl.ealse.ccnl.control.menu;

/**
 * A Choice group bundles a number of menu choices that are handled by one.
 * {@link nl.ealse.ccnl.event.support.EventListener#condition()}
 *
 * @author ealse
 */
public enum ChoiceGroup {
  SEARCH_MEMBER, SEARCH_PA_MEMBER, SEARCH_CLUB, SEARCH_EXTERNAL, SEARCH_INTERNAL, SEARCH_PARTNER, REPORTS, UNKNOWN;
}
