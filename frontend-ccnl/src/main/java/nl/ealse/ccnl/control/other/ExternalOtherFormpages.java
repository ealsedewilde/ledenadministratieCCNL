package nl.ealse.ccnl.control.other;

import nl.ealse.ccnl.form.FormPages;
import nl.ealse.ccnl.form.FormPane;
import nl.ealse.javafx.PageId;

/**
 * Control the form pages for an external relation.
 */
public class ExternalOtherFormpages extends FormPages<ExternalOtherController> {

  public ExternalOtherFormpages(ExternalOtherController controller) {
    super(2, controller);
    initialize(controller);
  }
  
  private void initialize(ExternalOtherController controller) {
    formPages[0] =
        new FormPane(new PageId("EXTERNAL_RELATION", "external/form/pageRelation"), controller);
    formPages[1] = new FormPane(new PageId("EXTERNAL_ADDRESS", "form/address"), controller);

    // initialize the submenu for form nsvigation.
    addMenuItem(0, "Relatiegegevens");
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
