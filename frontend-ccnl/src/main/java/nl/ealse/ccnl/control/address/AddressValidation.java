package nl.ealse.ccnl.control.address;

import nl.ealse.ccnl.validation.AddressNumberValidator;
import nl.ealse.ccnl.validation.CompositeValidator;
import nl.ealse.ccnl.validation.PostcodeValidator;
import nl.ealse.ccnl.view.AddressView;

public class AddressValidation extends CompositeValidator {

  private final AddressView addressController;

  public AddressValidation(AddressView addressController) {
    this.addressController = addressController;
  }

  /**
   * Initialize this validation with the JavaFX controls.
   */
  public void initialize() {
    required(addressController.getStreet(), addressController.getStreetE());
    AddressNumberValidator addressNumberValidator = new AddressNumberValidator(
        addressController.getAddressNumber(), addressController.getAddressNumberE());
    addValidator(addressController.getAddressNumber(), addressNumberValidator);
    required(addressController.getCity(), addressController.getCityE());

    PostcodeValidator postcodeValidator = new PostcodeValidator(addressController.getCountry(),
        addressController.getPostalCode(), addressController.getPostalCodeE());
    addValidator(addressController.getCountry(), postcodeValidator);
    addValidator(addressController.getPostalCode(), postcodeValidator);
    super.initialize();
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
