package nl.ealse.ccnl.control.member;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.FXMLMissingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MemberCancelControllerTest extends FXMLBaseTest<MemberCancelController> {

  private static PageController pageController;
  private static MemberService memberService;

  private MemberCancelController sut;
  private Member m;

  @Test
  void testController() {
    sut = new MemberCancelController(memberService, pageController);
    m = member();
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    MemberSeLectionEvent event = new MemberSeLectionEvent(sut, MenuChoice.CANCEL_MEMBERSHIP, m);
    sut.onApplicationEvent(event);
    Assertions.assertEquals(MembershipStatus.LAST_YEAR_MEMBERSHIP, m.getMemberStatus());
    sut.save();
    verify(pageController).setActivePage(PageName.MEMBER_CANCEL_MAIL);
  }

  private void prepare() {
    try {
      getPageWithFxController(sut, PageName.MEMBER_CANCEL);
    } catch (FXMLMissingException e) {
      Assertions.fail(e.getMessage());
    }
  }

  @BeforeAll
  static void setup() {
   
    pageController = mock(PageController.class);
    memberService = mock(MemberService.class);
  }

  private static Member member() {
    Member m = new Member();
    m.setMemberNumber(1234);
    m.setMemberStatus(MembershipStatus.ACTIVE);
    return m;
  }

}
