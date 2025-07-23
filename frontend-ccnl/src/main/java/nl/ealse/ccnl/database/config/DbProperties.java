package nl.ealse.ccnl.database.config;

import java.io.FileWriter;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseLocation;

@Slf4j
public final class DbProperties {
  private static final String PREFIX = "db.locatie = ";
  private static final String COMMENT = """
      # locatie (pad) en naam van database
      ####################################
      # Bijvoorbeeld: db.locatie = pad/dbName"
      #
      # Het pad mag een volledig pad zijn of een relatief pad."
      # Bij een relatief pad: relatief t.o.v. de Map waarin deze applicatie is geplaatst"
      # Let op: de database mag NIET in de Map van de applicatie worden geplaatst!
      """;

  private DbProperties() {}

  static String writeFile(String dbLocation) {
    try {
      FileWriter writer = new FileWriter(DatabaseLocation.DB_LOCATION_FILE);
      writer.write(COMMENT, 0, COMMENT.length());
      String prop = PREFIX + dbLocation;
      writer.write(prop, 0, prop.length());
      writer.close();
      return "Database configuratie is opgeslagen";
    } catch (IOException e) {
      log.error("Error writing file", e);
      return "Kan configuratie niet opslaan";
    }
  }

}
