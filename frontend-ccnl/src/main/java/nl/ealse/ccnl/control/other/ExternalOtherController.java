package nl.ealse.ccnl.control.other;

import lombok.Getter;
import nl.ealse.ccnl.control.external.ExternalRelationController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.form.FormController;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.ccnl.service.relation.ExternalOtherService;

public class ExternalOtherController extends ExternalRelationController<ExternalRelationOther> {
  
  @Getter
  private static final ExternalOtherController instance = new ExternalOtherController();
  
  private final PageController pageController;

  @Getter
  private FormController formController;

  private ExternalOtherController() {
    super(ExternalOtherService.getInstance());
    this.pageController = PageController.getInstance();
    setup();
  }

  private void setup() {
    formController = new ExternalOtherFormController(this);
    formController.initializeForm();
    formController.setOnSave(e -> save());
    formController.setOnReset(e -> reset());
  }

  @EventListener(menuChoice = MenuChoice.NEW_EXTERNAL_RELATION)
  public void newRelation(MenuChoiceEvent event) {
    this.selectedExternalRelation = new ExternalRelationOther();
    handleRelation(event);
  }

  @EventListener(menuChoice = MenuChoice.AMEND_EXTERNAL_RELATION)
  public void amendRelation(ExternalOtherSelectionEvent event) {
    this.selectedExternalRelation = event.getSelectedEntity();
    handleRelation(event);
  }

  private void handleRelation(MenuChoiceEvent event) {
    pageController.setActivePage(formController.getPageReference());
    formController.setActiveFormPage(0);
    this.currentMenuChoice = event.getMenuChoice();
    this.model = new ExternalRelationOther();
    formController.getHeaderText().setText(getHeaderTextValue());
    reset();
  }

  protected String getHeaderTextValue() {
    switch (currentMenuChoice) {
      case NEW_EXTERNAL_RELATION:
        return "Externe relatie opvoeren";
      case AMEND_EXTERNAL_RELATION:
        return "Externe relatie wijzigen";
      default:
        return "Invalid";
    }
  }

  @Override
  protected String getSaveText() {
    return "Externe relatie opgeslagen";
  }

}
