package nl.ealse.ccnl.ledenadministratie.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Data;

@Entity
@Data
public class PaymentFile {

  @Id
  private String fileName;

  @Lob
  private String xml;

}
