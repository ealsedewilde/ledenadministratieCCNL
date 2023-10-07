package nl.ealse.ccnl.control.club;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import nl.ealse.ccnl.control.external.ExternalRelationController;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.mapping.Mapping;
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
  }

  @PostConstruct
  void setup() {
    formPages = new ClubFormPages(this);
    try {
      formPages.initializeForm();
      formPages.setOnSave(e -> save());
      formPages.setOnReset(e -> reset());
    } catch (FXMLMissingException e) {
      pageController.showErrorMessage(e.getMessage());
    }
  }

  @EventListener(condition = "#event.name('NEW_EXTERNAL_CLUB','AMEND_EXTERNAL_CLUB')")
  public void handleClub(ExternalClubSelectionEvent event) {
    pageController.setActivateFormPage(formPages.getForm());
    formPages.setActiveFormPage(0);
    this.currentMenuChoice = event.getMenuChoice();
    this.selectedExternalRelation = event.getSelectedEntity();
    this.model = new ExternalRelationClub();
    formPages.getHeaderText().setText(getHeaderTextValue());
    reset();
  }

  protected String getHeaderTextValue() {
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
