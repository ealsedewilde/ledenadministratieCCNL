package nl.ealse.ccnl.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;

@Getter
public class ExternalRelationDeleteView {

  @FXML
  private Label relationNumber;

  @FXML
  private TextField relationName;

  @FXML
  private TextField contactName;

  @FXML
  private TextField contactNamePrefix;

  @FXML
  private TextField email;

  @FXML
  private TextField telephoneNumber;

}
