package nl.ealse.ccnl.event;

import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;

@SuppressWarnings("serial")
public class PartnerSelectionEvent extends EntitySelectionEvent<ExternalRelationPartner> {

  public PartnerSelectionEvent(Object source, MenuChoice menuChoice) {
    super(source, menuChoice, new ExternalRelationPartner());
  }

  public PartnerSelectionEvent(Object source, MenuChoice menuChoice,
      ExternalRelationPartner externalRelation) {
    super(source, menuChoice, externalRelation);
  }

}
