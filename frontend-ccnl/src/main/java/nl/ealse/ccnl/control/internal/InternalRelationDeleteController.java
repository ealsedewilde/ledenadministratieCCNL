package nl.ealse.ccnl.control.internal;

import javafx.fxml.FXML;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.service.relation.InternalRelationService;
import nl.ealse.ccnl.view.InternalRelationDeleteView;
import nl.ealse.javafx.mapping.ViewModel;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class InternalRelationDeleteController extends InternalRelationDeleteView {
  private final PageController pageController;

  private final InternalRelationService service;

  private InternalRelation selectedEntity;

  public InternalRelationDeleteController(InternalRelationService internalRelationService,
      PageController pageController) {
    this.pageController = pageController;
    this.service = internalRelationService;
  }

  @FXML
  void delete() {
    service.deleteInternalRelation(selectedEntity);
    pageController.showMessage("Gegevens zijn verwijderd");
    pageController.activateLogoPage();
  }

  @EventListener(condition = "#event.name('DELETE_INTERNAL_RELATION')")
  public void onApplicationEvent(InternalRelationSelectionEvent event) {
    pageController.setActivePage(PageName.INTERNAL_RELATION_DELETE);
    selectedEntity = event.getSelectedEntity();
    ViewModel.modelToView(this, event.getSelectedEntity());
  }

}
