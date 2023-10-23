package nl.ealse.ccnl.control;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.EntitySelectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.relation.SearchItem;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Super class for all controllers for searching a relation in the database.
 *
 * @author ealse
 * @param <T> - relation type to search
 * @param <E> - event for the selected relation
 */
public abstract class SearchController<T, E extends EntitySelectionEvent<T>> {

  private final ApplicationEventPublisher eventPublisher;

  @Getter
  private SearchPane<T, E> searchPane;

  @Getter
  private final Map<String, SearchItem> searchItemValues = new LinkedHashMap<>();

  @Getter
  @Setter
  private T selectedEntity;

  @Getter
  private MenuChoice currentMenuChoice;

  protected SearchController(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  protected void initialize(SearchPane<T, E> searchPane) {
    searchPane.initialize(this);
    this.searchPane = searchPane;
  }

  /**
   * Reset the search page to its initial state.
   */
  @FXML
  void reset() {
    searchPane.reset();
  }

  /**
   * Re-initialize the SearchPane.
   *
   * @param event fired due to a menu choice
   */
  protected void prepareSearch(MenuChoiceEvent event) {
    this.currentMenuChoice = event.getMenuChoice();
    if (this.searchPane != null) {
      searchPane.updateHeaderText(currentMenuChoice);
      searchPane.reset();
    }
  }

  /**
   * Perform the actual search operation.
   *
   * @param searchItem type of information to search for
   * @param value to search for
   * @return the selected model objects that match the search (if any)
   */
  protected abstract List<T> doSearch(SearchItem searchItem, String value);

  /**
   * Invoked when question mark column is clicked. (Invoked after selectedMember is set.)
   *
   * @param event fire when a question mark cell in the <code>TableView</code> is clicked
   */
  public void extraInfo(MouseEvent event) {
    event.consume(); // stop further propagation to handleSelected()
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Extra Info");
    alert.showAndWait();
  }

  protected abstract E newEntitySelectionEvent(MenuChoice currentMenuChoice);


  /**
   * Invoked when NON question mark column is clicked.
   *
   * @param event fire when a <code>Tableview</code> row is clicked
   */
  public void handleSelected(MouseEvent event) {
    if (event != null) {
      // event is null when only one search result is found.
      // In that case selectedEntity is previously injected via setter
      // See: SearchPane
      @SuppressWarnings("unchecked")
      TableRow<T> row = (TableRow<T>) event.getSource();
      selectedEntity = row.getItem();
    }
    eventPublisher.publishEvent(newEntitySelectionEvent(currentMenuChoice));
  }



}
