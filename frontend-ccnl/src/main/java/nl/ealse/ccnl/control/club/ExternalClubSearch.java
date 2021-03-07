package nl.ealse.ccnl.control.club;

import nl.ealse.ccnl.control.SearchPane;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.EntitySelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;

public class ExternalClubSearch
    extends SearchPane<ExternalRelationClub, EntitySelectionEvent<ExternalRelationClub>> {

  @Override
  protected String headerText(MenuChoice currentMenuChoice) {
    return "Opzoeken club";
  }

  @Override
  protected String columnName(int ix) {
    return "Club nr.";
  }

  @Override
  protected String resultHeaderText(MenuChoice currentMenuChoice) {
    return "Gevonden Clubs";
  }

}
