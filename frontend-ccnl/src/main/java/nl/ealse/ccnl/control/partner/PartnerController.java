package nl.ealse.ccnl.control.partner;

import javafx.fxml.FXML;
import lombok.Getter;
import nl.ealse.ccnl.control.external.ExternalRelationController;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class PartnerController extends ExternalRelationController<ExternalRelationPartner> {
  private final PageController pageController;

  @Getter
  private PartnerFormPages formPages;

  public PartnerController(PageController pageController,
      ExternalRelationService<ExternalRelationPartner> partnerService) {
    super(pageController, partnerService);
    this.pageController = pageController;
    bindFxml();
  }

  private void bindFxml() {
    pageController.loadForm(PageName.PARTNER_FORM, this);
    formPages = new PartnerFormPages(this);
  }

  @EventListener(condition = "#event.name('NEW_PARTNER','AMEND_PARTNER')")
  public void handlePartner(PartnerSelectionEvent event) {
    pageController.setActivePage(PageName.PARTNER_FORM);
    formPages.setActiveFormPage(0);
    this.selectedExternalRelation = event.getSelectedEntity();
    this.model = new ExternalRelationPartner();
    this.currentMenuChoice = event.getMenuChoice();
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
