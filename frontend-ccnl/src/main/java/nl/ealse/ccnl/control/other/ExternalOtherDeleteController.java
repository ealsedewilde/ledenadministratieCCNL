package nl.ealse.ccnl.control.other;

import nl.ealse.ccnl.control.external.ExternalRelationDeleteController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.ccnl.service.ExternalRelationService;
import nl.ealse.javafx.mapping.DataMapper;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

@Controller
public class ExternalOtherDeleteController
    extends ExternalRelationDeleteController<ExternalRelationOther>
    implements ApplicationListener<ExternalOtherSelectionEvent> {

  public ExternalOtherDeleteController(PageController pageController,
      ExternalRelationService<ExternalRelationOther> service) {
    super(pageController,service);

  }

  @Override
  public void onApplicationEvent(ExternalOtherSelectionEvent event) {
    if (MenuChoice.DELETE_EXTERNAL_RELATION == event.getMenuChoice()) {
      setSelectedEntity(event.getSelectedEntity());
      DataMapper.modelToForm(this, event.getSelectedEntity());
      getPageController().setActivePage(PageName.EXTERNAL_RELATION_DELETE);
    }
  }

}
