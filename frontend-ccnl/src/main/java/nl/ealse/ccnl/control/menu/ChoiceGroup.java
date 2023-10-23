package nl.ealse.ccnl.control.menu;

/**
 * A Choice group bundles a number of menu choices that are handled by one.
 * {@link org.springframework.context.event.EventListener#condition()}
 *
 * @author ealse
 */
public enum ChoiceGroup {
  SEARCH_MEMBER, SEARCH_CLUB, SEARCH_EXTERNAL, SEARCH_INTERNAL, SEARCH_PARTNER, REPORTS, COMMANDS;
}
