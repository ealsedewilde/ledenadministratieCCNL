package nl.ealse.ccnl.control.external;

import nl.ealse.ccnl.form.FormController;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;
import nl.ealse.javafx.FXMLLoaderBean;

public abstract class ExternalRelationFormController<T extends ExternalRelation, C extends ExternalRelationController<T>>
    extends FormController {
  
  private final C controller;

  protected ExternalRelationFormController(C controller) {
    super(2, new ExternalRelationValidation(controller));
    this.controller = controller;
  }

  protected void initialize(String fxmlName, String menuItemName) {
    formPageArray[0] = FXMLLoaderBean.getPage(fxmlName, controller);
    formPageArray[1] = FXMLLoaderBean.getPage("form/address", controller);

    // initialize the submenu for form nsvigation.
    addMenuItem(0, menuItemName);
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
