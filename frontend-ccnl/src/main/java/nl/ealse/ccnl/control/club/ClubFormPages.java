package nl.ealse.ccnl.control.club;

import nl.ealse.ccnl.form.FormPages;
import nl.ealse.ccnl.form.FormPane;
import nl.ealse.javafx.PageId;

/**
 * Control the form pages for an external club.
 */
public class ClubFormPages extends FormPages<ExternalClubController> {

  public ClubFormPages(ExternalClubController controller) {
    super(2, controller);
    initialize(controller);
  }

  private void initialize(ExternalClubController controller) {
    formPages[0] =
        new FormPane(new PageId("EXTERNAL_CLUB_RELATION", "club/form/pageClub"), controller);
    formPages[1] = new FormPane(new PageId("EXTERNAL_CLUB_ADDRESS", "form/address"), controller);

    // initialize the submenu for form nsvigation.
    addMenuItem(0, "Clubgegevens");
    addMenuItem(1, "Adresgegevens");

  }

  @Override
  protected void setFocus(int pageIndex) {
    if (pageIndex == 0) {
      controller.getRelationName().requestFocus();
    } else {
      controller.getStreet().requestFocus();
    }

  }

}
