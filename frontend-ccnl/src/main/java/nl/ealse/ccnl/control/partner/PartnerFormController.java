package nl.ealse.ccnl.control.partner;

import nl.ealse.ccnl.control.external.ExternalRelationFormController;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;

/**
 * Control the form pages for an external partner.
 */
public class PartnerFormController
    extends ExternalRelationFormController<ExternalRelationPartner, PartnerController> {

  public PartnerFormController(PartnerController controller) {
    super(controller);
  }

  @Override
  protected void initializePages() {
    initialize("partner/form/pagePartner", "Partnergegevens");
  }

}
