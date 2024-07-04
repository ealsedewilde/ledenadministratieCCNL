package nl.ealse.ccnl.ledenadministratie.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoProperties;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentFile;
import nl.ealse.ccnl.ledenadministratie.payment.ReconciliationContext.BankTransaction;
import nl.ealse.ccnl.ledenadministratie.payment.ReconciliationContext.MemberContext;
import nl.ealse.ccnl.ledenadministratie.payment.filter.FilterChain;
import nl.ealse.ccnl.ledenadministratie.util.AmountFormatter;

/**
 * Handler for the reconcile process.
 */
@Slf4j
public class PaymentHandler {

  private final MemberRepository dao;

  private static final EnumSet<MembershipStatus> statuses =
      EnumSet.of(MembershipStatus.ACTIVE, MembershipStatus.AFTER_APRIL);

   public PaymentHandler(MemberRepository dao) {
    this.dao = dao;
  }

  /**
   * Perform the reconciliation process.
   *
   * @param paymentFiles - the files to reconcile
   * @param referenceDate - start date of the oldest reconcile file
   * @param includeDD - assume Direct Debit process is executed
   * @return list of reconciliation results that need manual investigation
   */
  public List<String> handlePayments(List<PaymentFile> paymentFiles, LocalDate referenceDate,
      boolean includeDD) {
    // members that will be active in the upcoming year
    List<Member> members = dao.findMembersByStatuses(statuses);

    // Context with all payments made previously to the reconciliation
    final ReconciliationContext rc =
        ReconciliationContext.newInstance(members, referenceDate, includeDD);

    // Initialize the reconciliation process
    final FilterChain filterChain = new FilterChain(members, referenceDate);
    final List<IngBooking> bookingList = new ArrayList<>();

    // Reconcile the payment file(s).
    List<PaymentFileIterable> paymentFileIterables =
        paymentFiles.stream().map(PaymentFileIterable::new).toList();
    paymentFileIterables.forEach(pf -> pf.forEach(booking -> {
      if (filterChain.filter(booking)) {
        if (booking.getLidnummer() == 0) {
          String msg = String.format("Geen lidnummer te bepalen voor %s (%s)", booking.getNaam(),
              booking.getOmschrijving());
          log.warn(msg);
          rc.getMessages().add(msg);
        } else {
          log.info("boeking voor lid: " + booking.getLidnummer());
          bookingList.add(booking);
        }
      }
    }));

    // Handle the results of the reconciliation
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
      int lidnummer = booking.getLidnummer();
      MemberContext mc = rc.getMemberContext(lidnummer);
      BigDecimal amount = BigDecimal.valueOf(booking.getBedrag());
      BankTransaction t = new BankTransaction(amount, booking.getBoekdatum(),
          booking.getTypebooking(), getPaymentInfo(booking));
      mc.getBankTransactions().add(t);
    });

    // At this point ALL BankTransactions are put in the MemberContext of every member in
    // the ReconciliationContext.
    // We can now evaluate the status for every member.
    BigDecimal bankTransferAmount = BigDecimal.valueOf(MemberShipFee.getOverboeken());
    rc.getContexts().values().forEach(mc -> {
      Optional<Member> member = dao.findById(mc.getNumber());
      if (member.isPresent()) {
        Member m = member.get();
        performPaymentChecks(rc, bankTransferAmount, mc, m);
        dao.save(m);
      }
    });

  }

  private void performPaymentChecks(ReconciliationContext rc, BigDecimal bankTransferAmount,
      MemberContext mc, Member m) {
    m.setPaymentInfo(mc.toString());
    m.setPaymentDate(mc.getPaymentDate());

    /*
     * We have 2 membership fees. Bank Tranfer amount Direct Debit amount which has a small discount
     */


    /*
     * Check/set whether at least the DirectDebit-amount is payed.
     * We don't check the payment method. 
     * We accept that a Bank Transfer member sometimes just
     * pays the Direct Debit amount.
     */
    boolean payed = mc.getTotalAmount().compareTo(IncassoProperties.getIncassoBedrag()) >= 0;
    m.setCurrentYearPaid(payed);

    if (mc.isInactief() && payed) {
      String msg = "Betaling ontvangen voor inactief lid " + m.getMemberNumber();
      rc.getMessages().add(msg);
    } else if (mc.getTotalAmount().compareTo(bankTransferAmount) > 0) {
      // The total amount is larger than the Bank Transfer amount; the member payed too much
      String amountString = AmountFormatter.format(mc.getTotalAmount());
      String msg =
          String.format("Lid %d heeft totaal %s betaald", m.getMemberNumber(), amountString);
      rc.getMessages().add(msg);
    }
  }

  private String getPaymentInfo(IngBooking booking) {
    if (booking.isStornering()) {
      return booking.getStornoReden().getReden();
    }
    return booking.getOmschrijving();
  }

}
