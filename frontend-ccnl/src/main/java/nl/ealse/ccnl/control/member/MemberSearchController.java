package nl.ealse.ccnl.control.member;

import java.util.List;
import java.util.Map;
import nl.ealse.ccnl.control.SearchController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.service.relation.SearchItem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;


@Controller
public class MemberSearchController extends SearchController<Member, MemberSeLectionEvent> {

  private final MemberService service;

  public MemberSearchController(ApplicationContext springContext, MemberService service) {
    super(springContext);
    this.initializeSearchItems();
    this.service = service;
  }
  
  @EventListener(condition = "#event.group('SEARCH_MEMBER')")
  public void searchMember(MenuChoiceEvent event) {
    prepareSearch(event);
  }

  private void initializeSearchItems() {
    Map<String, SearchItem> map = getSearchItemValues();
    map.put("Lidnummer", SearchItem.values()[0]);
    map.put("Achternaam", SearchItem.values()[1]);
    map.put("Straat", SearchItem.values()[2]);
    map.put("Postcode", SearchItem.values()[3]);
    map.put("Woonplaats", SearchItem.values()[4]);
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
