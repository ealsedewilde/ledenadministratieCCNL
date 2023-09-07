package nl.ealse.ccnl.control.annual;

import java.io.File;
import java.io.IOException;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.HandledTask;
import nl.ealse.ccnl.control.exception.AsyncTaskException;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.settings.ExcelImportController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.excelexport.ExportService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class PaymentReminderReportCommand {

  @Value("${ccnl.directory.excel:c:/temp}")
  private String reportDirectory;

  private final PageController pageController;

  private final TaskExecutor executor;

  private final ExportService exportService;

  private WrappedFileChooser fileChooser;

  public PaymentReminderReportCommand(ExportService exportService,
      PageController pageController, TaskExecutor executor) {
    this.pageController = pageController;
    this.exportService = exportService;
    this.executor = executor;
  }

  @EventListener(condition = "#event.name('PRODUCE_REMINDER_REPORT')")
  public void executeCommand(MenuChoiceEvent event) {
    if (fileChooser == null) {
      initialize();
    }
    File reportFile = fileChooser.showSaveDialog();
    if (reportFile != null) {
      pageController.showPermanentMessage("Herinneringen overzicht wordt aangemaakt");
      ReminderTask reminderTask = new ReminderTask(this, reportFile);
      executor.execute(reminderTask);
    }
  }

  private void initialize() {
    fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), FileExtension.XLSX);
    fileChooser.setInitialDirectory(new File(reportDirectory));
  }

  protected static class ReminderTask extends HandledTask {

    private final ExportService exportService;
    private final File reportFile;

    ReminderTask(PaymentReminderReportCommand command, File reportFile) {
      super(command.pageController);
      this.exportService = command.exportService;
      this.reportFile = reportFile;
    }

    @Override
    protected String call() {
      try {
        exportService.paymentReminderReport(reportFile);
        return "Herinneringen overzicht is aangemaakt";
      } catch (IOException e) {
        log.error("failed to create Excel", e);
        throw new AsyncTaskException("Aanmaken MS Excel-werkblad is mislukt");
      }
    }

  }

}
