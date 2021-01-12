package nl.ealse.ccnl.ledenadministratie.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtil {

  public static final ZoneId ZI = ZoneId.systemDefault();

  public static final Date toDate(LocalDate date) {
    if (date != null) {
      return Date.from(date.atStartOfDay(ZI).toInstant());
    }
    return null;
  }

  public static LocalDate toLocaleDate(Date date) {
    if (date != null) {
      return Instant.ofEpochMilli(date.getTime()).atZone(ZI).toLocalDate();
    }
    return null;
  }

}
