package nl.ealse.ccnl.control.partner;

import java.util.Map;
import nl.ealse.ccnl.control.external.ExternalRelationSearchController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import nl.ealse.ccnl.service.relation.SearchItem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class PartnerSearchController
    extends ExternalRelationSearchController<ExternalRelationPartner> {

  public PartnerSearchController(ApplicationContext springContext,
      ExternalRelationService<ExternalRelationPartner> partnerService) {
    super(springContext, partnerService);
    this.initializeSearchItems();
  }
  
  @EventListener(condition = "#event.group('SEARCH_PARTNER')")
  public void searchPartner(MenuChoiceEvent event) {
    prepareSearch(event);
  }

  private void initializeSearchItems() {
    Map<String, SearchItem> map = getSearchItemValues();
    map.put("Adverteerder id", SearchItem.values()[0]);
    map.put("Naam adverteerder", SearchItem.values()[1]);
    map.put("Straat", SearchItem.values()[2]);
    map.put("Postcode", SearchItem.values()[3]);
    map.put("Woonplaats", SearchItem.values()[4]);
  }

  @Override
  public PartnerSelectionEvent newEntitySelectionEvent(
      MenuChoice currentMenuChoice) {
    return new PartnerSelectionEvent(this, currentMenuChoice, getSelectedEntity());
  }

}
