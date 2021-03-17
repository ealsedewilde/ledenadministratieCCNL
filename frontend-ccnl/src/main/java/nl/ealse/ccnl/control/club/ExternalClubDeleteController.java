package nl.ealse.ccnl.control.club;

import nl.ealse.ccnl.control.external.ExternalRelationDeleteController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import nl.ealse.javafx.mapping.DataMapper;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

@Controller
public class ExternalClubDeleteController
    extends ExternalRelationDeleteController<ExternalRelationClub>
    implements ApplicationListener<ExternalClubSelectionEvent> {

  public ExternalClubDeleteController(PageController pageController,
      ExternalRelationService<ExternalRelationClub> service) {
    super(pageController, service);
  }

  @Override
  public void onApplicationEvent(ExternalClubSelectionEvent event) {
    if (MenuChoice.DELETE_EXTERNAL_CLUB == event.getMenuChoice()) {
      setSelectedEntity(event.getSelectedEntity());
      DataMapper.modelToForm(this, event.getSelectedEntity());
      getPageController().setActivePage(PageName.EXTERNAL_CLUB_DELETE);
    }
  }

}
