package nl.ealse.ccnl;

import javafx.application.Application;

public class LedenadministratieApplication {

  public static void main(String[] args) {
    UniqueCheck check = new UniqueCheck();
    if (check.uniqueProcess()) {
      Application.launch(JavaFxApplication.class, args);
    } else {
      System.exit(0);
    }

  }

}
