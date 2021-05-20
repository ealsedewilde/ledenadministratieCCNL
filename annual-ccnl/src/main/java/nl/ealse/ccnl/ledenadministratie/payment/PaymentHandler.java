package nl.ealse.ccnl.ledenadministratie.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoProperties;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentFile;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.payment.ReconciliationContext.MemberContext;
import nl.ealse.ccnl.ledenadministratie.payment.ReconciliationContext.Transaction;
import nl.ealse.ccnl.ledenadministratie.payment.filter.FilterChain;
import nl.ealse.ccnl.ledenadministratie.util.AmountFormatter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentHandler {

  private static final BigDecimal DD_PROFIT = BigDecimal.valueOf(2.5);

  private final MemberRepository dao;
  private final IncassoProperties incassoProperties;

  public PaymentHandler(MemberRepository dao, IncassoProperties incassoProperties) {
    this.dao = dao;
    this.incassoProperties = incassoProperties;
  }

  public List<String> handlePayments(List<PaymentFile> paymentFiles, LocalDate referenceDate,
      boolean includeDD) {
    List<Member> members = dao.findMemberByMemberStatus(MembershipStatus.ACTIVE);
    ReconciliationContext rc =
        ReconciliationContext.newInstance(members, incassoProperties, includeDD);
    FilterChain filterChain = new FilterChain(members, referenceDate);

    final List<IngBooking> bookingList = new ArrayList<>();

    List<PaymentFileIterable> paymentFileIterables =
        paymentFiles.stream().map(PaymentFileIterable::new).collect(Collectors.toList());
    paymentFileIterables.forEach(pf -> pf.forEach(booking -> {
      if (filterChain.filter(booking)) {
        bookingList.add(booking);
      } else {
        String msg = String.format("Geen lidnummer te bepalen voor %s (%s)", booking.getNaam(), booking.getOmschrijving());
        log.warn(msg);
        rc.getMessages().add(msg);
      }
    }));
    processPaymentInfo(bookingList, rc);
    return rc.getMessages();
  }

  /**
   * Apply bookings result to the members state.
   * 
   * @param bookingList - all contribution bookings.
   * @param rc - context for this run
   */
  private void processPaymentInfo(List<IngBooking> bookingList, ReconciliationContext rc) {
    bookingList.forEach(booking -> {
      MemberContext mc = rc.getMemberContext(booking.getLidnummer());
      BigDecimal amount = BigDecimal.valueOf(booking.getBedrag());
      Transaction t = new Transaction(amount, booking.getBoekdatum(), booking.getTypebooking() ,getPaymentInfo(booking));
      mc.getTransactions().add(t);
    });

    BigDecimal refAmount = incassoProperties.getIncassoBedrag().add(DD_PROFIT);
    rc.getContexts().values().forEach(mc -> {
      Optional<Member> member = dao.findById(mc.getNumber());
      if (member.isPresent()) {
        Member m = member.get();
        m.setPaymentInfo(mc.toString());
        m.setPaymentDate(mc.getPaymentDate());
        boolean withAmount = mc.getTotalAmount().compareTo(BigDecimal.ZERO) > 0;
        m.setCurrentYearPaid(withAmount);
        if (mc.isInactief() && withAmount) {
          String msg = "Betaling ontvangen voor inactief lid " + m.getMemberNumber();
          rc.getMessages().add(msg);
        } else if (mc.getTotalAmount().compareTo(refAmount) > 0) {
            String amountString = AmountFormatter.format(mc.getTotalAmount());
            String msg =
                String.format("Lid %d heeft totaal %s betaald", m.getMemberNumber(), amountString);
            rc.getMessages().add(msg);
        }
        dao.save(m);
      }

    });
  }

  private String getPaymentInfo(IngBooking booking) {
    if (booking.isStornering()) {
      return booking.getStornoReden().getReden();
    }
    return booking.getOmschrijving();
  }

}
