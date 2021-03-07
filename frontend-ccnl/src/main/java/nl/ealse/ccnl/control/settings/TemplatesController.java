package nl.ealse.ccnl.control.settings;

import java.util.List;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateType;
import nl.ealse.ccnl.service.DocumentService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

@Controller
public class TemplatesController implements ApplicationListener<MenuChoiceEvent> {

  private final ApplicationContext springContext;

  private final DocumentService documentService;

  @FXML
  private TableView<DocumentTemplate> tableView;

  @FXML
  private Label templateHeader;

  @FXML
  private TextArea templateText;

  public TemplatesController(DocumentService documentService, ApplicationContext springContext) {
    this.springContext = springContext;
    this.documentService = documentService;
  }

  @Override
  public void onApplicationEvent(MenuChoiceEvent event) {
    if (MenuChoice.TEMPLATES_OVERVIEW == event.getMenuChoice()) {
      List<DocumentTemplate> templates = documentService.findAllDocumentTemplates();
      tableView.getItems().clear();
      tableView.getItems().addAll(templates);
    }
  }

  @FXML
  public void selectTemplate(Event event) {
    @SuppressWarnings("unchecked")
    TableRow<DocumentTemplate> row = (TableRow<DocumentTemplate>) event.getSource();
    DocumentTemplate selectedTemplate = row.getItem();
    if (selectedTemplate != null) {
      TemplateSelectionEvent templateEvent =
          new TemplateSelectionEvent(this, selectedTemplate, false);
      springContext.publishEvent(templateEvent);
    }
  }

  @FXML
  public void newWelcomeLetterTemplate() {
    fireNewTemplateEvent(DocumentTemplateType.WELCOME_LETTER);
  }

  @FXML
  public void newReminderLetterTemplate() {
    fireNewTemplateEvent(DocumentTemplateType.PAYMENT_REMINDER);
  }

  @FXML
  public void newCancelationMailTemplate() {
    fireNewTemplateEvent(DocumentTemplateType.MEMBERSHIP_CANCELATION_MAIL);
  }

  private void fireNewTemplateEvent(DocumentTemplateType type) {
    DocumentTemplate template = new DocumentTemplate();
    DocumentTemplateID id = new DocumentTemplateID();
    id.setDocumentTemplateType(type);
    template.setTemplateID(id);
    TemplateSelectionEvent event = new TemplateSelectionEvent(this, template, true);
    springContext.publishEvent(event);

  }

}
