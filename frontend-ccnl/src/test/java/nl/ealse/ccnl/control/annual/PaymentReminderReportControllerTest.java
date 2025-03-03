package nl.ealse.ccnl.control.annual;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PaymentReminderReportControllerTest extends FXMLBaseTest {

  private static WrappedFileChooser fileChooser;

  private PaymentReminderReportCommand sut;

  @Test
  void test() {
    Assertions.assertTrue(runFX(() -> {
      sut = getTestSubject(PaymentReminderReportCommand.class);
      sut.setup();
      setFileChooser();
      sut.executeCommandForNotPaid(
          new MenuChoiceEvent(fileChooser, MenuChoice.PRODUCE_REMINDER_REPORT));
      verify(getPageController()).showMessage("Herinneringen overzicht is aangemaakt");
      sut.executeCommandForPartlyPaid(
          new MenuChoiceEvent(fileChooser, MenuChoice.PRODUCE_REMINDER_PARTLY_PAID_REPORT));
      verify(getPageController(), times(2)).showMessage("Herinneringen overzicht is aangemaakt");
    }));
  }


  @BeforeAll
  static void setup() {
    fileChooser = mock(WrappedFileChooser.class);
    when(fileChooser.showSaveDialog()).thenReturn(new File("reminders.xlsx"));
  }


  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
