package nl.ealse.ccnl.control.club;

import java.util.LinkedHashMap;
import java.util.Map;
import nl.ealse.ccnl.control.external.ExternalRelationSearchController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import nl.ealse.ccnl.service.relation.SearchItem;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class ExternalClubSearchController
    extends ExternalRelationSearchController<ExternalRelationClub> {

  private final Map<String, SearchItem> searchItemMap;

  public ExternalClubSearchController(ApplicationContext springContext,
      ExternalRelationService<ExternalRelationClub> externalRelationService) {
    super(springContext, externalRelationService, MenuChoice.AMEND_EXTERNAL_CLUB,
        MenuChoice.DELETE_EXTERNAL_CLUB);
    this.searchItemMap = initializeSearchItems();
  }

  private Map<String, SearchItem> initializeSearchItems() {
    Map<String, SearchItem> map = new LinkedHashMap<>();
    map.put("Externe relatie id", SearchItem.values()[0]);
    map.put("Naam club", SearchItem.values()[1]);
    map.put("Straat", SearchItem.values()[2]);
    map.put("Postcode", SearchItem.values()[3]);
    map.put("Woonplaats", SearchItem.values()[4]);
    return map;
  }

  @Override
  public ExternalClubSelectionEvent newEntitySelectionEvent(MenuChoice currentMenuChoice) {
    return new ExternalClubSelectionEvent(this, currentMenuChoice, getSelectedEntity());
  }

  @Override
  public Map<String, SearchItem> searchItemValues() {
    return searchItemMap;
  }


}
