package nl.ealse.ccnl.control.club;

import javafx.fxml.FXML;
import lombok.Getter;
import nl.ealse.ccnl.control.external.ExternalRelationController;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class ExternalClubController extends ExternalRelationController<ExternalRelationClub> {
  private final PageController pageController;

  @Getter
  private ClubFormPages formPages;

  public ExternalClubController(PageController pageController,
      ExternalRelationService<ExternalRelationClub> externalRelationService) {
    super(pageController, externalRelationService);
    this.pageController = pageController;
    bindFxml();
  }

  private void bindFxml() {
    pageController.loadForm(PageName.EXTERNAL_CLUB_FORM, this);
    formPages = new ClubFormPages(this);
    
    initializeValidation();
  }

  @EventListener(condition = "#event.name('NEW_EXTERNAL_CLUB','AMEND_EXTERNAL_CLUB')")
  public void handleClub(ExternalClubSelectionEvent event) {
    pageController.setActivePage(PageName.EXTERNAL_CLUB_FORM);
    formPages.setActiveFormPage(0);
    this.currentMenuChoice = event.getMenuChoice();
    this.selectedExternalRelation = event.getSelectedEntity();
    this.model = new ExternalRelationClub();
    headerText.setText(getHeaderText());
    reset();
  }

  @FXML
  void nextPage() {
    formPages.setActiveFormPage(formPages.getCurrentPage() + 1);
  }

  @FXML
  void previousPage() {
    formPages.setActiveFormPage(formPages.getCurrentPage() - 1);
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
