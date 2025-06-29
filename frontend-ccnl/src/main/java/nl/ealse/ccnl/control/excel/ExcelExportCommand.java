package nl.ealse.ccnl.control.excel;

import static nl.ealse.ccnl.control.menu.MenuChoice.REPORT_ARCHIVE;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.AsyncTaskException;
import nl.ealse.ccnl.control.HandledTask;
import nl.ealse.ccnl.control.menu.ChoiceGroup;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.service.excelexport.ExportArchiveService;
import nl.ealse.ccnl.service.excelexport.ExportService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;

@Slf4j
public class ExcelExportCommand {

  private final PageController pageController;

  private final ExportService service;

  private final ExportArchiveService archiveService;

  private WrappedFileChooser fileChooser;

  public ExcelExportCommand(PageController pageController, ExportService service,
      ExportArchiveService archiveService) {
    this.pageController = pageController;
    this.archiveService = archiveService;
    this.service = service;
    setup();
  }

  private void setup() {
    fileChooser = new WrappedFileChooser(FileExtension.XLSX);
    fileChooser.setInitialDirectory(
        () -> ApplicationContext.getPreference("ccnl.directory.excel", "c:/temp"));
  }

  @EventListener(choiceGroup = ChoiceGroup.REPORTS)
  public void executeCommand(MenuChoiceEvent event) {
    File exportFile = fileChooser.showSaveDialog();
    if (exportFile != null) {
      pageController.showPermanentMessage("MS Excel-werkblad wordt aangemaakt; even geduld a.u.b.");
      if (event.getMenuChoice() == REPORT_ARCHIVE) {
        AsyncArchiveTask asyncArchiveTask = new AsyncArchiveTask(this, exportFile);
        asyncArchiveTask.executeTask();
      } else {
        AsyncExportTask asyncExportTask =
            new AsyncExportTask(this, event.getMenuChoice(), exportFile);
        asyncExportTask.executeTask();
      }
    }
  }

  protected static class AsyncArchiveTask extends HandledTask {
    private final ExportArchiveService archiveService;
    private final File exportFile;

    AsyncArchiveTask(ExcelExportCommand command, File exportFile) {
      this.archiveService = command.archiveService;
      this.exportFile = exportFile;
    }


    @Override
    protected String call() {
      try {
        archiveService.export(exportFile);
        return "MS Excel-werkblad voor archief is aangemaakt";
      } catch (IOException e) {
        log.error("Failed to create Excel", e);
        throw new AsyncTaskException("Aanmaken MS Excel-werkblad is mislukt");
      }
    }

  }

  protected static class AsyncExportTask extends HandledTask {
    private final ExportService service;
    private final File exportFile;
    private final MenuChoice menuChoice;

    AsyncExportTask(ExcelExportCommand command, MenuChoice menuChoice, File exportFile) {
      this.menuChoice = menuChoice;
      this.service = command.service;
      this.exportFile = exportFile;
    }

    @Override
    protected String call() {
      try {
        return switch (menuChoice) {
          case REPORT_NEW_MEMBERS:
            service.exportNew(exportFile);
            yield "MS Excel-werkblad voor nieuwe leden is aangemaakt";
          case REPORT_CANCELLED_MEMBERS:
            service.exportCancelled(exportFile);
            yield "MS Excel-werkblad voor opgezegde leden is aangemaakt";
          case REPORT_AFTER_APRIL:
            service.exportAfterApril(exportFile);
            yield "MS Excel-werkblad voor opgezegd na 1 april is aangemaakt";
          case REPORT_OVERDUE_MEMBERS:
            service.exportOverdue(exportFile);
            yield "MS Excel-werkblad voor niet betalers is aangemaakt";
          case REPORT_ALL_DATA:
            service.exportALL(exportFile);
            yield "MS Excel-werkblad voor alle gegevens is aangemaakt";
          default:
            throw new IllegalArgumentException(menuChoice.name());
        };
      } catch (IOException e) {
        throw new AsyncTaskException("Aanmaken MS Excel-werkblad is mislukt");
      }
    }


  }

}
