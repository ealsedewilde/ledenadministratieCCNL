package nl.ealse.ccnl.form;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import lombok.Getter;

/**
 * Helper to control the various pages of a form.
 */
public abstract class FormPages<C extends FormController> {

  private final Hyperlink[] links;
  private final Label[] labels;
  protected final FormPane[] formPages;

  protected final C controller;

  @Getter
  private int currentPage;

  private final int maxPageIndex;

  /**
   * Constructor.
   * @param numberOfPages of a form
   * @param controller of the form
   */
  public FormPages(int numberOfPages, C controller) {
    this.controller = controller;
    this.maxPageIndex = numberOfPages - 1;
    this.links = new Hyperlink[numberOfPages];
    this.labels = new Label[numberOfPages];
    this.formPages = new FormPane[numberOfPages];
  }

  /**
   * Show the requested form page.
   * @param pageIndex
   */
  public void setActiveFormPage(int pageIndex) {
    controller.validateForm();
    resetFormMenu();
    controller.getFormMenu().getChildren().set(pageIndex + 1, labels[pageIndex]);
    handleButtons(pageIndex);
    controller.getFormPage().getChildren().set(1, formPages[pageIndex]);
    setFocus(pageIndex);
    currentPage = pageIndex;
  }

  private void resetFormMenu() {
    List<Node> menuItems = controller.getFormMenu().getChildren();
    for (int ix = 0; ix < links.length; ix++) {
      menuItems.set(ix + 1, links[ix]);
    }
  }
  
  private void handleButtons(int pageIndex) {
    List<Node> buttons = controller.getFormButtons().getChildren();
    if (pageIndex == maxPageIndex) {
      buttons.remove(controller.getNextButton());
    } else if (!buttons.contains(controller.getNextButton())) {
      buttons.add(1, controller.getNextButton());
    }
    if (pageIndex == 0) {
      buttons.remove(controller.getPreviousButton());
    } else if (!buttons.contains(controller.getPreviousButton())) {
      buttons.add(0, controller.getPreviousButton());
    }
  }


  protected void addMenuItem(int index, String text) {
    Hyperlink link = new Hyperlink();
    link.setText(text);
    link.setOnAction(evt -> setActiveFormPage(index));
    controller.getFormMenu().getChildren().add(link);
    links[index] = link;

    Label label = new Label();
    label.setText(text);
    labels[index] = label;
  }

  protected abstract void setFocus(int pageIndex);

}
