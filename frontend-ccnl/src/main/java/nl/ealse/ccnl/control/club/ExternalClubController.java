package nl.ealse.ccnl.control.club;

import javafx.fxml.FXML;
import nl.ealse.ccnl.control.external.ExternalRelationController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.service.ExternalRelationService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

@Controller
public class ExternalClubController extends ExternalRelationController<ExternalRelationClub>
    implements ApplicationListener<ExternalClubSelectionEvent> {
  private final PageController pageController;

  private PageName currentPage;

  public ExternalClubController(PageController pageController,
      ExternalRelationService<ExternalRelationClub> externalRelationService) {
    super(pageController, externalRelationService);
    this.pageController = pageController;
  }

  @Override
  public void onApplicationEvent(ExternalClubSelectionEvent event) {
    if (event.getMenuChoice() == MenuChoice.NEW_EXTERNAL_CLUB
        || event.getMenuChoice() == MenuChoice.AMEND_EXTERNAL_CLUB) {
      pageController.loadPage(PageName.EXTERNAL_CLUB_ADDRESS);
      this.currentMenuChoice = event.getMenuChoice();
      this.selectedExternalRelation = event.getSelectedEntity();
      this.model = new ExternalRelationClub();
      reset();
    }
  }

  @FXML
  public void nextPage() {
    if (currentPage == PageName.EXTERNAL_CLUB_PERSONAL) {
      secondPage();
    }
  }

  @FXML
  public void previousPage() {
    if (currentPage == PageName.EXTERNAL_CLUB_ADDRESS) {
      firstPage();
    }
  }

  public void firstPage() {
    currentPage = PageName.EXTERNAL_CLUB_PERSONAL;
    pageController.setActivePage(currentPage);
    headerText.setText(getHeaderText());
    getRelationName().requestFocus();
    externalRelationValidation.validate();
  }

  public void secondPage() {
    currentPage = PageName.EXTERNAL_CLUB_ADDRESS;
    pageController.setActivePage(currentPage);
    headerText.setText(getHeaderText());
    getAddressController().getAddress().requestFocus();
    externalRelationValidation.validate();
  }

  protected String getHeaderText() {
    switch (currentMenuChoice) {
      case NEW_EXTERNAL_CLUB:
        return "Club opvoeren";
      case AMEND_EXTERNAL_CLUB:
        return "Club wijzigen";
      default:
        return "invalid";
    }
  }

  @Override
  protected String getSaveText() {
    return "Club gegevens zijn opgeslagen";
  }


}
