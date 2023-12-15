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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoProperties;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;

@Getter
@Slf4j
public class ReconciliationContext {

  private final Map<Integer, MemberContext> contexts = new HashMap<>();
  private final List<String> messages = new ArrayList<>();

  private ReconciliationContext(List<Member> members, boolean includeDD) {
    Transaction t = new Transaction(IncassoProperties.getIncassoBedrag(),
        IncassoProperties.getIncassoDatum(), BookingType.RDDT,
        IncassoProperties.getIncassoReden());
    members.forEach(m -> {
      MemberContext mc = new MemberContext(m.getMemberNumber());
      if (m.getPaymentMethod() == PaymentMethod.DIRECT_DEBIT && includeDD) {
        if (m.getPaymentDate() == null) {
          log.debug("Er is geen incasso uitgevoerd voor lid " + m.getMemberNumber());
        } else if (m.getPaymentDate().equals(IncassoProperties.getIncassoDatum())) {
          mc.setDirectDebit(IncassoProperties.getIncassoDatum());
          mc.getTransactions().add(t);
        }
      }
      contexts.put(m.getMemberNumber(), mc);
    });
  }

  public static ReconciliationContext newInstance(List<Member> members, boolean includeDD) {
    return new ReconciliationContext(members, includeDD);
  }

  public MemberContext getMemberContext(Integer lidnummer) {
    MemberContext mc = contexts.get(lidnummer); // NOSONAR
    if (mc == null) {
      mc = new MemberContext(lidnummer, true);
      contexts.put(lidnummer, mc);
    }
    return mc;
  }

  @Getter
  public static class MemberContext {

    private final int number;
    private final Set<Transaction> transactions = new TreeSet<>();
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
      for (Transaction t : transactions) {
        total = total.add(t.getAmount());
      }
      return total;
    }

    public LocalDate getPaymentDate() {
      if (directDebit != null) {
        return directDebit;
      }
      LocalDate pd = null;
      for (Transaction t : transactions) {
        if (pd == null || t.getTransactionDate().isAfter(pd)) {
          pd = t.getTransactionDate();
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
      for (Transaction t : transactions) {
        sj.add(String.format("%s: %s", t.getBookingType().getOmschrijving(), t.getDescription()));
      }
      return sj.toString();
    }

  }

  @Data
  public static class Transaction implements Comparable<Transaction> {
    private static final BookingType[] BTM =
        {BookingType.RDDT, BookingType.IDDT, BookingType.RCDT, BookingType.RDDT, BookingType.IRCT};
    private final BigDecimal amount;
    private final LocalDate transactionDate;
    private final BookingType bookingType;
    private final String description;

    @Override
    public int compareTo(Transaction o) {
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
