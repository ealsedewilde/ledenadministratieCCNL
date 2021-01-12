package nl.ealse.ccnl.ledenadministratie.dd;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Helper voor XML datums.
 * 
 * @author Ealse
 *
 */
@Slf4j
@UtilityClass
public class DateUtil {

  /**
   * Datum zonder tijd in XML formaat
   * 
   * @param date
   * @return
   */
  public XMLGregorianCalendar toXMLDate(LocalDate date) {
    ZonedDateTime zdt = ZonedDateTime.of(date.atStartOfDay(), ZoneId.systemDefault());
    GregorianCalendar gc = GregorianCalendar.from(zdt);
    try {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
    } catch (DatatypeConfigurationException e) {
      log.error("Fout bij aanmaken XML datum", e);
      throw new IncassoRuntimeException();
    }
  }

  /**
   * Datum + tijd in XML formaat.
   * 
   * @param date
   * @return
   */
  public static XMLGregorianCalendar toXMLDateTime(LocalDateTime date) {
    ZonedDateTime zdt = ZonedDateTime.of(date, ZoneId.systemDefault());
    GregorianCalendar gc = GregorianCalendar.from(zdt);
    try {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
    } catch (DatatypeConfigurationException e) {
      log.error("Fout bij aanmaken XML datum", e);
      throw new IncassoRuntimeException();
    }
  }

}
