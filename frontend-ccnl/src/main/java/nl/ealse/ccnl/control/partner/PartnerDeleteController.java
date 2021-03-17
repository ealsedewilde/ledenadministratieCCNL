package nl.ealse.ccnl.control.partner;

import nl.ealse.ccnl.control.external.ExternalRelationDeleteController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import nl.ealse.javafx.mapping.DataMapper;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

@Controller
public class PartnerDeleteController
    extends ExternalRelationDeleteController<ExternalRelationPartner>
    implements ApplicationListener<PartnerSelectionEvent> {

  public PartnerDeleteController(PageController pageController,
      ExternalRelationService<ExternalRelationPartner> service) {
    super(pageController, service);
  }

  @Override
  public void onApplicationEvent(PartnerSelectionEvent event) {
    if (MenuChoice.DELETE_PARTNER == event.getMenuChoice()) {
      setSelectedEntity(event.getSelectedEntity());
      DataMapper.modelToForm(this, event.getSelectedEntity());
      getPageController().setActivePage(PageName.PARTNER_DELETE);
    }
  }

}
