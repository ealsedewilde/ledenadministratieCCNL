package nl.ealse.ccnl.control.settings;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.control.TextField;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.support.EventProcessor;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateType;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.ccnl.test.MockProvider;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ManageTemplateControllerTest extends FXMLBaseTest {

  private ManageTemplateController sut;
  private TextField templateId;

  @Test
  void testController() {
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      initTemplateId();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    DocumentTemplate template = new DocumentTemplate();
    DocumentTemplateID id = new DocumentTemplateID();
    template.setTemplateID(id);
    id.setDocumentTemplateType(DocumentTemplateType.WELCOME_LETTER);
    TemplateSelectionEvent event = new TemplateSelectionEvent(sut, template, true);
    sut.onApplicationEvent(event);

    sut.textHelp();

    sut.save();
    verify(getPageController(), never()).showMessage("Document template is toegevoegd");
    templateId.setText("test");
    sut.save();
    verify(getPageController()).showMessage("Document template is toegevoegd");

    sut.delete();
    verify(getPageController()).showMessage("Document template is verwijderd");
  }

  private void prepare() {
    sut = ManageTemplateController.getInstance();
    getPageWithFxController(sut, PageName.MANAGE_TEMPLATE);
    getPageWithFxController(TemplatesController.getInstance(), PageName.TEMPLATES_OVERVIEW);

  }

  @BeforeAll
  static void setup() {
    MockProvider.mock(DocumentService.class);
   }

  private void initTemplateId() {
    try {
      templateId = (TextField) FieldUtils.readDeclaredField(sut, "templateId", true);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
