package nl.ealse.ccnl.ledenadministratie.h2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.h2.jdbcx.JdbcConnectionPool;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.connections.internal.DatabaseConnectionInfoImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.DatabaseConnectionInfo;
import org.hibernate.engine.jdbc.env.spi.ExtractedDatabaseMetaData;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.Stoppable;

/**
 * Wrapper around {@link org.h2.jdbcx.JdbcConnectionPool}.
 */

@SuppressWarnings("serial")
public class JdbcConnectionPoolProvider implements ConnectionProvider, Configurable, Stoppable {

  private transient JdbcConnectionPool cp;
  
  private Map<String, Object> configuration;

  @Override
  public boolean isUnwrappableAs(Class<?> unwrapType) {
    try {
      return cp.isWrapperFor(unwrapType);
    } catch (SQLException e) {
      throw new UnknownUnwrapTypeException(unwrapType, e);
    }
  }

  @Override
  public <T> T unwrap(Class<T> unwrapType) {
    try {
      return cp.unwrap(unwrapType);
    } catch (SQLException e) {
      throw new UnknownUnwrapTypeException(unwrapType, e);
    }
  }

  @Override
  public boolean supportsAggressiveRelease() {
    return false;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return cp.getConnection();
  }

  @Override
  public void closeConnection(Connection conn) throws SQLException {
    conn.close();
  }

  @Override
  public void configure(Map<String, Object> configuration) {
    this.configuration = configuration;
    String jdbcUrl = (String) configuration.get(JdbcSettings.JAKARTA_JDBC_URL);
    String user = (String) configuration.get(JdbcSettings.JAKARTA_JDBC_USER);
    String password = (String) configuration.get(JdbcSettings.JAKARTA_JDBC_PASSWORD);
    cp = JdbcConnectionPool.create(jdbcUrl, user, password);
  }

  @Override
  public DatabaseConnectionInfo getDatabaseConnectionInfo(Dialect dialect) {
    return new DatabaseConnectionInfoImpl(configuration, dialect);
  }

  @Override
  public DatabaseConnectionInfo getDatabaseConnectionInfo(Dialect dialect,
      ExtractedDatabaseMetaData metaData) {
    Map<String, Object> settings = new HashMap<>();
    settings.put(JdbcSettings.JAKARTA_JDBC_DRIVER, metaData.getDriver());
    settings.put(JdbcSettings.JAKARTA_JDBC_URL, metaData.getUrl());
    String tl = metaData.getTransactionIsolation() == Connection.TRANSACTION_READ_COMMITTED
        ? "TRANSACTION_READ_COMMITTED"
        : null;
    settings.put(JdbcSettings.ISOLATION, tl);

    try {
      Connection conn = getConnection();
      settings.put(JdbcSettings.AUTOCOMMIT, conn.getAutoCommit());
      conn.close();
    } catch (SQLException e) {
      // No action required
    }
    settings.put(JdbcSettings.POOL_SIZE, cp.getMaxConnections());
    return new DatabaseConnectionInfoImpl(settings, dialect);
  }

  @Override
  public void stop() {
    cp.dispose();

  }
}
