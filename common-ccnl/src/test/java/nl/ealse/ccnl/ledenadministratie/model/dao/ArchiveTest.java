package nl.ealse.ccnl.ledenadministratie.model.dao;

import java.time.LocalDate;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.ArchiveId;
import nl.ealse.ccnl.ledenadministratie.model.ArchivedMember;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.test.JpaTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ArchiveTest extends JpaTestBase {

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private ArchiveRepository archivedMemberRepository;

  @Test
  void testArchive() {
    Member member = memberRepository.saveAndFlush(initializedModel());

    ArchiveId archiveId = new ArchiveId();
    archiveId.setArchiveYear(2020);
    archiveId.setMemberNumber(member.getMemberNumber());

    ArchivedMember archivedMember = new ArchivedMember();
    archivedMember.setId(archiveId);
    archivedMember.setMember(member);
    archivedMemberRepository.saveAndFlush(archivedMember);

    List<ArchivedMember> archiveList = archivedMemberRepository.findAll();
    Assertions.assertEquals(1, archiveList.size());
    ArchivedMember am = archiveList.get(0);
    Assertions.assertEquals("2804 TV", am.getMember().getAddress().getPostalCode());
    boolean eq = archivedMember.equals(am);
    Assertions.assertFalse(eq);
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
    member.setMemberNumber(1473);
    member.setMemberSince(LocalDate.of(2000, 6, 1));
    member.setMemberStatus(MembershipStatus.ACTIVE);
    member.setPaymentMethod(PaymentMethod.DIRECT_DEBIT);
    member.setTelephoneNumber("06-123456789");

    return member;
  }


}
