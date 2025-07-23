package nl.ealse.ccnl;

import javafx.application.Application;

/**
 * Main entrypoint of the application.
 */
public class LedenadministratieApplication {

  /**
   * Start the single instance of this application.
   *
   * @param args - no args needed
   */
  public static void main(String[] args) {
    StartContext.performUniqueCheck();
    StartContext.loadEventRegistry();
    
    Application.launch(JavaFxApplication.class, args);
  }

}
