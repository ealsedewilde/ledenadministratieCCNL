package nl.ealse.ccnl.control.member;

import static nl.ealse.javafx.util.WrappedFileChooser.FileExtension.PDF;
import static nl.ealse.javafx.util.WrappedFileChooser.FileExtension.PNG;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.DocumentViewer;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.event.support.EventPublisher;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseProperties;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.javafx.util.PrintException;
import nl.ealse.javafx.util.PrintUtil;
import nl.ealse.javafx.util.WrappedFileChooser;

/**
 * Add SEPA authorization document for Direct Debits.
 */
@Slf4j
public class SepaAuthorizarionController {

  private final PageController pageController;

  private final DocumentService documentService;

  private final MemberService service;

  private DocumentViewer documentViewer;

  private Member selectedMember;

  private File selectedFile;

  private WrappedFileChooser fileChooser;

  /**
   * ADD SEPA-authorization PDF to a member.
   *
   * @param pageController
   * @param ibanController - popup for adding iban-number to member
   * @param documentService
   * @param service
   */
  public SepaAuthorizarionController(PageController pageController, DocumentService documentService, MemberService service) {
    this.pageController = pageController;
    this.documentService = documentService;
    this.service = service;
    setup();
  }

  private void setup() {
    documentViewer = DocumentViewer.builder().withSaveButton(e -> addSepaPDF())
        .withPrintButton(e -> printSepaPDF()).withCancelButton(e -> closePDFViewer()).build();
    documentViewer.setWindowTitle("SEPA machtiging toevoegen bij lid: %d (%s)");

    fileChooser = new WrappedFileChooser(PDF, PNG);
    fileChooser.setInitialDirectory(
        () -> DatabaseProperties.getProperty("ccnl.directory.sepa", "c:/temp"));

  }

  @EventListener(menuChoice = MenuChoice.PAYMENT_AUTHORIZATION)
  public void onApplicationEvent(MemberSeLectionEvent event) {
    this.selectedMember = event.getSelectedEntity();
    if (selectedMember.getIbanNumber() == null) {
      EventPublisher
          .publishEvent(new AddIbanNumberEvent(selectedMember, this::selectSepaAuthorization));
    } else {
      selectSepaAuthorization();
    }
  }

  /**
   * Start adding SEPA authorization document.
   */
  public Void selectSepaAuthorization() {
    selectedFile = fileChooser.showOpenDialog();
    if (selectedFile != null) {
      documentViewer.showDocument(selectedFile, selectedMember);
    }
    return null;
  }

  @FXML
  void printSepaPDF() {
    try {
      PrintUtil.print(documentViewer.getDocument());
    } catch (PrintException e) {
      pageController.showErrorMessage(e.getMessage());
    }
  }

  @FXML
  void addSepaPDF() {
    Document document = new Document();
    document.setPdf(toByteArray(selectedFile));
    document.setDocumentName(selectedFile.getName());
    document.setDocumentType(DocumentType.SEPA_AUTHORIZATION);
    document.setOwner(selectedMember);

    documentService.saveDocument(document);
    pageController.showMessage("SEPA-machtiging opgeslagen bij lid");
    pageController.activateLogoPage();
    closePDFViewer();

    selectedMember.setPaymentMethod(PaymentMethod.DIRECT_DEBIT);
    service.save(selectedMember);
  }

  @FXML
  void closePDFViewer() {
    documentViewer.close();
  }

  private byte[] toByteArray(File selectedFile) {
    try (FileInputStream fis = new FileInputStream(selectedFile)) {
      return fis.readAllBytes();
    } catch (IOException e) {
      log.error("Unable to read PDF-file", e);
      return new byte[0];
    }
  }


}
