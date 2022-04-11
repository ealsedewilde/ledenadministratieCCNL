package nl.ealse.ccnl.control.partner;

import nl.ealse.ccnl.control.external.ExternalRelationDeleteController;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import nl.ealse.javafx.mapping.ViewModel;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class PartnerDeleteController
    extends ExternalRelationDeleteController<ExternalRelationPartner> {

  public PartnerDeleteController(PageController pageController,
      ExternalRelationService<ExternalRelationPartner> service) {
    super(pageController, service);
  }

  @EventListener(condition = "#event.name('DELETE_PARTNER')")
  public void onApplicationEvent(PartnerSelectionEvent event) {
    setSelectedEntity(event.getSelectedEntity());
    ViewModel.modelToView(this, event.getSelectedEntity());
    getPageController().setActivePage(PageName.PARTNER_DELETE);
  }

}
