package nl.ealse.ccnl.control.settings;

import java.util.List;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateType;
import nl.ealse.ccnl.service.DocumentService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class TemplatesController {

  private final ApplicationEventPublisher eventPublisher;

  private final DocumentService documentService;

  @FXML
  private TableView<DocumentTemplate> tableView;

  @FXML
  private Label templateHeader;

  @FXML
  private TextArea templateText;

  public TemplatesController(DocumentService documentService,
      ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
    this.documentService = documentService;
  }

  @EventListener(condition = "#event.name('TEMPLATES_OVERVIEW')")
  public void onApplicationEvent(MenuChoiceEvent event) {
    List<DocumentTemplate> templates = documentService.findAllDocumentTemplates();
    tableView.getItems().clear();
    tableView.getItems().addAll(templates);
  }

  @FXML
  void selectTemplate(Event event) {
    @SuppressWarnings("unchecked")
    TableRow<DocumentTemplate> row = (TableRow<DocumentTemplate>) event.getSource();
    DocumentTemplate selectedTemplate = row.getItem();
    if (selectedTemplate != null) {
      TemplateSelectionEvent templateEvent =
          new TemplateSelectionEvent(this, selectedTemplate, false);
      eventPublisher.publishEvent(templateEvent);
    }
  }

  @FXML
  void newWelcomeLetterTemplate() {
    fireNewTemplateEvent(DocumentTemplateType.WELCOME_LETTER);
  }

  @FXML
  void newReminderLetterTemplate() {
    fireNewTemplateEvent(DocumentTemplateType.PAYMENT_REMINDER);
  }

  @FXML
  void newCancelationMailTemplate() {
    fireNewTemplateEvent(DocumentTemplateType.MEMBERSHIP_CANCELATION_MAIL);
  }

  private void fireNewTemplateEvent(DocumentTemplateType type) {
    DocumentTemplate template = new DocumentTemplate();
    DocumentTemplateID id = new DocumentTemplateID();
    id.setDocumentTemplateType(type);
    template.setTemplateID(id);
    TemplateSelectionEvent event = new TemplateSelectionEvent(this, template, true);
    eventPublisher.publishEvent(event);

  }

}
