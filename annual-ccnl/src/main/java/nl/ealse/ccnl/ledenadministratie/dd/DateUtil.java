package nl.ealse.ccnl.ledenadministratie.dd;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Helper for XML-dates.
 *
 * @author Ealse
 *
 */
@Slf4j
@UtilityClass
public class DateUtil {

  /**
   * Convert Date without time to XML-format.
   *
   * @param date - date to convert
   * @return - XML-data
   */
  public XMLGregorianCalendar toXMLDate(LocalDate date) {
    try {
      return DatatypeFactory.newInstance().newXMLGregorianCalendarDate(date.getYear(),
          date.getMonthValue(), date.getDayOfMonth(), DatatypeConstants.FIELD_UNDEFINED);
    } catch (DatatypeConfigurationException e) {
      log.error("Error converting to XML date", e);
      throw new IncassoRuntimeException();
    }
  }

  /**
   * Date + Time to XML format.
   *
   * @param date - date to convert
   * @return - XML-data
   */
  public static XMLGregorianCalendar toXMLDateTime(LocalDateTime date) {
    ZonedDateTime zdt = ZonedDateTime.of(date, ZoneId.systemDefault());
    GregorianCalendar gc = GregorianCalendar.from(zdt);
    try {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
    } catch (DatatypeConfigurationException e) {
      log.error("Error converting to XML date", e);
      throw new IncassoRuntimeException();
    }
  }

}
