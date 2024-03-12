package nl.ealse.ccnl.control.member;

import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Getter;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.event.support.EventPublisher;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoProperties;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.mappers.MembershipStatusMapper;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.view.MemberCancelView;
import nl.ealse.javafx.mapping.ViewModel;

/**
 * Controller for membership canceling.
 */
public class MemberCancelController extends MemberCancelView {
  
  @Getter
  static MemberCancelController instance = new MemberCancelController();

  private final PageController pageController;

  private final MemberService service;

  private final MembershipStatusMapper membershipStatusMapper = new MembershipStatusMapper();

  private Member selectedMember;


  @FXML
  private Label initialsLabel;

  private MemberCancelController() {
    this.pageController = PageController.getInstance();
    this.service = MemberService.getInstance();
  }


  @FXML
  void initialize() {
    super.initializeView();
  }

  @FXML
  void save() {
    MembershipStatus status = membershipStatusMapper.getPropertyFromJavaFx(getMemberStatus());
    selectedMember.setMemberStatus(status);
    service.save(selectedMember);
    pageController.showMessage("Lidgegevens opgeslagen");

    if (status == MembershipStatus.LAST_YEAR_MEMBERSHIP) {
      EventPublisher.publishEvent(new CancelMailEvent(this, selectedMember));
    } else {
      pageController.activateLogoPage();
    }
  }

  @EventListener(menuChoice = MenuChoice.CANCEL_MEMBERSHIP)
  public void onApplicationEvent(MemberSeLectionEvent event) {
    this.selectedMember = event.getSelectedEntity();
    pageController.setActivePage(PageName.MEMBER_CANCEL);
    if (MembershipStatus.ACTIVE == selectedMember.getMemberStatus()) {
      // Avoid that active is a valid choice in the ChoiceBox
      selectedMember.setMemberStatus(MembershipStatus.LAST_YEAR_MEMBERSHIP);
      LocalDate now = LocalDate.now();
      if (now.getMonthValue() < 4) {
        LocalDate incassoDatum = IncassoProperties.getIncassoDatum().minusWeeks(1);
        if (now.isAfter(incassoDatum)) {
          selectedMember.setMemberStatus(MembershipStatus.AFTER_APRIL);
        }
      } 
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
