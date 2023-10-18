package nl.ealse.ccnl.ledenadministratie.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Properties from the database. These properties should be loaded before the ApplicationContext
 * gets initialized. When no properties exist, the SETTINGS table is initialized from the init.sql
 * file on the classpath.
 * 
 * @author ealse
 *
 */
@Slf4j
public class DatabasePropertySource extends PropertySource<Properties> {
  private static final String PROP_DS_URL = "spring.datasource.url";

  public DatabasePropertySource(ConfigurableEnvironment environment) {
    super("databaseProperties", new Properties());
    initialize(environment);
  }

  @Override
  public Object getProperty(String name) {
    return getSource().getProperty(name);
  }

  /**
   * Create a DataSource with properties from the 'application.yml' file on the class path. Use this
   * DataSource to load properties from the SETTINGS database table.
   */
  private void initialize(final ConfigurableEnvironment environment) {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.execute(() -> {
      initializeDbLocation();
      String userName = environment.getProperty("spring.datasource.username");
      String url = environment.getProperty(PROP_DS_URL);
      String driverClassName = environment.getProperty("spring.datasource.driverClassName");
      DataSource ds = DataSourceBuilder.create().username(userName).url(url)
          .driverClassName(driverClassName).build();
      try {
        load(ds);
      } catch (SQLException e) {
        log.warn("Error loading properties from the database");
        initSql(ds);
      }
    });
  }

  /**
   * Is the default "spring.datasource.url" overridden? If so put the new URL is this
   * PropertySource.
   */
  private void initializeDbLocation() {
    DatabaseLocation dbl = new DatabaseLocation();
    Optional<String> dbUrl = dbl.getDataBaseUrl();
    if (dbUrl.isPresent()) {
      log.warn(PROP_DS_URL + dbUrl.get());
      getSource().put(PROP_DS_URL, dbUrl.get());
    }
  }

  /**
   * Load the properties from the SETTINGS table.
   * 
   * @param ds
   * @throws SQLException
   */
  private void load(DataSource ds) throws SQLException {
    try (Connection connection = ds.getConnection()) {
      try (PreparedStatement preparedStatement =
          connection.prepareStatement("SELECT * FROM SETTING")) {
        try (ResultSet rs = preparedStatement.executeQuery()) {
          while (rs.next()) {
            getSource().put(rs.getString("id"), rs.getString("settings_value"));
          }
        }
      }
    }
  }

  /**
   * Initialize the SETTINGS table from the init.sql file.
   * 
   * @param ds
   * @throws SQLException
   */
  private void initSql(DataSource ds) {
    try (Connection connection = ds.getConnection()) {
      executeInitSql(connection);
      load(ds);
    } catch (SQLException e) {
      log.error("Unable to start: Error initializing datasource properties", e);
      throw new DatabasePropertyException("Error initializing properties from the database", e);
    }
  }

  /**
   * Execute the statements in the 'init.sql' file.
   * 
   * @param c
   * @throws SQLException
   */
  private void executeInitSql(Connection c) throws SQLException {
    Resource initSql = new ClassPathResource("init.sql");
    Statement st = null;
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(initSql.getInputStream()))) {
      String line = reader.readLine();
      StringJoiner sj = new StringJoiner(" ");
      c.setAutoCommit(false);
      st = c.createStatement();
      while (line != null) {
        line = line.trim();
        sj.add(line);
        if (line.endsWith(";")) {
          st.execute(sj.toString());
          sj = new StringJoiner(" ");
        }
        line = reader.readLine();
      }
      c.commit();
    } catch (IOException e) {
      c.rollback();
      log.error("Unable to start: Error reading init.sql", e);
      throw new DatabasePropertyException("Error reading init.sql", e);
    } finally {
      if (st != null) {
        st.close();
      }
    }
  }


}
