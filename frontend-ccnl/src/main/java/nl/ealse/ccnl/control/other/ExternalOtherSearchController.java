package nl.ealse.ccnl.control.other;

import java.util.LinkedHashMap;
import java.util.Map;
import nl.ealse.ccnl.control.external.ExternalRelationSearchController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import nl.ealse.ccnl.service.relation.SearchItem;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class ExternalOtherSearchController
    extends ExternalRelationSearchController<ExternalRelationOther> {

  private final Map<String, SearchItem> searchItemMap;

  protected ExternalOtherSearchController(ApplicationContext springContext,
      ExternalRelationService<ExternalRelationOther> externalOtherService) {
    super(springContext, externalOtherService, MenuChoice.AMEND_EXTERNAL_RELATION,
        MenuChoice.DELETE_EXTERNAL_RELATION);
    this.searchItemMap = initializeSearchItems();
  }

  private Map<String, SearchItem> initializeSearchItems() {
    Map<String, SearchItem> map = new LinkedHashMap<>();
    map.put("Externe relatie id", SearchItem.values()[0]);
    map.put("Naam externe relatie", SearchItem.values()[1]);
    map.put("Straat", SearchItem.values()[2]);
    map.put("Postcode", SearchItem.values()[3]);
    map.put("Woonplaats", SearchItem.values()[4]);
    return map;
  }

  @Override
  public ExternalOtherSelectionEvent newEntitySelectionEvent(
      MenuChoice currentMenuChoice) {
    return new ExternalOtherSelectionEvent(this, currentMenuChoice, getSelectedEntity());
  }

  @Override
  public Map<String, SearchItem> searchItemValues() {
    return searchItemMap;
  }

}
