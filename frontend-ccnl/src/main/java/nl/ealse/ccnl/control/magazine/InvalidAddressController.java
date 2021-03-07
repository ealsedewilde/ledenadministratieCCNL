package nl.ealse.ccnl.control.magazine;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.MemberService;
import nl.ealse.ccnl.view.AddressView;
import nl.ealse.javafx.mapping.DataMapper;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

@Controller
public class InvalidAddressController extends AddressView
    implements ApplicationListener<MemberSeLectionEvent> {

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

  @Override
  public void onApplicationEvent(MemberSeLectionEvent event) {
    if (MenuChoice.MAGAZINE_INVALID_ADDRESS == event.getMenuChoice()) {
      selectedMember = event.getSelectedEntity();
      memberNumber.setText("Adres voor lidnummer: " + selectedMember.getMemberNumber().toString());
      memberName.setText(selectedMember.getFullName());
      pageController.setActivePage(PageName.MAGAZINE_INVALID_ADDRESS);
      DataMapper.modelToForm(this, selectedMember);
    }

  }

  @FXML
  public void addressInvalid() {
    selectedMember.getAddress().setAddressInvalid(true);
    selectedMember.setMemberInfo(memberInfo.getText());
    service.persistMember(selectedMember);
    pageController.setMessage("Wijziging opgeslagen");
    pageController.setActivePage(PageName.LOGO);
  }

  @FXML
  public void cancel() {
    pageController.setActivePage(PageName.LOGO);
  }

}
