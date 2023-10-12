package nl.ealse.ccnl.control.member;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javafx.fxml.FXML;
import javax.print.PrintService;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.DocumentTemplateController;
import nl.ealse.ccnl.control.PDFViewer;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.output.LetterData;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.javafx.util.PrintException;
import nl.ealse.javafx.util.PrintUtil;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class WelcomeLetterController extends DocumentTemplateController {

  private final PageController pageController;

  private final DocumentService documentService;

  private Member selectedMember;

  private PDFViewer pdfViewer;

  public WelcomeLetterController(PageController pageController, DocumentService documentService) {
    super(pageController, documentService, DocumentTemplateContext.WELCOME_LETTER);
    this.pageController = pageController;
    this.documentService = documentService;
  }

  @EventListener(condition = "#event.name('NEW_MEMBER')")
  public void onApplicationEvent(MemberSeLectionEvent event) {
    this.selectedMember = event.getSelectedEntity();
  }
  
  @FXML
  protected void initialize() {
    super.initialize();
    initializeTemplates();
    pdfViewer = PDFViewer.builder().withPrintButton(evt -> printPDF())
        .withCancelButton(evt -> closePDF()).build();
    pdfViewer.setWindowTitle("Welkomsbrief voor lid: %d (%s)");
  }

  @FXML
  void showLetterExample() {
    log.debug("showLetterExample");
    LetterData data = new LetterData(getLetterText().getText());
    data.getMembers().add(selectedMember);
    byte[] pdf = documentService.generatePDF(data);
    pdfViewer.showPDF(pdf, selectedMember);
  }

  @FXML
  void saveletter() {
    getFileChooser().setInitialFileName("WelkomLid " + selectedMember.getMemberNumber() + ".docx");
    File file = getFileChooser().showSaveDialog();
    if (file != null) {
      LetterData data = new LetterData(getLetterText().getText());
      data.getMembers().add(selectedMember);
      byte[] docx = documentService.generateWordDocument(data);
      try (FileOutputStream fos = new FileOutputStream(file)) {
        fos.write(docx);
      } catch (IOException e) {
        log.error("Could not write Word document", e);
        pageController.showErrorMessage("Schrijven MS Word-document is mislukt");
      }
    }
  }

  @FXML
  void printLetter() {
    log.debug("printLetter");
    LetterData data = new LetterData(getLetterText().getText());
    data.getMembers().add(selectedMember);
    byte[] pdf = documentService.generatePDF(data);
    saveWelcomeLetter(pdf);
    try {
      Optional<PrintService> ps = PrintUtil.print(pdf);
      if (ps.isPresent()) {
        printAttachement(ps);
        pageController.activateLogoPage();
      }
    } catch (PrintException e) {
      pageController.showErrorMessage(e.getMessage());
    }
  }

  private void saveWelcomeLetter(byte[] pdf) {
    List<Document> letters =
        documentService.findDocuments(selectedMember, DocumentType.WELCOME_LETTER);
    Document document;
    if (letters.isEmpty()) {
      document = new Document();
      document.setDocumentType(DocumentType.WELCOME_LETTER);
      document.setDocumentName("WelkomLid " + selectedMember.getMemberNumber() + ".pdf");
      document.setDescription(DocumentType.WELCOME_LETTER.getDescription());
      document.setOwner(selectedMember);
    } else {
      document = letters.get(0);
    }
    document.setPdf(pdf);
    documentService.saveDocument(document);
  }

  @FXML
  void printPDF() {
    try {
      Optional<PrintService> ps = PrintUtil.print(pdfViewer.getPdf());
      if (ps.isPresent()) {
        printAttachement(ps);
        pdfViewer.close();
        pageController.activateLogoPage();
      }
    } catch (PrintException e) {
      pageController.showErrorMessage(e.getMessage());
    }
  }

  private void printAttachement(Optional<PrintService> ps) throws PrintException {
    if (getAddSepa().isSelected() && ps.isPresent()) {
      Optional<Document> attachment = documentService.findSepaAuthorizationForm();
      if (attachment.isPresent()) {
        PrintUtil.printAttachment(ps.get(), attachment.get().getPdf());
      }
    }
  }

  @FXML
  void closePDF() {
    pdfViewer.close();
  }

}
