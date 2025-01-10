package nl.ealse.ccnl.control.member;

import java.util.List;
import nl.ealse.ccnl.control.menu.ChoiceGroup;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.service.relation.SearchItem;

public class SepaAuthorizationMemberSearchController extends MemberSearchController {
  
  public SepaAuthorizationMemberSearchController(PageController pageController, MemberService service) {
    super(pageController, service);
  }
  
  @Override
  @EventListener(choiceGroup = ChoiceGroup.SEARCH_PA_MEMBER)
  public void searchMember(MenuChoiceEvent event) {
    super.searchMember(event);
  }


  @Override
  protected List<Member> doSearch(SearchItem searchItem, String value) {
    return service.searchMemberWithoutSepa(searchItem, value);
  }

  @Override
  protected String headerText(MenuChoice currentMenuChoice) {
    return "SEPA-machtiging - Opzoeken lid";
  }

  @Override
  protected String resultHeaderText(MenuChoice currentMenuChoice) {
     return "Gevonden leden zonder SEPA machtiging";
  }

}
