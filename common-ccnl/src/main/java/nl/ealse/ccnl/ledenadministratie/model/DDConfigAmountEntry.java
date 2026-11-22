package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.Data;

@Embeddable
@Data
public class DDConfigAmountEntry {

  private BigDecimal value;

  private String description;

}