package nl.ealse.ccnl.service.relation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.util.MemberNumberFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
  
  @Mock
  private MemberNumberFactory memberNumberFactory;
  
  @Mock
  private MemberRepository dao;
  
  @InjectMocks
  private MemberService sut;
  
  
  @Test
  void testSearchCity() {
    String searchValue ="foo";
    sut.searchMember(SearchItem.CITY, searchValue);
    verify(dao).findMembersByCity(searchValue);
  }
  
  @Test
  void testSearchName() {
    String searchValue ="foo";
    sut.searchMember(SearchItem.NAME, searchValue);
    verify(dao).findMembersByName(searchValue);
  }
  
  @Test
  void testSearchNumber() {
    String searchValue ="1234";
    sut.searchMember(SearchItem.NUMBER, searchValue);
    verify(dao).findById(any(Integer.class));
  }
  
  @Test
  void testSearchPC() {
    String searchValue ="foo";
    sut.searchMember(SearchItem.POSTAL_CODE, searchValue);
    verify(dao).findMembersByPostalCode(searchValue);
  }
  
  @Test
  void testSearchStreet() {
    String searchValue ="foo";
    sut.searchMember(SearchItem.STREET, searchValue);
    verify(dao).findMembersByAddress(searchValue);
  }
  
  @Test
  void testSearchMemberWithoutSepa() {
    List<Member> members = new ArrayList<>();
    Member m = new Member();
    members.add(m);
    List<Document> documents = new ArrayList<>();
    m.setDocuments(documents);
    Document d = new Document();
    documents.add(d);
    d.setOwner(m);
    d.setDocumentType(DocumentType.SEPA_AUTHORIZATION);
    String searchValue ="foo";
    when(dao.findMembersByAddress(searchValue)).thenReturn(members);
    List<Member> result = sut.searchMemberWithoutSepa(SearchItem.STREET, searchValue);
    Assertions.assertTrue(result.isEmpty());
  }
  
  @Test
  void testGetFreeNumber() {
    sut.getFreeNumber();
    verify(memberNumberFactory).getNewNumber();
  }
  
  @Test
  void testFindMembersCurrentYearNotPaid() {
    sut.findMembersCurrentYearNotPaid(PaymentMethod.BANK_TRANSFER);
    verify(dao).findMembersCurrentYearNotPaid(EnumSet.of(PaymentMethod.BANK_TRANSFER));
  }
  
  @Test
  void testReadMember() {
    Integer nr = Integer.valueOf(1234);
    sut.readMember(nr);
    verify(dao).findById(nr);
  }
  
  @Test
  void testPersistMember() {
    Member m = new Member();
    sut.persistMember(m);
    verify(dao).save(m);
  }

  
  

}
