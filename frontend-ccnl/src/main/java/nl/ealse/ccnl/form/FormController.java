package nl.ealse.ccnl.form;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public interface FormController {
  
  Pane getFormMenu();
  
  Pane getFormPage();
  
  Pane getFormButtons();
  
  Button getNextButton();
  
  Button getPreviousButton();
  
  void validateForm();

}
