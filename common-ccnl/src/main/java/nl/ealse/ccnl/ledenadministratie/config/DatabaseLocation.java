package nl.ealse.ccnl.ledenadministratie.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
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

  public static final File DB_LOCATION_FILE = new File("db.properties");

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
      try (StringReader reader = new StringReader(readDbLocation())) {
        dbProperties.load(reader);
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
  
  private String readDbLocation() throws IOException {
    StringBuilder sb = new StringBuilder();
    char[] buffer = new char[2048];
    try (FileReader reader = new FileReader(DB_LOCATION_FILE)) {
      int result = reader.read(buffer);
      while (result != -1) {
        sb.append(buffer, 0, result);
        result = reader.read(buffer);
      }
      return sb.toString().replace(File.separatorChar, '/'); 
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
