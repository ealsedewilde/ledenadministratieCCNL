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
import nl.ealse.ccnl.ledenadministratie.config.DatabaseProperties;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateType;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;

/**
 * Super class for all Controller that require a DocumentTemplate.
 *
 * @author ealse
 *
 */
public abstract class DocumentTemplateController {

  private final DocumentTemplateContextData templateContext;
  @Getter(value = AccessLevel.PROTECTED)
  private WrappedFileChooser fileChooser;

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

  private List<DocumentTemplate> templates;

  protected DocumentTemplateController(DocumentTemplateContext templateContext) {
    this.templateContext = templateContext.data;
    if (this.templateContext.fileType != null) {
      this.fileChooser = new WrappedFileChooser(this.templateContext.fileType);
      this.fileChooser.setInitialDirectory(
          () -> DatabaseProperties.getProperty("ccnl.directory.workdir", "c:/temp"));
    }
  }

  protected void initializeTemplates() {
    templates = getDocumentService().findDocumentTemplates(templateContext.documentType);
    initializeTextSelection();
  }

  protected void initializeTemplates(boolean withSepa) {
    templates = getDocumentService().findDocumentTemplates(templateContext.documentType, withSepa);
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
      getDocumentService().persistDocumentemplate(template);
      getPageController().showMessage("DocumentTemplate is opgeslagen");
    }
  }

  protected abstract PageController getPageController();

  protected abstract DocumentService getDocumentService();

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
    final String helpPage;
    final FileExtension fileType;
    final DocumentTemplateType documentType;
  }
}
