package nl.ealse.ccnl.control.other;

import java.util.Map;
import nl.ealse.ccnl.control.external.ExternalRelationSearchController;
import nl.ealse.ccnl.control.menu.ChoiceGroup;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.ccnl.service.relation.ExternalOtherService;
import nl.ealse.ccnl.service.relation.SearchItem;

public class ExternalOtherSearchController
    extends ExternalRelationSearchController<ExternalRelationOther> {

  private final PageController pageController;

  public ExternalOtherSearchController(PageController pageController, ExternalOtherService service) {
    super(service);
    this.pageController = pageController;
  }

  @EventListener(choiceGroup = ChoiceGroup.SEARCH_EXTERNAL)
  public void search(MenuChoiceEvent event) {
    pageController.setActivePage(getPageReference());
    prepareSearch(event);
  }

  @Override
  protected void initializeSearchItems() {
    Map<String, SearchItem> map = getSearchItemValues();
    map.put("Relatie id (84xx)", SearchItem.values()[0]);
    map.put("Naam externe relatie", SearchItem.values()[1]);
    map.put("Straat", SearchItem.values()[2]);
    map.put("Postcode", SearchItem.values()[3]);
    map.put("Woonplaats", SearchItem.values()[4]);
  }

  @Override
  protected ExternalOtherSelectionEvent newEntitySelectionEvent(MenuChoice currentMenuChoice) {
    return new ExternalOtherSelectionEvent(this, currentMenuChoice, getSelectedEntity());
  }

  @Override
  protected String headerText(MenuChoice curentContext) {
    return "Opzoeken externe relatie";
  }

  @Override
  protected String columnName(int ix) {
    return "Relatie nr.";
  }

  @Override
  protected String resultHeaderText(MenuChoice currentMenuChoice) {
    return "Gevonden externe relaties";
  }

}
