package nl.ealse.ccnl.ledenadministratie.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;
import java.util.Properties;

/**
 * Determine the file location of the database.
 *
 * @author ealse
 */
public class DatabaseLocation {

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
        databaseUrl = initialize(dbLocation);
      } catch (IOException e) {
        throw new ConfigurationException("Error initializing database location", e);
      }
    }

  }

  public static String initialize(String dbLocation) {
    if (dbLocation != null) {
      if (dbLocation.startsWith("jdbc:h2:mem")) {
        return dbLocation;
      } else if (new File(dbLocation).isAbsolute()) {
        return "jdbc:h2:" + dbLocation;
      } else if (dbLocation.startsWith("/")) {
        return "jdbc:h2:.." + dbLocation;
      } else {
        return "jdbc:h2:../" + dbLocation;
      }
    }
    return null;
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
  
}
