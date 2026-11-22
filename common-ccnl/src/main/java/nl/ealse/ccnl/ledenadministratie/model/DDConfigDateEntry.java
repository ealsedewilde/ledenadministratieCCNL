package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import lombok.Data;

@Embeddable
@Data
public class DDConfigDateEntry {

  private LocalDate value;

  private String description;

}