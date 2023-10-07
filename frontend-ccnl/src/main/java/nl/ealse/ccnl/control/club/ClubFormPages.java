package nl.ealse.ccnl.control.club;

import nl.ealse.ccnl.control.external.ExternalRelationFormPages;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;

/**
 * Control the form pages for an external club.
 */
public class ClubFormPages
    extends ExternalRelationFormPages<ExternalRelationClub, ExternalClubController> {

  public ClubFormPages(ExternalClubController controller) {
    super(controller);
  }

  @Override
  protected void initializePages() {
    initialize("club/form/pageClub", "Clubgegevens");
  }

}
