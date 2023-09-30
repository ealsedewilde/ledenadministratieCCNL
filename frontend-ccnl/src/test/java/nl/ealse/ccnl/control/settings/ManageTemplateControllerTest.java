package nl.ealse.ccnl.control.settings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateType;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.FXMLMissingException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

class ManageTemplateControllerTest extends FXMLBaseTest<ManageTemplateController> {

  private static ApplicationContext context;
  private static DocumentService service;
  private static PageController pageController;


  private ManageTemplateController sut;
  private TextField templateId;

  @Test
  void testController() {
    sut = new ManageTemplateController(pageController, service, context);
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
    verify(pageController, never()).showMessage("Document template is toegevoegd");
    templateId.setText("test");
    sut.save();
    verify(pageController).showMessage("Document template is toegevoegd");

    sut.delete();
    pageController.showMessage("Document template is verwijderd");
  }

  private void prepare() {
    try {
      Parent p = getPageWithFxController(sut, PageName.MANAGE_TEMPLATE_TEXT_HELP);
      when(pageController.loadPage(PageName.MANAGE_TEMPLATE_TEXT_HELP)).thenReturn(p);
      p = getPageWithFxController(sut, PageName.MANAGE_MAIL_HELP);
      when(pageController.loadPage(PageName.MANAGE_MAIL_HELP)).thenReturn(p);
      getPageWithFxController(sut, PageName.MANAGE_TEMPLATE);
    } catch (FXMLMissingException e) {
      Assertions.fail(e.getMessage());
    }

  }

  @BeforeAll
  static void setup() {
   
    pageController = mock(PageController.class);
    context = mock(ApplicationContext.class);
    service = mock(DocumentService.class);
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
