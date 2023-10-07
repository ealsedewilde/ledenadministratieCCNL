package nl.ealse.ccnl.control.member;

import static nl.ealse.javafx.util.WrappedFileChooser.FileExtension.PDF;
import static nl.ealse.javafx.util.WrappedFileChooser.FileExtension.PNG;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.PDFViewer;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.javafx.util.PrintException;
import nl.ealse.javafx.util.PrintUtil;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

/**
 * Add SEPA authorization document for Direct Debits.
 */
@Controller
@Slf4j
public class SepaAuthorizarionController {

  @Value("${ccnl.directory.sepa:c:/temp}")
  private String sepaDirectory;

  private final IbanController ibanController;

  private final PageController pageController;

  private final DocumentService documentService;

  private final MemberService service;

  private PDFViewer pdfViewer;

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
  public SepaAuthorizarionController(PageController pageController, IbanController ibanController,
      DocumentService documentService, MemberService service) {
    this.pageController = pageController;
    this.ibanController = ibanController;
    this.documentService = documentService;
    this.service = service;
  }

  @PostConstruct
  void setup() {
    pdfViewer = PDFViewer.builder().withSaveButton(e -> addSepaPDF())
        .withPrintButton(e -> printSepaPDF()).withCancelButton(e -> closePDFViewer()).build();
    pdfViewer.setWindowTitle("SEPA machtiging toevoegen bij lid: %d (%s)");

    fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), PDF, PNG);
    fileChooser.setInitialDirectory(new File(sepaDirectory));

  }

  @EventListener(condition = "#event.name('PAYMENT_AUTHORIZATION')")
  public void onApplicationEvent(MemberSeLectionEvent event) {
    this.selectedMember = event.getSelectedEntity();
    if (selectedMember.getIbanNumber() == null) {
      ibanController.show();
    } else {
      selectSepaAuthorization();
    }
  }

  /**
   * Start adding SEPA authorization document.
   */
  @EventListener(value = IbanNumberAddedEvent.class)
  public void selectSepaAuthorization() {
    selectedFile = fileChooser.showOpenDialog();
    if (selectedFile != null) {
      pdfViewer.showPDF(selectedFile, selectedMember);
    }
  }

  @FXML
  void printSepaPDF() {
    try {
      PrintUtil.print(pdfViewer.getPdf());
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
    service.persistMember(selectedMember);
  }

  @FXML
  void closePDFViewer() {
    pdfViewer.close();
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
