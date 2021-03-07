package nl.ealse.ccnl.ledenadministratie.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AmountFormatter {
  
  private static DecimalFormat nf;
  private static DecimalFormat df;

  
  static {
    nf = (DecimalFormat) NumberFormat.getCurrencyInstance();
    nf.getDecimalFormatSymbols().setDecimalSeparator(',');
    nf.getDecimalFormatSymbols().setCurrencySymbol("€");
    df = new DecimalFormat("###.00");
    df.getDecimalFormatSymbols().setDecimalSeparator(',');
  }
  
  public String format(double d) {
    return nf.format(d);
  }
  
  public String format(BigDecimal amount) {
    return nf.format(amount.doubleValue());
  }
  
  public double parse(String amount) throws ParseException {
      return df.parse(amount).doubleValue();
  }


}
