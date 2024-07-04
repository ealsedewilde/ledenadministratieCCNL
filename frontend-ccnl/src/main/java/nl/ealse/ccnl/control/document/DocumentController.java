package nl.ealse.ccnl.control.document;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import nl.ealse.ccnl.control.DocumentViewer;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.javafx.util.PrintException;
import nl.ealse.javafx.util.PrintUtil;

public class DocumentController {

  private final PageController pageController;

  private final DocumentService documentService;

  private Document selectedDocument;

  @FXML
  private Label memberNumber;
  @FXML
  private Label memberName;

  @FXML
  private TableView<Document> tableView;

  private DocumentViewer documentViewer;

  public DocumentController(PageController pageController, DocumentService documentService) {
    this.pageController = pageController;
    this.documentService = documentService;
    setup();
  }

  private void setup() {
    documentViewer = DocumentViewer.builder().withPrintButton(evt -> printDocument())
        .withDeleteButton(evt -> deleteDocument()).withCancelButton(evet -> closeDocument())
        .build();
  }

  @EventListener(menuChoice = MenuChoice.VIEW_DOCUMENT)
  public void viewDocument(MemberSeLectionEvent event) {
    pageController.setActivePage(PageName.VIEW_DOCUMENTS);
    Member member = event.getSelectedEntity();
    memberNumber.setText("Documenten voor lidnummer: " + member.getMemberNumber().toString());
    memberName.setText(member.getFullName());
    fillTableView(member);
  }

  private void fillTableView(Member member) {
    List<Document> documents = documentService.findDocuments(member);
    tableView.getItems().clear();
    tableView.getItems().addAll(documents);
  }

  @FXML
  void selectDocument(MouseEvent event) {
    @SuppressWarnings("unchecked")
    TableRow<Document> row = (TableRow<Document>) event.getSource();
    selectedDocument = row.getItem();
    if (selectedDocument != null) {
      documentViewer.setWindowTitle(
          String.format("Document '%s' voor lid:", selectedDocument.getDocumentName())
              + " %d (%s)");
      documentViewer.showDocument(selectedDocument);
    }
  }

  @FXML
  void printDocument() {
    try {
      PrintUtil.print(documentViewer.getDocument());
    } catch (PrintException e) {
      pageController.showErrorMessage(e.getMessage());
    }
  }

  @FXML
  void deleteDocument() {
    documentService.deleteDocument(selectedDocument);
    documentViewer.close();
    fillTableView(selectedDocument.getOwner());
    pageController.showMessage("Het document is verwijderd");
  }

  @FXML
  void closeDocument() {
    documentViewer.close();
  }

}
