package nl.ealse.ccnl.control.other;

import nl.ealse.ccnl.control.external.ExternalRelationDeleteController;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import nl.ealse.javafx.mapping.ViewModel;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class ExternalOtherDeleteController
    extends ExternalRelationDeleteController<ExternalRelationOther> {

  public ExternalOtherDeleteController(PageController pageController,
      ExternalRelationService<ExternalRelationOther> service) {
    super(pageController, service);

  }

  @EventListener(condition = "#event.name('DELETE_EXTERNAL_RELATION')")
  public void onApplicationEvent(ExternalOtherSelectionEvent event) {
    setSelectedEntity(event.getSelectedEntity());
    ViewModel.modelToView(this, event.getSelectedEntity());
    getPageController().setActivePage(PageName.EXTERNAL_RELATION_DELETE);
  }

}
