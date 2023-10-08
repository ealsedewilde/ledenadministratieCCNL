package nl.ealse.ccnl.control.club;

import nl.ealse.ccnl.control.external.ExternalRelationFormController;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;

/**
 * Control the form pages for an external club.
 */
public class ClubFormController
    extends ExternalRelationFormController<ExternalRelationClub, ExternalClubController> {

  public ClubFormController(ExternalClubController controller) {
    super(controller);
  }

  @Override
  protected void initializePages() {
    initialize("club/form/pageClub", "Clubgegevens");
  }

}
