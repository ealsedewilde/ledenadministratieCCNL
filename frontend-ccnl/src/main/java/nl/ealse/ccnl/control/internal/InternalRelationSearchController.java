package nl.ealse.ccnl.control.internal;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import nl.ealse.ccnl.control.SearchController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.service.relation.InternalRelationService;
import nl.ealse.ccnl.service.relation.SearchItem;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class InternalRelationSearchController
    extends SearchController<InternalRelation, InternalRelationSelectionEvent> {

  private final InternalRelationService internalRelationService;

  private final Map<String, SearchItem> searchItemMap;

  public InternalRelationSearchController(ApplicationContext springContext,
      InternalRelationService internalRelationService) {
    super(springContext, MenuChoice.AMEND_INTERNAL_RELATION, MenuChoice.DELETE_INTERNAL_RELATION);
    this.internalRelationService = internalRelationService;
    this.searchItemMap = initializeSearchItems();
  }

  private Map<String, SearchItem> initializeSearchItems() {
    Map<String, SearchItem> map = new LinkedHashMap<>();
    map.put("Functie", SearchItem.values()[1]);
    map.put("Interne relatie id", SearchItem.values()[0]);
    map.put("Straat", SearchItem.values()[2]);
    map.put("Postcode", SearchItem.values()[3]);
    map.put("Woonplaats", SearchItem.values()[4]);
    return map;
  }


  @Override
  public void extraInfo(MouseEvent event) {
    event.consume(); // stop further propagation to handleSelected()
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Extra Info");
    alert.showAndWait();
  }

  @Override
  public Map<String, SearchItem> searchItemValues() {
    return searchItemMap;
  }

  @Override
  public InternalRelationSelectionEvent newEntitySelectionEvent(MenuChoice currentMenuChoice) {
    return new InternalRelationSelectionEvent(this, currentMenuChoice, getSelectedEntity());
  }

  @Override
  public List<InternalRelation> doSearch(SearchItem searchItem, String value) {
    return internalRelationService.searchExternalRelation(searchItem, value);
  }

}
