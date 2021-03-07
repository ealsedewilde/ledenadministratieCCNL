package nl.ealse.ccnl.control.partner;

import nl.ealse.ccnl.control.SearchPane;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.EntitySelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;

public class PartnerSearch extends
    SearchPane<ExternalRelationPartner, EntitySelectionEvent<ExternalRelationPartner>> {

  @Override
  protected String headerText(MenuChoice curentContext) {
    return "Opzoeken adverteerder";
  }

  @Override
  protected String columnName(int ix) {
    return "Partner nr.";
  }

  @Override
  protected String resultHeaderText(MenuChoice currentMenuChoice) {
    return "Gevonden adverteerders";
  }

}
