package nl.ealse.ccnl.control.settings;

import java.time.LocalDate;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateType;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.javafx.ImagesMap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class ManageTemplateController {

  private final ApplicationContext springContext;

  private final PageController pageController;

  private final DocumentService documentService;

  private DocumentTemplate selectedTemplate;

  @FXML
  private Label headerText;

  @FXML
  private TextArea templateText;

  @FXML
  private HBox showSepa;

  @FXML
  private CheckBox addSepa;

  @FXML
  private TextField templateId;

  @FXML
  private Label templateIdE;

  @FXML
  private Button deleteButton;

  private boolean cancelationMail;

  private boolean isNew;

  private Stage dialog;

  private Scene letterHelpScene;

  private Scene mailHelpScene;


  public ManageTemplateController(PageController pageController, DocumentService documentService,
      ApplicationContext springContext) {
    this.springContext = springContext;
    this.pageController = pageController;
    this.documentService = documentService;
  }

  @FXML
  public void initialize() {
    this.dialog = new Stage();
    this.dialog.setResizable(false);
    this.dialog.setTitle("Invul hulp");
    this.dialog.getIcons().add(ImagesMap.get("info.png"));

    dialog.initOwner(pageController.getPrimaryStage());
    letterHelpScene =
        new Scene(pageController.loadPage(PageName.MANAGE_TEMPLATE_TEXT_HELP), 550, 330);
    mailHelpScene = new Scene(pageController.loadPage(PageName.MANAGE_MAIL_HELP), 550, 150);

  }

  @EventListener
  public void onApplicationEvent(TemplateSelectionEvent event) {
    this.selectedTemplate = event.getSelectedEntity();
    this.cancelationMail = selectedTemplate.getTemplateID()
        .getDocumentTemplateType() == DocumentTemplateType.MEMBERSHIP_CANCELATION_MAIL;
    this.isNew = event.isNewTemplate();
    templateId.setText(selectedTemplate.getTemplateID().getName());
    addSepa.setSelected(selectedTemplate.isIncludeSepaForm());
    templateText.setText(selectedTemplate.getTemplate());
    if (isNew) {
      deleteButton.setVisible(false);
      templateId.setDisable(false);
    } else {
      deleteButton.setVisible(true);
      templateId.setDisable(true);
    }
    if (cancelationMail) {
      showSepa.setVisible(false);
      this.dialog.setScene(mailHelpScene);
    } else {
      showSepa.setVisible(true);
      this.dialog.setScene(letterHelpScene);
    }
    headerText.setText(getheaderText());
    templateIdE.setVisible(false);
    pageController.setActivePage(PageName.MANAGE_TEMPLATE);
  }


  private String getheaderText() {
    StringBuilder sb = new StringBuilder();
    if (isNew) {
      sb.append("Nieuwe ");
    } else {
      sb.append("Bestaande ");
    }
    sb.append(selectedTemplate.getTemplateID().getDocumentTemplateType().getDescription());
    sb.append(" template");
    return sb.toString();
  }

  @FXML
  public void textHelp() {
    if (!dialog.isShowing()) {
      dialog.show();
    }
  }

  @FXML
  public void save() {
    if (isNew && templateId.getText() == null) {
      templateIdE.setText("Template naam invullen a.u.b.");
      templateIdE.setVisible(true);
    } else {
      templateIdE.setVisible(false);
      DocumentTemplateID id = selectedTemplate.getTemplateID();
      id.setName(templateId.getText());
      Optional<DocumentTemplate> tp = documentService.findDocumentTemplate(id);
      if (tp.isPresent()) {
        selectedTemplate = tp.get();
      }
      selectedTemplate.setTemplate(templateText.getText());
      selectedTemplate.setModificationDate(LocalDate.now());
      if (!cancelationMail) {
        selectedTemplate.setIncludeSepaForm(addSepa.isSelected());
      }
      documentService.persistDocumentemplate(selectedTemplate);
      pageController.showMessage("Document template is toegevoegd");
      close();
    }
  }

  @FXML
  public void delete() {
    documentService.deleteDocumentTemplate(selectedTemplate);
    pageController.showMessage("Document template is verwijderd");
    close();
  }

  @FXML
  public void close() {
    if (dialog.isShowing()) {
      dialog.close();
    }
    pageController.setActivePage(PageName.TEMPLATES_OVERVIEW);
    springContext.publishEvent(new MenuChoiceEvent(this, MenuChoice.TEMPLATES_OVERVIEW));
  }

}
