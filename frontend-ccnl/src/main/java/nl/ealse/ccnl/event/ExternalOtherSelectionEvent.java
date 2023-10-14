package nl.ealse.ccnl.event;

import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;

@SuppressWarnings("serial")
public class ExternalOtherSelectionEvent extends EntitySelectionEvent<ExternalRelationOther> {

  public ExternalOtherSelectionEvent(Object source, MenuChoice menuChoice,
      ExternalRelationOther externalRelation) {
    super(source, menuChoice, externalRelation);
  }

}
