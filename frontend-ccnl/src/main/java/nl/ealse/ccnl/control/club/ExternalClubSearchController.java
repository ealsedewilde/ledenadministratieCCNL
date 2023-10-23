package nl.ealse.ccnl.control.club;

import java.util.Map;
import nl.ealse.ccnl.control.external.ExternalRelationSearchController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import nl.ealse.ccnl.service.relation.SearchItem;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class ExternalClubSearchController
    extends ExternalRelationSearchController<ExternalRelationClub> {

  private final PageController pageController;

  public ExternalClubSearchController(ApplicationEventPublisher eventPublisher,
      ExternalRelationService<ExternalRelationClub> externalRelationService,
      PageController pageController) {
    super(eventPublisher, externalRelationService);
    this.pageController = pageController;
  }

  @EventListener(condition = "#event.group('SEARCH_CLUB')")
  public void searchClub(MenuChoiceEvent event) {
    pageController.setActivePage(getPageReference());
    prepareSearch(event);
  }

  @Override
  protected void initializeSearchItems() {
    Map<String, SearchItem> map = getSearchItemValues();
    map.put("Club id (82xx)", SearchItem.values()[0]);
    map.put("Naam club", SearchItem.values()[1]);
    map.put("Straat", SearchItem.values()[2]);
    map.put("Postcode", SearchItem.values()[3]);
    map.put("Woonplaats", SearchItem.values()[4]);
  }

  @Override
  protected ExternalClubSelectionEvent newEntitySelectionEvent(MenuChoice currentMenuChoice) {
    return new ExternalClubSelectionEvent(this, currentMenuChoice, getSelectedEntity());
  }

  @Override
  protected String headerText(MenuChoice currentMenuChoice) {
    return "Opzoeken club";
  }

  @Override
  protected String columnName(int ix) {
    return "Club nr.";
  }

  @Override
  protected String resultHeaderText(MenuChoice currentMenuChoice) {
    return "Gevonden Clubs";
  }

}
