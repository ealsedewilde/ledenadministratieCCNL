package nl.ealse.ccnl.ledenadministratie.payment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import nl.ealse.ccnl.ledenadministratie.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentFile;
import org.junit.jupiter.api.Test;

class PaymentHandlerTest {
  
  private static final EnumSet<MembershipStatus> statuses =
      EnumSet.of(MembershipStatus.ACTIVE, MembershipStatus.AFTER_APRIL);

  
  private MemberRepository memberRepository;
  
  private PaymentHandler sut;
  
  private int n = 9999;
  
  @Test
  void handlePayment() {
    memberRepository = mock(MemberRepository.class);
    Optional<Member> om = Optional.of(member());
    List<Member>  ml = new ArrayList<>();
    ml.add(member());
    when(memberRepository.findById(any(int.class))).thenReturn(om);
    when(memberRepository.findMembersByStatuses(statuses)).thenReturn(ml);
    sut = new PaymentHandler(memberRepository);
    List<PaymentFile> files = new ArrayList<>();
    files.add(paymentFile());
    sut.handlePayments(files, LocalDate.of(2020, 12, 5), true);
    verify(memberRepository, atLeastOnce()).save(any(Member.class));
  }
  
  private PaymentFile paymentFile() {
    PaymentFile pf = new PaymentFile();
    URL url = getClass().getResource("/booking.xml");
    pf.setFileName(url.getFile());
    try (InputStream is = url.openStream()) {
      String xml = new String(is.readAllBytes());
      pf.setXml(xml);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return pf;
  }
  
  private Member member() {
    Member m = new Member();
    m.setMemberNumber(n);
    return m;
  }

}
