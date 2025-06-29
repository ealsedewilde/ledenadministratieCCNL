package nl.ealse.ccnl.control.partner;

import lombok.Getter;
import nl.ealse.ccnl.control.external.ExternalRelationController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.form.FormController;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import nl.ealse.ccnl.service.relation.CommercialPartnerService;

public class PartnerController extends ExternalRelationController<ExternalRelationPartner> {

  private final PageController pageController;

  @Getter
  private FormController formController;

  public PartnerController(PageController pageController, CommercialPartnerService service) {
    super(pageController, service);
    this.pageController = pageController;
    setup();
  }

  private void setup() {
    formController = new PartnerFormController(this);
    formController.initializeForm();
    formController.setOnSave(e -> save());
    formController.setOnReset(e -> reset());
  }

  @EventListener(menuChoice = MenuChoice.NEW_PARTNER)
  public void newPartner(MenuChoiceEvent event) {
    this.selectedExternalRelation = new ExternalRelationPartner();
    handlePartner(event);
  }

  @EventListener(menuChoice = MenuChoice.AMEND_PARTNER)
  public void amendPartner(PartnerSelectionEvent event) {
    this.selectedExternalRelation = event.getSelectedEntity();
    handlePartner(event);
  }

  private void handlePartner(MenuChoiceEvent event) {
    pageController.setActivePage(formController.getPageReference());
    formController.setActiveFormPage(0);
    this.model = new ExternalRelationPartner();
    this.currentMenuChoice = event.getMenuChoice();
    formController.getHeaderText().setText(getHeaderTextValue());
    reset();
  }

  protected String getHeaderTextValue() {
    return switch (currentMenuChoice) {
      case NEW_PARTNER -> "Adverteerder opvoeren";
      case AMEND_PARTNER -> "Adverteerder wijzigen";
      default -> "Invalid";
    };
  }

  @Override
  protected String getSaveText() {
    return "Partnergegevens opgeslagen";
  }

}
