package nl.ealse.ccnl.ledenadministratie.config;

import java.util.Properties;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;

public interface ContextInitializer {
  
  EntityManagerProvider getEntityManagerProvider();
  
  Properties getPreferences();
  
  Properties getProperties();
  
  DirectDebitConfig getIncassoProperties();
  
  <T> T getComponent(Class<T> clazz);
  
  void loadPreferences();
  
  void start();

}
