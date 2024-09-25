package nl.ealse.ccnl.control.annual;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.AsyncTaskException;
import nl.ealse.ccnl.control.DocumentTemplateController;
import nl.ealse.ccnl.control.DocumentViewer;
import nl.ealse.ccnl.control.HandledTask;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.output.LetterData;
import nl.ealse.ccnl.ledenadministratie.pdf.content.FOContent;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.javafx.util.PdfPrintDocument;
import nl.ealse.javafx.util.PrintException;
import nl.ealse.javafx.util.PrintUtil;

public class PaymentReminderLettersController extends DocumentTemplateController {

  private final PageController pageController;

  private final DocumentService documentService;

  private final MemberService memberService;

  private final MemberLetterHandler memberLetterHandler;

  private List<Member> selectedMembers;

  private DocumentViewer documentViewer;

  @FXML
  private Label showSepa;

  @FXML
  private Label headerText;

  public PaymentReminderLettersController(PageController pageController,
      DocumentService documentService, MemberService memberService,
      MemberLetterHandler memberLetterHandler) {
    super(DocumentTemplateContext.PAYMENT_REMINDER);
    this.pageController = pageController;
    this.documentService = documentService;
    this.memberService = memberService;
    this.memberLetterHandler = memberLetterHandler;
  }

  @FXML
  protected void initialize() {
    documentViewer = DocumentViewer.builder().withPrintButton(evt -> printPDF())
        .withCancelButton(evt -> closePDF()).build();
    documentViewer.setWindowTitle("Herinneringsbrief voor lid: %d (%s)");
  }

  @EventListener(menuChoice = MenuChoice.PRODUCE_REMINDER_LETTERS_BT)
  public void remindersBT(MenuChoiceEvent event) {
    pageController.setActivePage(PageName.PAYMENT_REMINDER_LETTERS);
    selectedMembers = memberService.findMembersCurrentYearNotPaid(PaymentMethod.BANK_TRANSFER);
    initTemplates(true);
    headerText.setText("Herinneringsbrief leden met Overboeking");
  }

  @EventListener(menuChoice = MenuChoice.PRODUCE_REMINDER_LETTERS_DD)
  public void remindersDD(MenuChoiceEvent event) {
    pageController.setActivePage(PageName.PAYMENT_REMINDER_LETTERS);
    selectedMembers = memberService.findMembersCurrentYearNotPaid(PaymentMethod.DIRECT_DEBIT);
    initTemplates(false);
    headerText.setText("Herinneringsbrief leden met Automatische Incasso");
  }

  private void initTemplates(boolean withSepa) {
    initializeTemplates(withSepa);
    showSepa.setVisible(withSepa);
    getAddSepa().setVisible(withSepa);
    getAddSepa().setSelected(withSepa);
  }

  // Begin Letter related method

  @FXML
  void showLetterExample() {
    if (overdueExists()) {
      Member selectedMember = selectedMembers.get(0);
      LetterData data = new LetterData(getLetterText().getText());
      data.members().add(selectedMember);
      byte[] pdf = documentService.generatePDF(data);
      documentViewer.showPdf(pdf, selectedMember);
    }
  }

  @FXML
  void saveLettersToFile() {
    if (overdueExists()) {
      File file = getFileChooser().showSaveDialog();
      if (file != null) {
        pageController.showPermanentMessage("Brieven worden aangemaakt; even geduld a.u.b.");
        PdfToFile pdfToFile = new PdfToFile(this, getLetterText().getText(), file);
        pdfToFile.executeTask();
        pageController.activateLogoPage();
      }
    }
  }

  @FXML
  void printLetters() {
    if (overdueExists()) {
      pageController.showPermanentMessage("Printen wordt voorbereid; even geduld a.u.b.");
      PdfToPrint pdfToPrint = new PdfToPrint(this, getLetterText().getText());
      pdfToPrint.executeTask();
      pageController.activateLogoPage();
    }
  }

  // End Letter related method

  @FXML
  void printPDF() {
    try {
      PrintUtil.print(documentViewer.getDocument());
    } catch (PrintException e) {
      pageController.showErrorMessage(e.getMessage());
    }
    documentViewer.close();
    pageController.activateLogoPage();
  }

  @FXML
  void closePDF() {
    documentViewer.close();
  }

  private boolean overdueExists() {
    if (selectedMembers.isEmpty()) {
      pageController.showMessage("Geen betalingsachterstanden gevonden");
      return false;
    }
    return true;
  }

  private abstract static class AsyncGenerator extends HandledTask {

    private final PaymentReminderLettersController controller;
    private final String template;

    AsyncGenerator(PaymentReminderLettersController controller, String template) {
      this.controller = controller;
      this.template = template;
    }

    @Override
    protected String call() {
      LetterData data = new LetterData(template);
      data.members().addAll(controller.selectedMembers);
      FOContent foContent = controller.documentService.generateFO(data);
      controller.memberLetterHandler.addLetterToMembers(foContent, controller.selectedMembers);
      byte[] pdf = controller.documentService.generatePDF(foContent, data);
      return postProcess(pdf);
    }

    protected abstract String postProcess(byte[] pdf);
  }

  @Slf4j
  protected static class PdfToFile extends AsyncGenerator {
    private final File targetFile;

    PdfToFile(PaymentReminderLettersController controller, String template, File targetFile) {
      super(controller, template);
      this.targetFile = targetFile;
    }

    @Override
    protected String postProcess(byte[] pdf) {
      try (FileOutputStream fos = new FileOutputStream(targetFile)) {
        fos.write(pdf);
        return "Bestand is aangemaakt";
      } catch (IOException e) {
        log.error("Bestand aanmaken is mislukt", e);
        throw new AsyncTaskException("Bestand aanmaken is mislukt");
      }
    }

  }

  @Slf4j
  protected static class PdfToPrint extends AsyncGenerator {

    PdfToPrint(PaymentReminderLettersController controller, String template) {
      super(controller, template);
    }

    @Override
    protected String postProcess(byte[] pdf) {
      try {
        PrintUtil.print(new PdfPrintDocument(pdf));
        return "";
      } catch (PrintException e) {
        log.error("Printen is mislukt", e);
        throw new AsyncTaskException("Printen is mislukt");
      }
    }

  }

  @Override
  protected PageController getPageController() {
    return pageController;
  }

  @Override
  protected DocumentService getDocumentService() {
    return documentService;
  }

}
