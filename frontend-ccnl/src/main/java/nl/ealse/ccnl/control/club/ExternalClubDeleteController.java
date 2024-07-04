package nl.ealse.ccnl.control.club;

import nl.ealse.ccnl.control.external.ExternalRelationDeleteController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.service.relation.ExternalClubService;
import nl.ealse.javafx.mapping.ViewModel;

public class ExternalClubDeleteController
    extends ExternalRelationDeleteController<ExternalRelationClub> {

  public ExternalClubDeleteController(PageController pageController, ExternalClubService service) {
    super(pageController, service);
  }

  @EventListener(menuChoice = MenuChoice.DELETE_EXTERNAL_CLUB)
  public void onApplicationEvent(ExternalClubSelectionEvent event) {
    getPageController().setActivePage(PageName.EXTERNAL_CLUB_DELETE);
    setSelectedEntity(event.getSelectedEntity());
    ViewModel.modelToView(this, event.getSelectedEntity());
  }

}
