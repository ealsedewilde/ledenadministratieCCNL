package nl.ealse.ccnl.ledenadministratie.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Generate a random new member number from the list with free numbers.
 *
 * @author ealse
 *
 */
public class NumberFactory {

  private final List<Integer> freeNumbers = new ArrayList<>();

  private final int boundary;

  private final int base;

  private final SecureRandom random;

  public NumberFactory(int boundary, int base) {
    this.boundary = boundary;
    this.base = base;
    this.random = new SecureRandom();
  }

  public Integer getNewNumber() {
    double d = random.nextDouble() * freeNumbers.size();
    int randomInt = (int) d;
    Integer freeNumber = freeNumbers.get(randomInt);
    freeNumbers.remove(freeNumber);
    return freeNumber;
  }

  protected void initialize(List<Number> numbers) {
    for (int ix = 1; ix <= boundary; ix++) {
      freeNumbers.add(Integer.valueOf(ix + base));
    }
    freeNumbers.removeAll(numbers);
  }

}
