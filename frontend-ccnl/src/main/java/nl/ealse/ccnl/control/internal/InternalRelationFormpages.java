package nl.ealse.ccnl.control.internal;

import nl.ealse.ccnl.form.FormPages;
import nl.ealse.ccnl.form.FormPane;

/**
 * Control the form pages for an internal relation.
 */
public class InternalRelationFormpages extends FormPages {
  
  private final InternalRelationController controller;

  public InternalRelationFormpages(InternalRelationController controller) {
    super(2, new InternalRelationValidation(controller));
    this.controller = controller;
  }
  
  @Override
  protected void initializePages() {
    formPageArray[0] =
        new FormPane("internal/form/pageRelation", controller);
    formPageArray[1] = new FormPane("form/address", controller);

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
