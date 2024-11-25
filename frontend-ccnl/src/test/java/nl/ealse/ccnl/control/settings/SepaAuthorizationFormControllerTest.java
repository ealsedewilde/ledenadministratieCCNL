package nl.ealse.ccnl.control.settings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.net.URL;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SepaAuthorizationFormControllerTest extends FXMLBaseTest {

  private static WrappedFileChooser fileChooser;

  private SepaAuthorizationFormCommand sut;

  @Test
  void testController() {
    Assertions.assertTrue(runFX(() -> {
      sut = getTestSubject(SepaAuthorizationFormCommand.class);
      doTest();
    }));

  }

  private void doTest() {
    doInitialize();
    setFileChooser();
    sut.executeCommand(null);
    verify(getPageController()).showMessage("Formulier is opgeslagen");
  }

  @BeforeAll
  static void setup() {
    fileChooser = mock(WrappedFileChooser.class);
    URL url = SepaAuthorizationFormCommand.class.getResource("/MachtigingsformulierSEPA.pdf");
    when(fileChooser.showOpenDialog()).thenReturn(new File(url.getFile()));
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void doInitialize() {
    try {
      MethodUtils.invokeMethod(sut, true, "setup");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
