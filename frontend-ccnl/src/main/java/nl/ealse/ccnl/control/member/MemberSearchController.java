package nl.ealse.ccnl.control.member;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import nl.ealse.ccnl.control.SearchController;
import nl.ealse.ccnl.control.menu.ChoiceGroup;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.service.relation.SearchItem;

public class MemberSearchController extends SearchController<Member, MemberSeLectionEvent> {
  
  @Getter
  private static final MemberSearchController instance = new MemberSearchController();

  private final PageController pageController;
  private final MemberService service;

  private MemberSearchController() {
    this.pageController = PageController.getInstance();
    this.service = MemberService.getInstance();
  }

  @EventListener(choiceGroup = ChoiceGroup.SEARCH_MEMBER)
  public void searchMember(MenuChoiceEvent event) {
    pageController.setActivePage(getPageReference());
    prepareSearch(event);
  }

  @Override
  protected void initializeSearchItems() {
    Map<String, SearchItem> map = getSearchItemValues();
    map.put("Lidnummer", SearchItem.values()[0]);
    map.put("Achternaam", SearchItem.values()[1]);
    map.put("Straat", SearchItem.values()[2]);
    map.put("Postcode", SearchItem.values()[3]);
    map.put("Woonplaats", SearchItem.values()[4]);
  }

  @Override
  protected MemberSeLectionEvent newEntitySelectionEvent(MenuChoice currentMenuChoice) {
    return new MemberSeLectionEvent(this, currentMenuChoice, getSelectedEntity());
  }


  @Override
  protected List<Member> doSearch(SearchItem searchItem, String value) {
    if (getCurrentMenuChoice() == MenuChoice.PAYMENT_AUTHORIZATION) {
      return service.searchMemberWithoutSepa(searchItem, value);
    }
    return service.searchMember(searchItem, value);
  }

  @Override
  protected String headerText(MenuChoice currentMenuChoice) {
    switch (currentMenuChoice) {
      case MAGAZINE_INVALID_ADDRESS:
        return "Ongeldig adres - Opzoeken lid";
      case CANCEL_MEMBERSHIP:
        return "Opzeggen - Opzoeken lid";
      case AMEND_MEMBER:
        return "Opzoeken te wijzigen lid";
      case PAYMENT_AUTHORIZATION:
        return "SEPA-machtiging - Opzoeken lid";
      case ADD_DOCUMENT:
        return "Document toevoegen - Opzoeken lid";
      case VIEW_DOCUMENT:
        return "Documenten inzien - Opzoeken lid";
      case DELETE_DOCUMENT:
        return "Documenten verwijderen - Opzoeken lid";
      default:
        return "";
    }
  }

  @Override
  protected String columnName(int ix) {
    return "Lid nr.";
  }

  @Override
  protected String resultHeaderText(MenuChoice currentMenuChoice) {
    return "Gevonden leden";
  }


}
