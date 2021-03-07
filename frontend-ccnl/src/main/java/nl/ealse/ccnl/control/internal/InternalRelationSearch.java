package nl.ealse.ccnl.control.internal;

import nl.ealse.ccnl.control.SearchPane;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;

public class InternalRelationSearch
    extends SearchPane<InternalRelation, InternalRelationSelectionEvent> {

  @Override
  public String headerText(MenuChoice currentMenuChoice) {
    return "Opzoeken functie";
  }

  @Override
  protected String columnName(int ix) {
    return "Intern nr.";
  }

  @Override
  protected String resultHeaderText(MenuChoice currentMenuChoice) {
    return "Gevonden functies";
  }

}
