package nl.ealse.ccnl.control.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Getter;
import nl.ealse.ccnl.control.address.AddressController;
import nl.ealse.ccnl.control.button.SaveButton;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.service.relation.InternalRelationService;
import nl.ealse.ccnl.view.InternalRelationView;
import nl.ealse.javafx.mapping.ViewModel;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class InternalRelationController extends InternalRelationView {
  private final PageController pageController;

  private final InternalRelationValidation internalRelationValidation;

  private final InternalRelationService internalRelationService;

  private InternalRelation model;

  private InternalRelation selectedInternalRelation;

  private MenuChoice currentMenuChoice;

  private PageName currentPage;

  @FXML
  private Label headerText;

  @FXML
  private SaveButton saveButton;

  @Getter
  @FXML
  private AddressController addressController;

  private final List<SaveButton> saveButtonList = new ArrayList<>();

  public InternalRelationController(PageController pageController,
      InternalRelationService internalRelationService) {
    this.pageController = pageController;
    this.internalRelationService = internalRelationService;
    this.internalRelationValidation = new InternalRelationValidation(this);
  }

  @FXML
  public void initialize() {
    saveButtonList.add(saveButton);
    internalRelationValidation.initializeValidation(valid -> saveButtonList
        .forEach(sb -> sb.setDisable(getTitle().getItems().isEmpty() || !valid)));
  }

  @EventListener(
      condition = "#event.name('NEW_INTERNAL_RELATION','AMEND_INTERNAL_RELATION')")
  public void handleRelation(InternalRelationSelectionEvent event) {
    this.currentMenuChoice = event.getMenuChoice();
    pageController.loadPage(PageName.INTERNAL_RELATION_ADDRESS);
    addressController.getHeaderText().setText(getHeaderText());
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
  public void reset() {
    ViewModel.modelToView(this, selectedInternalRelation);
    ViewModel.viewToModel(this, model);
    if (currentMenuChoice == MenuChoice.NEW_INTERNAL_RELATION && !getTitle().getItems().isEmpty()) {
      getTitle().getSelectionModel().selectFirst();
    }
    internalRelationValidation.initialize();
    saveButtonList
        .forEach(button -> button.setDisable(currentMenuChoice == MenuChoice.NEW_PARTNER));
    firstPage();
  }

  @FXML
  public void save() {
    addressController.enrich();
    ViewModel.viewToModel(this, model);
    RelationNumberValue rn = RelationNumberValue.fromLabel(model.getTitle());
    model.setRelationNumber(rn.getRelationNumber());
    internalRelationService.persistInternalRelation(model);
    pageController.showMessage("Functiegegevens opgeslagen");
    pageController.setActivePage(PageName.LOGO);
  }

  @FXML
  public void nextPage() {
    if (currentPage == PageName.INTERNAL_RELATION_PERSONAL) {
      secondPage();
    }
  }

  @FXML
  public void previousPage() {
    if (currentPage == PageName.INTERNAL_RELATION_ADDRESS) {
      firstPage();
    }
  }

  public void firstPage() {
    currentPage = PageName.INTERNAL_RELATION_PERSONAL;
    pageController.setActivePage(currentPage);
    this.headerText.setText(getHeaderText());
    getTitle().requestFocus();
    internalRelationValidation.validate();
  }

  public void secondPage() {
    currentPage = PageName.INTERNAL_RELATION_ADDRESS;
    pageController.setActivePage(currentPage);
    this.headerText.setText(getHeaderText());
    addressController.getStreet().requestFocus();
    internalRelationValidation.validate();
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


}
