package nl.ealse.ccnl.ledenadministratie.h2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import org.h2.jdbcx.JdbcConnectionPool;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.Stoppable;

@SuppressWarnings("serial")
public class JdbcConnectionPoolProvider implements ConnectionProvider, Configurable, Stoppable {

  private JdbcConnectionPool cp;

  @Override
  public boolean isUnwrappableAs(Class<?> unwrapType) {
    return ConnectionProvider.class.equals(unwrapType)
        || JdbcConnectionPoolProvider.class.isAssignableFrom(unwrapType);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T unwrap(Class<T> unwrapType) {
    if (ConnectionProvider.class.equals(unwrapType)
        || JdbcConnectionPoolProvider.class.isAssignableFrom(unwrapType)) {
      return (T) this;
    } else {
      throw new UnknownUnwrapTypeException(unwrapType);
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
    String jdbcUrl = (String) configuration.get(JdbcSettings.JAKARTA_JDBC_URL);
    String user = (String) configuration.get(JdbcSettings.JAKARTA_JDBC_USER);
    String password = (String) configuration.get(JdbcSettings.JAKARTA_JDBC_PASSWORD);
    cp = JdbcConnectionPool.create(jdbcUrl, user, password);
  }

  @Override
  public void stop() {
    cp.dispose();

  }
}
