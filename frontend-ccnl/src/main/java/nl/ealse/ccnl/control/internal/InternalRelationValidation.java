package nl.ealse.ccnl.control.internal;

import java.util.function.Consumer;
import nl.ealse.ccnl.control.address.AddressValidation;

public class InternalRelationValidation extends AddressValidation {

  private final InternalRelationController controller;

  public InternalRelationValidation(InternalRelationController controller) {
    this.controller = controller;
  }

  @Override
  public void initializeValidation(Consumer<Boolean> vc) {
    if (controller.getStreet() != null) {
      setAddressController(controller);
      super.initializeValidation(vc);
    } else if (controller.getContactName() != null) {
      required(controller.getContactName(), controller.getContactNameE());
    }
  }

  @Override
  protected void resetErrorMessages() {
    super.resetErrorMessages();
    controller.getContactNameE().setVisible(false);
  }



}
