package nl.ealse.ccnl.control.internal;

import java.util.List;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.HBox;
import nl.ealse.ccnl.control.SearchController;
import nl.ealse.ccnl.control.SearchField;
import nl.ealse.ccnl.control.SearchTextField;
import nl.ealse.ccnl.control.menu.ChoiceGroup;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.service.relation.InternalRelationService;
import nl.ealse.ccnl.service.relation.SearchItem;

public class InternalRelationSearchController
    extends SearchController<InternalRelation, InternalRelationSelectionEvent> {

  private final PageController pageController;
  private final InternalRelationService internalRelationService;

  public InternalRelationSearchController(PageController pageController,
      InternalRelationService internalRelationService) {
    this.pageController = pageController;
    this.internalRelationService = internalRelationService;
  }

  @EventListener(choiceGroup = ChoiceGroup.SEARCH_INTERNAL)
  public void searchInternalRelation(MenuChoiceEvent event) {
    pageController.setActivePage(getPageReference());
    prepareSearch(event);
  }



  @Override
  protected void initializeSearchItems() {
    getSearchCriterium().getSelectionModel().selectedIndexProperty()
        .addListener(new SearchChangeListener(this));
    Map<String, SearchItem> map = getSearchItemValues();
    map.put("Functie", SearchItem.values()[1]);
    map.put("Interne relatie id", SearchItem.values()[0]);
    map.put("Straat", SearchItem.values()[2]);
    map.put("Postcode", SearchItem.values()[3]);
    map.put("Woonplaats", SearchItem.values()[4]);
  }

  @Override
  protected InternalRelationSelectionEvent newEntitySelectionEvent(MenuChoice currentMenuChoice) {
    return new InternalRelationSelectionEvent(this, currentMenuChoice, getSelectedEntity());
  }

  @Override
  protected List<InternalRelation> doSearch(SearchItem searchItem, String value) {
    return internalRelationService.searchInternalRelation(searchItem, value);
  }

  @Override
  protected String headerText(MenuChoice currentMenuChoice) {
    return "Opzoeken functie";
  }

  @Override
  protected String columnName(int ix) {
    return "Intern nr.";
  }

  @Override
  protected String resultHeaderText(MenuChoice currentMenuChoice) {
    return "Gevonden functies";
  }

  private static class SearchChangeListener implements ChangeListener<Number> {

    private final SearchController<?, ?> searchController;
    private final SearchTextField searchTextField;
    private final SearchChoiceBox searchChoiceBox;
    private final HBox parent;

    public SearchChangeListener(SearchController<?, ?> searchController) {
      this.searchController = searchController;
      this.searchTextField = (SearchTextField) searchController.getSearchField().getAsControl();
      this.searchChoiceBox = new SearchChoiceBox(searchTextField);
      this.parent = (HBox) searchTextField.getParent();
      parent.getChildren().set(3, searchChoiceBox);
      searchController.setSearchField(searchChoiceBox);
    }

    @Override
    public void changed(ObservableValue<? extends Number> o, Number oldIx, Number newIx) {
      SearchField sf;
      if (newIx.intValue() == 0) {
        sf = searchChoiceBox;
        searchChoiceBox.reset();
      } else {
        sf = searchTextField;
      }
      parent.getChildren().set(3, sf.getAsControl());
      searchController.setSearchField(sf);
    }

  }

}
