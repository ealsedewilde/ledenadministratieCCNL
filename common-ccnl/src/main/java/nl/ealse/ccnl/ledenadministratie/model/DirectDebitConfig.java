package nl.ealse.ccnl.ledenadministratie.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity

public class DirectDebitConfig {

  @Getter(value = AccessLevel.NONE)
  @Setter(value = AccessLevel.NONE)
  @Id
  private int id = 1;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "IBAN_NUMBER_VALUE"))
  @AttributeOverride(name = "description", column = @Column(name = "IBAN_NUMBER_DESCRIPTION"))
  private DDConfigStringEntry ibanNumber;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "DD_ID_VALUE"))
  @AttributeOverride(name = "description", column = @Column(name = "DD_ID_DESCRIPTION"))
  private DDConfigStringEntry directDebitId;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "DD_AMOUNT_VALUE"))
  @AttributeOverride(name = "description", column = @Column(name = "DD_AMOUNT_DESCRIPTION"))
  private DDConfigAmountEntry directDebitAmount;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "DD_DATE_VALUE"))
  @AttributeOverride(name = "description", column = @Column(name = "DD_DATE_DESCRIPTION"))
  private DDConfigDateEntry directDebitDate;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "DD_REASON_VALUE"))
  @AttributeOverride(name = "description", column = @Column(name = "DD_REASON_DESCRIPTION"))
  private DDConfigStringEntry directDebitDescription;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "AUTH_VALUE"))
  @AttributeOverride(name = "description", column = @Column(name = "AUTH_DESCRIPTION"))
  private DDConfigStringEntry authorization;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "AUTH_TYPE_VALUE"))
  @AttributeOverride(name = "description", column = @Column(name = "AUTH_TYPE_DESCRIPTION"))
  private DDConfigStringEntry authorizationType;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "MSSG_ID_VALUE"))
  @AttributeOverride(name = "description", column = @Column(name = "MSG_ID_DESCRIPTION"))
  private DDConfigStringEntry messageId;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "IBAN_NAME_VALUE"))
  @AttributeOverride(name = "description", column = @Column(name = "IBAN_NAME_DESCRIPTION"))
  private DDConfigStringEntry clubName;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "TESTRUN_VALUE"))
  @AttributeOverride(name = "description", column = @Column(name = "TESTRUN_DESCRIPTION"))
  private DDConfigBooleanEntry testRun;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "DD_DIR_VALUE"))
  @AttributeOverride(name = "description", column = @Column(name = "DD_DIR_DESCRIPTION"))
  private DDConfigStringEntry directDebitDir;

  @Embeddable
  @Data
  public static class DDConfigStringEntry {

    String value;

    String description;

  }

  @Embeddable
  @Data
  public static class DDConfigDateEntry {

    private LocalDate value;

    private String description;

  }

  @Embeddable
  @Data
  public static class DDConfigBooleanEntry {

    private boolean value;

    private String description;

  }

  @Embeddable
  @Data
  public static class DDConfigAmountEntry {

    private BigDecimal value;

    private String description;

  }

}
