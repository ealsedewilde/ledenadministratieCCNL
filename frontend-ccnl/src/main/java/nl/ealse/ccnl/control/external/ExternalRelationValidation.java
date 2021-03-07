package nl.ealse.ccnl.control.external;

import java.util.function.Consumer;
import nl.ealse.ccnl.control.address.AddressValidation;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;

public class ExternalRelationValidation extends AddressValidation {

  private final ExternalRelationController<? extends ExternalRelation> controller;

  public ExternalRelationValidation(ExternalRelationController<? extends ExternalRelation> controller) {
    this.controller = controller;
  }

  @Override
  public void initializeValidation(Consumer<Boolean> vc) {
    if (controller.getAddressController() != null) {
      setAddressController(controller.getAddressController());
      super.initializeValidation(vc);
    } else if (controller.getRelationNumber() != null) {
      required(controller.getRelationName(), controller.getRelationNameE());
      required(controller.getContactName(), controller.getContactNameE());
    }
  }

  @Override
  protected void resetErrorMessages() {
    super.resetErrorMessages();
    controller.getRelationNameE().setVisible(false);
    controller.getContactNameE().setVisible(false);
  }

}
