package nl.ealse.ccnl.control.member;

import nl.ealse.ccnl.control.address.AddressValidation;
import nl.ealse.ccnl.validation.AmountValidator;
import nl.ealse.ccnl.validation.EmailValidator;
import nl.ealse.ccnl.validation.IbanNumberValidator;

public class MemberValidation extends AddressValidation {

  private final MemberController controller;

  public MemberValidation(MemberController controller) {
    super(controller);
    this.controller = controller;
  }

  @Override
  public void initialize() {
    IbanNumberValidator ddValidator = new IbanNumberValidator(controller.getPaymentMethod(),
        controller.getIbanNumber(), controller.getIbanNumberE());
    addValidator(controller.getPaymentMethod(), ddValidator);
    addValidator(controller.getIbanNumber(), ddValidator);

    AmountValidator amountValidator =
        new AmountValidator(controller.getAmountPaid(), controller.getAmountPaidE());
    addValidator(controller.getAmountPaid(), amountValidator);

    required(controller.getInitials(), controller.getInitialsE());
    required(controller.getLastName(), controller.getLastNameE());

    EmailValidator emailValidator =
        new EmailValidator(controller.getEmail(), controller.getEmailE());
    addValidator(controller.getEmail(), emailValidator);
    super.initialize();
  }

  @Override
  public void resetErrorMessages() {
    super.resetErrorMessages();
    controller.getInitialsE().setVisible(false);
    controller.getLastNameE().setVisible(false);
  }

}
