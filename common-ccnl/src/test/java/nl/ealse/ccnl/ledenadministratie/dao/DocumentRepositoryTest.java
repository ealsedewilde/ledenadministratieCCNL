package nl.ealse.ccnl.ledenadministratie.dao;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DocumentRepositoryTest {

  private static final String DOC_NAME = "/Lid 0030.pdf";

  private DocumentRepository dao = new DocumentRepository();

  private MemberRepository memberDao = new MemberRepository();

  @Test
  void findByOwnerAndDocumentTypeTest() {
    Member owner = initializedModel();
    Member savedMember = memberDao.saveAndFlush(owner);
    Document sepa = initializeDocument(savedMember);
    dao.saveAndFlush(sepa);

    List<Document> sepaList =
        dao.findByOwnerAndDocumentType(savedMember, DocumentType.SEPA_AUTHORIZATION);
    Assertions.assertEquals(1, sepaList.size());
    
    Set<Integer> numberSet = dao.findMemberNummbersWithSepa().collect(Collectors.toSet());
    Assertions.assertEquals(1, numberSet.size());
  }

  private Document initializeDocument(Member owner) {
    Document doc = new Document();
    doc.setOwner(owner);
    doc.setDocumentType(DocumentType.SEPA_AUTHORIZATION);
    doc.setDocumentName(DOC_NAME);
    doc.setPdf(getPDF());
    return doc;
  }

  private byte[] getPDF() {
    try (InputStream is = getClass().getResourceAsStream(DOC_NAME)) {
      return is.readAllBytes();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Member initializedModel() {
    Member member = new Member();
    Address address = new Address();
    address.setStreet("Ida Hoeve");
    address.setAddressNumber("16");
    address.setPostalCode("2804 TV");
    address.setCity("Gouda");
    member.setAddress(address);
    member.setEmail("santaclaus@gmail.com");
    member.setIbanNumber("NL54ASNB0709093276");
    member.setInitials("I.M.");
    member.setLastName("Wolf");
    member.setLastNamePrefix("van der");
    member.setMemberInfo("Some additional text");
    member.setMemberNumber(1474);
    member.setMemberSince(LocalDate.of(2000, 6, 1));
    member.setMemberStatus(MembershipStatus.ACTIVE);
    member.setPaymentMethod(PaymentMethod.DIRECT_DEBIT);
    member.setTelephoneNumber("06-123456789");

    return member;
  }


}
