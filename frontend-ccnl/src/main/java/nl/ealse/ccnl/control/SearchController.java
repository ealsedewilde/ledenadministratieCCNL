package nl.ealse.ccnl.control;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import nl.ealse.ccnl.MainStage;
import nl.ealse.ccnl.control.button.ImageCell;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageReference;
import nl.ealse.ccnl.event.EntitySelectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventPublisher;
import nl.ealse.ccnl.service.relation.SearchItem;
import nl.ealse.javafx.FXMLLoaderUtil;

/**
 * Super class for all controllers for searching a relation in the database.
 *
 * @author ealse
 * @param <T> - relation type to search
 * @param <E> - event for the selected relation
 */
public abstract class SearchController<T, E extends EntitySelectionEvent<T>> {

  @FXML
  private Label headerText;

  @FXML
  private Label resultHeader;

  @FXML
  private ChoiceBox<String> searchCriterium;

  @FXML
  private TextField searchField;

  @FXML
  private Label errorMessage;

  @FXML
  private VBox result;

  @FXML
  private TableView<T> tableView;

  @FXML
  private TableColumn<T, Void> buttonColumn;

  @Getter
  private final Map<String, SearchItem> searchItemValues = new LinkedHashMap<>();

  @Getter
  @Setter
  private T selectedEntity;

  @Getter
  private MenuChoice currentMenuChoice;
  
  private Parent parent;

  protected SearchController() {
    this.parent = FXMLLoaderUtil.getPage("search/search", this);
  }

  @FXML
  void initialize() {
    initializeSearchItems();
    searchCriterium.getItems().addAll(searchItemValues.keySet());
    searchCriterium.getSelectionModel().selectFirst();

    tableView.setPlaceholder(new Label("Geen gegevens gevonden"));
    tableView.getColumns().get(0).setText(columnName(0));

    // define an event when a row is clicked
    RowFactory<T> rf = new RowFactory<>();
    rf.setOnMouseClicked(this::handleSelected);
    tableView.setRowFactory(rf);

    // define an event when a questionmark column is clicked
    buttonColumn.setCellFactory(param -> new ImageCell<T>("question.png", this::extraInfo));
  }

  /**
   * Search for models according to search criteria.
   */
  @FXML
  public void search() {
    SearchItem searchItem = searchItemValues.get(searchCriterium.getValue());
    if (validate(searchItem, searchField.getText())) {
      String searchValue = searchField.getText().trim();
      searchValue = formatPostalCode(searchItem, searchValue);
      List<T> searchResult = doSearch(searchItem, searchValue);
      if (searchResult.size() == 1) {
        selectedEntity = searchResult.get(0);
        handleSelected(null);
      } else {
        tableView.getItems().clear();
        tableView.getItems().addAll(searchResult);
        result.setVisible(true);
      }
    }
  }

  /**
   * Reset the search page to its initial state.
   */
  @FXML
  public void reset() {
    searchCriterium.getSelectionModel().selectFirst();
    searchField.setText(null);
    resetResult();
  }
  
  /**
   * Reset the search page result to its initial state.
   */
  @FXML
  public void resetResult() {
    tableView.getItems().clear();
    result.setVisible(false);
    searchField.requestFocus();
    errorMessage.setVisible(false);
  }

  /**
   * Re-initialize the SearchPane.
   *
   * @param event fired due to a menu choice
   */
  protected void prepareSearch(MenuChoiceEvent event) {
    this.currentMenuChoice = event.getMenuChoice();
    if (headerText != null) {
      headerText.setText(headerText(currentMenuChoice));
    }
    if (resultHeader != null) {
      resultHeader.setText(resultHeaderText(currentMenuChoice));
    }
    reset();
  }
  
  protected PageReference getPageReference() {
    return () -> parent;
  }

  /**
   * Perform the actual search operation.
   *
   * @param searchItem type of information to search for
   * @param value to search for
   * @return the selected model objects that match the search (if any)
   */
  protected abstract List<T> doSearch(SearchItem searchItem, String value);

  protected abstract E newEntitySelectionEvent(MenuChoice currentMenuChoice);
  
  protected abstract void initializeSearchItems();

  protected abstract String headerText(MenuChoice currentMenuChoice);

  protected abstract String resultHeaderText(MenuChoice currentMenuChoice);

  protected abstract String columnName(int ix);

  /**
   * Invoked when question mark column is clicked. (Invoked after selectedMember is set.)
   *
   * @param event fire when a question mark cell in the <code>TableView</code> is clicked
   */
  public void extraInfo(MouseEvent event) {
    event.consume(); // stop further propagation to handleSelected()
    Alert alert = new Alert(AlertType.INFORMATION);
    Stage cs = (Stage) alert.getDialogPane().getScene().getWindow();
    cs.getIcons().add(MainStage.getIcon());

    alert.setTitle("Extra Info");
    alert.setHeaderText("Niet van toepassing");
    alert.showAndWait();
  }

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
    EventPublisher.publishEvent(newEntitySelectionEvent(currentMenuChoice));
  }

  private boolean validate(SearchItem searchItem, String searchValue) {
    errorMessage.setVisible(false);
    result.setVisible(false);
    StringJoiner errors = new StringJoiner("\n");
    if (searchItem == null) {
      errors.add("Selecteer een zoekcriterium.");
    }
    if (searchValue == null || searchValue.isBlank()) {
      errors.add("Vul het zoekgegeven.");
    } else if (SearchItem.NUMBER == searchItem && !searchValue.matches("^[1-9]\\d{0,3}")) {
      errors.add("Zoekgegeven moet een nummer zijn van maximaal 4 cijfers.");
    }

    if (errors.length() > 0) {
      errorMessage.setText(errors.toString());
      errorMessage.setVisible(true);
      return false;
    }
    return true;
  }

  private String formatPostalCode(SearchItem searchItem, String searchValue) {
    if (SearchItem.POSTAL_CODE == searchItem && searchValue.length() == 6) {
      int letters = 0;
      int digits = 0;
      for (char ch : searchValue.toCharArray()) {
        if (Character.isLetter(ch)) {
          letters++;
        } else if (Character.isDigit(ch)) {
          digits++;
        }
      }
      if (digits == 4 && letters == 2) {
        return searchValue.substring(0, 4) + " " + searchValue.substring(4);
      }
    }
    return searchValue;
  }

}
