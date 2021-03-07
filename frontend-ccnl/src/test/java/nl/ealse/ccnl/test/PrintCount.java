package nl.ealse.ccnl.test;

import lombok.Getter;
import lombok.Setter;

public class PrintCount {

  @Getter
  @Setter
  private static int count;

  public static void increment() {
    count++;
  }

  public static void reset() {
    count = 0;
  }

}
