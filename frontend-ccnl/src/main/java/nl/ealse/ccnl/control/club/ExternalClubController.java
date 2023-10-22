package nl.ealse.ccnl.control.club;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import nl.ealse.ccnl.control.external.ExternalRelationController;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.form.FormController;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
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
    formController.initializeForm();
    formController.setOnSave(e -> save());
    formController.setOnReset(e -> reset());
  }

  @EventListener(condition = "#event.name('NEW_EXTERNAL_CLUB')")
  public void newClub(MenuChoiceEvent event) {
    this.selectedExternalRelation = new ExternalRelationClub();
    handleClub(event);
  }

  @EventListener(condition = "#event.name('AMEND_EXTERNAL_CLUB')")
  public void amendClub(ExternalClubSelectionEvent event) {
    this.selectedExternalRelation = event.getSelectedEntity();
    handleClub(event);
  }
  
  private void handleClub(MenuChoiceEvent event) {
    pageController.setActivePage(formController.getPageReference());
    formController.setActiveFormPage(0);
    this.currentMenuChoice = event.getMenuChoice();
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
