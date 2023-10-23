package nl.ealse.ccnl.control;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import nl.ealse.ccnl.control.button.ImageCell;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageReference;
import nl.ealse.ccnl.event.EntitySelectionEvent;
import nl.ealse.ccnl.service.relation.SearchItem;
import nl.ealse.javafx.FXMLLoaderBean;

/**
 * Generic pane to seach a relation.
 *
 * @author ealse
 *
 * @param <T> - relation type to search
 * @param <E> - event for the selected relation
 */
public abstract class SearchPane<T, E extends EntitySelectionEvent<T>> {

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

  private SearchController<T, E> controller;

  private Map<String, SearchItem> searchItems;
  
  private final Parent parent;

  protected SearchPane() {
    parent = FXMLLoaderBean.getPage("search/search", this);
  }

  /**
   * Search for models according to search criteria.
   */
  @FXML
  public void search() {
    SearchItem searchItem = searchItems.get(searchCriterium.getValue());
    if (validate(searchItem, searchField.getText())) {
      String searchValue = searchField.getText().trim();
      searchValue = formatPostalCode(searchItem, searchValue);
      List<T> searchResult = controller.doSearch(searchItem, searchValue);
      if (searchResult.size() == 1) {
        controller.setSelectedEntity(searchResult.get(0));
        controller.handleSelected(null);
      } else {
        tableView.getItems().clear();
        tableView.getItems().addAll(searchResult);
        result.setVisible(true);
      }
    }
  }

  /**
   * Reset this page to its initial state.
   */
  public void reset() {
    tableView.getItems().clear();
    result.setVisible(false);
    searchCriterium.getSelectionModel().selectFirst();
    searchField.setText(null);
    searchField.requestFocus();
    errorMessage.setVisible(false);
  }

  /**
   * The same (already initialized) page can be used in a different contexts. The header text must
   * reflect the actual context.
   *
   * @param currentMenuChoice the choice that steers the text selection
   */
  public void updateHeaderText(MenuChoice currentMenuChoice) {
    if (headerText != null) {
      headerText.setText(headerText(currentMenuChoice));
    }
    if (resultHeader != null) {
      resultHeader.setText(resultHeaderText(currentMenuChoice));
    }
  }

  /**
   * Initialize this abstract JavaFX-container.
   *
   * @param controller the controller for this Pane
   */
  public void initialize(SearchController<T, E> controller) {
    this.controller = controller;
    searchItems = controller.getSearchItemValues();
    searchCriterium.getItems().addAll(searchItems.keySet());
    searchCriterium.getSelectionModel().selectFirst();

    tableView.setPlaceholder(new Label("Geen gegevens gevonden"));
    tableView.getColumns().get(0).setText(columnName(0));

    // define an event when a row is clicked
    RowFactory<T> rf = new RowFactory<>();
    rf.setOnMouseClicked(controller::handleSelected);
    tableView.setRowFactory(rf);

    // define an event when a questionmark column is clicked
    buttonColumn.setCellFactory(param -> new ImageCell<T>("question.png", controller::extraInfo));
  }
  
  public PageReference getPageReference() {
    return () -> parent;
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

  protected abstract String headerText(MenuChoice currentMenuChoice);

  protected abstract String resultHeaderText(MenuChoice currentMenuChoice);

  protected abstract String columnName(int ix);


}
