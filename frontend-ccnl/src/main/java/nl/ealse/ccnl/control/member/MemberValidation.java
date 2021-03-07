package nl.ealse.ccnl.control.member;

import java.util.function.Consumer;
import nl.ealse.ccnl.control.address.AddressValidation;
import nl.ealse.ccnl.validation.EmailValidator;
import nl.ealse.ccnl.validation.IbanNumberValidator;

public class MemberValidation extends AddressValidation {

  private final MemberController controller;

  public MemberValidation(MemberController controller) {
    this.controller = controller;
  }

  @Override
  public void initializeValidation(Consumer<Boolean> vc) {
    if (controller.getMemberInfo() != null) {
      // memberExtraInfo.fxml is loaded

    } else if (controller.getIbanNumber() != null) {
      // memberFinancial.fxml is loaded
      setCallback(vc);
      IbanNumberValidator ddValidator = new IbanNumberValidator(controller.getPaymentMethod(),
          controller.getIbanNumber(), controller.getIbanNumberE());
      addValidator(controller.getPaymentMethod(), ddValidator);
      addValidator(controller.getIbanNumber(), ddValidator);
    } else if (controller.getAddressController() != null) {
      // memberAddress.fxml is loaded
      setAddressController(controller.getAddressController());
      super.initializeValidation(null);
    } else if (controller.getInitials() != null) {
      // memberPersonal.fxml is loaded
      required(controller.getInitials(), controller.getInitialsE());
      required(controller.getLastName(), controller.getLastNameE());

      EmailValidator emailValidator =
          new EmailValidator(controller.getEmail(), controller.getEmailE());
      addValidator(controller.getEmail(), emailValidator);
    }

  }

  @Override
  public void resetErrorMessages() {
    super.resetErrorMessages();
    controller.getInitialsE().setVisible(false);
    controller.getLastNameE().setVisible(false);
  }

}
