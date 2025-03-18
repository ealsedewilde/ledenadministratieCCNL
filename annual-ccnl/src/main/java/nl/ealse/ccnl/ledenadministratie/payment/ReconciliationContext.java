package nl.ealse.ccnl.ledenadministratie.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoProperties;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;

/**
 * Context with the complete payment history of every member.
 */
@Getter
@Slf4j
public class ReconciliationContext {

  private static final BankTransaction DD_TRANSACTION =
      new BankTransaction(IncassoProperties.getIncassoBedrag(), IncassoProperties.getIncassoDatum(),
          BookingType.RDDT, IncassoProperties.getIncassoReden());

  private final Map<Integer, MemberContext> contexts = new HashMap<>();
  private final List<String> messages = new ArrayList<>();

  /**
   * Construct the context with the payment history before this recinciliation is executed.
   * 
   * @param members
   * @param referenceDate - start date of the first reconciliation file
   * @param includeDD - assume that the Direct Debet process is executed.
   */
  private ReconciliationContext(List<Member> members, LocalDate referenceDate, boolean includeDD) {
    // Add bank transactions previous to of the reconciliation execution.

    members.forEach(m -> {
      MemberContext mc = new MemberContext(m.getMemberNumber());
      if (m.getPaymentMethod() == PaymentMethod.DIRECT_DEBIT && includeDD) {
        if (m.isDirectDebitExecuted()) {
          mc.setDirectDebit(IncassoProperties.getIncassoDatum());
          mc.getBankTransactions().add(DD_TRANSACTION);
        } else {
          log.debug("No Direct Debet for member " + m.getMemberNumber());
        }
      } else if (m.getPaymentMethod() == PaymentMethod.BANK_TRANSFER && m.getAmountPaid().doubleValue() > 0d
          && referenceDate.isAfter(m.getPaymentDate())) {
        log.debug("Bank Tranfer Payment before reference date " + m.getMemberNumber());
        BankTransaction btt = new BankTransaction(m.getAmountPaid(), m.getPaymentDate(), BookingType.RCDT,
            BookingType.RCDT.getOmschrijving());
        mc.getBankTransactions().add(btt);
      }
      contexts.put(m.getMemberNumber(), mc);
    });
  }

  public static ReconciliationContext newInstance(List<Member> members, LocalDate referenceDate,
      boolean includeDD) {
    return new ReconciliationContext(members, referenceDate, includeDD);
  }

  public MemberContext getMemberContext(Integer lidnummer) {
    MemberContext mc = contexts.get(lidnummer); // NOSONAR
    if (mc == null) {
      mc = new MemberContext(lidnummer, true);
      contexts.put(lidnummer, mc);
    }
    return mc;
  }

  /**
   * Context with complete payment history of one member.
   */
  @Getter
  public static class MemberContext {

    private final int number;
    private final Set<BankTransaction> bankTransactions = new TreeSet<>();
    private final boolean inactief;
    @Setter
    private LocalDate directDebit;

    private MemberContext(int number) {
      this.number = number;
      this.inactief = false;
    }

    private MemberContext(int number, boolean inactief) {
      this.number = number;
      this.inactief = inactief;
    }

    public BigDecimal getTotalAmount() {
      BigDecimal total = BigDecimal.ZERO;
      for (BankTransaction t : bankTransactions) {
        total = total.add(t.amount());
      }
      return total;
    }

    public LocalDate getPaymentDate() {
      if (directDebit != null) {
        return directDebit;
      }
      LocalDate pd = null;
      for (BankTransaction t : bankTransactions) {
        if (pd == null || t.transactionDate().isAfter(pd)) {
          pd = t.transactionDate();
        }
      }
      return pd;
    }

    @Override
    public int hashCode() {
      return number;
    }

    @Override
    public boolean equals(Object obj) {
      MemberContext mc = MemberContext.class.cast(obj);
      return this.number == mc.number;
    }

    @Override
    public String toString() {
      StringJoiner sj = new StringJoiner("\n");
      for (BankTransaction t : bankTransactions) {
        sj.add(String.format("%s: %s", t.bookingType().getOmschrijving(), t.description()));
      }
      return sj.toString();
    }

  }

  /**
   * Data regarding one payment for a specific member.
   */
  public static record BankTransaction(BigDecimal amount, LocalDate transactionDate, BookingType bookingType, String description) implements Comparable<BankTransaction> {
    private static final BookingType[] BTM =
        {BookingType.RDDT, BookingType.IDDT, BookingType.RCDT, BookingType.RDDT, BookingType.IRCT};

    @Override
    public int compareTo(BankTransaction o) {
      int r = this.transactionDate.compareTo(o.transactionDate);
      if (r == 0) {
        int bt1 = 0;
        int bt2 = 0;
        while (bt1 < BTM.length && BTM[bt1] != this.bookingType) {
          bt1++;
        }
        while (bt2 < BTM.length && BTM[bt2] != o.bookingType) {
          bt2++;
        }
        return bt1 - bt2;
      }
      return r;
    }
  }

}
