package nl.ealse.ccnl.ledenadministratie.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AmountFormatter {
  
  private static DecimalFormat nf;
  
  static {
    nf = (DecimalFormat) NumberFormat.getCurrencyInstance();
    nf.getDecimalFormatSymbols().setDecimalSeparator(',');
    nf.getDecimalFormatSymbols().setCurrencySymbol("€");
  }
  
  public String format(double d) {
    return nf.format(d);
  }
  
  public String format(BigDecimal amount) {
    return nf.format(amount.doubleValue());
  }
  
  /**
   * The String amount from the database seems sometimes have
   * a problem with the decimal separator when using a DecimalFormat.
   * @param amount
   * @return
   */
  public double parse(String amount) throws NumberFormatException {
    StringBuilder sb = new StringBuilder();
    for (char c : amount.toCharArray()) {
      if (Character.isDigit(c)) {
        sb.append(c);
        continue;
      }
      if (c == ',' || c == '.') {
        sb.append('.');
      }
    }
     return Double.parseDouble(sb.toString());
  }


}
