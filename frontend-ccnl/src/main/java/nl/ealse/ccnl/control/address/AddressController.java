package nl.ealse.ccnl.control.address;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;
import nl.ealse.ccnl.mappers.InvalidAddressMapper;
import nl.ealse.ccnl.view.AddressView;
import nl.ealse.javafx.mapping.Mapping;
import nl.ealse.javafx.util.ContentUpdate;
import org.springframework.stereotype.Controller;

@Getter
@Setter
@Controller
public class AddressController extends AddressView {

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

  @FXML
  @Mapping(ignore = true)
  private Label headerText;

  /**
   * Perform some formatting before processing the form data.
   */
  public void enrich() {

    ContentUpdate.firstCapital(getStreet());
    ContentUpdate.firstCapital(getCity());

   String country = getCountry().getText();
    if ((country == null || country.isEmpty())) {
      ContentUpdate.formatPostalCode(getPostalCode());
    } else {
      ContentUpdate.firstCapital(getCountry());
    }

  }

}
