package nl.ealse.ccnl.control.partner;

import java.util.LinkedHashMap;
import java.util.Map;
import nl.ealse.ccnl.control.external.ExternalRelationSearchController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import nl.ealse.ccnl.service.ExternalRelationService;
import nl.ealse.ccnl.service.SearchItem;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class PartnerSearchController
    extends ExternalRelationSearchController<ExternalRelationPartner> {

  private final Map<String, SearchItem> searchItemMap;

  public PartnerSearchController(ApplicationContext springContext,
      ExternalRelationService<ExternalRelationPartner> partnerService) {
    super(springContext, partnerService, MenuChoice.AMEND_PARTNER, MenuChoice.DELETE_PARTNER);
    this.searchItemMap = initializeSearchItems();
  }

  private Map<String, SearchItem> initializeSearchItems() {
    Map<String, SearchItem> map = new LinkedHashMap<>();
    map.put("Adverteerder id", SearchItem.values()[0]);
    map.put("Naam adverteerder", SearchItem.values()[1]);
    map.put("Straat", SearchItem.values()[2]);
    map.put("Postcode", SearchItem.values()[3]);
    map.put("Woonplaats", SearchItem.values()[4]);
    return map;
  }

  @Override
  public PartnerSelectionEvent newEntitySelectionEvent(
      MenuChoice currentMenuChoice) {
    return new PartnerSelectionEvent(this, currentMenuChoice, getSelectedEntity());
  }

  @Override
  public Map<String, SearchItem> searchItemValues() {
    return searchItemMap;
  }

}
