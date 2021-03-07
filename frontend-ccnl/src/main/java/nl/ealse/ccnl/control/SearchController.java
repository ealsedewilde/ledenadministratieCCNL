package nl.ealse.ccnl.control;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.EntitySelectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.SearchItem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

/**
 * Super class for all controllers for searching a relation in the database.
 * 
 * @author ealse
 * @param <T> - relation type to search
 * @param <E> - event for the selected relation
 */
public abstract class SearchController<T, E extends EntitySelectionEvent<T>>
    implements ApplicationListener<MenuChoiceEvent> {

  private final ApplicationContext springContext;

  private final List<MenuChoice> validChoices;

  @Getter
  @Setter
  private T selectedEntity;

  @Getter
  private MenuChoice currentMenuChoice;

  @FXML
  private SearchPane<T, E> searchPane;

  protected SearchController(ApplicationContext springContext, MenuChoice... choices) {
    this.springContext = springContext;
    this.validChoices = Arrays.asList(choices);
  }

  @FXML
  public void initialize() {
    searchPane.initialize(this);
  }

  /**
   * Reset the search page to its initial state.
   */
  @FXML
  public void reset() {
    searchPane.reset();
  }

  /**
   * Re-initialize the SearchPane.
   * @param event fired due to a menu choice 
   */
  @Override
  public void onApplicationEvent(MenuChoiceEvent event) {
    if (validChoices.contains(event.getMenuChoice())) {
      this.currentMenuChoice = event.getMenuChoice();
      if (this.searchPane != null) {
        searchPane.updateHeaderText(currentMenuChoice);
        searchPane.reset();
      }
    }
  }

  /**
   * Perform the actual search operation.
   * 
   * @param searchItem type of information to search for
   * @param value to search for
   * @return the selected model objects that match the search (if any)
   */
  public abstract List<T> doSearch(SearchItem searchItem, String value);

  public abstract Map<String, SearchItem> searchItemValues();

  public abstract void extraInfo(MouseEvent event);

  public abstract E newEntitySelectionEvent(MenuChoice currentMenuChoice);


  /**
   * Invoked when NON question mark column is clicked.
   * 
   * @param event fire when a <code>Tableview</code> row is clicked
   */
  public void handleSelected(MouseEvent event) {
    if (event != null) {
      // event is null when only one search result.
      // In that case selectedEntity is previously injected via setter
      // See: SearchPane
      @SuppressWarnings("unchecked")
      TableRow<T> row = (TableRow<T>) event.getSource();
      selectedEntity = row.getItem();
    }
    springContext.publishEvent(newEntitySelectionEvent(currentMenuChoice));
  }



}
