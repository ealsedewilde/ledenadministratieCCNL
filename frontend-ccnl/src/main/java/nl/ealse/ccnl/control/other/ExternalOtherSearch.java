package nl.ealse.ccnl.control.other;

import nl.ealse.ccnl.control.SearchPane;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.EntitySelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;

public class ExternalOtherSearch extends
    SearchPane<ExternalRelationOther, EntitySelectionEvent<ExternalRelationOther>> {

  @Override
  protected String headerText(MenuChoice curentContext) {
    return "Opzoeken externe relatie";
  }

  @Override
  protected String columnName(int ix) {
    return "Relatie nr.";
  }

  @Override
  protected String resultHeaderText(MenuChoice currentMenuChoice) {
    return "Gevonden externe relaties";
  }

}
