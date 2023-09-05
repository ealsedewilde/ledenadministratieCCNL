package nl.ealse.ccnl.control.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class AddDocumentController {

  @Value("${ccnl.directory.sepa:c:/temp}")
  private String sepaDirectory;

  private final PageController pageController;

  private final DocumentService documentService;

  private Member selectedMember;

  private File selectedFile;

  @FXML
  private Label fileName;

  @FXML
  private Label memberNumber;
  @FXML
  private Label memberName;

  @FXML
  private ChoiceBox<String> documentType;

  @FXML
  private TextField documentDescription;

  @FXML
  private Button saveButton;

  private WrappedFileChooser fileChooser;


  public AddDocumentController(PageController pageController, DocumentService documentService) {
    this.pageController = pageController;
    this.documentService = documentService;
  }

  @EventListener(condition = "#event.name('ADD_DOCUMENT')")
  public void addDocument(MemberSeLectionEvent event) {
    this.selectedMember = event.getSelectedEntity();
    memberNumber.setText("Document voor lidnummer: " + selectedMember.getMemberNumber().toString());
    memberName.setText(selectedMember.getFullName());
    documentType.getSelectionModel().select(DocumentType.values().length - 2);
    documentDescription.setText(null);
    selectedFile = null;
    saveButton.setDisable(true);
    pageController.setActivePage(PageName.ADD_DOCUMENT);
    fileName.setText(null);
  }

  @FXML
  void initialize() {
    for (DocumentType type : DocumentType.values()) {
      if (!"N/A".equals(type.getDescription())) {
        documentType.getItems().add(type.getDescription());
      }
    }
    fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), FileExtension.PDF);
    fileChooser.setInitialDirectory(new File(sepaDirectory));
  }

  @FXML
  void searchDocument() {
    File file = fileChooser.showOpenDialog();
    if (file != null) {
      selectedFile = file;
      fileName.setText("Geslecteerd bestand: " + selectedFile.getName());
      saveButton.setDisable(false);
    }
  }

  public void addDocument() {
    Optional<byte[]> pdf = getFileContent();
    if (pdf.isPresent()) {
      Document document = new Document();
      int ix = documentType.getSelectionModel().getSelectedIndex();
      document.setDocumentType(DocumentType.values()[ix]);
      String dt = documentDescription.getText();
      if (dt == null || dt.isBlank()) {
        document.setDescription(DocumentType.values()[ix].getDescription());
      } else {
        document.setDescription(dt);
      }
      document.setPdf(pdf.get());
      document.setCreationDate(LocalDate.now());
      document.setDocumentName(selectedFile.getName());
      document.setOwner(selectedMember);
      documentService.saveDocument(document);
      pageController.showMessage("Document is toegevoegd");
      pageController.setActivePage(PageName.LOGO);

      saveButton.setDisable(true);
      fileName.setText(null);
    }
  }

  private Optional<byte[]> getFileContent() {
    try (FileInputStream fis = new FileInputStream(selectedFile)) {
      return Optional.of(fis.readAllBytes());
    } catch (IOException e) {
      log.error("Error loading file", e);
      pageController.showErrorMessage("Fout bij inlezen bestand");
      return Optional.empty();
    }
  }

}
