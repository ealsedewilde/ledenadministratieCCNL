package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class DDConfigStringEntry {

  private String value;

  private String description;

}