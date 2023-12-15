package nl.ealse.ccnl.ledenadministratie.payment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.PaymentFile;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import nl.ealse.ccnl.test.MockProvider;
import org.junit.jupiter.api.Test;

class PaymentHandlerTest {
  
  private MemberRepository memberRepository;
  
  private PaymentHandler sut;
  
  private int n = 1234;
  
  @Test
  void handlePayment() {
    memberRepository = MockProvider.mock(MemberRepository.class);
    Optional<Member> om = Optional.of(member());
    when(memberRepository.findById(any(int.class))).thenReturn(om);
    sut = PaymentHandler.getInstance();
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
