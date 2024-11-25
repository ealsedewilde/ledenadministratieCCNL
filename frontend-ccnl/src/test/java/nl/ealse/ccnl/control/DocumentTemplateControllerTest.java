package nl.ealse.ccnl.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateType;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DocumentTemplateControllerTest extends FXMLBaseTest {

  private DocumentService documentService;

  private Tester sut;

  @Test
  void onEvent() {

    Platform.setImplicitExit(false);
    Assertions.assertTrue(runFX(() -> {
      documentService = mock(DocumentService.class);
      sut = new Tester(getPageController(), documentService);
      initFXML();
      sut.textHelp();
      Stage dialog = sut.getStage();
      assertNotNull(dialog);
      double height = dialog.getScene().getHeight();
      assertEquals(330, height);

      sut.initializeTemplates();

      List<DocumentTemplate> templates = new ArrayList<>();
      DocumentTemplate t = new DocumentTemplate();
      DocumentTemplateID id = new DocumentTemplateID();
      id.setName("test");
      t.setTemplateID(id);
      t.setTemplate("DEMO");
      templates.add(t);
      when(documentService.findDocumentTemplates(DocumentTemplateType.PAYMENT_REMINDER))
          .thenReturn(templates);
      sut.initializeTemplates();
      Assertions.assertEquals("DEMO", sut.getLetterText().getText());

      sut.saveText();
      verify(documentService).persistDocumentemplate(any(DocumentTemplate.class));
    }));

  }

  private void initFXML() {
    try {
      FieldUtils.writeField(sut, "textId", new TextField(), true);
      FieldUtils.writeField(sut, "letterText", new TextArea(), true);

      FieldUtils.writeField(sut, "textIdE", new Label(), true);
      FieldUtils.writeField(sut, "textSelection", new ChoiceBox<String>(), true);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }


  public static class Tester extends DocumentTemplateController {

    private final PageController pageController;
    private final DocumentService documentService;

    public Tester(PageController pageController, DocumentService documentService) {
      super(DocumentTemplateContext.PAYMENT_REMINDER);
      this.pageController = pageController;
      this.documentService = documentService;
    }

    public Stage getStage() {
      try {
        return (Stage) FieldUtils.readField(this, "dialog", true);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected PageController getPageController() {
      return pageController;
    }

    @Override
    protected DocumentService getDocumentService() {
      return documentService;
    }

  }

}
