package nl.ealse.ccnl.control.other;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import nl.ealse.ccnl.control.external.ExternalRelationController;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.form.FormController;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class ExternalOtherController extends ExternalRelationController<ExternalRelationOther> {
  private final PageController pageController;

  @Getter
  private FormController formController;

  public ExternalOtherController(PageController pageController,
      ExternalRelationService<ExternalRelationOther> externalRelationService) {
    super(pageController, externalRelationService);
    this.pageController = pageController;
  }

  @PostConstruct
  void setup() {
    formController = new ExternalOtherFormController(this);
    formController.initializeForm();
    formController.setOnSave(e -> save());
    formController.setOnReset(e -> reset());
  }

  @EventListener(condition = "#event.name('NEW_EXTERNAL_RELATION')")
  public void newRelation(MenuChoiceEvent event) {
    this.selectedExternalRelation = new ExternalRelationOther();
    handleRelation(event);
  }

  @EventListener(condition = "#event.name('AMEND_EXTERNAL_RELATION')")
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
