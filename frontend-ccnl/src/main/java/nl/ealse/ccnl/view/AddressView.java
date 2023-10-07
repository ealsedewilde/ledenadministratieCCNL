package nl.ealse.ccnl.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;
import nl.ealse.ccnl.mappers.InvalidAddressMapper;
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

}
