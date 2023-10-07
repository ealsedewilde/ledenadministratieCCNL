package nl.ealse.ccnl.control.partner;

import nl.ealse.ccnl.control.external.ExternalRelationFormPages;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;

/**
 * Control the form pages for an external partner.
 */
public class PartnerFormPages
    extends ExternalRelationFormPages<ExternalRelationPartner, PartnerController> {

  public PartnerFormPages(PartnerController controller) {
    super(controller);
    initialize(controller, "partner/form/pagePartner");
  }

}
