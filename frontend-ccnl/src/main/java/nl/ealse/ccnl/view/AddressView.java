package nl.ealse.ccnl.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressView {

  @FXML
  private TextField street;

  @FXML
  private TextField addressNumber;

  @FXML
  private TextField addressNumberAppendix;

  @FXML
  private TextField postalCode;

  @FXML
  private TextField city;

  @FXML
  private TextField country;

}
