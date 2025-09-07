package nl.ealse.ccnl.ledenadministratie.util;

import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.dao.MemberRepository;

/**
 * Generate a random new member number from the list with free numbers.
 *
 * @author ealse
 *
 */
public class MemberNumberFactory extends NumberFactory {
  
  private boolean initialized;

  public MemberNumberFactory() {
    super(Integer.valueOf(ApplicationContext.getProperty("ccnl.members")), 0);
  }
  
  @Override
  public Integer getNewNumber() {
    if (!initialized) {
      MemberRepository dao = ApplicationContext.getComponent(MemberRepository.class);
      super.initialize(dao.getAllMemberNumbers());
      initialized = true;
    }
    return super.getNewNumber();
  }

}
