package nl.ealse.ccnl.control.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
import nl.ealse.ccnl.ledenadministratie.util.MemberNumberFactory;
import nl.ealse.ccnl.mappers.PaymentMethodMapper;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.MemberService;
import nl.ealse.ccnl.view.MemberView;
import nl.ealse.javafx.mapping.DataMapper;
import nl.ealse.javafx.util.PrintException;
import nl.ealse.javafx.util.PrintUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController extends MemberView
    implements ApplicationListener<MemberSeLectionEvent> {

  private final PageController pageController;

  private final MemberService service;

  private final DocumentService documentService;

  private final MemberValidation memberValidation;

  private final MemberNumberFactory numberFactory;

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

  /**
   * Every page attached to this controller has its own SaveButton. This List will contain all
   * SaveButtons after FXML initialization of every page in this controller
   */
  private final List<SaveButton> saveButtonList = new ArrayList<>();

  public MemberController(PageController pageController, MemberService service,
      MemberNumberFactory numberFactory, DocumentService documentService) {
    this.pageController = pageController;
    this.service = service;
    this.documentService = documentService;
    this.numberFactory = numberFactory;
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
  @Override
  public void onApplicationEvent(MemberSeLectionEvent event) {
    if (event.getMenuChoice() == MenuChoice.NEW_MEMBER
        || event.getMenuChoice() == MenuChoice.AMEND_MEMBER) {
      this.currentMenuChoice = event.getMenuChoice();
      pageController.loadPage(PageName.MEMBER_ADDRESS);
      addressController.getHeaderText().setText(getHeaderText());
      pageController.loadPage(PageName.MEMBER_FINANCIAL);
      pageController.loadPage(PageName.MEMBER_EXTRA);
      String s = getHeaderText();
      headerTextList.forEach(ht -> ht.setText(s));
      this.selectedMember = event.getSelectedEntity();
      this.model = new Member();
      if (event.getMenuChoice() == MenuChoice.AMEND_MEMBER) {
        pageController.setActivePage(PageName.MEMBER_PERSONAL);
        Optional<Document> optSepaAuthorization =
            documentService.findSepaAuthorization(selectedMember);
        if (optSepaAuthorization.isPresent()) {
          pageController.loadPage(PageName.SEPA_AUTHORIZATION_SHOW);
          sepaAuthorization = optSepaAuthorization.get();
        } else {
          sepaAuthorization = null;
        }
      } else {
        selectedMember.setMemberNumber(numberFactory.getNewNumber());
        sepaAuthorization = null;
      }
      getIbanNumber().textProperty()
          .addListener((observable, oldValue, newValue) -> formatIbanOwnerName(newValue));
      reset();
    }
  }

  @FXML
  public void reset() {
    // the selectedMember remains unchanged, so we can repeatedly call reset().
    DataMapper.modelToForm(this, selectedMember);
    DataMapper.formToModel(this, model);
    initializeInitialsType();

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
  public void checkPaymentMethod() {
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
  public void showSepaAuthorization() {
    pdfViewer.showPDF(sepaAuthorization);
  }

  @FXML
  public void save() {
    enrich();
    addressController.enrich();
    DataMapper.formToModel(this, model);

    if (model.getAddress().isAddressInvalid() && !model.getAddress().getAddressAndNumber()
        .equals(selectedMember.getAddress().getAddressAndNumber())) {
      // assume that the invalid address is fixed
      model.getAddress().setAddressInvalid(false);
    }

    service.persistMember(model);
    pageController.setMessage("Lidgegevens opgeslagen");

    if (currentMenuChoice == MenuChoice.NEW_MEMBER) {
      // next page
      DataMapper.formToModel(this, selectedMember);
      pageController.setActivePage(PageName.WELCOME_LETTER);
    } else {
      pageController.setActivePage(PageName.LOGO);
    }
  }

  @FXML
  public void deletePDF() {
    documentService.deleteDocument(sepaAuthorization);
    pageController.setMessage("SEPA-machtiging is verwijderd");
    pdfViewer.close();

    selectedMember.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
    getPaymentMethod().setValue(PaymentMethodMapper.BANK_TRANSFER);
    service.persistMember(selectedMember);

    sepaAuthorization = null;
    getSepaButton().setVisible(false);
    getSepaLabel().setVisible(false);
  }

  @FXML
  public void printPDF() {
    try {
      PrintUtil.print(pdfViewer.getPdf());
    } catch (PrintException e) {
      pageController.setErrorMessage(e.getMessage());
    }
  }

  @FXML
  public void closePDF() {
    pdfViewer.close();
  }

  /*
   * Page selection method
   */

  @FXML
  public void nextPage() {
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
  public void previousPage() {
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

  public void firstPage() {
    currentPage = PageName.MEMBER_PERSONAL;
    pageController.setActivePage(currentPage);
    getInitials().requestFocus();
    memberValidation.validate();
  }

  public void secondPage() {
    currentPage = PageName.MEMBER_ADDRESS;
    pageController.setActivePage(currentPage);
    addressController.getAddress().requestFocus();
    memberValidation.validate();
  }

  public void thirdPage() {
    formatIbanOwnerName(getIbanNumber().getText());
    currentPage = PageName.MEMBER_FINANCIAL;
    pageController.setActivePage(currentPage);
    getIbanNumber().requestFocus();
    memberValidation.validate();
    checkPaymentMethod();
  }

  public void fourthPage() {
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
    if (ibanNumber != null && !ibanNumber.isBlank()) {
      String name = getIbanOwnerName().getText();
      if (name == null || name.isBlank() || name.indexOf("null") != -1) {
        StringJoiner sj = new StringJoiner(" ");
        if (getInitials().getText() != null && getLastName().getText() != null) {
          formatName();
          sj.add(getInitials().getText());
          if (getLastNamePrefix() != null) {
            sj.add(getLastNamePrefix().getText());
          }
          sj.add(getLastName().getText());
        }
        getIbanOwnerName().setText(sj.toString());
        getIbanOwnerNameL().setVisible(true);
        getIbanOwnerName().setVisible(true);
      }
    } else {
      getIbanOwnerName().setText(null);
      getIbanOwnerNameL().setVisible(false);
      getIbanOwnerName().setVisible(false);
    }
  }

}
