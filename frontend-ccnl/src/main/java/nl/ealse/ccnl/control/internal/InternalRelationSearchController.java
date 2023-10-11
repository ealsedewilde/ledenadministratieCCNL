package nl.ealse.ccnl.control.internal;

import java.util.List;
import java.util.Map;
import nl.ealse.ccnl.control.SearchController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.service.relation.InternalRelationService;
import nl.ealse.ccnl.service.relation.SearchItem;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class InternalRelationSearchController
    extends SearchController<InternalRelation, InternalRelationSelectionEvent> {

  private final PageController pageController;
  private final InternalRelationService internalRelationService;

  public InternalRelationSearchController(ApplicationEventPublisher eventPublisher,
      InternalRelationService internalRelationService, PageController pageController) {
    super(eventPublisher);
    this.pageController = pageController;
    this.internalRelationService = internalRelationService;
    this.initializeSearchItems();
  }

  @EventListener(condition = "#event.group('SEARCH_INTERNAL')")
  public void searchInternalRelation(MenuChoiceEvent event) {
    pageController.setActivePage(PageName.INTERNAL_RELATION_SEARCH);
    prepareSearch(event);
  }

  private void initializeSearchItems() {
    Map<String, SearchItem> map = getSearchItemValues();
    map.put("Functie", SearchItem.values()[1]);
    map.put("Interne relatie id", SearchItem.values()[0]);
    map.put("Straat", SearchItem.values()[2]);
    map.put("Postcode", SearchItem.values()[3]);
    map.put("Woonplaats", SearchItem.values()[4]);
  }

  @Override
  public InternalRelationSelectionEvent newEntitySelectionEvent(MenuChoice currentMenuChoice) {
    return new InternalRelationSelectionEvent(this, currentMenuChoice, getSelectedEntity());
  }

  @Override
  public List<InternalRelation> doSearch(SearchItem searchItem, String value) {
    return internalRelationService.searchInternalRelation(searchItem, value);
  }

}
