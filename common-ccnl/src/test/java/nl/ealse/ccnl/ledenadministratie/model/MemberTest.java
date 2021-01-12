package nl.ealse.ccnl.ledenadministratie.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberTest {

  @Test
  void fullNameTest() {
    Member m = new Member();
    m.setInitials("E.R.W.");
    m.setLastNamePrefix("de");
    m.setLastName("Wilde");
    Assertions.assertEquals("E.R.W. de Wilde", m.getFullName());
  }

  @Test
  void initialsTest() {
    Member m = new Member();
    m.setInitials("Ealse");
    Assertions.assertTrue(m.hasFirstName());
  }

}
