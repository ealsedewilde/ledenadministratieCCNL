package nl.ealse.ccnl.ledenadministratie.dd;

import static org.mockito.Mockito.when;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.model.dao.DocumentRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import nl.ealse.ccnl.test.MockProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SepaIncassoGeneratorTest {

  @TempDir
  File tempDir;

  private MemberRepository dao;

  private SepaIncassoGenerator sut;

  @Test
  void generateSepaDirectDebitFile() {
    dao = MockProvider.mock(MemberRepository.class);
    MockProvider.mock(DocumentRepository.class);
    List<Member> members = new ArrayList<>();
    members.add(member());
    when(dao.findMemberByPaymentMethodAndMemberStatusAndCurrentYearPaidOrderByMemberNumber(PaymentMethod.DIRECT_DEBIT,
        MembershipStatus.ACTIVE, false)).thenReturn(members);
    sut = SepaIncassoGenerator.getInstance();
    File xmlFile = new File(tempDir, "sepa.xml");
    File excelFile = new File(tempDir, "sepa.xlsx");
    try {
      SepaIncassoResult result = sut.generateSepaDirectDebitFile(xmlFile, excelFile);
      Assertions.assertEquals(1, result.getNumberOfTransactions());
    } catch (IncassoException e) {
      e.printStackTrace();
    }
  }

  private Member member() {
    Member m = new Member();
    m.setMemberNumber(1234);
    Address a = m.getAddress();
    a.setStreet("straat");
    a.setAddressNumber("99");
    a.setPostalCode("1234 AA");
    a.setCity("Ons Dorp");
    m.setInitials("T.");
    m.setLastName("Tester");
    m.setEmail("tester@Test.nl");
    m.setIbanNumber("NL54ASNB0709093276");
    m.setPaymentMethod(PaymentMethod.DIRECT_DEBIT);
    return m;
  }



}
