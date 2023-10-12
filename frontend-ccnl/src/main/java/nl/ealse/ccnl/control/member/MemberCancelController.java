package nl.ealse.ccnl.control.member;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.mappers.MembershipStatusMapper;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.view.MemberCancelView;
import nl.ealse.javafx.mapping.ViewModel;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

/**
 * Controller for membership canceling.
 */
@Controller
public class MemberCancelController extends MemberCancelView {

  private final PageController pageController;

  private final MemberService service;

  private final ApplicationEventPublisher eventPublisher;

  private final MembershipStatusMapper membershipStatusMapper = new MembershipStatusMapper();

  private Member selectedMember;


  @FXML
  private Label initialsLabel;

  public MemberCancelController(MemberService service, PageController pageController,
      ApplicationEventPublisher eventPublisher) {
    this.pageController = pageController;
    this.service = service;
    this.eventPublisher = eventPublisher;
  }


  @FXML
  void initialize() {
    super.initializeView();
  }

  @FXML
  void save() {
    MembershipStatus status = membershipStatusMapper.getPropertyFromJavaFx(getMemberStatus());
    selectedMember.setMemberStatus(status);
    service.persistMember(selectedMember);
    pageController.showMessage("Lidgegevens opgeslagen");

    if (status == MembershipStatus.LAST_YEAR_MEMBERSHIP) {
      eventPublisher.publishEvent(new CancelMailEvent(this, selectedMember));
    } else {
      pageController.activateLogoPage();
    }
  }

  @EventListener(condition = "#event.name('CANCEL_MEMBERSHIP')")
  public void onApplicationEvent(MemberSeLectionEvent event) {
    this.selectedMember = event.getSelectedEntity();
    pageController.setActivePage(PageName.MEMBER_CANCEL);
    if (MembershipStatus.ACTIVE == selectedMember.getMemberStatus()) {
      // Avoid that active is a valid choice in the ChoiceBox
      selectedMember.setMemberStatus(MembershipStatus.LAST_YEAR_MEMBERSHIP);
    }
    ViewModel.modelToView(this, selectedMember);
    initializeInitialsType();
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
