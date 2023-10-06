package nl.ealse.ccnl.control.external;

import nl.ealse.ccnl.control.address.AddressValidation;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;

public class ExternalRelationValidation extends AddressValidation {

  private final ExternalRelationController<? extends ExternalRelation> controller;

  public ExternalRelationValidation(
      ExternalRelationController<? extends ExternalRelation> controller) {
    super(controller);
    this.controller = controller;
  }

  @Override
  public void initialize() {
    required(controller.getRelationName(), controller.getRelationNameE());
    required(controller.getContactName(), controller.getContactNameE());
    super.initialize();
  }

  @Override
  protected void resetErrorMessages() {
    super.resetErrorMessages();
    controller.getRelationNameE().setVisible(false);
    controller.getContactNameE().setVisible(false);
  }

}
