package nl.ealse.ccnl.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import nl.ealse.ccnl.MainStage;
import nl.ealse.ccnl.mappers.InvalidAddressMapper;
import nl.ealse.javafx.ImagesMap;
import nl.ealse.javafx.mapping.Mapping;
import nl.ealse.javafx.util.ContentUpdate;

@Getter
@Setter
public abstract class AddressView {

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

  @FXML
  @Mapping(ignore = true)
  private Label streetE;

  @FXML
  @Mapping(ignore = true)
  private Label addressNumberE;

  @FXML
  @Mapping(ignore = true)
  private Label postalCodeE;

  @FXML
  @Mapping(ignore = true)
  private Label cityE;

  @FXML
  @Mapping(propertyMapper = InvalidAddressMapper.class)
  private Label addressInvalid;

  /**
   * Perform some formatting before processing the form data.
   */
  public void enrichAddress() {

    ContentUpdate.firstCapital(getStreet());
    ContentUpdate.firstCapital(getCity());

    String countryText = getCountry().getText();
    if ((countryText == null || countryText.isEmpty())) {
      ContentUpdate.formatPostalCode(getPostalCode());
    } else {
      ContentUpdate.firstCapital(getCountry());
    }

  }
  
  public void validateAddress() {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    Stage cs = (Stage) alert.getDialogPane().getScene().getWindow();
    cs.getIcons().add(MainStage.getIcon());
    alert.setTitle("Is huidige adres correct?");
    alert.setHeaderText("Bevestig huidige adres met 'Opslaan' knop");
    ImageView image = new ImageView(ImagesMap.get("approve.png"));
    alert.setGraphic(image);
    Button b = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
    b.setText("Annuleren");
    alert.showAndWait().filter(response -> response == ButtonType.OK)
    .ifPresent(response -> addressInvalid.setText(""));
  }

}
