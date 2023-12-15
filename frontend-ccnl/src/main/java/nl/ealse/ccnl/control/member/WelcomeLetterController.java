package nl.ealse.ccnl.control.member;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javafx.fxml.FXML;
import javax.print.PrintService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.DocumentTemplateController;
import nl.ealse.ccnl.control.DocumentViewer;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.output.LetterData;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.javafx.util.PdfPrintDocument;
import nl.ealse.javafx.util.PrintException;
import nl.ealse.javafx.util.PrintUtil;

@Slf4j
public class WelcomeLetterController extends DocumentTemplateController {
  
  @Getter
  private static final WelcomeLetterController instance = new WelcomeLetterController();

  private final PageController pageController;

  private final DocumentService documentService;

  private Member selectedMember;

  private DocumentViewer documentViewer;

  private WelcomeLetterController() {
    super(DocumentTemplateContext.WELCOME_LETTER);
    this.pageController = PageController.getInstance();
    this.documentService = DocumentService.getInstance();
  }

  @EventListener
  public void onApplicationEvent(WelcomeletterEvent event) {
    this.selectedMember = event.getMember();
    pageController.setActivePage(PageName.WELCOME_LETTER);
  }
  
  @FXML
  protected void initialize() {
    initializeTemplates();
    documentViewer = DocumentViewer.builder().withPrintButton(evt -> printPDF())
        .withCancelButton(evt -> closePDF()).build();
    documentViewer.setWindowTitle("Welkomsbrief voor lid: %d (%s)");
  }

  @FXML
  void showLetterExample() {
    log.debug("showLetterExample");
    LetterData data = new LetterData(getLetterText().getText());
    data.getMembers().add(selectedMember);
    byte[] pdf = documentService.generatePDF(data);
    documentViewer.showPdf(pdf, selectedMember);
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
      Optional<PrintService> ps = PrintUtil.print(new PdfPrintDocument(pdf));
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
      Optional<PrintService> ps = PrintUtil.print(documentViewer.getDocument());
      if (ps.isPresent()) {
        printAttachement(ps);
        documentViewer.close();
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
    documentViewer.close();
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
