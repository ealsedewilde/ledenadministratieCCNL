package nl.ealse.ccnl.control.external;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.Getter;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.form.FormController;
import nl.ealse.ccnl.form.FormPages;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import nl.ealse.ccnl.view.ExternalRelationView;
import nl.ealse.javafx.mapping.Mapping;
import nl.ealse.javafx.mapping.ViewModel;

/**
 * Super class for all External relations.
 */
public abstract class ExternalRelationController<T extends ExternalRelation>
    extends ExternalRelationView implements FormController {
  private final PageController pageController;

  protected final ExternalRelationValidation externalRelationValidation;

  private final ExternalRelationService<T> externalRelationService;

  @Mapping(ignore = true)
  protected T model;

  @Mapping(ignore = true)
  protected T selectedExternalRelation;

  protected MenuChoice currentMenuChoice;

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
  protected Label headerText;

  @FXML
  @Getter
  @Mapping(ignore = true)
  private Button nextButton;

  @FXML
  @Getter
  @Mapping(ignore = true)
  private Button previousButton;

  @FXML
  private Button saveButton;

  protected ExternalRelationController(PageController pageController,
      ExternalRelationService<T> externalRelationService) {
    this.pageController = pageController;
    this.externalRelationService = externalRelationService;
    this.externalRelationValidation = new ExternalRelationValidation(this);
  }

  @FXML
  void initialize() {
    externalRelationValidation
        .initializeValidation(valid -> saveButton.setDisable(!valid));
  }

  @FXML
  protected void reset() {
    if (selectedExternalRelation.getRelationNumber() == null) {
      selectedExternalRelation.setRelationNumber(externalRelationService.getFreeNumber());
    }
    ViewModel.modelToView(this, selectedExternalRelation);
    ViewModel.viewToModel(this, model);
    externalRelationValidation.initialize();
    externalRelationValidation
    .initializeValidation(valid -> saveButton.setDisable(!valid));
    getFormPages().setActiveFormPage(0);

  }

  @FXML
  public void save() {
    enrich();
    enrichAddress();
    ViewModel.viewToModel(this, model);
    externalRelationService.persistExternalRelation(model);
    pageController.showMessage(getSaveText());
    pageController.activateLogoPage();
  }

  protected abstract String getSaveText();

  protected abstract String getHeaderText();

  protected abstract FormPages getFormPages();


}
