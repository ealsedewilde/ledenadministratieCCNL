package nl.ealse.javafx.mapping;

import nl.ealse.ccnl.control.member.MemberController;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExplainTest {
  
  @Test
  void testExplain() {
    String ex = ViewModel.explain(MemberController.class, Member.class);
    System.out.println(ex);
    Assertions.assertTrue(ex.contains("[INFO] Property"));
  }


}
