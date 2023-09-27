package nl.ealse.ccnl.control.other;

import java.util.Map;
import nl.ealse.ccnl.control.external.ExternalRelationSearchController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import nl.ealse.ccnl.service.relation.SearchItem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class ExternalOtherSearchController
    extends ExternalRelationSearchController<ExternalRelationOther> {

  private final PageController pageController;

  protected ExternalOtherSearchController(ApplicationContext springContext,
      ExternalRelationService<ExternalRelationOther> externalOtherService,
      PageController pageController) {
    super(springContext, externalOtherService);
    this.pageController = pageController;
    this.initializeSearchItems();
  }

  @EventListener(condition = "#event.group('SEARCH_EXTERNAL')")
  public void search(MenuChoiceEvent event) {
    pageController.setActivePage(PageName.EXTERNAL_RELATION_SEARCH);
    prepareSearch(event);
  }

  private void initializeSearchItems() {
    Map<String, SearchItem> map = getSearchItemValues();
    map.put("Relatie id (84xx)", SearchItem.values()[0]);
    map.put("Naam externe relatie", SearchItem.values()[1]);
    map.put("Straat", SearchItem.values()[2]);
    map.put("Postcode", SearchItem.values()[3]);
    map.put("Woonplaats", SearchItem.values()[4]);
  }

  @Override
  public ExternalOtherSelectionEvent newEntitySelectionEvent(MenuChoice currentMenuChoice) {
    return new ExternalOtherSelectionEvent(this, currentMenuChoice, getSelectedEntity());
  }

}
