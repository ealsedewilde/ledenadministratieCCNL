package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class DDConfigBooleanEntry {

  private boolean value;

  private String description;

}