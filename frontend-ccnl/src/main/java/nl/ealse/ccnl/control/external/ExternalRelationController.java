package nl.ealse.ccnl.control.external;

import javafx.fxml.FXML;
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
    extends ExternalRelationView {
  private final PageController pageController;

  protected final ExternalRelationValidation externalRelationValidation;

  private final ExternalRelationService<T> externalRelationService;

  @Mapping(ignore = true)
  protected T model;

  @Mapping(ignore = true)
  protected T selectedExternalRelation;

  protected MenuChoice currentMenuChoice;

  protected ExternalRelationController(PageController pageController,
      ExternalRelationService<T> externalRelationService) {
    this.pageController = pageController;
    this.externalRelationService = externalRelationService;
    this.externalRelationValidation = new ExternalRelationValidation(this);
  }

  protected void initializeValidation() {
    externalRelationValidation.initialize();
    externalRelationValidation.setCallback(valid -> saveButton.setDisable(!valid));
  }

  @FXML
  protected void reset() {
    if (selectedExternalRelation.getRelationNumber() == null) {
      selectedExternalRelation.setRelationNumber(externalRelationService.getFreeNumber());
    }
    ViewModel.modelToView(this, selectedExternalRelation);
    ViewModel.viewToModel(this, model);
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

  @FXML
  public void nextPage() {
    getFormPages().setActiveFormPage(getFormPages().getCurrentPage() + 1);
  }

  @FXML
  public void previousPage() {
    getFormPages().setActiveFormPage(getFormPages().getCurrentPage() - 1);
  }

  protected abstract String getSaveText();

  protected abstract String getHeaderTextValue();

  protected abstract <F extends FormPages<FormController>> F getFormPages();

  @Override
  public void validateForm() {
    externalRelationValidation.validate();
  }
}
