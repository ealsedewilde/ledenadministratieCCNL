package nl.ealse.ccnl.ledenadministratie.dao;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberRepositoryTest {
  
  private MemberRepository sut = MemberRepository.getInstance();
  
  @Test
  void test() {
    Member owner = initializedModel();
    sut.saveAndFlush(owner);
    EnumSet<MembershipStatus> statuses =
        EnumSet.of(MembershipStatus.ACTIVE, MembershipStatus.AFTER_APRIL);
    List<Member> members = sut.findMembersCurrentYearNotPaid(statuses, EnumSet.of(PaymentMethod.DIRECT_DEBIT));
    Assertions.assertFalse(members.isEmpty());

 
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
