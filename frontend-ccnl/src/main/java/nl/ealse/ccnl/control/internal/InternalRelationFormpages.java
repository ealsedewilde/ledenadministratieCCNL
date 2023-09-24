package nl.ealse.ccnl.control.internal;

import nl.ealse.ccnl.form.FormPages;
import nl.ealse.ccnl.form.FormPane;
import nl.ealse.javafx.PageId;

/**
 * Control the form pages for an internal relation.
 */
public class InternalRelationFormpages extends FormPages<InternalRelationController> {

  public InternalRelationFormpages(InternalRelationController controller) {
    super(2, controller);
    initialize(controller);
  }
  
  private void initialize(InternalRelationController controller) {
    formPages[0] =
        new FormPane(new PageId("INTERNAL_RELATION", "internal/form/pageRelation"), controller);
    formPages[1] = new FormPane(new PageId("INTERNAL_ADDRESS", "form/address"), controller);

    // initialize the submenu for form nsvigation.
    addMenuItem(0, "Functiegegevens");
    addMenuItem(1, "Adresgegevens");
  }

  @Override
  protected void setFocus(int pageIndex) {
    if (pageIndex == 0) {
      controller.getTitle().requestFocus();
    } else {
      controller.getStreet().requestFocus();
    }
  }

}