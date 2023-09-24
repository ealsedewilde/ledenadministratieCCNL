package nl.ealse.ccnl.control.partner;

import nl.ealse.ccnl.form.FormPages;
import nl.ealse.ccnl.form.FormPane;
import nl.ealse.javafx.PageId;

/**
 * Control the form pages for an external partner.
 */
public class PartnerFormPages extends FormPages<PartnerController> {

  public PartnerFormPages(PartnerController controller) {
    super(2, controller);
    initialize(controller);
  }

  private void initialize(PartnerController controller) {
    formPages[0] =
        new FormPane(new PageId("PARTNER_RELATION", "partner/form/pagePartner"), controller);
    formPages[1] = new FormPane(new PageId("PARTNER_ADDRESS", "form/address"), controller);

    // initialize the submenu for form nsvigation.
    addMenuItem(0, "Adverteerder");
    addMenuItem(1, "Adresgegevens");

  }

  @Override
  protected void setFocus(int pageIndex) {
    if (pageIndex == 0) {
      controller.getRelationName().requestFocus();
    } else {
      controller.getStreet().requestFocus();
    }

  }

}
