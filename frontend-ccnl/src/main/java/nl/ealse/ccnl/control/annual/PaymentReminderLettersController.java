package nl.ealse.ccnl.control.annual;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.DocumentTemplateController;
import nl.ealse.ccnl.control.PDFViewer;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.output.LetterData;
import nl.ealse.ccnl.ledenadministratie.pdf.content.FOContent;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.MemberService;
import nl.ealse.javafx.util.PrintException;
import nl.ealse.javafx.util.PrintUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class PaymentReminderLettersController extends DocumentTemplateController
    implements ApplicationListener<MenuChoiceEvent> {

  private final PageController pageController;

  private final DocumentService documentService;

  private final MemberService memberService;

  private final MemberLetterHandler memberLetterHandler;

  private List<Member> selectedMembers;

  @FXML
  private PDFViewer pdfViewer;

  @FXML
  private Label showSepa;

  @FXML
  private Label headerText;

  public PaymentReminderLettersController(DocumentService documentService,
      MemberService memberService, PageController pageController,
      MemberLetterHandler memberLetterHandler) {
    super(pageController, documentService, DocumentTemplateContext.PAYMENT_REMINDER);
    this.pageController = pageController;
    this.documentService = documentService;
    this.memberService = memberService;
    this.memberLetterHandler = memberLetterHandler;
  }

  @Override
  public void onApplicationEvent(MenuChoiceEvent event) {
    if (MenuChoice.PRODUCE_REMINDER_LETTERS_BT == event.getMenuChoice()) {
      selectedMembers = memberService.findMembersCurrentYearNotPaid(PaymentMethod.BANK_TRANSFER);
      initTemplates(true);
      headerText.setText("Herinneringsbrief leden met Overboeking");
    } else if (MenuChoice.PRODUCE_REMINDER_LETTERS_DD == event.getMenuChoice()) {
      selectedMembers = memberService.findMembersCurrentYearNotPaid(PaymentMethod.DIRECT_DEBIT);
      initTemplates(false);
      headerText.setText("Herinneringsbrief leden met Automatische Incasso");
    }
  }

  private void initTemplates(boolean withSepa) {
    initializeTemplates(withSepa);
    showSepa.setVisible(withSepa);
    getAddSepa().setVisible(withSepa);
    getAddSepa().setSelected(withSepa);
  }

  // Begin Letter related method

  @FXML
  public void showLetterExample() {
    if (overdueExists()) {
      Member selectedMember = selectedMembers.get(0);
      LetterData data = new LetterData(getLetterText().getText());
      data.getMembers().add(selectedMember);
      byte[] pdf = documentService.generatePDF(data);
      pdfViewer.showPDF(pdf, selectedMember);
    }
  }

  @FXML
  public void saveLettersToFile() {
    if (overdueExists()) {
      File file = getFileChooser().showSaveDialog();
      if (file != null) {
        LetterData data = new LetterData(getLetterText().getText());
        data.getMembers().addAll(selectedMembers);
        FOContent foContent = documentService.generateFO(data);
        byte[] pdf = documentService.generatePDF(foContent, data);
        try (FileOutputStream fos = new FileOutputStream(file)) {
          fos.write(pdf);
          memberLetterHandler.addLetterToMembers(foContent, selectedMembers);
          pageController.setMessage("Bestand is aangemaakt");
        } catch (IOException e) {
          log.error("Could not letters document", e);
          pageController.setErrorMessage("Schrijven brieven is mislukt");
        }
      }
    }
  }

  @FXML
  public void printLetters() {
    if (overdueExists()) {
      LetterData data = new LetterData(getLetterText().getText());
      data.getMembers().addAll(selectedMembers);
      FOContent foContent = documentService.generateFO(data);
      byte[] pdf = documentService.generatePDF(foContent, data);
      try {
        PrintUtil.print(pdf);
      } catch (PrintException e) {
        pageController.setErrorMessage(e.getMessage());
      }
      pageController.setActivePage(PageName.LOGO);
      memberLetterHandler.addLetterToMembers(foContent, selectedMembers);
    }
  }

  // End Letter related method

  @FXML
  public void printPDF() {
    try {
      PrintUtil.print(pdfViewer.getPdf());
    } catch (PrintException e) {
      pageController.setErrorMessage(e.getMessage());
    }
    pdfViewer.close();
    pageController.setActivePage(PageName.LOGO);
  }

  @FXML
  public void closePDF() {
    pdfViewer.close();
  }

  private boolean overdueExists() {
    if (selectedMembers.isEmpty()) {
      pageController.setMessage("Geen betalingsachterstanden gevonden");
      return false;
    }
    return true;
  }

}
