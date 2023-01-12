package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Entity
@Data
public class PaymentFile {

  @Id
  private String fileName;

  @Lob
  private String xml;

}
