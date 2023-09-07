package nl.ealse.ccnl.control.annual;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.DocumentTemplateController;
import nl.ealse.ccnl.control.HandledTask;
import nl.ealse.ccnl.control.PDFViewer;
import nl.ealse.ccnl.control.exception.AsyncTaskException;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.output.LetterData;
import nl.ealse.ccnl.ledenadministratie.pdf.content.FOContent;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.javafx.util.PrintException;
import nl.ealse.javafx.util.PrintUtil;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;

@Controller
public class PaymentReminderLettersController extends DocumentTemplateController {

  private final PageController pageController;

  private final DocumentService documentService;

  private final MemberService memberService;

  private final MemberLetterHandler memberLetterHandler;

  private final TaskExecutor executor;

  private List<Member> selectedMembers;

  @FXML
  private PDFViewer pdfViewer;

  @FXML
  private Label showSepa;

  @FXML
  private Label headerText;

  public PaymentReminderLettersController(DocumentService documentService,
      MemberService memberService, PageController pageController,
      MemberLetterHandler memberLetterHandler, TaskExecutor executor) {
    super(pageController, documentService, DocumentTemplateContext.PAYMENT_REMINDER);
    this.pageController = pageController;
    this.documentService = documentService;
    this.memberService = memberService;
    this.memberLetterHandler = memberLetterHandler;
    this.executor = executor;
  }

  @EventListener(condition = "#event.name('PRODUCE_REMINDER_LETTERS_BT')")
  public void remindersBT(MenuChoiceEvent event) {
    selectedMembers = memberService.findMembersCurrentYearNotPaid(PaymentMethod.BANK_TRANSFER);
    initTemplates(true);
    headerText.setText("Herinneringsbrief leden met Overboeking");
  }

  @EventListener(condition = "#event.name('PRODUCE_REMINDER_LETTERS_DD')")
  public void remindersDD(MenuChoiceEvent event) {
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
      data.getMembers().add(selectedMember);
      byte[] pdf = documentService.generatePDF(data);
      pdfViewer.showPDF(pdf, selectedMember);
    }
  }

  @FXML
  void saveLettersToFile() {
    if (overdueExists()) {
      File file = getFileChooser().showSaveDialog();
      if (file != null) {
        pageController.showPermanentMessage("Brieven worden aangemaakt; even geduld a.u.b.");
        PdfToFile pdfToFile = new PdfToFile(this, getLetterText().getText(), file);
        executor.execute(pdfToFile);
        pageController.setActivePage(PageName.LOGO);
      }
    }
  }

  @FXML
  void printLetters() {
    if (overdueExists()) {
      pageController.showPermanentMessage("Printen wordt voorbereid; even geduld a.u.b.");
      PdfToPrint pdfToPrint = new PdfToPrint(this, getLetterText().getText());
      executor.execute(pdfToPrint);
      pageController.setActivePage(PageName.LOGO);
    }
  }

  // End Letter related method

  @FXML
  void printPDF() {
    try {
      PrintUtil.print(pdfViewer.getPdf());
    } catch (PrintException e) {
      pageController.showErrorMessage(e.getMessage());
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
      pageController.showMessage("Geen betalingsachterstanden gevonden");
      return false;
    }
    return true;
  }

  private abstract static class AsyncGenerator extends HandledTask {

    private final PaymentReminderLettersController controller;
    private final String template;

    AsyncGenerator(PaymentReminderLettersController controller, String template) {
      super(controller.pageController);
      this.controller = controller;
      this.template = template;
    }

    @Override
    protected String call() {
      LetterData data = new LetterData(template);
      data.getMembers().addAll(controller.selectedMembers);
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
        PrintUtil.print(pdf);
        return "";
      } catch (PrintException e) {
        log.error("Printen is mislukt", e);
        throw new AsyncTaskException("Printen is mislukt");
      }
    }

  }

}
