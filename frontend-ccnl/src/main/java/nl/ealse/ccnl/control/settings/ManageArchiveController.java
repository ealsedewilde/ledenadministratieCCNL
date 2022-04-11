package nl.ealse.ccnl.control.settings;

import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.ArchiveService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class ManageArchiveController {

  private final PageController pageController;

  private final ArchiveService service;

  @FXML
  private TextField referenceYear;

  @FXML
  private Label referenceYearE;

  public ManageArchiveController(ArchiveService service, PageController pageController) {
    this.pageController = pageController;
    this.service = service;
  }

  @FXML
  public void delete() {
    String s = referenceYear.getText();
    try {
      int year = Integer.parseInt(s);
      if (valid(year)) {
        service.delete(year);
        pageController
            .showMessage(String.format("Archiefgegevens van %d of ouder zijn verwijderd", year));
      }
    } catch (NullPointerException e) {
      pageController.showErrorMessage("Geef een geldig jaar op");
    }
  }

  private boolean valid(int year) {
    int currentYear = LocalDate.now().getYear();
    if (currentYear - year < 3) {
      referenceYearE.setText("Bewaar gegevens tenminste 2 jaar");
      referenceYearE.setVisible(true);
      return false;
    } else if (currentYear - year > 5) {
      referenceYearE.setText("Opgegeven jaar is te ver in het verleden");
      referenceYearE.setVisible(true);
      return false;
    }
    referenceYearE.setVisible(false);
    return true;
  }

  @EventListener(condition = "#event.name('MANAGE_ARCHIVE')")
  public void onApplicationEvent(MenuChoiceEvent event) {
    referenceYearE.setVisible(false);
    int year = LocalDate.now().getYear() - 3;
    referenceYear.setText(Integer.toString(year));
  }

}
