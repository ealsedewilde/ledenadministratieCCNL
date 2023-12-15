package nl.ealse.ccnl.control.internal;

import javafx.fxml.FXML;
import lombok.Getter;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.service.relation.InternalRelationService;
import nl.ealse.ccnl.view.InternalRelationDeleteView;
import nl.ealse.javafx.mapping.ViewModel;

public class InternalRelationDeleteController extends InternalRelationDeleteView {
  
  @Getter
  private static final InternalRelationDeleteController instance = new InternalRelationDeleteController();
  
  private final PageController pageController;

  private final InternalRelationService service;

  private InternalRelation selectedEntity;

  private InternalRelationDeleteController() {
    this.pageController = PageController.getInstance();
    this.service = InternalRelationService.getInstance();
  }

  @FXML
  void delete() {
    service.deleteInternalRelation(selectedEntity);
    pageController.showMessage("Gegevens zijn verwijderd");
    pageController.activateLogoPage();
  }

  @EventListener(menuChoice = MenuChoice.DELETE_INTERNAL_RELATION)
  public void onApplicationEvent(InternalRelationSelectionEvent event) {
    pageController.setActivePage(PageName.INTERNAL_RELATION_DELETE);
    selectedEntity = event.getSelectedEntity();
    ViewModel.modelToView(this, event.getSelectedEntity());
  }

}
