package nl.ealse.ccnl.control.magazine;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.view.AddressView;
import nl.ealse.javafx.mapping.ViewModel;

public class InvalidAddressController extends AddressView {

  private final PageController pageController;

  private final MemberService service;

  private Member selectedMember;

  @FXML
  private Label memberNumber;
  @FXML
  private Label memberName;
  @FXML
  private TextArea memberInfo;

  public InvalidAddressController(PageController pageController, MemberService service) {
    this.pageController = pageController;
    this.service = service;
  }

  @EventListener(menuChoice = MenuChoice.MAGAZINE_INVALID_ADDRESS)
  public void onApplicationEvent(MemberSeLectionEvent event) {
    pageController.setActivePage(PageName.MAGAZINE_INVALID_ADDRESS);
    selectedMember = event.getSelectedEntity();
    memberNumber.setText("Adres voor lidnummer: " + selectedMember.getMemberNumber().toString());
    memberName.setText(selectedMember.getFullName());
    ViewModel.modelToView(this, selectedMember);
  }

  @FXML
  void addressInvalid() {
    selectedMember.getAddress().setAddressInvalid(true);
    selectedMember.setMemberInfo(memberInfo.getText());
    service.save(selectedMember);
    pageController.showMessage("Wijziging opgeslagen");
    pageController.activateLogoPage();
  }

  @FXML
  void cancel() {
    pageController.activateLogoPage();
  }

}
