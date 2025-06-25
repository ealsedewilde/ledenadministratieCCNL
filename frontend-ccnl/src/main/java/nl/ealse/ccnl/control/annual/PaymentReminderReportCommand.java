package nl.ealse.ccnl.control.annual;

import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.AsyncTaskException;
import nl.ealse.ccnl.control.HandledTask;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.service.excelexport.ExportService;
import nl.ealse.ccnl.service.excelexport.ExportService.ReportType;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;

@Slf4j
public class PaymentReminderReportCommand {

  private final PageController pageController;

  private final ExportService exportService;

  private WrappedFileChooser fileChooser;

  public PaymentReminderReportCommand(PageController pageController, ExportService exportService) {
    this.pageController = pageController;
    this.exportService = exportService;
    setup();
  }

  void setup() {
    fileChooser = new WrappedFileChooser(FileExtension.XLSX);
    fileChooser.setInitialDirectory(
        () -> ApplicationContext.getPreference("ccnl.directory.excel", "c:/temp"));
  }

  @EventListener(menuChoice = MenuChoice.PRODUCE_REMINDER_REPORT)
  public void executeCommandForNotPaid(MenuChoiceEvent event) {
    executeCommand(event.getMenuChoice());
  }

  @EventListener(menuChoice = MenuChoice.PRODUCE_REMINDER_PARTLY_PAID_REPORT)
  public void executeCommandForPartlyPaid(MenuChoiceEvent event) {
    executeCommand(event.getMenuChoice());
  }

  private void executeCommand(MenuChoice reportType) {
    File reportFile = fileChooser.showSaveDialog();
    if (reportFile != null) {
      pageController.showPermanentMessage("Herinneringen overzicht wordt aangemaakt");
      ReminderTask reminderTask = new ReminderTask(this, reportType, reportFile);
      reminderTask.executeTask();
    }
  }

  protected static class ReminderTask extends HandledTask {

    private final ExportService exportService;
    private final File reportFile;
    private final MenuChoice reportType;

    ReminderTask(PaymentReminderReportCommand command, MenuChoice reportType, File reportFile) {
      this.exportService = command.exportService;
      this.reportFile = reportFile;
      this.reportType = reportType;
    }

    @Override
    protected String call() {
      try {
        if (MenuChoice.PRODUCE_REMINDER_REPORT == reportType) {
          exportService.paymentReminderReport(ReportType.NOT_PAID, reportFile);
        } else {
          exportService.paymentReminderReport(ReportType.PARTLY_PAID, reportFile);
        }
        return "Herinneringen overzicht is aangemaakt";
      } catch (IOException e) {
        log.error("failed to create Excel", e);
        throw new AsyncTaskException("Aanmaken MS Excel-werkblad is mislukt");
      }
    }

  }

}
