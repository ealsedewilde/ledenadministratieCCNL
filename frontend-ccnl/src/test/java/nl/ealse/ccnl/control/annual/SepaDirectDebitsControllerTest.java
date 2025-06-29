package nl.ealse.ccnl.control.annual;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import jakarta.persistence.EntityManager;
import java.io.File;
import java.math.BigDecimal;
import javafx.scene.control.Label;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.dao.util.TransactionUtil;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigAmountEntry;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.service.SepaDirectDebitService;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatProperty;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatPropertyKey;
import nl.ealse.ccnl.service.SepaDirectDebitService.MappingResult;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SepaDirectDebitsControllerTest extends FXMLBaseTest {

  private static SepaDirectDebitService service;
  private static WrappedFileChooser fileChooser;
  private static MappingResult result;

  @TempDir
  File tempDir;

  private SepaDirectDebitsController sut;

  @Test
  void testController() {
    File sepaFile = new File(tempDir, "sepa.xml");
    when(fileChooser.showSaveDialog()).thenReturn(sepaFile);
    Assertions.assertTrue(runFX(() -> {
      prepare();
      setFileChooser();
      doTest();
    }));

  }

  private void doTest() {
    MenuChoiceEvent event = new MenuChoiceEvent(this, MenuChoice.PRODUCE_DIRECT_DEBITS_FILE);
    sut.onApplicationEvent(event);

    sut.selectFile();

    sut.generateDirectDebits();
    verify(getPageController()).showMessage("Incassobestand is aangemaakt");
    
    Label errorMessageLabel = errorMessageLabel();
    DDConfigAmountEntry entry = new DDConfigAmountEntry();
    entry.setValue(BigDecimal.valueOf(2750, 2));
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
    sut = getTestSubject(SepaDirectDebitsController.class);
    getPageWithFxController(sut, PageName.DIRECT_DEBITS);
    
    Setting incassoSetting = new Setting();
    incassoSetting.setSettingsGroup("ccnl.contributie");
    incassoSetting.setKey("incasso");
    incassoSetting.setValue("32,50");
    incassoSetting.setDescription("Contributie bij automatische incasso");
    incassoSetting.prePersist();
    EntityManager em = ApplicationContext.getEntityManagerProvider().getEntityManager();
    TransactionUtil.inTransction(() -> em.persist(incassoSetting));
    
  }

  @BeforeAll
  static void setup() {
    service = ApplicationContext.getComponent(SepaDirectDebitService.class);
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

}
