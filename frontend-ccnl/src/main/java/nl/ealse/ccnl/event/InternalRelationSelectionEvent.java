package nl.ealse.ccnl.event;

import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;

@SuppressWarnings("serial")
public class InternalRelationSelectionEvent extends EntitySelectionEvent<InternalRelation> {

  public InternalRelationSelectionEvent(Object source, MenuChoice menuChoice,
      InternalRelation internalRelation) {
    super(source, menuChoice, internalRelation);
  }

}
