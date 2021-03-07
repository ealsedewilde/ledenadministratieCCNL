package nl.ealse.ccnl.control.internal;

import javafx.fxml.FXML;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.service.InternalRelationService;
import nl.ealse.ccnl.view.InternalRelationDeleteView;
import nl.ealse.javafx.mapping.DataMapper;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

@Controller
public class InternalRelationDeleteController extends InternalRelationDeleteView
    implements ApplicationListener<InternalRelationSelectionEvent> {
  private final PageController pageController;

  private final InternalRelationService service;

  private InternalRelation selectedEntity;

  public InternalRelationDeleteController(InternalRelationService internalRelationService,
      PageController pageController) {
    this.pageController = pageController;
    this.service = internalRelationService;
  }

  @FXML
  public void delete() {
    service.deleteInternalRelation(selectedEntity);
    pageController.setMessage("Gegevens zijn verwijderd");
    pageController.setActivePage(PageName.LOGO);
  }

  @Override
  public void onApplicationEvent(InternalRelationSelectionEvent event) {
    if (MenuChoice.DELETE_INTERNAL_RELATION == event.getMenuChoice()) {
      selectedEntity = event.getSelectedEntity();
      DataMapper.modelToForm(this, event.getSelectedEntity());
      pageController.setActivePage(PageName.INTERNAL_RELATION_DELETE);
    }
  }

}
