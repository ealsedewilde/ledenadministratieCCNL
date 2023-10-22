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
import nl.ealse.ccnl.control.menu.PageReference;
import nl.ealse.ccnl.validation.CompositeValidator;
import nl.ealse.javafx.FXMLLoaderBean;

/**
 * Helper to control the a multiple page form.
 * <p>
 * A form consist of a frame page in which the various pages of the form are loaded.
 * The frame also ha a menu to select a page of the form.
 * </p>
 */
public abstract class FormController {

  /**
   * Menu to select a page of the form
   */
  @FXML
  private Pane formMenu;

  /**
   * Pane wherein a page of the form is loade.
   */
  @FXML
  private Pane formPage;

  /**
   * Pane for the various buttons to control the form.
   */
  @FXML
  private Pane formButtons;

  /**
   * Text showing the context of the form.
   */
  @FXML
  @Getter
  protected Label headerText;

  @FXML
  private Button nextButton;

  @FXML
  private Button previousButton;

  @FXML
  @Getter
  protected Button saveButton;

  @FXML
  @Getter
  protected Button undoButton;

  private final Hyperlink[] links;
  private final Label[] labels;
  protected final Parent[] formPageArray;

  /**
   * Form validator.
   */
  @Getter
  private final CompositeValidator validator;

  /**
   * The frame form.
   */
  private Parent form;

  /**
   * Zero based Index of the form page currently active
   */
  private int currentPage;

  /**
   * The the number of forma pages in the form.
   */
  private final int maxPageIndex;

  /**
   * Constructor.
   *
   * @param numberOfPages of a form
   * @param validator of the form
   */
  protected FormController(int numberOfPages, CompositeValidator validator) {
    this.validator = validator;
    this.maxPageIndex = numberOfPages - 1;
    this.links = new Hyperlink[numberOfPages];
    this.labels = new Label[numberOfPages];
    this.formPageArray = new Parent[numberOfPages];
  }

  /**
   * Initialize this FromController.
   */
  public void initializeForm() {
    this.form = FXMLLoaderBean.getPage("form/form", this);
    initializePages();
    validator.initialize();
    
    // The save button is only enabled when the for m is valid
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
   *
   * @param pageIndex - zero based index of the form page
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

  /**
   * Get reference to the load form fxml.
   *
   * @return the page reference
   */
  public PageReference getPageReference() {
    return () -> {
      return form;
    };
  }

  protected abstract void initializePages();

  protected abstract void setFocus(int pageIndex);

}
