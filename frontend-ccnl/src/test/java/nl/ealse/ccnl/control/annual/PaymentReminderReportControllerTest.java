package nl.ealse.ccnl.control.annual;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.annual.PaymentReminderReportController.ReminderTask;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.excelexport.ExportService;
import nl.ealse.ccnl.test.FXBase;
import nl.ealse.ccnl.test.TestExecutor;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;

class PaymentReminderReportControllerTest  extends FXBase {
  
  private static PageController pageController;
  private static ApplicationContext springContext;
  private static ExportService exportService;
  private static WrappedFileChooser fileChooser;
  private static TaskExecutor executor = new TestExecutor<ReminderTask>();
  
  private PaymentReminderReportController sut;
  
  @Test
  void test() {
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      sut = new PaymentReminderReportController(springContext, pageController, executor);
      reportDirectory();
      doInitialize();
      setFileChooser();
      MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.PRODUCE_REMINDER_REPORT);
      sut.onApplicationEvent(event);
      verify(pageController).showMessage("Herinneringen overzicht is aangemaakt");
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  
  @BeforeAll
  static void setup() {
    pageController = mock(PageController.class);
    springContext = mock(ApplicationContext.class);
    exportService = mock(ExportService.class);
    when(springContext.getBean(ExportService.class)).thenReturn(exportService);
    fileChooser = mock(WrappedFileChooser.class);
    when(fileChooser.showSaveDialog()).thenReturn(new File("reminders.xlsx"));
  }


  private void reportDirectory() {
    try {
      FieldUtils.writeField(sut, "reportDirectory", "C:/temp", true);
    } catch (Exception e) {
      e.printStackTrace();
    }
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
      MethodUtils.invokeMethod(sut, true, "initialize");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
