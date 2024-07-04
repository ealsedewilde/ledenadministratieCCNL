package nl.ealse.ccnl.control.other;

import nl.ealse.ccnl.control.external.ExternalRelationDeleteController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.ccnl.service.relation.ExternalOtherService;
import nl.ealse.javafx.mapping.ViewModel;

public class ExternalOtherDeleteController
    extends ExternalRelationDeleteController<ExternalRelationOther> {

  public ExternalOtherDeleteController(PageController pageController,
      ExternalOtherService service) {
    super(pageController, service);

  }

  @EventListener(menuChoice = MenuChoice.DELETE_EXTERNAL_RELATION)
  public void onApplicationEvent(ExternalOtherSelectionEvent event) {
    getPageController().setActivePage(PageName.EXTERNAL_RELATION_DELETE);
    setSelectedEntity(event.getSelectedEntity());
    ViewModel.modelToView(this, event.getSelectedEntity());
  }

}
