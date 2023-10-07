package nl.ealse.ccnl.control.member;

import javafx.scene.layout.Pane;
import nl.ealse.ccnl.form.FormPages;
import nl.ealse.ccnl.form.FormPane;

/**
 * Helper for form pages.
 */
public class MemberFormPages extends FormPages<MemberController> {

  public MemberFormPages(MemberController controller) {
    super(4, controller);
    initialize(controller);
  }

  private void initialize(MemberController controller) {
    // initialize the 4 pages of the form.
    formPageArray[0] =
        new FormPane("member/form/pagePersonal", controller);
    formPageArray[1] = new FormPane("form/address", controller);
    formPageArray[2] =
        new FormPane("member/form/pageFinancial", controller);
    formPageArray[3] =
        new FormPane("member/form/pageExtraInfo", controller);

    // initialize the submenu for form nsvigation.
    addMenuItem(0, "Persoonsgegevens");
    addMenuItem(1, "Adresgegevens");
    addMenuItem(2, "Betaalgegevens");
    addMenuItem(3, "Extra informatie");
  }
  
  public Pane getThirdPage() {
    return formPageArray[3];
  }

  @Override
  protected void setFocus(int pageIndex) {
    switch (pageIndex) {
      case 1:
        controller.getStreet().requestFocus();
        break;
      case 2:
        controller.getIbanNumber().requestFocus();
        break;
      case 3:
        controller.getMemberInfo().requestFocus();
        break;
      default:
        controller.getInitials().requestFocus();
        break;
    }

  }

}
