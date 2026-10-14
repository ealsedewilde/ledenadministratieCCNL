package nl.ealse.ccnl.control.magazine;

import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.relation.MemberService;

public class InvalidAdressTable {

  private final PageController pageController;

  private final MemberService memberService;

  @FXML
  private TableView<InvalidAddressData> tableView;

  public InvalidAdressTable(PageController pageController, MemberService memberService) {
    this.pageController = pageController;
    this.memberService = memberService;
  }

  @EventListener(menuChoice = MenuChoice.MAGAZINE_INVALID_ADDRESS_TABLE)
  public void invalidAddressTable() {
    pageController.setActivePage(PageName.MAGAZINE_INVALID_ADDRESS_TABLE);
    tableView.getItems().clear();
    List<Member> members = memberService.findMembersWithInvalidAddress();
    List<InvalidAddressData> invalidAddressData =
        members.stream().map(e -> new InvalidAddressData(e)).collect(Collectors.toList());
    tableView.getItems().addAll(invalidAddressData);
  }

}
