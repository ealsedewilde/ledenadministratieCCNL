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
import javafx.stage.Modality;
import javafx.stage.Stage;
import nl.ealse.ccnl.MainStage;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateType;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.javafx.FXMLLoaderBean;
import nl.ealse.javafx.ImagesMap;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class ManageTemplateController {

  private final ApplicationEventPublisher eventPublisher;

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
      ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
    this.pageController = pageController;
    this.documentService = documentService;
  }

  @FXML
  void initialize() {
    this.dialog = new Stage();
    this.dialog.initModality(Modality.APPLICATION_MODAL);
    this.dialog.setResizable(false);
    this.dialog.setTitle("Invul hulp");
    this.dialog.getIcons().add(ImagesMap.get("info.png"));

    dialog.initOwner(MainStage.getStage());
    letterHelpScene = new Scene(FXMLLoaderBean.getPage("dialog/texthelp"), 550, 330);
    mailHelpScene = new Scene(FXMLLoaderBean.getPage("dialog/mailhelp"), 550, 150);
  }

  @EventListener
  public void onApplicationEvent(TemplateSelectionEvent event) {
    pageController.setActivePage(PageName.MANAGE_TEMPLATE);
    this.selectedTemplate = event.getSelectedTemplate();
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
  void textHelp() {
    if (!dialog.isShowing()) {
      dialog.show();
    }
  }

  @FXML
  void save() {
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
  void delete() {
    documentService.deleteDocumentTemplate(selectedTemplate);
    pageController.showMessage("Document template is verwijderd");
    close();
  }

  @FXML
  void close() {
    if (dialog.isShowing()) {
      dialog.close();
    }
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.TEMPLATES_OVERVIEW));
  }

}
