package nl.ealse.ccnl.control.settings;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class SepaAuthorizationFormCommand {

  @Value("${ccnl.directory.sepa:c:/temp}")
  private String sepaDirectory;

  private WrappedFileChooser fileChooser;

  private final PageController pageController;

  private final DocumentService documentService;

  public SepaAuthorizationFormCommand(PageController pageController,
      DocumentService documentService) {
    this.pageController = pageController;
    this.documentService = documentService;
  }
  
  @PostConstruct
  void setup() {
    fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), FileExtension.PDF);
    boolean b = new File(sepaDirectory).exists();
    fileChooser.setInitialDirectory(new File(sepaDirectory));
    
  }

  @EventListener(condition = "#event.name('UPLOAD_SEPA_FORM')")
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
