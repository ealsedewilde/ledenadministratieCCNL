package nl.ealse.ccnl.test;

import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;

public class ApplicationContextAware {
  
  static {
    ApplicationContext.start();
  }

}
