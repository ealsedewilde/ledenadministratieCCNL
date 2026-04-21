package nl.ealse.ccnl.control.menu;

import javafx.scene.Parent;

/**
 * Reference to a loaded fxml page.
 */
@FunctionalInterface
public interface PageReference {

  /**
   * Get the UI component.
   * 
   * @return requested UI component
   */
  public Parent getPage();

}
