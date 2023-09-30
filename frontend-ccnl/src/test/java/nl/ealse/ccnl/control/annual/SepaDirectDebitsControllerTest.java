package nl.ealse.ccnl.control.annual;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import nl.ealse.ccnl.control.annual.SepaDirectDebitsController.DirectDebitTask;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigAmountEntry;
import nl.ealse.ccnl.service.SepaDirectDebitService;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatProperty;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatPropertyKey;
import nl.ealse.ccnl.service.SepaDirectDebitService.MappingResult;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.ccnl.test.TestExecutor;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.task.TaskExecutor;

class SepaDirectDebitsControllerTest extends FXMLBaseTest<SepaDirectDebitsController> {

  private static PageController pageController;
  private static SepaDirectDebitService service;
  private static WrappedFileChooser fileChooser;
  private static MappingResult result;
  private static TaskExecutor executor = new TestExecutor<DirectDebitTask>();

  @TempDir
  File tempDir;

  private SepaDirectDebitsController sut;

  @Test
  void testController() {
    sut = new SepaDirectDebitsController(service, pageController, executor);
    File sepaFile = new File(tempDir, "sepa.xml");
    when(fileChooser.showSaveDialog()).thenReturn(sepaFile);
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      setFileChooser();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    MenuChoiceEvent event = new MenuChoiceEvent(this, MenuChoice.PRODUCE_DIRECT_DEBITS_FILE);
    sut.onApplicationEvent(event);

    sut.selectFile();

    sut.generateDirectDebits();
    verify(pageController).showMessage("Incassobestand is aangemaakt");

    Label errorMessageLabel = errorMessageLabel();
    DDConfigAmountEntry entry = new DDConfigAmountEntry();
    entry.setValue(BigDecimal.valueOf(27, 5));
    entry.setDescription("Contributie");
    FlatProperty fp = new FlatProperty(FlatPropertyKey.DD_AMOUNT, entry);
    sut.saveProperty(fp);
    Assertions.assertFalse(errorMessageLabel.isVisible());
    result.setValid(false);
    sut.saveProperty(fp);
    Assertions.assertTrue(errorMessageLabel.isVisible());

    sut.manageSettings();
    sut.closeSettings();
  }

  private void prepare() {
    try {
      setDialog(true, "settingsStage");
      setDialog(true, "messagesStage");
      getPageWithFxController(sut, PageName.DIRECT_DEBITS);
      Parent p = getPageWithFxController(sut, PageName.DIRECT_DEBITS_SETTINGS);
      when(pageController.loadPage(PageName.DIRECT_DEBITS_SETTINGS)).thenReturn(p);
      Parent m = getPageWithFxController(sut, PageName.DIRECT_DEBITS_MESSAGES);
      when(pageController.loadPage(PageName.DIRECT_DEBITS_MESSAGES)).thenReturn(m);
      setDialog(false, "settingsStage");
      setDialog(false, "messagesStage");
      sut.initialize();
    } catch (FXMLMissingException e) {
      e.printStackTrace();
    }
  }

  @BeforeAll
  static void setup() {
   
    pageController = mock(PageController.class);
    service = mock(SepaDirectDebitService.class);
    result = new MappingResult();
    result.setValid(true);
    when(service.saveProperty(any(FlatProperty.class))).thenReturn(result);
    fileChooser = mock(WrappedFileChooser.class);
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Label errorMessageLabel() {
    try {
      return (Label) FieldUtils.readDeclaredField(sut, "errorMessageLabel", true);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private void setDialog(boolean b, String name) {
    try {
      Object value = b ? new Stage() : null;
      FieldUtils.writeDeclaredField(sut, name, value, true);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
