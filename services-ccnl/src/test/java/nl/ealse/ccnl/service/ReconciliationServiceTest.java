package nl.ealse.ccnl.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import nl.ealse.ccnl.ledenadministratie.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.dao.PaymentFileRepository;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.payment.PaymentHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ReconciliationServiceTest {
  
  private static final Set<MembershipStatus> STATUSES =
      EnumSet.of(MembershipStatus.ACTIVE, MembershipStatus.LAST_YEAR_MEMBERSHIP);

  
  private static PaymentFileRepository dao;
  private static MemberRepository memberDao;

  private static ReconciliationService sut;
  
  @Test
  void saveFileTest() {
    URL url = ReconciliationService.class.getResource("/booking.xml");
    try {
      boolean result = sut.saveFile(new File(url.getFile()));
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
  
  @BeforeAll
  static void setup() {
    dao = mock(PaymentFileRepository.class);
    memberDao = mock(MemberRepository.class);
    sut = new ReconciliationService(dao, memberDao, mock(PaymentHandler.class));
  }

}
