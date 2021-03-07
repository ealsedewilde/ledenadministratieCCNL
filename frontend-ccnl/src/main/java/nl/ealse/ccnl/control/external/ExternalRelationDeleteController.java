package nl.ealse.ccnl.control.external;

import javafx.fxml.FXML;
import lombok.Getter;
import lombok.Setter;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;
import nl.ealse.ccnl.service.ExternalRelationService;
import nl.ealse.ccnl.view.ExternalRelationDeleteView;
import nl.ealse.javafx.mapping.Mapping;

public abstract class ExternalRelationDeleteController<T extends ExternalRelation>
    extends ExternalRelationDeleteView {

  @Getter
  @Mapping(ignore = true)
  private final PageController pageController;

  private final ExternalRelationService<T> service;

  @Setter
  private T selectedEntity;

  protected ExternalRelationDeleteController(PageController pageController,
      ExternalRelationService<T> service) {
    this.pageController = pageController;
    this.service = service;
  }

  @FXML
  public void delete() {
    service.deleteExternalRelation(selectedEntity);
    pageController.setMessage("Gegevens zijn verwijderd");
    pageController.setActivePage(PageName.LOGO);
  }

}
