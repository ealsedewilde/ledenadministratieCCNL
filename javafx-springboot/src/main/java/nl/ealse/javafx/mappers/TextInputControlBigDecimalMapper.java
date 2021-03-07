package nl.ealse.javafx.mappers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javafx.scene.control.TextInputControl;
import nl.ealse.javafx.mapping.MappingException;

public class TextInputControlBigDecimalMapper
    implements PropertyMapper<TextInputControl, BigDecimal> {

  private final Locale locale;

  public TextInputControlBigDecimalMapper() {
    this(Locale.getDefault());
  }

  public TextInputControlBigDecimalMapper(Locale locale) {
    this.locale = locale;
  }

  @Override
  public BigDecimal getPropertyFromJavaFx(TextInputControl javaFx) {
    String input = javaFx.getText();
    if (!input.isBlank()) {
      try {
        return new BigDecimal(getFormatter().parse(input).toString());
      } catch (ParseException e) {
        throw new MappingException("Invalid decimal input " + input);
      }
    }
    return null;
  }

  @Override
  public void mapPropertyToJavaFx(BigDecimal modelProperty, TextInputControl javaFx) {
    javaFx.setText(getFormatter().format(modelProperty));
  }

  private DecimalFormat getFormatter() {
    DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(locale);
    df.applyPattern("###,##0.00");
    return df;
  }

}
