package nl.ealse.ccnl.control.magazine;

import static org.mockito.Mockito.verify;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InvalidAddressControllerTest extends FXMLBaseTest {

  private InvalidAddressController sut;

  @Test
  void testController() {
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    MemberSeLectionEvent event =
        new MemberSeLectionEvent(sut, MenuChoice.MAGAZINE_INVALID_ADDRESS, member());
    sut.onApplicationEvent(event);

    sut.addressInvalid();
    verify(getPageController()).showMessage("Wijziging opgeslagen");
    sut.cancel();
  }

  private void prepare() {
    sut = getTestSubject(InvalidAddressController.class);
    getPageWithFxController(sut, PageName.MAGAZINE_INVALID_ADDRESS);
  }

  private Member member() {
    Member m = new Member();
    m.setMemberNumber(1234);
    m.setInitials("T.");
    m.setLastNamePrefix("de");
    m.setLastName("Tester");
    return m;
  }


}
