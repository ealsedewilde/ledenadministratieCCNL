package nl.ealse.ccnl.control.magazine;

import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nl.ealse.ccnl.MainStage;
import nl.ealse.ccnl.control.button.ImageCell;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.javafx.ImagesMap;

public class InvalidAdressTable {

  private final PageController pageController;

  private final MemberService memberService;

  @FXML
  private TableView<InvalidAddressData> tableView;

  private List<Member> members;
  
  private Alert confirmation;

  @FXML
  private TableColumn<InvalidAddressData, Void> buttonColumn;

  public InvalidAdressTable(PageController pageController, MemberService memberService) {
    this.pageController = pageController;
    this.memberService = memberService;
  }

  @FXML
  void initialize() {
    buttonColumn.setCellFactory(
        param -> new ImageCell<InvalidAddressData>("undo_small.png", this::resetInvalidAddress));
    confirmation = buildAlert();
  }

  @EventListener(menuChoice = MenuChoice.MAGAZINE_INVALID_ADDRESS_TABLE)
  public void invalidAddressTable() {
    pageController.setActivePage(PageName.MAGAZINE_INVALID_ADDRESS_TABLE);
    processMembers();
  }

  private void resetInvalidAddress(MouseEvent event) {
    event.consume(); // stop further propagation to handleSelected()
    confirmation.showAndWait().filter(response -> response == ButtonType.OK)
        .ifPresent(response -> resetInvalidity(event));
  }

  private void resetInvalidity(MouseEvent event) {
    int ix = ((ImageCell<?>) event.getSource()).getIndex();
    Member m = members.get(ix);
    m.getAddress().setAddressInvalid(false);
    memberService.save(m);
    processMembers();
  }

  private void processMembers() {
    tableView.getItems().clear();
    members = memberService.findMembersWithInvalidAddress();
    List<InvalidAddressData> invalidAddressData =
        members.stream().map(e -> new InvalidAddressData(e)).collect(Collectors.toList());
    tableView.getItems().addAll(invalidAddressData);
  }
  
  private Alert buildAlert() {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    Stage cs = (Stage) alert.getDialogPane().getScene().getWindow();
    cs.getIcons().add(MainStage.getIcon());
    alert.setTitle("Bevestigen");
    alert.setHeaderText("Is huidige adres correct?");
    Button b = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
    b.setText("Adres is correct");
    b = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
    b.setText("Annuleren");
    alert.setGraphic(new ImageView(ImagesMap.get("approve.png")));
    return alert;
  }
 
}
