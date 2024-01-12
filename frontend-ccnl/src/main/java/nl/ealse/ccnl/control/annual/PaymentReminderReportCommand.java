package nl.ealse.ccnl.control.annual;

import java.io.File;
import java.io.IOException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.TaskExecutor;
import nl.ealse.ccnl.control.AsyncTaskException;
import nl.ealse.ccnl.control.HandledTask;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseProperties;
import nl.ealse.ccnl.service.excelexport.ExportService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;

@Slf4j
public class PaymentReminderReportCommand {

  @Getter
  private static final PaymentReminderReportCommand instance = new PaymentReminderReportCommand();

  private final PageController pageController;

  private final TaskExecutor executor;

  private final ExportService exportService;

  private WrappedFileChooser fileChooser;

  private PaymentReminderReportCommand() {
    this.pageController = PageController.getInstance();
    this.exportService = ExportService.getInstance();
    this.executor = TaskExecutor.getInstance();
    setup();
  }

  void setup() {
    fileChooser = new WrappedFileChooser(FileExtension.XLSX);
    fileChooser.setInitialDirectory(() ->
        DatabaseProperties.getProperty("ccnl.directory.excel", "c:/temp"));
  }

  @EventListener(menuChoice = MenuChoice.PRODUCE_REMINDER_REPORT)
  public void executeCommand(MenuChoiceEvent event) {
    File reportFile = fileChooser.showSaveDialog();
    if (reportFile != null) {
      pageController.showPermanentMessage("Herinneringen overzicht wordt aangemaakt");
      ReminderTask reminderTask = new ReminderTask(this, reportFile);
      executor.execute(reminderTask);
    }
  }

  protected static class ReminderTask extends HandledTask {

    private final ExportService exportService;
    private final File reportFile;

    ReminderTask(PaymentReminderReportCommand command, File reportFile) {
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
