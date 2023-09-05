package nl.ealse.ccnl.control.other;

import javafx.fxml.FXML;
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

  private PageName currentPage;

  public ExternalOtherController(PageController pageController,
      ExternalRelationService<ExternalRelationOther> externalRelationService) {
    super(pageController, externalRelationService);
    this.pageController = pageController;
  }

  @EventListener(condition = "#event.name('NEW_EXTERNAL_RELATION','AMEND_EXTERNAL_RELATION')")
  public void handleRelation(ExternalOtherSelectionEvent event) {
    pageController.loadPage(PageName.EXTERNAL_RELATION_ADDRESS);
    this.currentMenuChoice = event.getMenuChoice();
    this.selectedExternalRelation = event.getSelectedEntity();
    this.model = new ExternalRelationOther();
    reset();
  }

  @FXML
  void nextPage() {
    if (currentPage == PageName.EXTERNAL_RELATION_PERSONAL) {
      secondPage();
    }
  }

  @FXML
  void previousPage() {
    if (currentPage == PageName.EXTERNAL_RELATION_ADDRESS) {
      firstPage();
    }
  }

  @FXML
  protected void firstPage() {
    currentPage = PageName.EXTERNAL_RELATION_PERSONAL;
    pageController.setActivePage(currentPage);
    this.headerText.setText(getHeaderText());
    getRelationName().requestFocus();
    externalRelationValidation.validate();
  }

  @FXML
  void secondPage() {
    currentPage = PageName.EXTERNAL_RELATION_ADDRESS;
    pageController.setActivePage(currentPage);
    this.headerText.setText(getHeaderText());
    getAddressController().getStreet().requestFocus();
    externalRelationValidation.validate();
  }

  protected String getHeaderText() {
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
