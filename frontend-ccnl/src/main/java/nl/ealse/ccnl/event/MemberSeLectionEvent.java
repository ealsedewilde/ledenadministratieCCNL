package nl.ealse.ccnl.event;

import nl.ealse.ccnl.control.member.MemberSearchController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.MenuController;
import nl.ealse.ccnl.ledenadministratie.model.Member;

/**
 * Event fired by {@link MenuController} It is the {@link MemberSearchController} that listens to
 * these events.
 *
 * @author ealse
 *
 */
@SuppressWarnings("serial")
public class MemberSeLectionEvent extends EntitySelectionEvent<Member> {

  public MemberSeLectionEvent(Object source, MenuChoice menuChoice, Member member) {
    super(source, menuChoice, member);
  }

}
