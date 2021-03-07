package nl.ealse.ccnl.control.member;

import nl.ealse.ccnl.validation.CompositeValidator;
import nl.ealse.ccnl.validation.EmailValidator;

public class CancelMailValidation extends CompositeValidator {

  private final CancelationMailController controller;

  public CancelMailValidation(CancelationMailController controller) {
    this.controller = controller;
    required(controller.getToMailAddress(), controller.getToMailAddressE());
    EmailValidator emailValidator =
        new EmailValidator(controller.getToMailAddress(), controller.getToMailAddressE());
    addValidator(controller.getToMailAddress(), emailValidator);
  }

  @Override
  protected void resetErrorMessages() {
    controller.getToMailAddressE().setVisible(false);
  }

}
