package nl.ealse.ccnl.control.partner;

import nl.ealse.ccnl.control.external.ExternalRelationDeleteController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import nl.ealse.ccnl.service.relation.CommercialPartnerService;
import nl.ealse.javafx.mvvm.ViewModel;

public class PartnerDeleteController
    extends ExternalRelationDeleteController<ExternalRelationPartner> {

  public PartnerDeleteController(PageController pageController, CommercialPartnerService service) {
    super(pageController, service);
  }

  @EventListener(menuChoice = MenuChoice.DELETE_PARTNER)
  public void onApplicationEvent(PartnerSelectionEvent event) {
    getPageController().setActivePage(PageName.PARTNER_DELETE);
    setSelectedEntity(event.getSelectedEntity());
    ViewModel.modelToView(this, event.getSelectedEntity());
  }

}
