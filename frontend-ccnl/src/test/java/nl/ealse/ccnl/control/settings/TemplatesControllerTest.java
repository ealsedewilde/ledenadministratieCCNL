package nl.ealse.ccnl.control.settings;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.event.Event;
import javafx.scene.control.TableRow;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventPublisher;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateType;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.ccnl.test.MockProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class TemplatesControllerTest extends FXMLBaseTest {

  private static DocumentService service;
  private static Event ev;

  private static DocumentTemplate template;

  private TemplatesController sut;

  @Test
  void testController() {
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    MenuChoiceEvent event = new MenuChoiceEvent(this, MenuChoice.TEMPLATES_OVERVIEW);
    try (MockedStatic<EventPublisher> context = mockStatic(EventPublisher.class)) {
      sut.onApplicationEvent(event);
  
      sut.selectTemplate(ev);
  
      sut.newCancelationMailTemplate();
      sut.newReminderLetterTemplate();
      sut.newWelcomeLetterTemplate();
      context.verify(() -> EventPublisher.publishEvent(any(TemplateSelectionEvent.class)), times(4));
    }
  }

  private void prepare() {
    sut = TemplatesController.getInstance();
    getPageWithFxController(sut, PageName.TEMPLATES_OVERVIEW);
    TableRow<DocumentTemplate> row = new TableRow<>();
    row.setItem(template);
    ev = mock(Event.class);
    when(ev.getSource()).thenReturn(row);
  }

  @BeforeAll
  static void setup() {

    service = MockProvider.mock(DocumentService.class);
    List<DocumentTemplate> templates = new ArrayList<>();
    template = template();
    templates.add(template);
    when(service.findAllDocumentTemplates()).thenReturn(templates);
  }

  private static DocumentTemplate template() {
    DocumentTemplate t = new DocumentTemplate();
    t.setModificationDate(LocalDate.of(2020, 12, 5));
    DocumentTemplateID id = new DocumentTemplateID();
    id.setName("welkom");
    id.setDocumentTemplateType(DocumentTemplateType.WELCOME_LETTER);
    t.setTemplateID(id);
    t.setTemplate("template");
    return t;
  }


}
