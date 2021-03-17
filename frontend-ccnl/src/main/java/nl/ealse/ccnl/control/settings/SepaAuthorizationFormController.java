package nl.ealse.ccnl.control.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.DocumentService;
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
public class SepaAuthorizationFormController implements ApplicationListener<MenuChoiceEvent> {

  @Value("${ccnl.directory.sepa:c:/temp}")
  private String sepaDirectory;

  private WrappedFileChooser fileChooser;

  private final PageController pageController;

  private final ApplicationContext springContext;

  private DocumentService documentService;

  public SepaAuthorizationFormController(PageController pageController,
      ApplicationContext springContext) {
    this.pageController = pageController;
    this.springContext = springContext;
  }

  private void initialize() {
    documentService = springContext.getBean(DocumentService.class);
    fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), FileExtension.PDF);
    fileChooser.setInitialDirectory(new File(sepaDirectory));
  }

  @Override
  public void onApplicationEvent(MenuChoiceEvent event) {
    if (MenuChoice.MANAGE_SEPA_FORM == event.getMenuChoice()) {
      if (fileChooser == null) {
        // There is no fxml associated with this controller; so no @FXML initialize() available!
        initialize();
      }
      File selectedFile = fileChooser.showOpenDialog();
      if (selectedFile != null) {
        handleSelected(selectedFile);
      }
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
