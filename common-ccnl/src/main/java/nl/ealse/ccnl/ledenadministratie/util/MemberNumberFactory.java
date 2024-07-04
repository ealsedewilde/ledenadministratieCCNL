package nl.ealse.ccnl.ledenadministratie.util;

import nl.ealse.ccnl.ledenadministratie.config.ApplicationProperties;
import nl.ealse.ccnl.ledenadministratie.dao.MemberRepository;

/**
 * Generate a random new member number from the list with free numbers.
 *
 * @author ealse
 *
 */
public class MemberNumberFactory extends NumberFactory {

  private static final String CCNL_MEMBERS =
      ApplicationProperties.getProperty("ccnl.members");

  public MemberNumberFactory(MemberRepository dao) {
    super(Integer.valueOf(CCNL_MEMBERS), 0);
    initialize(dao);
  }

  private void initialize(MemberRepository dao) {
    super.initialize(dao.getAllMemberNumbers());
  }

}
