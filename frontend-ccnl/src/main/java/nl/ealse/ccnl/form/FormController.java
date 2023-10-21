package nl.ealse.ccnl.form;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.Getter;
import nl.ealse.ccnl.validation.CompositeValidator;
import nl.ealse.javafx.FXMLLoaderBean;
import nl.ealse.javafx.mapping.Mapping;

/**
 * Helper to control the various pages of a form.
 */
public abstract class FormController {

  @FXML
  private Pane formMenu;

  @FXML
  private Pane formPage;

  @FXML
  private Pane formButtons;

  @FXML
  @Getter
  @Mapping(ignore = true)
  protected Label headerText;

  @FXML
  private Button nextButton;

  @FXML
  private Button previousButton;

  @FXML
  @Getter
  @Mapping(ignore = true)
  protected Button saveButton;

  @FXML
  @Getter
  @Mapping(ignore = true)
  protected Button undoButton;

  private final Hyperlink[] links;
  private final Label[] labels;
  protected final Parent[] formPageArray;

  @Getter
  private final CompositeValidator validator;
  
  @Getter
  private Parent form;

  private int currentPage;

  private final int maxPageIndex;

  /**
   * Constructor.
   * @param numberOfPages of a form
   * @param controller of the form
   */
  protected FormController(int numberOfPages, CompositeValidator validator) {
    this.validator = validator;
    this.maxPageIndex = numberOfPages - 1;
    this.links = new Hyperlink[numberOfPages];
    this.labels = new Label[numberOfPages];
    this.formPageArray = new Parent[numberOfPages];
  }
  
  public void initializeForm() {
    this.form = FXMLLoaderBean.getPage("form/form", this);
    initializePages();
    validator.initialize();
    validator.setCallback(valid -> saveButton.setDisable(!valid));
  }
  
  public void setOnSave(EventHandler<ActionEvent> handler) {
    saveButton.setOnAction(handler);
  }
  
  public void setOnReset(EventHandler<ActionEvent> handler) {
    undoButton.setOnAction(handler);
  }

  /**
   * Show the requested form page.
   * @param pageIndex
   */
  public void setActiveFormPage(int pageIndex) {
    validator.validate();
    resetFormMenu();
    formMenu.getChildren().set(pageIndex + 1, labels[pageIndex]);
    handleButtons(pageIndex);
    formPage.getChildren().set(1, formPageArray[pageIndex]);
    setFocus(pageIndex);
    currentPage = pageIndex;
  }

  @FXML
  public void nextPage() {
    setActiveFormPage(currentPage + 1);
  }

  @FXML
  public void previousPage() {
    setActiveFormPage(currentPage - 1);
  }

  private void resetFormMenu() {
    List<Node> menuItems = formMenu.getChildren();
    for (int ix = 0; ix < links.length; ix++) {
      menuItems.set(ix + 1, links[ix]);
    }
  }
  
  private void handleButtons(int pageIndex) {
    List<Node> buttons = formButtons.getChildren();
    if (pageIndex == maxPageIndex) {
      buttons.remove(nextButton);
    } else if (!buttons.contains(nextButton)) {
      buttons.add(1, nextButton);
    }
    if (pageIndex == 0) {
      buttons.remove(previousButton);
    } else if (!buttons.contains(previousButton)) {
      buttons.add(0, previousButton);
    }
  }

  protected void addMenuItem(int index, String text) {
    Hyperlink link = new Hyperlink();
    link.setText(text);
    link.setOnAction(evt -> setActiveFormPage(index));
    formMenu.getChildren().add(link);
    links[index] = link;

    Label label = new Label();
    label.setText(text);
    labels[index] = label;
  }
  
  protected abstract void initializePages();

  protected abstract void setFocus(int pageIndex);

}
