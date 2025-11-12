package nl.ealse.ccnl.control;

import javafx.scene.control.Control;

public interface SearchField  {
  
  String getSearchText();
  
  void reset();
  
  void requestFocus();
  
  Control getAsControl();
  
}
