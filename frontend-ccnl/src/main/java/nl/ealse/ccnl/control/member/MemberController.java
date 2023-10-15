package nl.ealse.ccnl.control.member;

import jakarta.annotation.PostConstruct;
import java.util.Optional;
import java.util.StringJoiner;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import nl.ealse.ccnl.control.PDFViewer;
import nl.ealse.ccnl.control.SearchController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.MenuController;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.mappers.PaymentMethodMapper;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.view.MemberView;
import nl.ealse.javafx.mapping.ViewModel;
import nl.ealse.javafx.util.PrintException;
import nl.ealse.javafx.util.PrintUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController extends MemberView {

  private final PageController pageController;

  private final MemberService service;

  private final DocumentService documentService;
  
  private final ApplicationEventPublisher eventPublisher;

  private Member model;

  private Member selectedMember;

  private Document sepaAuthorization;

  private MenuChoice currentMenuChoice;

  private PDFViewer pdfViewer;

  // Helper to detect changes in the iban owner
  private String savedName;

  private MemberFormController formController;

  public MemberController(PageController pageController, MemberService service,
      DocumentService documentService, ApplicationEventPublisher eventPublisher) {
    this.pageController = pageController;
    this.service = service;
    this.documentService = documentService;
    this.eventPublisher = eventPublisher;
  }

  @PostConstruct
  void setup() {
    formController = new MemberFormController(this);
    formController.initializeForm();
    formController.setOnSave(e -> save());
    formController.setOnReset(e -> reset());

    pdfViewer = PDFViewer.builder().withDeleteButton(e -> deletePDF())
        .withPrintButton(e -> printPDF()).withCancelButton(e -> closePDF()).build();
    pdfViewer.setWindowTitle("SEPA machtiging voor lid: %d (%s)");
    
    getIbanNumber().textProperty()
    .addListener((observable, oldValue, newValue) -> formatIbanOwnerName(newValue));
  }

  @FXML
  public void initialize() {
    super.initializeView();
  }

  /**
   * Initializes the Model. For a new Member the event was fired by {@link MenuController} For an
   * existing Member the {@link SearchController} fires the event.
   */
  @EventListener(condition = "#event.name('NEW_MEMBER')")
  public void newMember(MenuChoiceEvent event) {
    this.selectedMember = new Member();
    selectedMember.setMemberNumber(service.getFreeNumber());
    handleEvent(event);
    sepaAuthorization = null;
    reset();
  }

  /**
   * Initializes the Model. For a new Member the event was fired by {@link MenuController} For an
   * existing Member the {@link SearchController} fires the event.
   */
  @EventListener(condition = "#event.name('AMEND_MEMBER')")
  public void amendMember(MemberSeLectionEvent event) {
    this.selectedMember = event.getSelectedEntity();
    handleEvent(event);
    Optional<Document> optSepaAuthorization = documentService.findSepaAuthorization(selectedMember);
    if (optSepaAuthorization.isPresent()) {
      sepaAuthorization = optSepaAuthorization.get();
    } else {
      sepaAuthorization = null;
    }
    reset();
  }

  private void handleEvent(MenuChoiceEvent event) {
    pageController.setActivateFormPage(formController.getForm());
    formController.setActiveFormPage(0);
    this.currentMenuChoice = event.getMenuChoice();
    this.model = new Member();
    formController.getHeaderText().setText(getHeaderTextValue());
  }



  @FXML
  void reset() {
    // the selectedMember remains unchanged, so we can repeatedly call reset().
    ViewModel.modelToView(this, selectedMember);
    ViewModel.viewToModel(this, model);
    initializeInitialsType();
    savedName = getIbanOwnerName().getText();

    if (sepaAuthorization == null) {
      getSepaButton().setVisible(false);
      getSepaLabel().setVisible(false);
    } else {
      getSepaButton().setVisible(true);
      getSepaLabel().setVisible(true);
    }
    formController.getValidator().initialize();
    formController.getSaveButton().setDisable(currentMenuChoice == MenuChoice.NEW_MEMBER);
    formController.setActiveFormPage(0);
    
    setIbanControls(selectedMember.getIbanNumber());
  }

  private void initializeInitialsType() {
    if (getInitials().getText() == null || getInitials().getText().indexOf(".") > -1) {
      // initials
      getRbGroup().selectToggle(getRbGroup().getToggles().get(0));
    } else {
      // firstname
      getRbGroup().selectToggle(getRbGroup().getToggles().get(1));
    }
  }

  /**
   * Check on the financial page.
   */
  @FXML
  void checkPaymentMethod() {
    if (PaymentMethodMapper.NOT_APPLICABLE.equals(getPaymentMethod().getValue())) {
      // there is no payment obligation
      getIbanNumber().setDisable(true);
      getPaymentDate().setDisable(true);
      getCurrentYearPaid().setDisable(true);
    } else {
      getIbanNumber().setDisable(false);
      getPaymentDate().setDisable(false);
      getCurrentYearPaid().setDisable(false);
    }
    // and now revalidate
    formController.getValidator().validate();
  }

  @FXML
  void showSepaAuthorization() {
    pdfViewer.showPDF(sepaAuthorization);
  }

  @FXML
  void save() {
    enrich();
    enrichAddress();
    updateIbanOwnerName();
    if (savedName == null || savedName.equals(formatMemberName())) {
      // The iban owner is implicit
      getIbanOwnerName().setText(null);
    }
    ViewModel.viewToModel(this, model);

    if (model.getAddress().isAddressInvalid() && !model.getAddress().getStreetAndNumber()
        .equals(selectedMember.getAddress().getStreetAndNumber())) {
      // assume that the invalid address is fixed
      model.getAddress().setAddressInvalid(false);
    }

    service.persistMember(model);
    pageController.showMessage("Lidgegevens opgeslagen");

    if (currentMenuChoice == MenuChoice.NEW_MEMBER) {
      // next page
      eventPublisher.publishEvent(new WelcomeletterEvent(this, selectedMember));
    } else {
      pageController.activateLogoPage();
    }
  }

  @FXML
  void deletePDF() {
    documentService.deleteDocument(sepaAuthorization);
    pageController.showMessage("SEPA-machtiging is verwijderd");
    pdfViewer.close();

    selectedMember.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
    getPaymentMethod().setValue(PaymentMethodMapper.BANK_TRANSFER);
    service.persistMember(selectedMember);

    sepaAuthorization = null;
    getSepaButton().setVisible(false);
    getSepaLabel().setVisible(false);
  }

  @FXML
  void printPDF() {
    try {
      PrintUtil.print(pdfViewer.getPdf());
    } catch (PrintException e) {
      pageController.showErrorMessage(e.getMessage());
    }
  }

  @FXML
  void closePDF() {
    pdfViewer.close();
  }

  private String getHeaderTextValue() {
    switch (currentMenuChoice) {
      case NEW_MEMBER:
        return "Nieuw Lid opvoeren";
      case AMEND_MEMBER:
        return "Lid wijzigen";
      default:
        return "Invalid";
    }
  }

  void formatIbanOwnerName(String ibanNumber) {
    if (hasContent(ibanNumber)) {
      updateIbanOwnerName();
    } else {
      savedName = null;
      getIbanOwnerName().setText(null);
    }
    setIbanControls(ibanNumber);
  }
  
  private void setIbanControls(String ibanNumber) {
    ObservableList<Node> children = formController.getFinancialPage().getChildren();
    if (hasContent(ibanNumber)) {
      if (!children.contains(getIbanOwnerName())) {
        children.addAll(getIbanOwnerNameL(), getIbanOwnerName(), getBicCodeL(), getBicCode());
      }
    } else {
      children.removeAll(getIbanOwnerNameL(), getIbanOwnerName(),
          getBicCodeL(), getBicCode());
    }
  }

  private void updateIbanOwnerName() {
    String ibanName = getIbanOwnerName().getText();
    String memberName = formatMemberName();
    if (hasContent(ibanName)) {
      if (!ibanName.equals(savedName)) {
        // an explicit iban owner is specified by the user
        savedName = ibanName;
      } else if (!ibanName.equals(memberName)) {
        // the member name is changed an thus the implicit iban owner
        getIbanOwnerName().setText(memberName);
        savedName = memberName;
      }
    } else {
      // There is no iban owner yet
      getIbanOwnerName().setText(memberName);
      savedName = memberName;
    }
  }

  /**
   * Return the full name of the member as filled in the user interface. Return null when nothing is
   * filled in the user interface
   * 
   * @return the full name of the member or null
   */
  private String formatMemberName() {
    StringJoiner sj = new StringJoiner(" ");
    if (getInitials().getText() != null && getLastName().getText() != null) {
      formatName();
      sj.add(getInitials().getText());
      String lastNamePrefix = getLastNamePrefix().getText();
      if (hasContent(lastNamePrefix) && !"null".equals(lastNamePrefix)) {
        sj.add(lastNamePrefix);
      }
      sj.add(getLastName().getText());
      return sj.toString();
    }
    return null;
  }


  private boolean hasContent(String text) {
    return text != null && !text.isBlank();
  }

}
