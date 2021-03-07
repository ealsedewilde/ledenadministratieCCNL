package nl.ealse.ccnl.control.external;

import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import nl.ealse.ccnl.control.SearchController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.EntitySelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;
import nl.ealse.ccnl.service.ExternalRelationService;
import nl.ealse.ccnl.service.SearchItem;
import org.springframework.context.ApplicationContext;

public abstract class ExternalRelationSearchController<T extends ExternalRelation>
    extends SearchController<T, EntitySelectionEvent<T>> {

  private final ExternalRelationService<T> externalRelationService;

  protected ExternalRelationSearchController(ApplicationContext springContext,
      ExternalRelationService<T> externalRelationService, MenuChoice... choices) {
    super(springContext, choices);
    this.externalRelationService = externalRelationService;
  }

  public void extraInfo(MouseEvent event) {
    event.consume(); // stop further propagation to handleSelected()
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Extra Info");
    alert.showAndWait();
  }


  @Override
  public List<T> doSearch(SearchItem searchItem, String value) {
    return externalRelationService.searchExternalRelation(searchItem, value);
  }



}
