package nl.ealse.ccnl.control.member;

import java.util.Optional;
import java.util.StringJoiner;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.Getter;
import nl.ealse.ccnl.control.PDFViewer;
import nl.ealse.ccnl.control.SearchController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.MenuController;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.form.FormController;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.mappers.PaymentMethodMapper;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.view.MemberView;
import nl.ealse.javafx.mapping.Mapping;
import nl.ealse.javafx.mapping.ViewModel;
import nl.ealse.javafx.util.PrintException;
import nl.ealse.javafx.util.PrintUtil;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController extends MemberView implements FormController {

  private final PageController pageController;

  private final MemberService service;

  private final DocumentService documentService;

  @Getter
  private final MemberValidation memberValidation;

  private Member model;

  private Member selectedMember;

  private Document sepaAuthorization;

  private MenuChoice currentMenuChoice;

  private PDFViewer pdfViewer;

  @FXML
  @Getter
  @Mapping(ignore = true)
  private Pane formMenu;

  @FXML
  @Getter
  @Mapping(ignore = true)
  private Pane formPage;

  @FXML
  @Getter
  @Mapping(ignore = true)
  private Pane formButtons;

  @FXML
  private Label headerText;

  @FXML
  @Getter
  @Mapping(ignore = true)
  private Button nextButton;

  @FXML
  @Getter
  @Mapping(ignore = true)
  private Button previousButton;

  @FXML
  private Button saveButton;

  // Helper to detect changes in the iban owner
  private String savedName;

  private MemberFormPages formPages;

  public MemberController(PageController pageController, MemberService service,
      DocumentService documentService) {
    this.pageController = pageController;
    this.service = service;
    this.documentService = documentService;
    this.memberValidation = new MemberValidation(this);
    bindFxml();
  }

  private void bindFxml() {
    pageController.loadPage(PageName.MEMBER_FORM, this);
    formPages = new MemberFormPages(this);

    memberValidation.initialize();
    memberValidation.setCallback(valid -> saveButton.setDisable(!valid));

    pdfViewer = PDFViewer.builder().withDeleteButton(e -> deletePDF())
        .withPrintButton(e -> printPDF()).withCancelButton(e -> closePDF()).build();
    pdfViewer.setWindowTitle("SEPA machtiging voor lid: %d (%s)");
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
  public void newMember(MemberSeLectionEvent event) {
    pageController.setActivePage(PageName.MEMBER_FORM);
    formPages.setActiveFormPage(0);
    handleEvent(event);
    selectedMember.setMemberNumber(service.getFreeNumber());
    sepaAuthorization = null;
    reset();
  }

  /**
   * Initializes the Model. For a new Member the event was fired by {@link MenuController} For an
   * existing Member the {@link SearchController} fires the event.
   */
  @EventListener(condition = "#event.name('AMEND_MEMBER')")
  public void amendMember(MemberSeLectionEvent event) {
    pageController.setActivePage(PageName.MEMBER_FORM);
    formPages.setActiveFormPage(0);
    handleEvent(event);
    Optional<Document> optSepaAuthorization = documentService.findSepaAuthorization(selectedMember);
    if (optSepaAuthorization.isPresent()) {
      sepaAuthorization = optSepaAuthorization.get();
    } else {
      sepaAuthorization = null;
    }
    reset();
  }

  private void handleEvent(MemberSeLectionEvent event) {
    this.currentMenuChoice = event.getMenuChoice();
    this.selectedMember = event.getSelectedEntity();
    this.model = new Member();
    getIbanNumber().textProperty()
        .addListener((observable, oldValue, newValue) -> formatIbanOwnerName(newValue));
    headerText.setText(getHeaderText());
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
    memberValidation.initialize();
    saveButton.setDisable(currentMenuChoice == MenuChoice.NEW_MEMBER);
    formPages.setActiveFormPage(0);
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
    memberValidation.validate();
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
      ViewModel.viewToModel(this, selectedMember);
      pageController.setActivePage(PageName.WELCOME_LETTER);
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

  /*
   * Page selection method
   */

  @FXML
  void nextPage() {
    formPages.setActiveFormPage(formPages.getCurrentPage() + 1);
  }

  @FXML
  void previousPage() {
    formPages.setActiveFormPage(formPages.getCurrentPage() - 1);
  }

  private String getHeaderText() {
    switch (currentMenuChoice) {
      case NEW_MEMBER:
        return "Nieuw Lid opvoeren";
      case AMEND_MEMBER:
        return "Lid wijzigen";
      default:
        return "Invalid";
    }
  }

  private void formatIbanOwnerName(String ibanNumber) {
    ObservableList<Node> children = formPages.getThirdPage().getChildren();
    if (hasContent(ibanNumber)) {
      updateIbanOwnerName();
      if (!children.contains(getIbanOwnerName())) {
        children.addAll(getIbanOwnerNameL(), getIbanOwnerName(), getBicCodeL(), getBicCode());
      }
    } else {
      savedName = null;
      getIbanOwnerName().setText(null);
      formPages.getThirdPage().getChildren().removeAll(getIbanOwnerNameL(), getIbanOwnerName(),
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

  @Override
  public void validateForm() {
    memberValidation.validate();

  }

}
