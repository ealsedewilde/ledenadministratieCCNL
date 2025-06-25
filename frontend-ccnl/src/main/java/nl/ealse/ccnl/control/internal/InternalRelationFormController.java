package nl.ealse.ccnl.control.internal;

import nl.ealse.ccnl.form.FormController;
import nl.ealse.javafx.FXMLLoaderUtil;

/**
 * Control the form pages for an internal relation.
 */
public class InternalRelationFormController extends FormController {

  private final InternalRelationController controller;

  public InternalRelationFormController(InternalRelationController controller) {
    super(2, new InternalRelationValidation(controller));
    this.controller = controller;
  }

  @Override
  protected void initializePages() {
    formPageArray[0] = FXMLLoaderUtil.getPage("internal/form/pageRelation", controller);
    formPageArray[1] = FXMLLoaderUtil.getPage("form/address", controller);

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
