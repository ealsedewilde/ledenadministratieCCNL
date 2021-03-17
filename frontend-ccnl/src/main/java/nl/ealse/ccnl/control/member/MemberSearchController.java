package nl.ealse.ccnl.control.member;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import nl.ealse.ccnl.control.SearchController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.service.relation.SearchItem;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;


@Controller
public class MemberSearchController extends SearchController<Member, MemberSeLectionEvent> {

  private static final MenuChoice[] memberSearches = new MenuChoice[] {MenuChoice.AMEND_MEMBER,
      MenuChoice.PAYMENT_AUTHORIZATION, MenuChoice.CANCEL_MEMBERSHIP, MenuChoice.ADD_DOCUMENT,
      MenuChoice.VIEW_DOCUMENT, MenuChoice.DELETE_DOCUMENT, MenuChoice.MAGAZINE_INVALID_ADDRESS,};

  private final Map<String, SearchItem> searchItemMap;

  private final MemberService service;

  public MemberSearchController(ApplicationContext springContext, MemberService service) {
    super(springContext, memberSearches);
    this.searchItemMap = initializeSearchItems();
    this.service = service;
  }

  private Map<String, SearchItem> initializeSearchItems() {
    Map<String, SearchItem> map = new LinkedHashMap<>();
    map.put("Lidnummer", SearchItem.values()[0]);
    map.put("Achternaam", SearchItem.values()[1]);
    map.put("Straat", SearchItem.values()[2]);
    map.put("Postcode", SearchItem.values()[3]);
    map.put("Woonplaats", SearchItem.values()[4]);
    return map;
  }

  /**
   * Invoked when question mark column is clicked. (Invoked after selectedMember is set.)
   * 
   * @param event fire when a question mark cell in the <code>TableView</code> is clicked
   */
  public void extraInfo(MouseEvent event) {
    event.consume(); // stop further propagation to handleSelected()
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Extra Info");
    alert.showAndWait();
  }

  @Override
  public Map<String, SearchItem> searchItemValues() {
    return searchItemMap;
  }

  @Override
  public MemberSeLectionEvent newEntitySelectionEvent(MenuChoice currentMenuChoice) {
    return new MemberSeLectionEvent(this, currentMenuChoice, getSelectedEntity());
  }


  @Override
  public List<Member> doSearch(SearchItem searchItem, String value) {
    if (getCurrentMenuChoice() == MenuChoice.PAYMENT_AUTHORIZATION) {
      return service.searchMemberWithoutSepa(searchItem, value);
    }
    return service.searchMember(searchItem, value);
  }


}
