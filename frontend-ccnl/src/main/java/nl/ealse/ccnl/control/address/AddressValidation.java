package nl.ealse.ccnl.control.address;

import java.util.function.Consumer;
import lombok.Setter;
import nl.ealse.ccnl.validation.AddressNumberValidator;
import nl.ealse.ccnl.validation.CompositeValidator;
import nl.ealse.ccnl.validation.PostcodeValidator;
import nl.ealse.ccnl.view.AddressView;

public class AddressValidation extends CompositeValidator {

  @Setter
  private AddressView addressController;

  /**
   * Initialize this validation with the JavaFX controls.
   * @param vc the callback target for the validation result
   */
  public void initializeValidation(Consumer<Boolean> vc) {
    setCallback(vc);
    if (addressController.getStreet() != null) {
      required(addressController.getStreet(), addressController.getStreetE());
      AddressNumberValidator addressNumberValidator = new AddressNumberValidator(
          addressController.getAddressNumber(), addressController.getAddressNumberE());
      addValidator(addressController.getAddressNumber(), addressNumberValidator);
      required(addressController.getCity(), addressController.getCityE());

      PostcodeValidator postcodeValidator = new PostcodeValidator(addressController.getCountry(),
          addressController.getPostalCode(), addressController.getPostalCodeE());
      addValidator(addressController.getCountry(), postcodeValidator);
      addValidator(addressController.getPostalCode(), postcodeValidator);
    }
  }

  /**
   * Avoid showing too much validation messages. Required field validations are reset to non
   * visible.
   */
  @Override
  protected void resetErrorMessages() {
    if (addressController.getStreet() != null) {
      addressController.getStreetE().setVisible(false);
      addressController.getAddressNumberE().setVisible(false);
      addressController.getCityE().setVisible(false);
      String address = addressController.getStreet().getText();
      if (address == null || address.isEmpty()) {
        // assume the page is in its initial state.
        addressController.getPostalCodeE().setVisible(false);
      }
    }
  }


}
