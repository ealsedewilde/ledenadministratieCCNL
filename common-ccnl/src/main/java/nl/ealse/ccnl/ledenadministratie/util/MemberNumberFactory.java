package nl.ealse.ccnl.ledenadministratie.util;

import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Generate a random new member number from the list with free numbers.
 * 
 * @author ealse
 *
 */
@Component
public class MemberNumberFactory extends NumberFactory {

  public MemberNumberFactory(MemberRepository dao, @Value("${ccnl.members}") String members) {
    super(Integer.valueOf(members), 0);
    initialize(dao);
  }

  private void initialize(MemberRepository dao) {
    super.initialize(dao.getAllMemberNumbers());
  }

}
