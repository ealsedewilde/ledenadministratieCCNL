package nl.ealse.ccnl.ledenadministratie.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DateUtilTest {

  @Test
  void toDateTest() {
    LocalDate d = LocalDate.of(2020, 12, 01);
    Date date = DateUtil.toDate(d);
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    String s = df.format(date);
    Assertions.assertEquals("20201201", s);
  }

  @Test
  void toLocaleDateTest() {
    Calendar c = Calendar.getInstance();
    c.set(2020, 11, 01);
    LocalDate date = DateUtil.toLocaleDate(c.getTime());
    Assertions.assertEquals("2020-12-01", date.toString());
  }

}
