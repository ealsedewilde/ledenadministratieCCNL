package nl.ealse.ccnl.ledenadministratie.model;

import java.util.StringJoiner;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.Data;

@Entity
@Data
public class Setting {

  @Id
  private String id;

  @Column(name = "settings_key", nullable = false)
  private String key;

  @Column(name = "settings_group", nullable = false)
  private String settingsGroup;

  @Column(name = "settings_value", nullable = false)
  private String value;

  private String description;

  @PrePersist
  @PreUpdate
  public void prePersist() {
    if (settingsGroup == null) {
      id = key;
    } else {
      StringJoiner sj = new StringJoiner(".");
      sj.add(settingsGroup).add(key);
      id = sj.toString();
    }
  }

}
