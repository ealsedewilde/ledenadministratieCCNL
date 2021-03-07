package nl.ealse.ccnl.control.member;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.mappers.MembershipStatusMapper;
import nl.ealse.ccnl.service.MemberService;
import nl.ealse.ccnl.view.MemberCancelView;
import nl.ealse.javafx.mapping.DataMapper;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

@Controller
public class MemberCancelController extends MemberCancelView
    implements ApplicationListener<MemberSeLectionEvent> {

  private final PageController pageController;

  private final MemberService service;

  private final MembershipStatusMapper membershipStatusMapper = new MembershipStatusMapper();

  private Member selectedMember;


  @FXML
  private Label initialsLabel;

  public MemberCancelController(MemberService service, PageController pageController) {
    this.pageController = pageController;
    this.service = service;
  }


  @FXML
  public void initialize() {
    super.initializeView();
  }

  @FXML
  public void save() {
    MembershipStatus status = membershipStatusMapper.getPropertyFromJavaFx(getMemberStatus());
    selectedMember.setMemberStatus(status);
    service.persistMember(selectedMember);
    pageController.setMessage("Lidgegevens opgeslagen");

    if (status == MembershipStatus.LAST_YEAR_MEMBERSHIP) {
      // next page
      pageController.setActivePage(PageName.MEMBER_CANCEL_MAIL);
    } else {
      pageController.setActivePage(PageName.LOGO);
    }
  }

  @Override
  public void onApplicationEvent(MemberSeLectionEvent event) {
    if (event.getMenuChoice() == MenuChoice.CANCEL_MEMBERSHIP) {
      this.selectedMember = event.getSelectedEntity();
      pageController.setActivePage(PageName.MEMBER_CANCEL);
      if (MembershipStatus.ACTIVE == selectedMember.getMemberStatus()) {
        // Avoid that active is a valid choice in the ChoiceBox
        selectedMember.setMemberStatus(MembershipStatus.LAST_YEAR_MEMBERSHIP);
      }
      DataMapper.modelToForm(this, selectedMember);
      initializeInitialsType();
    }
  }

  private void initializeInitialsType() {
    if (getInitials().getText() == null || getInitials().getText().indexOf(".") > -1) {
      // initials
      initialsLabel.setText("Voorletters");
    } else {
      // surname
      initialsLabel.setText("Voornaam");
    }
  }

}
