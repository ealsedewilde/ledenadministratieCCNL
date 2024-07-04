package nl.ealse.ccnl.control.partner;

import java.util.Map;
import nl.ealse.ccnl.control.external.ExternalRelationSearchController;
import nl.ealse.ccnl.control.menu.ChoiceGroup;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import nl.ealse.ccnl.service.relation.CommercialPartnerService;
import nl.ealse.ccnl.service.relation.SearchItem;

public class PartnerSearchController
    extends ExternalRelationSearchController<ExternalRelationPartner> {

  private final PageController pageController;

  public PartnerSearchController(PageController pageController, CommercialPartnerService service) {
    super(service);
    this.pageController = pageController;
  }

  @EventListener(choiceGroup = ChoiceGroup.SEARCH_PARTNER)
  public void searchPartner(MenuChoiceEvent event) {
    pageController.setActivePage(getPageReference());
    prepareSearch(event);
  }

  @Override
  protected void initializeSearchItems() {
    Map<String, SearchItem> map = getSearchItemValues();
    map.put("Adverteerder id (85xx)", SearchItem.values()[0]);
    map.put("Naam adverteerder", SearchItem.values()[1]);
    map.put("Straat", SearchItem.values()[2]);
    map.put("Postcode", SearchItem.values()[3]);
    map.put("Woonplaats", SearchItem.values()[4]);
  }

  @Override
  protected PartnerSelectionEvent newEntitySelectionEvent(MenuChoice currentMenuChoice) {
    return new PartnerSelectionEvent(this, currentMenuChoice, getSelectedEntity());
  }

  @Override
  protected String headerText(MenuChoice curentContext) {
    return "Opzoeken adverteerder";
  }

  @Override
  protected String columnName(int ix) {
    return "Partner nr.";
  }

  @Override
  protected String resultHeaderText(MenuChoice currentMenuChoice) {
    return "Gevonden adverteerders";
  }

}
