package nl.ealse.ccnl.control.settings;

import static org.mockito.Mockito.verify;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.control.TextField;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.ArchiveService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.ccnl.test.MockProvider;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ManageArchiveControllerTest extends FXMLBaseTest {

  private ManageArchiveController sut;

  private TextField referenceYear;

  @Test
  void testController() {
    sut = ManageArchiveController.getInstance();
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      referenceYear();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.MANAGE_ARCHIVE);
    sut.onApplicationEvent(event);

    sut.delete();
    String y = referenceYear.getText();
    String msg =
        String.format("Archiefgegevens van %s of ouder zijn verwijderd", referenceYear.getText());
    verify(getPageController()).showMessage(msg);

    int year = Integer.parseInt(y) + 3;
    referenceYear.setText(Integer.toString(year));
    sut.delete();
  }

  private void prepare() {
    sut = ManageArchiveController.getInstance();
    getPageWithFxController(sut, PageName.MANAGE_ARCHIVE);
  }

  @BeforeAll
  static void setup() {
    MockProvider.mock(ArchiveService.class);
  }

  private void referenceYear() {
    try {
      referenceYear = (TextField) FieldUtils.readDeclaredField(sut, "referenceYear", true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }



}
