package nl.ealse.ccnl.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.PaymentFileRepository;
import nl.ealse.ccnl.ledenadministratie.payment.PaymentHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
class ReconciliationServiceTest {
  
  private static final Set<MembershipStatus> STATUSES =
      EnumSet.of(MembershipStatus.ACTIVE, MembershipStatus.LAST_YEAR_MEMBERSHIP);

  
  @Mock
  private PaymentFileRepository dao;
  @Mock
  private MemberRepository memberDao;
  @Mock
  private PaymentHandler reconciliationHandler;

  @InjectMocks
  private ReconciliationService sut;
  
  @Test
  void saveFileTest() {
    Resource r = new ClassPathResource("booking.xml");
    try {
      boolean result = sut.saveFile(r.getFile());
      Assertions.assertTrue(result);
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
  }
  
  @Test
  void resetPaymentStatusTest() {
    List<Member> members = new ArrayList<>();
    members.add(new Member());
    when(memberDao.findMembersByStatuses(STATUSES)).thenReturn(members);
    sut.resetPaymentStatus();
    verify(dao).deleteAll();
  }
  
  

}
