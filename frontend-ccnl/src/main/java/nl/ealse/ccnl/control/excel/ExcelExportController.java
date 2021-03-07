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
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.excelexport.ExportArchiveService;
import nl.ealse.ccnl.ledenadministratie.excelexport.ExportService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
@Lazy(false) // Because no FXML
@Slf4j
public class ExcelExportController implements ApplicationListener<MenuChoiceEvent> {

  @Value("${ccnl.directory.excel:c:/temp}")
  private String excelDirectory;

  private final PageController pageController;

  private final ApplicationContext springContext;

  private ExportService service;

  private ExportArchiveService archiveService;

  private final List<MenuChoice> validChoices;

  private WrappedFileChooser fileChooser;

  public ExcelExportController(PageController pageController, ApplicationContext springContext) {
    this.pageController = pageController;
    this.springContext = springContext;
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
      try {
        String message = null;
        switch (event.getMenuChoice()) {
          case REPORT_ARCHIVE:
            archiveService.export(exportFile);
            message = "MS Excel-werkblad voor archief is aangemaakt";
            break;
          case REPORT_NEW_MEMBERS:
            service.exportNew(exportFile);
            message = "MS Excel-werkblad voor nieuwe leden is aangemaakt";
            break;
          case REPORT_CANCELLED_MEMBERS:
            service.exportCancelled(exportFile);
            message = "MS Excel-werkblad voor opgezegde leden is aangemaakt";
            break;
          case REPORT_OVERDUE_MEMBERS:
            service.exportOverdue(exportFile);
            message = "MS Excel-werkblad voor niet betalers is aangemaakt";
            break;
          case REPORT_ALL_DATA:
          default:
            service.exportALL(exportFile);
            message = "MS Excel-werkblad voor alle gegevens is aangemaakt";
            break;
        }
        pageController.setMessage(message);
      } catch (IOException e) {
        log.error("Could not write Excel document", e);
        pageController.setErrorMessage("Schrijven MS Excel-werkblad is mislukt");
      }
    }
  }

}
