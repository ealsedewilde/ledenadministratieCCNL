package nl.ealse.ccnl.control.club;

import java.util.Map;
import nl.ealse.ccnl.control.external.ExternalRelationSearchController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import nl.ealse.ccnl.service.relation.SearchItem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class ExternalClubSearchController
    extends ExternalRelationSearchController<ExternalRelationClub> {

  private final PageController pageController;

  public ExternalClubSearchController(ApplicationContext springContext,
      ExternalRelationService<ExternalRelationClub> externalRelationService,
      PageController pageController) {
    super(springContext, externalRelationService);
    this.pageController = pageController;
    this.initializeSearchItems();
  }

  @EventListener(condition = "#event.group('SEARCH_CLUB')")
  public void searchClub(MenuChoiceEvent event) {
    pageController.setActivePage(PageName.EXTERNAL_CLUB_SEARCH);
    prepareSearch(event);
  }

  private void initializeSearchItems() {
    Map<String, SearchItem> map = getSearchItemValues();
    map.put("Club id (82xx)", SearchItem.values()[0]);
    map.put("Naam club", SearchItem.values()[1]);
    map.put("Straat", SearchItem.values()[2]);
    map.put("Postcode", SearchItem.values()[3]);
    map.put("Woonplaats", SearchItem.values()[4]);
  }

  @Override
  public ExternalClubSelectionEvent newEntitySelectionEvent(MenuChoice currentMenuChoice) {
    return new ExternalClubSelectionEvent(this, currentMenuChoice, getSelectedEntity());
  }

}
