package nl.ealse.ccnl.control.member;

import nl.ealse.ccnl.control.SearchPane;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;

public class MemberSearch extends SearchPane<Member, MemberSeLectionEvent> {

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
