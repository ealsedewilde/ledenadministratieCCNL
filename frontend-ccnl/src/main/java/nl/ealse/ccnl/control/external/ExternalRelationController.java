package nl.ealse.ccnl.control.external;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Getter;
import nl.ealse.ccnl.control.address.AddressController;
import nl.ealse.ccnl.control.button.SaveButton;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import nl.ealse.ccnl.view.ExternalRelationView;
import nl.ealse.javafx.mapping.DataMapper;
import nl.ealse.javafx.mapping.Mapping;

public abstract class ExternalRelationController<T extends ExternalRelation>
    extends ExternalRelationView {
  private final PageController pageController;

  protected final ExternalRelationValidation externalRelationValidation;

  private final ExternalRelationService<T> externalRelationService;

  @Mapping(ignore = true)
  protected T model;

  @Mapping(ignore = true)
  protected T selectedExternalRelation;

  @FXML
  @Mapping(ignore = true)
  protected Label headerText;

  @FXML
  @Mapping(ignore = true)
  private SaveButton saveButton;
  
  @Getter
  @FXML
  private AddressController addressController;

  protected MenuChoice currentMenuChoice;


  protected final List<SaveButton> saveButtonList = new ArrayList<>();

  protected ExternalRelationController(PageController pageController,
      ExternalRelationService<T> externalRelationService) {
    this.pageController = pageController;
    this.externalRelationService = externalRelationService;
    this.externalRelationValidation = new ExternalRelationValidation(this);
  }

  @FXML
  public void initialize() {
    saveButtonList.add(saveButton);
    externalRelationValidation
        .initializeValidation(valid -> saveButtonList.forEach(button -> button.setDisable(!valid)));
  }

  @FXML
  public void reset() {
    if (selectedExternalRelation.getRelationNumber() == null) {
      selectedExternalRelation.setRelationNumber(externalRelationService.getFreeNumber());
    }
    DataMapper.modelToForm(this, selectedExternalRelation);
    DataMapper.formToModel(this, model);
    addressController.getHeaderText().setText(getHeaderText());
    externalRelationValidation.initialize();
    saveButtonList
        .forEach(button -> button.setDisable(currentMenuChoice == MenuChoice.NEW_PARTNER));
    firstPage();
  }

  @FXML
  public void save() {
    enrich();
    addressController.enrich();
    DataMapper.formToModel(this, model);
    externalRelationService.persistExternalRelation(model);
    pageController.showMessage(getSaveText());
    pageController.setActivePage(PageName.LOGO);
  }

  protected abstract String getSaveText();

  protected abstract String getHeaderText();

  protected abstract void firstPage();


}
