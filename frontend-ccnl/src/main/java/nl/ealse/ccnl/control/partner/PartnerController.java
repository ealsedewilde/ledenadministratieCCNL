package nl.ealse.ccnl.control.partner;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import nl.ealse.ccnl.control.external.ExternalRelationController;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import nl.ealse.ccnl.form.FormController;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import nl.ealse.javafx.FXMLMissingException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class PartnerController extends ExternalRelationController<ExternalRelationPartner> {
  private final PageController pageController;

  @Getter
  private FormController formController;

  public PartnerController(PageController pageController,
      ExternalRelationService<ExternalRelationPartner> partnerService) {
    super(pageController, partnerService);
    this.pageController = pageController;
  }

  @PostConstruct
  void setup() {
    formController = new PartnerFormController(this);
    try {
      formController.initializeForm();
      formController.setOnSave(e -> save());
      formController.setOnReset(e -> reset());
    } catch (FXMLMissingException e) {
      pageController.showErrorMessage(e.getMessage());
    }
  }

  @EventListener(condition = "#event.name('NEW_PARTNER','AMEND_PARTNER')")
  public void handlePartner(PartnerSelectionEvent event) {
    pageController.setActivateFormPage(formController.getForm());
    formController.setActiveFormPage(0);
    this.selectedExternalRelation = event.getSelectedEntity();
    this.model = new ExternalRelationPartner();
    this.currentMenuChoice = event.getMenuChoice();
    formController.getHeaderText().setText(getHeaderTextValue());
    reset();
  }

  protected String getHeaderTextValue() {
    switch (currentMenuChoice) {
      case NEW_PARTNER:
        return "Adverteerder opvoeren";
      case AMEND_PARTNER:
        return "Adverteerder wijzigen";
      default:
        return "Invalid";
    }
  }

  @Override
  protected String getSaveText() {
    return "Partnergegevens opgeslagen";
  }

}
