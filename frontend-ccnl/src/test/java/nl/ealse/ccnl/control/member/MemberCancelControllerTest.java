package nl.ealse.ccnl.control.member;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.event.support.EventPublisher;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.ccnl.test.MockProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class MemberCancelControllerTest extends FXMLBaseTest {

  private MemberCancelController sut;
  private Member m;

  @Test
  void testController() {
    sut = MemberCancelController.getInstance();
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
    try (MockedStatic<EventPublisher> context = mockStatic(EventPublisher.class)) {
      MemberSeLectionEvent event = new MemberSeLectionEvent(sut, MenuChoice.CANCEL_MEMBERSHIP, m);
      sut.onApplicationEvent(event);
      Assertions.assertEquals(MembershipStatus.LAST_YEAR_MEMBERSHIP, m.getMemberStatus());
      sut.save();
      context.verify(() -> EventPublisher.publishEvent(isA(CancelMailEvent.class)), times(1));
    }
  }

  private void prepare() {
    sut = MemberCancelController.getInstance();
    getPageWithFxController(sut, PageName.MEMBER_CANCEL);
  }

  @BeforeAll
  static void setup() {
    MockProvider.mock(MemberService.class);
  }

  private static Member member() {
    Member m = new Member();
    m.setMemberNumber(1234);
    m.setMemberStatus(MembershipStatus.ACTIVE);
    return m;
  }

}
