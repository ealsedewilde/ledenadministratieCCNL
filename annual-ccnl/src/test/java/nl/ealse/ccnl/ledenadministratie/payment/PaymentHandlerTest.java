package nl.ealse.ccnl.ledenadministratie.payment;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoProperties;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoPropertiesInitializer;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.PaymentFile;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class PaymentHandlerTest {
  
  private MemberRepository memberRepository;
  private IncassoProperties incassoProperties;
  
  private PaymentHandler sut;
  
  private int n = 1234;
  
  @Test
  void handlePayment() {
    initIncassoProperties();
    memberRepository = mock(MemberRepository.class);
    Optional<Member> om = Optional.of(member());
    when(memberRepository.findById(any(int.class))).thenReturn(om);
    sut = new PaymentHandler(memberRepository, incassoProperties);
    List<PaymentFile> files = new ArrayList<>();
    files.add(paymentFile());
    sut.handlePayments(files, LocalDate.of(02020, 12, 5), true);
    verify(memberRepository, atLeastOnce()).save(any(Member.class));
  }
  
  private PaymentFile paymentFile() {
    PaymentFile pf = new PaymentFile();
    Resource r = new ClassPathResource("booking.xml");
    pf.setFileName(r.getFilename());
    try (InputStream is = r.getInputStream()) {
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
  
  private void initIncassoProperties() {
    EntityManager em = mock(EntityManager.class);
    incassoProperties = new IncassoProperties(em, new IncassoPropertiesInitializer(em));
    incassoProperties.load();
  }


}
