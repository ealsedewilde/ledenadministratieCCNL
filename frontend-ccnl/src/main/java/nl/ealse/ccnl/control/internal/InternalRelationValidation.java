package nl.ealse.ccnl.control.internal;

import nl.ealse.ccnl.control.address.AddressValidation;

public class InternalRelationValidation extends AddressValidation {

  private final InternalRelationController controller;

  public InternalRelationValidation(InternalRelationController controller) {
    super(controller);
    this.controller = controller;
  }

  @Override
  public void initialize() {
    required(controller.getContactName(), controller.getContactNameE());
    super.initialize();
  }

  @Override
  protected void resetErrorMessages() {
    super.resetErrorMessages();
    controller.getContactNameE().setVisible(false);
  }



}
