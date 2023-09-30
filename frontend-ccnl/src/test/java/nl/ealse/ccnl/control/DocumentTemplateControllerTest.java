package nl.ealse.ccnl.control;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateType;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.test.FXBase;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DocumentTemplateControllerTest extends FXBase {

  private PageController pageController;

  private DocumentService documentService;

  private Tester sut;

  @Test
  void onEvent() {
   
    Platform.setImplicitExit(false);
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      Pane p = new Pane();
      pageController = mock(PageController.class);
      when(pageController.loadPage(PageName.REMINDER_TEXT_HELP)).thenReturn(p);
      documentService = mock(DocumentService.class);


      sut = new Tester(pageController, documentService);
      initFXML();
      sut.initialize();
      verify(pageController).loadPage(PageName.PAYMENT_REMINDER_LETTER_SHOW, sut);

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

      sut.textHelp();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
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

    public Tester(PageController pageController, DocumentService documentService) {
      super(pageController, documentService, DocumentTemplateContext.PAYMENT_REMINDER);
    }

  }

}
