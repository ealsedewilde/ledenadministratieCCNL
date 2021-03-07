package nl.ealse.ccnl.control.address;

import java.util.function.Consumer;
import lombok.Setter;
import nl.ealse.ccnl.validation.AddressNumberValidator;
import nl.ealse.ccnl.validation.CompositeValidator;
import nl.ealse.ccnl.validation.PostcodeValidator;

public class AddressValidation extends CompositeValidator {

  @Setter
  private AddressController addressController;

  /**
   * Initialize this validation with the JavaFX controls.
   * @param vc the callback target for the validation result
   */
  public void initializeValidation(Consumer<Boolean> vc) {
    setCallback(vc);
    if (addressController.getAddress() != null) {
      required(addressController.getAddress(), addressController.getAddressE());
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
    if (addressController.getAddress() != null) {
      addressController.getAddressE().setVisible(false);
      addressController.getAddressNumberE().setVisible(false);
      addressController.getCityE().setVisible(false);
      String address = addressController.getAddress().getText();
      if (address == null || address.isEmpty()) {
        // assume the page is in its initial state.
        addressController.getPostalCodeE().setVisible(false);
      }
    }
  }


}
