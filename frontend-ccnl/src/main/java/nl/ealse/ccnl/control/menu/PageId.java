package nl.ealse.ccnl.control.menu;

import lombok.Getter;

/**
 * Link between uniqueid for a page and its fxml-file. It is possible that the same fxml is used in
 * more than once scene. That situation requires the fxml-file to be loaded more then once Each
 * instance of the fxml-file will have its own unique page key.
 * 
 * @author ealse
 */
@Getter
public class PageId {

  /**
   * Unique id for a page in the application.
   */
  private final String pagekey;

  /**
   * Name of the fxml-file for the page.
   */
  private final String fxmlName;

  public PageId(String pagekey, String fxmlName) {
    this.pagekey = pagekey;
    this.fxmlName = fxmlName;
  }

}
