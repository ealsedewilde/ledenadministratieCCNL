package nl.ealse.ccnl.control.member;

import static nl.ealse.javafx.util.WrappedFileChooser.FileExtension.PDF;
import static nl.ealse.javafx.util.WrappedFileChooser.FileExtension.PNG;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.PDFViewer;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.javafx.ImagesMap;
import nl.ealse.javafx.util.PrintException;
import nl.ealse.javafx.util.PrintUtil;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class SepaAuthorizarionController {

  @Value("${ccnl.directory.sepa:c:/temp}")
  private String sepaDirectory;

  private final PageController pageController;

  private final DocumentService documentService;

  private final MemberService service;

  @FXML
  private PDFViewer pdfViewer;

  private Member selectedMember;

  private File selectedFile;

  private WrappedFileChooser fileChooser;

  @Getter
  private Stage ibanNumberStage;


  public SepaAuthorizarionController(PageController pageController, DocumentService documentService,
      MemberService service) {
    this.pageController = pageController;
    this.documentService = documentService;
    this.service = service;
  }

  @FXML
  void initialize() {
    fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), PDF, PNG);
    fileChooser.setInitialDirectory(new File(sepaDirectory));

    ibanNumberStage = new Stage();
    ibanNumberStage.initModality(Modality.APPLICATION_MODAL);
    ibanNumberStage.setTitle("IBAN-nummer toevoegen");
    ibanNumberStage.getIcons().add(ImagesMap.get("Citroen.png"));
    ibanNumberStage.initOwner(pageController.getPrimaryStage());
    Parent p = pageController.loadPage(PageName.ADD_IBAN_NUMBER);
    Scene dialogScene = new Scene(p, 1200, 400);
    ibanNumberStage.setScene(dialogScene);

  }

  @EventListener(condition = "#event.name('PAYMENT_AUTHORIZATION')")
  public void onApplicationEvent(MemberSeLectionEvent event) {
    this.selectedMember = event.getSelectedEntity();
    if (selectedMember.getIbanNumber() == null) {
      ibanNumberStage.show();
    } else {
      selectSepaAuthorization();
    }
  }

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
    pdfViewer.close();

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
