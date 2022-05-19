package nl.ealse.ccnl.ledenadministratie.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

/**
 * Check if we need to override the "spring.datasource.url". Invoke this class before the
 * application is started.
 * 
 * @author ealse
 */
@SuppressWarnings("serial")
public class DatabaseLocation extends Properties {

  private static final File DB_LOCATION_FILE = new File("db.properties");

  private String databaseUrl;

  public DatabaseLocation() {
    initialize();
  }

  public Optional<String> getDataBaseUrl() {
    return Optional.ofNullable(databaseUrl);
  }

  private void initialize() {
    if (DB_LOCATION_FILE.exists()) {
      Properties dbProperties = new Properties();
      try (InputStream is = new FileInputStream(DB_LOCATION_FILE)) {
        dbProperties.load(is);
        String dbLocation = dbProperties.getProperty("db.locatie");
        if (dbLocation != null) {
          if (dbLocation.startsWith("jdbc:h2:mem")) {
            databaseUrl = dbLocation;
          } else if (new File(dbLocation).isAbsolute()) {
            databaseUrl = "jdbc:h2:" + dbLocation;
          } else if (dbLocation.startsWith("/")) {
            databaseUrl = "jdbc:h2:.." + dbLocation;
          } else {
            databaseUrl = "jdbc:h2:../" + dbLocation;
          }
        }
      } catch (IOException e) {
        throw new DatabasePropertyException("Error initializing database location", e);
      }
    }

  }

  @Override
  public synchronized boolean equals(Object o) {
    if (o instanceof DatabaseLocation) {
      return super.equals(o);
    }
    return false;
  }

  @Override
  public synchronized int hashCode() {
    return super.hashCode();
  }
  
}
