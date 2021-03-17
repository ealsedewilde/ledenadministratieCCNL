package nl.ealse.ccnl.ledenadministratie.excelimport;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.ealse.ccnl.ledenadministratie.excel.lid.CCNLLid;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MemberImportTest {
  
  private MemberImport sut;
  private static MemberRepository repository;
  private static CCNLLid lid;
  private static Member m = new Member();
  
  @Test
  void testImportMember() {
    sut = new MemberImport(repository, ProcessType.REPLACE);
    sut.importMember(lid);
    verify(repository).save(m);
    sut.finalizeImport();
    verify(repository).deleteById(Integer.valueOf(1111));
    verify(repository, never()).deleteById(Integer.valueOf(1234));
  }
  
  @Test
  void testAddressInvalid() {
    sut = new MemberImport(repository, ProcessType.REPLACE);
    sut.addressInvalid(lid);
    Assertions.assertTrue(m.getAddress().isAddressInvalid());
  }
  
  @BeforeAll
  static void setup() {
    repository = mock(MemberRepository.class);
    when(repository.findById(any(Integer.class))).thenReturn(Optional.of(m));
    List<Number> members = new ArrayList<>();
    members.add(Integer.valueOf(1234));
    members.add(Integer.valueOf(1111));
    when(repository.getAllMemberNumbers()).thenReturn(members);
    lid = mock(CCNLLid.class);
    when(lid.getRelatienummer()).thenReturn(1234);
    when(lid.getAchternaam()).thenReturn("Tester");
    when(lid.getEmail()).thenReturn("Tester@test.nl");
    when(lid.getIncassoAanduiding()).thenReturn("Erelid");
    when(lid.getLidNummer()).thenReturn(1234);
    when(lid.getPlaats()).thenReturn("Ons Dorp");
    when(lid.getPostcode()).thenReturn("1234 AA");
    when(lid.getStraat()).thenReturn("Brink");
    when(lid.getVoornaam()).thenReturn("T.");
 }

}
