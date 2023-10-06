package nl.ealse.ccnl.control.other;

import lombok.Getter;
import nl.ealse.ccnl.control.external.ExternalRelationController;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class ExternalOtherController extends ExternalRelationController<ExternalRelationOther> {
  private final PageController pageController;
  
  @Getter
  private ExternalOtherFormpages formPages;

  public ExternalOtherController(PageController pageController,
      ExternalRelationService<ExternalRelationOther> externalRelationService) {
    super(pageController, externalRelationService);
    this.pageController = pageController;
    bindFxml();
  }

  private void bindFxml() {
    pageController.loadPage(PageName.EXTERNAL_RELATION_FORM, this);
    formPages = new ExternalOtherFormpages(this);
    
    initializeValidation();
  }

  @EventListener(condition = "#event.name('NEW_EXTERNAL_RELATION','AMEND_EXTERNAL_RELATION')")
  public void handleRelation(ExternalOtherSelectionEvent event) {
    pageController.setActivePage(PageName.EXTERNAL_RELATION_FORM);
    formPages.setActiveFormPage(0);
    this.currentMenuChoice = event.getMenuChoice();
    this.selectedExternalRelation = event.getSelectedEntity();
    this.model = new ExternalRelationOther();
    headerText.setText(getHeaderTextValue());
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
