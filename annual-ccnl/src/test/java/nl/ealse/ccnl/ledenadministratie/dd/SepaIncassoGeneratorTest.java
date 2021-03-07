package nl.ealse.ccnl.ledenadministratie.dd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.model.dao.DocumentRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.mockito.Mockito.*;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class SepaIncassoGeneratorTest {

  @TempDir
  File tempDir;

  private CCNLColumnProperties excelProperties;
  private IncassoProperties incassoProperties;
  private MemberRepository dao;
  private DocumentRepository documentDao;

  private SepaIncassoGenerator sut;

  @Test
  void generateSepaDirectDebitFile() {
    dao = mock(MemberRepository.class);
    documentDao = mock(DocumentRepository.class);
    List<Member> members = new ArrayList<>();
    members.add(member());
    when(dao.findMemberByPaymentMethodAndMemberStatusAndCurrentYearPaidOrderByMemberNumber(PaymentMethod.DIRECT_DEBIT,
        MembershipStatus.ACTIVE, false)).thenReturn(members);
    initProperties();
    sut = new SepaIncassoGenerator(excelProperties, incassoProperties, dao, documentDao);
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
    a.setAddress("straat");
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


  private void initProperties() {
    initExcelProperties();
    initIncassoProperties();
  }

  private void initExcelProperties() {
    ConfigurableEnvironment environment = new StandardEnvironment();
    MutablePropertySources propertySources = environment.getPropertySources();
    Properties props = new Properties();
    Map<String, Object> map = new HashMap<>();
    propertySources.addFirst(new MapPropertySource("excel.properties", map));
    Resource r = new ClassPathResource("excel.properties");
    try {
      props.load(r.getInputStream());
      props.forEach((key, value) -> map.put((String) key, value));
    } catch (IOException e) {
      e.printStackTrace();
    }

    excelProperties = new CCNLColumnProperties(environment);

  }

  private void initIncassoProperties() {
    EntityManager em = mock(EntityManager.class);
    incassoProperties = new IncassoProperties(em, new IncassoPropertiesInitializer(em));
    incassoProperties.load();
  }

}
