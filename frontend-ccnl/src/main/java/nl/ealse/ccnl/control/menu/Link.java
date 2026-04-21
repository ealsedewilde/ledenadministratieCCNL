package nl.ealse.ccnl.control.menu;

/**
 * Link in a chain of events.
 * {@link nl.ealse.ccnl.event.support.EventListener#link()}
 *
 * @author ealse
 */
public enum Link implements Step {
  SEARCH_MEMBER, SEARCH_PA_MEMBER,
  //
  SEARCH_CLUB, SEARCH_EXTERNAL, SEARCH_INTERNAL, SEARCH_PARTNER, 
  //
  REPORTS,
  //
  NONE;
}
