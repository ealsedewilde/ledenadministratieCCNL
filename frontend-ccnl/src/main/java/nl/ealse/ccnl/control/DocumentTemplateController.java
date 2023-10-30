package nl.ealse.ccnl.control;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateType;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Super class for all Controller that require a DocumentTemplate.
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

  @Getter(value = AccessLevel.PROTECTED)
  private WrappedFileChooser fileChooser;

  private List<DocumentTemplate> templates;

  protected DocumentTemplateController(PageController pageController,
      DocumentService documentService, DocumentTemplateContext templateContext) {
    this.pageController = pageController;
    this.documentService = documentService;
    this.templateContext = templateContext.data;
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
    if (this.dialog == null) {
      double height;
      if (templateContext.documentType != DocumentTemplateType.MEMBERSHIP_CANCELATION_MAIL) {
        height = 330;
        fileChooser = new WrappedFileChooser(templateContext.fileType);
      } else {
        height = 100;
      }
      this.dialog = new StageBuilder().fxml(templateContext.helpPage, this).title("Invul hulp")
          .size(550, height).build();
    }
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
    MEMBERSHIP_CANCELATION_MAIL(new DocumentTemplateContextData("dialog/mailhelp", null,
        DocumentTemplateType.MEMBERSHIP_CANCELATION_MAIL)),
    //
    PAYMENT_REMINDER(new DocumentTemplateContextData("dialog/texthelp", FileExtension.PDF,
        DocumentTemplateType.PAYMENT_REMINDER)),
    //
    WELCOME_LETTER(new DocumentTemplateContextData("dialog/texthelp", FileExtension.DOCX,
        DocumentTemplateType.WELCOME_LETTER));

    final DocumentTemplateContextData data;

    private DocumentTemplateContext(DocumentTemplateContextData data) {
      this.data = data;
    }
  }

  /**
   * Context for the specific DocumentTemplateController subclass.
   *
   * @author ealse
   */
  @AllArgsConstructor
  private static class DocumentTemplateContextData {
    @NonNull
    final String helpPage;
    @Nullable
    final FileExtension fileType;
    @NonNull
    final DocumentTemplateType documentType;
  }
}
