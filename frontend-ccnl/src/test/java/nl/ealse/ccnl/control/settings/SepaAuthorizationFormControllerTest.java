package nl.ealse.ccnl.control.settings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.test.FXBase;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class SepaAuthorizationFormControllerTest extends FXBase {

  private static PageController pageController;
  private static ApplicationContext springContext;
  private static DocumentService documentService;
  private static WrappedFileChooser fileChooser;

  private SepaAuthorizationFormController sut;

  @Test
  void testController() {
    sut = new SepaAuthorizationFormController(pageController, springContext);
    sepaDirectory();
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    doInitialize();
    setFileChooser();
    MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.MANAGE_SEPA_FORM);
    sut.onApplicationEvent(event);
    verify(pageController).showMessage("Formulier is opgeslagen");
  }

  @BeforeAll
  static void setup() {
   
    pageController = mock(PageController.class);
    springContext = mock(ApplicationContext.class);
    documentService = mock(DocumentService.class);
    when(springContext.getBean(DocumentService.class)).thenReturn(documentService);
    fileChooser = mock(WrappedFileChooser.class);
    Resource r = new ClassPathResource("MachtigingsformulierSEPA.pdf");
    try {
      when(fileChooser.showOpenDialog()).thenReturn(r.getFile());
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void sepaDirectory() {
    try {
      FieldUtils.writeField(sut, "sepaDirectory", "C:/temp", true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void doInitialize() {
    try {
      MethodUtils.invokeMethod(sut, true, "initialize");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}