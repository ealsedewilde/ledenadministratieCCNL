package nl.ealse.ccnl.control.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;

@Slf4j
public class SepaAuthorizationFormCommand {

  private WrappedFileChooser fileChooser;

  private final PageController pageController;

  private final DocumentService documentService;

  public SepaAuthorizationFormCommand(PageController pageController,
      DocumentService documentService) {
    this.pageController = pageController;
    this.documentService = documentService;
    setup();
  }

  void setup() {
    fileChooser = new WrappedFileChooser(FileExtension.PDF);
    fileChooser.setInitialDirectory(
        () -> ApplicationContext.getPreference("ccnl.directory.sepa", "c:/temp"));

  }

  @EventListener(menuChoice = MenuChoice.UPLOAD_SEPA_FORM)
  public void executeCommand(MenuChoiceEvent event) {
    File selectedFile = fileChooser.showOpenDialog();
    if (selectedFile != null) {
      handleSelected(selectedFile);
    }
  }

  private void handleSelected(File selectedFile) {
    try (FileInputStream fis = new FileInputStream(selectedFile)) {
      byte[] form = fis.readAllBytes();
      documentService.saveSepaAuthorizationForm(selectedFile.getName(), form);
      pageController.showMessage("Formulier is opgeslagen");
    } catch (IOException e) {
      log.error("Error loading file", e);
      pageController.showErrorMessage("Fout bij inlezen bestand");
    }
  }

}
