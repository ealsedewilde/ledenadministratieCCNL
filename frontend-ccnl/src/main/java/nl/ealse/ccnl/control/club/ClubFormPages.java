package nl.ealse.ccnl.control.club;

import nl.ealse.ccnl.control.external.ExternalRelationFormPages;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.javafx.PageId;

/**
 * Control the form pages for an external club.
 */
public class ClubFormPages extends ExternalRelationFormPages<ExternalRelationClub, ExternalClubController> {

  public ClubFormPages(ExternalClubController controller) {
    super(controller);
    initialize(controller, new PageId("EXTERNAL_CLUB_RELATION", "club/form/pageClub"));
  }

}
