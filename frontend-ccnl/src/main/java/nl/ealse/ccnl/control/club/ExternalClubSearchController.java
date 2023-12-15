package nl.ealse.ccnl.control.club;

import java.util.Map;
import lombok.Getter;
import nl.ealse.ccnl.control.external.ExternalRelationSearchController;
import nl.ealse.ccnl.control.menu.ChoiceGroup;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.service.relation.ExternalClubService;
import nl.ealse.ccnl.service.relation.SearchItem;

public class ExternalClubSearchController
    extends ExternalRelationSearchController<ExternalRelationClub> {
  
  @Getter
  private static final ExternalClubSearchController instance = new ExternalClubSearchController();

  private final PageController pageController;

  private ExternalClubSearchController() {
    super(ExternalClubService.getInstance());
    this.pageController = PageController.getInstance();
  }

  @EventListener(choiceGroup = ChoiceGroup.SEARCH_CLUB)
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
