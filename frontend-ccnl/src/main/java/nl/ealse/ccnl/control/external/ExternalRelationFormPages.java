package nl.ealse.ccnl.control.external;

import nl.ealse.ccnl.form.FormPages;
import nl.ealse.ccnl.form.FormPane;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;

public abstract class ExternalRelationFormPages<T extends ExternalRelation, C extends ExternalRelationController<T>>
    extends FormPages<C> {

  protected ExternalRelationFormPages(C controller) {
    super(2, controller);
  }

  protected void initialize(C controller, String fxmlName) {
    formPageArray[0] = new FormPane(fxmlName, controller);
    formPageArray[1] = new FormPane("form/address", controller);

    // initialize the submenu for form nsvigation.
    addMenuItem(0, "Clubgegevens");
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
