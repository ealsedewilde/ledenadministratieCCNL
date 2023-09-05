package nl.ealse.ccnl.control.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import nl.ealse.ccnl.control.PDFViewer;
import nl.ealse.ccnl.control.SearchController;
import nl.ealse.ccnl.control.address.AddressController;
import nl.ealse.ccnl.control.button.SaveButton;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.MenuController;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
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
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController extends MemberView {

  private final PageController pageController;

  private final MemberService service;

  private final DocumentService documentService;

  private final MemberValidation memberValidation;

  private Member model;

  private Member selectedMember;

  private Document sepaAuthorization;

  private PageName currentPage;

  private MenuChoice currentMenuChoice;

  @Getter
  @FXML
  private AddressController addressController;

  /**
   * 
   */
  @FXML
  private PDFViewer pdfViewer;


  /*
   * The FXML-references below are per current page. So if this controller services a sequence of 4
   * pages, then there are 4 separate references but three of them are 'hidden'.
   */

  @FXML
  private Label headerText;
  /**
   * Every page attached to this controller has its own 'headerText' Label. This List will contain
   * all 'headerText' Labels after FXML initialization of every page in this controller
   */
  private final List<Label> headerTextList = new ArrayList<>();

  @FXML
  private SaveButton saveButton;

  // Helper to detect changes in the iban owner
  private String savedName;

  /**
   * Every page attached to this controller has its own SaveButton. This List will contain all
   * SaveButtons after FXML initialization of every page in this controller
   */
  private final List<SaveButton> saveButtonList = new ArrayList<>();

  public MemberController(PageController pageController, MemberService service,
      DocumentService documentService) {
    this.pageController = pageController;
    this.service = service;
    this.documentService = documentService;
    this.memberValidation = new MemberValidation(this);
  }

  @FXML
  public void initialize() {
    super.initializeView();

    headerTextList.add(headerText);
    saveButtonList.add(saveButton);
    memberValidation
        .initializeValidation(valid -> saveButtonList.forEach(button -> button.setDisable(!valid)));
  }

  /**
   * Initializes the Model. For a new Member the event was fired by {@link MenuController} For an
   * existing Member the {@link SearchController} fires the event.
   */
  @EventListener(condition = "#event.name('NEW_MEMBER')")
  public void newMember(MemberSeLectionEvent event) {
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
    handleEvent(event);
    pageController.setActivePage(PageName.MEMBER_PERSONAL);
    Optional<Document> optSepaAuthorization = documentService.findSepaAuthorization(selectedMember);
    if (optSepaAuthorization.isPresent()) {
      pageController.loadPage(PageName.SEPA_AUTHORIZATION_SHOW);
      sepaAuthorization = optSepaAuthorization.get();
    } else {
      sepaAuthorization = null;
    }
    reset();
  }

  public void handleEvent(MemberSeLectionEvent event) {
    this.currentMenuChoice = event.getMenuChoice();
    pageController.loadPage(PageName.MEMBER_ADDRESS);
    addressController.getHeaderText().setText(getHeaderText());
    pageController.loadPage(PageName.MEMBER_FINANCIAL);
    pageController.loadPage(PageName.MEMBER_EXTRA);
    String s = getHeaderText();
    headerTextList.forEach(ht -> ht.setText(s));
    this.selectedMember = event.getSelectedEntity();
    this.model = new Member();
    getIbanNumber().textProperty()
        .addListener((observable, oldValue, newValue) -> formatIbanOwnerName(newValue));
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
    saveButtonList.forEach(button -> button.setDisable(currentMenuChoice == MenuChoice.NEW_MEMBER));
    firstPage();
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
    addressController.enrich();
    updateIbanOwnerName();
    if (savedName == null || savedName.equals(formatMemberName())) {
      // The iban owner is implicit
      getIbanOwnerName().setText(null);
    }
    ViewModel.viewToModel(this, model);

    if (model.getAddress().isAddressInvalid() && !model.getAddress().getAddressAndNumber()
        .equals(selectedMember.getAddress().getAddressAndNumber())) {
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
      pageController.setActivePage(PageName.LOGO);
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
    switch (currentPage) {
      case MEMBER_PERSONAL:
        secondPage();
        break;
      case MEMBER_ADDRESS:
        thirdPage();
        break;
      case MEMBER_FINANCIAL:
      default:
        fourthPage();
        break;
    }
  }

  @FXML
  void previousPage() {
    switch (currentPage) {
      case MEMBER_ADDRESS:
        firstPage();
        break;
      case MEMBER_FINANCIAL:
        secondPage();
        break;
      case MEMBER_EXTRA:
      default:
        thirdPage();
        break;
    }
    pageController.setActivePage(currentPage);

  }

  @FXML
  void firstPage() {
    currentPage = PageName.MEMBER_PERSONAL;
    pageController.setActivePage(currentPage);
    getInitials().requestFocus();
    memberValidation.validate();
  }

  @FXML
  void secondPage() {
    currentPage = PageName.MEMBER_ADDRESS;
    pageController.setActivePage(currentPage);
    addressController.getStreet().requestFocus();
    memberValidation.validate();
  }

  @FXML
  void thirdPage() {
    formatIbanOwnerName(getIbanNumber().getText());
    currentPage = PageName.MEMBER_FINANCIAL;
    pageController.setActivePage(currentPage);
    getIbanNumber().requestFocus();
    memberValidation.validate();
    checkPaymentMethod();
  }

  @FXML
  void fourthPage() {
    currentPage = PageName.MEMBER_EXTRA;
    pageController.setActivePage(currentPage);
    getMemberInfo().requestFocus();
    memberValidation.validate();
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
    ObservableList<Node> children = getFinancialPane().getChildren();
    if (hasContent(ibanNumber)) {
      updateIbanOwnerName();
      if (!children.contains(getIbanOwnerName())) {
        children.addAll(getIbanOwnerNameL(), getIbanOwnerName(),
            getBicCodeL(), getBicCode());
      }
    } else {
      savedName = null;
      getIbanOwnerName().setText(null);
      getFinancialPane().getChildren().removeAll(getIbanOwnerNameL(), getIbanOwnerName(),
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
