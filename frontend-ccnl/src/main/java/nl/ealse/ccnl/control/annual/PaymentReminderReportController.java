package nl.ealse.ccnl.control.annual;

import java.io.File;
import java.io.IOException;
import javafx.concurrent.Task;
import nl.ealse.ccnl.control.exception.AsyncTaskException;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.excelexport.ExportService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;

@Controller
@Lazy(false) // Because no FXML
public class PaymentReminderReportController {

  @Value("${ccnl.directory.excel:c:/temp}")
  private String reportDirectory;

  private final PageController pageController;

  private final ApplicationContext springContext;

  private final TaskExecutor executor;

  private ExportService exportService;

  private WrappedFileChooser fileChooser;

  public PaymentReminderReportController(ApplicationContext springContext,
      PageController pageController, TaskExecutor executor) {
    this.pageController = pageController;
    this.springContext = springContext;
    this.executor = executor;
  }

  @EventListener(condition = "#event.name('PRODUCE_REMINDER_REPORT')")
  public void onApplicationEvent(MenuChoiceEvent event) {
    if (fileChooser == null) {
      initialize();
    }
    File reportFile = fileChooser.showSaveDialog();
    if (reportFile != null) {
      pageController.showPermanentMessage("Herinneringen overzicht wordt aangemaakt");
      ReminderTask reminderTask = new ReminderTask(exportService, reportFile);
      reminderTask
          .setOnSucceeded(t -> pageController.showMessage("Herinneringen overzicht is aangemaakt"));
      reminderTask.setOnFailed(
          t -> pageController.showErrorMessage(t.getSource().getException().getMessage()));
      executor.execute(reminderTask);
    }
  }

  private void initialize() {
    exportService = springContext.getBean(ExportService.class);
    fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), FileExtension.XLSX);
    fileChooser.setInitialDirectory(new File(reportDirectory));
  }

  protected static class ReminderTask extends Task<Void> {

    private final ExportService exportService;
    private final File reportFile;

    ReminderTask(ExportService exportService, File reportFile) {
      this.exportService = exportService;
      this.reportFile = reportFile;
    }

    @Override
    protected Void call() {
      try {
        exportService.paymentReminderReport(reportFile);
      } catch (IOException e) {
        throw new AsyncTaskException("Aanmaken MS Excel-werkblad is mislukt");
      }
      return null;
    }

  }

}
