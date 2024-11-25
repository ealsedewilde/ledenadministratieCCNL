package nl.ealse.ccnl.control.member;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.event.support.EventPublisher;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class MemberCancelControllerTest extends FXMLBaseTest {

  private MemberCancelController sut;
  private Member m;

  @Test
  void testController() {
    m = member();
    Assertions.assertTrue(runFX(() -> {
      prepare();
      doTest();
    }));

  }

  private void doTest() {
    try (MockedStatic<EventPublisher> context = mockStatic(EventPublisher.class)) {
      MemberSeLectionEvent event = new MemberSeLectionEvent(sut, MenuChoice.CANCEL_MEMBERSHIP, m);
      sut.onApplicationEvent(event);
      Assertions.assertEquals(MembershipStatus.LAST_YEAR_MEMBERSHIP, m.getMemberStatus());
      sut.save();
      context.verify(() -> EventPublisher.publishEvent(isA(CancelMailEvent.class)), times(1));
    }
  }

  private void prepare() {
    sut = getTestSubject(MemberCancelController.class);
    getPageWithFxController(sut, PageName.MEMBER_CANCEL);
  }

  private static Member member() {
    Member m = new Member();
    m.setMemberNumber(1234);
    m.setMemberStatus(MembershipStatus.ACTIVE);
    return m;
  }

}
