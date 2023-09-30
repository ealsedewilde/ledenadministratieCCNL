package nl.ealse.ccnl.control.internal;

import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.Getter;
import nl.ealse.ccnl.control.button.SaveButton;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.form.FormController;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.service.relation.InternalRelationService;
import nl.ealse.ccnl.view.InternalRelationView;
import nl.ealse.javafx.mapping.Mapping;
import nl.ealse.javafx.mapping.ViewModel;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class InternalRelationController extends InternalRelationView implements FormController {
  private final PageController pageController;

  private final InternalRelationValidation internalRelationValidation;

  private final InternalRelationService internalRelationService;

  private InternalRelation model;

  private InternalRelation selectedInternalRelation;

  private MenuChoice currentMenuChoice;


  @FXML
  @Getter
  @Mapping(ignore = true)
  private Pane formMenu;

  @FXML
  @Getter
  @Mapping(ignore = true)
  private Pane formPage;

  @FXML
  @Getter
  @Mapping(ignore = true)
  private Pane formButtons;

  @FXML
  private Label headerText;

  @FXML
  @Getter
  @Mapping(ignore = true)
  private Button nextButton;

  @FXML
  @Getter
  @Mapping(ignore = true)
  private Button previousButton;

  @FXML
  private SaveButton saveButton;

  private InternalRelationFormpages formPages;

  public InternalRelationController(PageController pageController,
      InternalRelationService internalRelationService) {
    this.pageController = pageController;
    this.internalRelationService = internalRelationService;
    this.internalRelationValidation = new InternalRelationValidation(this);
    bindFxml();
  }

  private void bindFxml() {
    pageController.loadPage(PageName.INTERNAL_RELATION_FORM, this);
    formPages = new InternalRelationFormpages(this);
    
    internalRelationValidation.initialize();
    internalRelationValidation.setCallback(valid -> saveButton.setDisable(!valid));

  }

  @EventListener(condition = "#event.name('NEW_INTERNAL_RELATION','AMEND_INTERNAL_RELATION')")
  public void handleRelation(InternalRelationSelectionEvent event) {
    this.currentMenuChoice = event.getMenuChoice();
    pageController.setActivePage(PageName.INTERNAL_RELATION_FORM);
    formPages.setActiveFormPage(0);
    headerText.setText(getHeaderText());
    this.selectedInternalRelation = event.getSelectedEntity();
    this.model = new InternalRelation();
    if (event.getMenuChoice() == MenuChoice.NEW_INTERNAL_RELATION) {
      initializeTitles();
    } else {
      getTitle().getItems().clear();
      Arrays.stream(RelationNumberValue.values())
          .forEach(rn -> getTitle().getItems().add(rn.getLabel()));
      getTitle().setDisable(true);
      getTitleE().setVisible(false);
    }
    reset();
  }

  private void initializeTitles() {
    List<String> titles = internalRelationService.getAllTitles();
    getTitle().getItems().clear();
    for (RelationNumberValue rn : RelationNumberValue.values()) {
      if (!titles.contains(rn.getLabel())) {
        getTitle().getItems().add(rn.getLabel());
      }
    }
    if (getTitle().getItems().isEmpty()) {
      getTitle().setDisable(true);
      getTitleE().setVisible(true);
    } else {
      getTitle().setDisable(false);
      getTitleE().setVisible(false);
    }
  }

  @FXML
  void reset() {
    ViewModel.modelToView(this, selectedInternalRelation);
    ViewModel.viewToModel(this, model);
    if (currentMenuChoice == MenuChoice.NEW_INTERNAL_RELATION && !getTitle().getItems().isEmpty()) {
      getTitle().getSelectionModel().selectFirst();
    }
    formPages.setActiveFormPage(0);
  }

  @FXML
  void save() {
    enrichAddress();
    ViewModel.viewToModel(this, model);
    RelationNumberValue rn = RelationNumberValue.fromLabel(model.getTitle());
    model.setRelationNumber(rn.getRelationNumber());
    internalRelationService.persistInternalRelation(model);
    pageController.showMessage("Functiegegevens opgeslagen");
    pageController.activateLogoPage();
  }

  @FXML
  void nextPage() {
    formPages.setActiveFormPage(formPages.getCurrentPage() + 1);
  }

  @FXML
  void previousPage() {
    formPages.setActiveFormPage(formPages.getCurrentPage() - 1);
  }

  private String getHeaderText() {
    switch (currentMenuChoice) {
      case NEW_INTERNAL_RELATION:
        return "Interne functie opvoeren";
      case AMEND_INTERNAL_RELATION:
        return "Interne functie wijzigen";
      default:
        return "Invalid";
    }
  }

  private enum RelationNumberValue {
    VOORZITTER("Voorzitter", 8080),

    SECRETARIAAT("Secretariaat", 8081),

    PENNINGMEESTER("Penningmeester", 8082),

    EVENEMENTEN("Evenementen", 8083),

    ALGEMEEN("Algemeen", 8084),

    REDACTIE("Redactie", 8085),

    LEDENADMINISTRATIE("Ledenadministratie", 8086),

    MAGAZIJN("Magazijn", 8087);

    @Getter
    private final String label;

    @Getter
    private final int relationNumber;

    private RelationNumberValue(String label, int relationNumber) {
      this.label = label;
      this.relationNumber = relationNumber;
    }

    public static RelationNumberValue fromLabel(String label) {
      for (RelationNumberValue rn : RelationNumberValue.values()) {
        if (rn.label.equals(label)) {
          return rn;
        }
      }
      throw new IllegalArgumentException(label);
    }
  }

  @Override
  public void validateForm() {
    internalRelationValidation.validate();
  }

}
