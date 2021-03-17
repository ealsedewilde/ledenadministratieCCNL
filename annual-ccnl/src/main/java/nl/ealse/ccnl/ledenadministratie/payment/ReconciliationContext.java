package nl.ealse.ccnl.ledenadministratie.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import lombok.Data;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoProperties;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;

@Getter
public class ReconciliationContext {

  private final Map<Integer, MemberContext> contexts = new HashMap<>();
  private final List<String> messages = new ArrayList<>();

  private ReconciliationContext(List<Member> members, IncassoProperties incassoProperties,
      boolean includeDD) {
    Transaction t = new Transaction(incassoProperties.getIncassoBedrag(),
        incassoProperties.getIncassoDatum(), incassoProperties.getIncassoReden());
    members.forEach(m -> {
      MemberContext mc = new MemberContext(m.getMemberNumber());
      if (m.getPaymentMethod() == PaymentMethod.DIRECT_DEBIT && includeDD) {
        if (m.getPaymentDate() == null) {
          messages.add("Er is geen incasso uitgevoerd voor lid " + m.getMemberNumber());
        } else if (m.getPaymentDate().equals(incassoProperties.getIncassoDatum())) {
          mc.getTransactions().add(t);
        }
      }
      contexts.put(m.getMemberNumber(), mc);
    });
  }

  public static ReconciliationContext newInstance(List<Member> members,
      IncassoProperties incassoProperties, boolean includeDD) {
    return new ReconciliationContext(members, incassoProperties, includeDD);
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
    private final Set<Transaction> transactions = new HashSet<>();
    private final boolean inactief;

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
        sj.add(t.getDescription());
      }
      return sj.toString();
    }

  }

  @Data
  public static class Transaction {
    private final BigDecimal amount;
    private final LocalDate transactionDate;
    private final String description;
  }

}
