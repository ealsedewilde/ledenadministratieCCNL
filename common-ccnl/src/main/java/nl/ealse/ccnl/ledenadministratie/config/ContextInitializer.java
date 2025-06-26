package nl.ealse.ccnl.ledenadministratie.config;

import java.util.Properties;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;

public interface ContextInitializer {
  
  EntityManagerProvider getEntityManagerProvider();
  
  Properties getPreferences();
  
  Properties getProperties();
  
  <T> T getComponent(Class<T> clazz);
  
  void reloadPreferences();
  
  void start();

}
