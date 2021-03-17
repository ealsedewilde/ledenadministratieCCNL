package nl.ealse.ccnl.control.excel;

import static nl.ealse.ccnl.control.menu.MenuChoice.REPORT_ALL_DATA;
import static nl.ealse.ccnl.control.menu.MenuChoice.REPORT_ARCHIVE;
import static nl.ealse.ccnl.control.menu.MenuChoice.REPORT_CANCELLED_MEMBERS;
import static nl.ealse.ccnl.control.menu.MenuChoice.REPORT_NEW_MEMBERS;
import static nl.ealse.ccnl.control.menu.MenuChoice.REPORT_OVERDUE_MEMBERS;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javafx.concurrent.Task;
import nl.ealse.ccnl.control.exception.AsyncTaskException;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.excelexport.ExportArchiveService;
import nl.ealse.ccnl.service.excelexport.ExportService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;

@Controller
@Lazy(false) // Because no FXML
public class ExcelExportController implements ApplicationListener<MenuChoiceEvent> {

  @Value("${ccnl.directory.excel:c:/temp}")
  private String excelDirectory;

  private final PageController pageController;

  private final ApplicationContext springContext;

  private final TaskExecutor executor;

  private ExportService service;

  private ExportArchiveService archiveService;

  private final List<MenuChoice> validChoices;

  private WrappedFileChooser fileChooser;

  public ExcelExportController(PageController pageController, ApplicationContext springContext,
      TaskExecutor executor) {
    this.pageController = pageController;
    this.springContext = springContext;
    this.executor = executor;
    validChoices = Arrays.asList(REPORT_ALL_DATA, REPORT_ARCHIVE, REPORT_CANCELLED_MEMBERS,
        REPORT_NEW_MEMBERS, REPORT_OVERDUE_MEMBERS);
  }

  private void initialize() {
    service = springContext.getBean(ExportService.class);
    archiveService = springContext.getBean(ExportArchiveService.class);
    fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), FileExtension.XLSX);
    fileChooser.setInitialDirectory(new File(excelDirectory));
  }

  @Override
  public void onApplicationEvent(MenuChoiceEvent event) {
    if (!validChoices.contains(event.getMenuChoice())) {
      return;
    }
    if (fileChooser == null) {
      // There is no fxml associated with this controller; so no @FXML initialize() available!
      initialize();
    }
    File exportFile = fileChooser.showSaveDialog();
    if (exportFile != null) {
      pageController.showPermanentMessage("MS Excel-werkblad wordt aangemaakt; even geduld a.u.b.");
      if (event.getMenuChoice() == REPORT_ARCHIVE) {
        AsyncArchiveTask asyncArchiveTask = new AsyncArchiveTask(archiveService, exportFile);
        asyncArchiveTask
            .setOnSucceeded(t -> pageController.showMessage(t.getSource().getValue().toString()));
        asyncArchiveTask.setOnFailed(
            t -> pageController.showErrorMessage(t.getSource().getException().getMessage()));
        executor.execute(asyncArchiveTask);
      } else {
        AsyncExportTask asyncExportTask =
            new AsyncExportTask(event.getMenuChoice(), service, exportFile);
        asyncExportTask
            .setOnSucceeded(t -> pageController.showMessage(t.getSource().getValue().toString()));
        asyncExportTask.setOnFailed(
            t -> pageController.showErrorMessage("Schrijven MS Excel-werkblad is mislukt"));
        executor.execute(asyncExportTask);
      }
    }
  }

  protected static class AsyncArchiveTask extends Task<String> {
    private final ExportArchiveService archiveService;
    private final File exportFile;

    AsyncArchiveTask(ExportArchiveService archiveService, File exportFile) {
      this.archiveService = archiveService;
      this.exportFile = exportFile;
    }


    @Override
    protected String call() throws Exception {
      archiveService.export(exportFile);
      return "MS Excel-werkblad voor archief is aangemaakt";
    }

  }

  protected static class AsyncExportTask extends Task<String> {
    private final ExportService service;
    private final File exportFile;
    private final MenuChoice menuChoice;

    AsyncExportTask(MenuChoice menuChoice, ExportService service, File exportFile) {
      this.menuChoice = menuChoice;
      this.service = service;
      this.exportFile = exportFile;
    }

    @Override
    protected String call() {
      try {
        switch (menuChoice) {
          case REPORT_NEW_MEMBERS:
            service.exportNew(exportFile);
            return "MS Excel-werkblad voor nieuwe leden is aangemaakt";
          case REPORT_CANCELLED_MEMBERS:
            service.exportCancelled(exportFile);
            return "MS Excel-werkblad voor opgezegde leden is aangemaakt";
          case REPORT_OVERDUE_MEMBERS:
            service.exportOverdue(exportFile);
            return "MS Excel-werkblad voor niet betalers is aangemaakt";
          case REPORT_ALL_DATA:
            service.exportALL(exportFile);
            return "MS Excel-werkblad voor alle gegevens is aangemaakt";
          default:
            throw new IllegalArgumentException(menuChoice.name());
        }
      } catch (IOException e) {
        throw new AsyncTaskException("Aanmaken MS Excel-werkblad is mislukt");
      }
    }


  }

}
