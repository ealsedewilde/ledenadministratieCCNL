package nl.ealse.ccnl.control.external;

import nl.ealse.ccnl.form.FormPages;
import nl.ealse.ccnl.form.FormPane;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;
import nl.ealse.javafx.PageId;

public abstract class ExternalRelationFormPages<T extends ExternalRelation, C extends ExternalRelationController<T>>
    extends FormPages<C> {

  protected ExternalRelationFormPages(C controller) {
    super(2, controller);
  }

  protected void initialize(C controller, PageId pageId) {
    formPageArray[0] = new FormPane(pageId, controller);
    formPageArray[1] = new FormPane(new PageId("EXTERNAL_CLUB_ADDRESS", "form/address"), controller);

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
