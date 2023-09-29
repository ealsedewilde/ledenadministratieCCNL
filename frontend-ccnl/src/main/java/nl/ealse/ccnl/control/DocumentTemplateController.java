package nl.ealse.ccnl.control;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateType;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.javafx.ImagesMap;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Supper class for all Controller that require a DocumentTemplate.
 * 
 * @author ealse
 *
 */
public abstract class DocumentTemplateController {

  private final PageController pageController;

  private final DocumentService documentService;

  private final DocumentTemplateContextData templateContext;

  private Stage dialog;

  @FXML
  @Getter
  private CheckBox addSepa = new CheckBox();

  @FXML
  private TextField textId;

  @FXML
  private Label textIdE;

  @FXML
  private ChoiceBox<String> textSelection;

  @FXML
  @Getter
  private TextArea letterText;

  @Getter
  private WrappedFileChooser fileChooser;

  private List<DocumentTemplate> templates;

  protected DocumentTemplateController(PageController pageController,
      DocumentService documentService, DocumentTemplateContext templateContext) {
    this.pageController = pageController;
    this.documentService = documentService;
    this.templateContext = templateContext.data;
  }

  @FXML
  public void initialize() {
    if (this.dialog == null) {
      this.dialog = new Stage();
      this.dialog.setResizable(false);
      this.dialog.setTitle("Invul hulp");
      this.dialog.getIcons().add(ImagesMap.get("info.png"));
      this.dialog.initOwner(pageController.getPrimaryStage());

      Parent textHelp = pageController.loadPage(templateContext.helpPage);
      Scene dialogScene;

      if (templateContext.documentType != DocumentTemplateType.MEMBERSHIP_CANCELATION_MAIL) {
        dialogScene = new Scene(textHelp, 550, 330);
        fileChooser =
            new WrappedFileChooser(pageController.getPrimaryStage(), templateContext.fileType);
        pageController.loadPage(templateContext.nextPage, this);
      } else {
        dialogScene = new Scene(textHelp, 550, 100);
      }
      dialog.setScene(dialogScene);
    }
  }

  protected void initializeTemplates() {
    templates = documentService.findDocumentTemplates(templateContext.documentType);
    initializeTextSelection();
  }

  protected void initializeTemplates(boolean withSepa) {
    templates = documentService.findDocumentTemplates(templateContext.documentType, withSepa);
    initializeTextSelection();
  }

  private void initializeTextSelection() {
    textSelection.getItems().clear();
    templates.forEach(template -> textSelection.getItems().add(template.getTemplateID().getName()));
    textSelection.getSelectionModel().selectFirst();
    selectText();
  }


  @FXML
  void selectText() {
    int ix = textSelection.getSelectionModel().getSelectedIndex();
    if (ix > -1) {
      DocumentTemplate template = templates.get(ix);
      textId.setText(template.getName());
      letterText.setText(template.getTemplate());
      addSepa.setSelected(template.isIncludeSepaForm());
    }
  }



  @FXML
  void textHelp() {
    if (!dialog.isShowing()) {
      dialog.show();
    }
  }

  @FXML
  void saveText() {
    String name = textId.getText();
    if (name == null || name.isBlank()) {
      textIdE.setVisible(true);
    } else {
      textIdE.setVisible(false);
      DocumentTemplate template = new DocumentTemplate();
      DocumentTemplateID id = new DocumentTemplateID();
      id.setName(textId.getText());
      id.setDocumentTemplateType(templateContext.documentType);
      template.setTemplateID(id);
      template.setTemplate(letterText.getText());
      template.setIncludeSepaForm(addSepa.isSelected());
      documentService.persistDocumentemplate(template);
      pageController.showMessage("DocumentTemplate is opgeslagen");
    }
  }

  /**
   * Context for the three sub classes of the DocumentTemplateController.
   * 
   * @author ealse
   */
  public enum DocumentTemplateContext {
    MEMBERSHIP_CANCELATION_MAIL(new DocumentTemplateContextData(PageName.MAIL_HELP, null, null,
        DocumentTemplateType.MEMBERSHIP_CANCELATION_MAIL)),
    //
    PAYMENT_REMINDER(new DocumentTemplateContextData(PageName.REMINDER_TEXT_HELP,
        PageName.PAYMENT_REMINDER_LETTER_SHOW, FileExtension.PDF,
        DocumentTemplateType.PAYMENT_REMINDER)),
    //
    WELCOME_LETTER(new DocumentTemplateContextData(PageName.WELCOME_TEXT_HELP,
        PageName.WELCOME_LETTER_SHOW, FileExtension.DOCX, DocumentTemplateType.WELCOME_LETTER));

    final DocumentTemplateContextData data;

    private DocumentTemplateContext(DocumentTemplateContextData data) {
      this.data = data;
    }
  }

  /**
   * Context for the specific DocumentTemplateController subclass
   * 
   * @author ealse
   */
  @AllArgsConstructor
  private static class DocumentTemplateContextData {
    @NonNull
    final PageName helpPage;
    @Nullable
    final PageName nextPage;
    @Nullable
    final FileExtension fileType;
    @NonNull
    final DocumentTemplateType documentType;
  }
}
