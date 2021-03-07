package nl.ealse.ccnl.control.other;

import javafx.fxml.FXML;
import nl.ealse.ccnl.control.external.ExternalRelationController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.ccnl.service.ExternalRelationService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

@Controller
public class ExternalOtherController extends ExternalRelationController<ExternalRelationOther>
    implements ApplicationListener<ExternalOtherSelectionEvent> {
  private final PageController pageController;

  private PageName currentPage;

  public ExternalOtherController(PageController pageController,
      ExternalRelationService<ExternalRelationOther> externalRelationService) {
    super(pageController, externalRelationService);
    this.pageController = pageController;
  }

  @Override
  public void onApplicationEvent(ExternalOtherSelectionEvent event) {
    if (event.getMenuChoice() == MenuChoice.NEW_EXTERNAL_RELATION
        || event.getMenuChoice() == MenuChoice.AMEND_EXTERNAL_RELATION) {
      pageController.loadPage(PageName.EXTERNAL_RELATION_ADDRESS);
      this.currentMenuChoice = event.getMenuChoice();
      this.selectedExternalRelation = event.getSelectedEntity();
      this.model = new ExternalRelationOther();
      reset();
    }
  }

  @FXML
  public void nextPage() {
    if (currentPage == PageName.EXTERNAL_RELATION_PERSONAL) {
      secondPage();
    }
  }

  @FXML
  public void previousPage() {
    if (currentPage == PageName.EXTERNAL_RELATION_ADDRESS) {
      firstPage();
    }
  }

  public void firstPage() {
    currentPage = PageName.EXTERNAL_RELATION_PERSONAL;
    pageController.setActivePage(currentPage);
    this.headerText.setText(getHeaderText());
    getRelationName().requestFocus();
    externalRelationValidation.validate();
  }

  public void secondPage() {
    currentPage = PageName.EXTERNAL_RELATION_ADDRESS;
    pageController.setActivePage(currentPage);
    this.headerText.setText(getHeaderText());
    getAddressController().getAddress().requestFocus();
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
