package nl.ealse.ccnl.control.other;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import nl.ealse.ccnl.control.external.ExternalRelationController;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.form.FormController;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import nl.ealse.javafx.FXMLMissingException;
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
    try {
      formController.initializeForm();
      formController.setOnSave(e -> save());
      formController.setOnReset(e -> reset());
    } catch (FXMLMissingException e) {
      pageController.showErrorMessage(e.getMessage());
    }
  }

  @EventListener(condition = "#event.name('NEW_EXTERNAL_RELATION','AMEND_EXTERNAL_RELATION')")
  public void handleRelation(ExternalOtherSelectionEvent event) {
    pageController.setActivateFormPage(formController.getForm());
    formController.setActiveFormPage(0);
    this.currentMenuChoice = event.getMenuChoice();
    this.selectedExternalRelation = event.getSelectedEntity();
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
