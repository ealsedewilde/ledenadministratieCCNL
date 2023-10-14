package nl.ealse.ccnl.event;

import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;

@SuppressWarnings("serial")
public class ExternalClubSelectionEvent extends EntitySelectionEvent<ExternalRelationClub> {

  public ExternalClubSelectionEvent(Object source, MenuChoice menuChoice,
      ExternalRelationClub externalRelation) {
    super(source, menuChoice, externalRelation);
  }

}
