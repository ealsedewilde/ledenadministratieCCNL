package nl.ealse.ccnl.control;

import javafx.scene.control.Control;
import javafx.scene.control.TextField;

/**
 * Default implementation for search value.
 */
public class SearchTextField extends TextField implements SearchField {

  @Override
  public String getSearchText() {
    // TODO Auto-generated method stub
    return getText();
  }

  @Override
  public void reset() {
    setText(null);

  }

  @Override
  public Control getAsControl() {
    return this;
  }



}
