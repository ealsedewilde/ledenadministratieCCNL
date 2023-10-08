package nl.ealse.ccnl.control.club;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import nl.ealse.ccnl.control.external.ExternalRelationController;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.form.FormController;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import nl.ealse.javafx.FXMLMissingException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class ExternalClubController extends ExternalRelationController<ExternalRelationClub> {
  private final PageController pageController;

  @Getter
  private FormController formController;

  public ExternalClubController(PageController pageController,
      ExternalRelationService<ExternalRelationClub> externalRelationService) {
    super(pageController, externalRelationService);
    this.pageController = pageController;
  }

  @PostConstruct
  void setup() {
    formController = new ClubFormController(this);
    try {
      formController.initializeForm();
      formController.setOnSave(e -> save());
      formController.setOnReset(e -> reset());
    } catch (FXMLMissingException e) {
      pageController.showErrorMessage(e.getMessage());
    }
  }

  @EventListener(condition = "#event.name('NEW_EXTERNAL_CLUB','AMEND_EXTERNAL_CLUB')")
  public void handleClub(ExternalClubSelectionEvent event) {
    pageController.setActivateFormPage(formController.getForm());
    formController.setActiveFormPage(0);
    this.currentMenuChoice = event.getMenuChoice();
    this.selectedExternalRelation = event.getSelectedEntity();
    this.model = new ExternalRelationClub();
    formController.getHeaderText().setText(getHeaderTextValue());
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
