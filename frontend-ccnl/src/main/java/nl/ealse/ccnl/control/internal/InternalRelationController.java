package nl.ealse.ccnl.control.internal;

import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXML;
import lombok.Getter;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.form.FormController;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.service.relation.InternalRelationService;
import nl.ealse.ccnl.view.InternalRelationView;
import nl.ealse.javafx.mapping.ViewModel;

public class InternalRelationController extends InternalRelationView {

  private final PageController pageController;

  private final InternalRelationService internalRelationService;

  private InternalRelation model;

  private InternalRelation selectedInternalRelation;

  private MenuChoice currentMenuChoice;

  private FormController formController;

  public InternalRelationController(PageController pageController,
      InternalRelationService internalRelationService) {
    this.pageController = pageController;
    this.internalRelationService = internalRelationService;
    setup();
  }

  private void setup() {
    formController = new InternalRelationFormController(this);
    formController.initializeForm();
    formController.setOnSave(e -> save());
    formController.setOnReset(e -> reset());
  }

  @EventListener(menuChoice = MenuChoice.NEW_INTERNAL_RELATION)
  public void newRelation(MenuChoiceEvent event) {
    this.selectedInternalRelation = new InternalRelation();
    handleRelation(event);
  }

  @EventListener(menuChoice = MenuChoice.AMEND_INTERNAL_RELATION)
  public void amendRelation(InternalRelationSelectionEvent event) {
    this.selectedInternalRelation = event.getSelectedEntity();
    handleRelation(event);
  }

  private void handleRelation(MenuChoiceEvent event) {
    this.currentMenuChoice = event.getMenuChoice();
    pageController.setActivePage(formController.getPageReference());
    formController.setActiveFormPage(0);
    formController.getHeaderText().setText(getHeaderTextValue());
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
    formController.setActiveFormPage(0);
  }

  @FXML
  void save() {
    enrichAddress();
    ViewModel.viewToModel(this, model);
    RelationNumberValue rn = RelationNumberValue.fromLabel(model.getTitle());
    model.setRelationNumber(rn.getRelationNumber());
    internalRelationService.save(model);
    pageController.showMessage("Functiegegevens opgeslagen");
    pageController.activateLogoPage();
  }

  private String getHeaderTextValue() {
    return switch (currentMenuChoice) {
      case NEW_INTERNAL_RELATION -> "Interne functie opvoeren";
      case AMEND_INTERNAL_RELATION -> "Interne functie wijzigen";
      default -> "Invalid";
    };
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

}
