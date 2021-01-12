package nl.ealse.ccnl.ledenadministratie.payment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentFile;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.payment.filter.FilterChain;
import org.springframework.stereotype.Component;

@Component
public class PaymentHandler {

  private final MemberRepository dao;

  public PaymentHandler(MemberRepository dao) {
    this.dao = dao;
  }

  public void handlePayment(List<PaymentFile> paymentFiles, LocalDate referenceDate) {
    List<Member> members = dao.findMemberByMemberStatus(MembershipStatus.ACTIVE);
    FilterChain filterChain = new FilterChain(members, referenceDate);

    final List<IngBooking> bookingList = new ArrayList<>();

    List<PaymentFileIterable> paymentFileIterables =
        paymentFiles.stream().map(PaymentFileIterable::new).collect(Collectors.toList());
    paymentFileIterables.forEach(pf -> pf.forEach(booking -> {
      if (filterChain.filter(booking)) {
        bookingList.add(booking);
      }
    }));
    processPaymentInfo(bookingList);
  }

  private void processPaymentInfo(List<IngBooking> bookingList) {
    bookingList.forEach(booking -> {
      Optional<Member> member = dao.findById(booking.getLidnummer());
      if (member.isPresent()) {
        Member m = member.get();
        m.setPaymentDate(booking.getBoekdatum());
        if (booking.isStornering()) {
          m.setPaymentInfo(booking.getStornoReden().getReden());
          m.setCurrentYearPaid(false);
        } else if (booking.isContributie()) {
          m.setPaymentInfo(booking.getOmschrijving());
          m.setCurrentYearPaid(true);
        }
        dao.save(m);
      }
    });
  }

}
