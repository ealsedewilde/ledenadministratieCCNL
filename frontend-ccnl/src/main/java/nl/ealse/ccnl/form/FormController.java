package nl.ealse.ccnl.form;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.Getter;
import nl.ealse.javafx.mapping.Mapping;

@Getter
public abstract class FormController {

  @FXML
  @Mapping(ignore = true)
  private Pane formMenu;

  @FXML
  @Mapping(ignore = true)
  private Pane formPage;

  @FXML
  @Mapping(ignore = true)
  private Pane formButtons;

  @FXML
  @Mapping(ignore = true)
  protected Label headerText;

  @FXML
  @Mapping(ignore = true)
  private Button nextButton;

  @FXML
  @Mapping(ignore = true)
  private Button previousButton;

  @FXML
  @Mapping(ignore = true)
  protected Button saveButton;
  
  /**
   * Validate this form.
   */
  public void validateForm() {
    throw new UnsupportedOperationException("Override this method!");
  }


}
