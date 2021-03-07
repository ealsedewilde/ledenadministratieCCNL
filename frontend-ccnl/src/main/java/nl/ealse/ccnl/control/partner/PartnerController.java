package nl.ealse.ccnl.control.partner;

import javafx.fxml.FXML;
import nl.ealse.ccnl.control.external.ExternalRelationController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import nl.ealse.ccnl.service.ExternalRelationService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

@Controller
public class PartnerController extends ExternalRelationController<ExternalRelationPartner>
    implements ApplicationListener<PartnerSelectionEvent> {
  private final PageController pageController;

  private PageName currentPage;

  public PartnerController(PageController pageController,
      ExternalRelationService<ExternalRelationPartner> partnerService) {
    super(pageController, partnerService);
    this.pageController = pageController;
  }

  @Override
  public void onApplicationEvent(PartnerSelectionEvent event) {
    if (event.getMenuChoice() == MenuChoice.NEW_PARTNER
        || event.getMenuChoice() == MenuChoice.AMEND_PARTNER) {
      pageController.loadPage(PageName.PARTNER_ADDRESS);
      this.selectedExternalRelation = event.getSelectedEntity();
      this.model = new ExternalRelationPartner();
      this.currentMenuChoice = event.getMenuChoice();
      reset();
    }
  }

  @FXML
  public void nextPage() {
    if (currentPage == PageName.PARTNER_PERSONAL) {
      secondPage();
    }
  }

  @FXML
  public void previousPage() {
    if (currentPage == PageName.PARTNER_ADDRESS) {
      firstPage();
    }
  }

  public void firstPage() {
    currentPage = PageName.PARTNER_PERSONAL;
    pageController.setActivePage(currentPage);
    this.headerText.setText(getHeaderText());
    getRelationName().requestFocus();
    externalRelationValidation.validate();
  }

  public void secondPage() {
    currentPage = PageName.PARTNER_ADDRESS;
    pageController.setActivePage(currentPage);
    this.headerText.setText(getHeaderText());
    getAddressController().getAddress().requestFocus();
    externalRelationValidation.validate();
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
