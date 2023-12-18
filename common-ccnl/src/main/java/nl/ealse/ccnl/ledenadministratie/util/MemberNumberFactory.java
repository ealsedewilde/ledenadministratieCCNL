package nl.ealse.ccnl.ledenadministratie.util;

import lombok.Getter;
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
 
  @Getter
  private static MemberNumberFactory instance = new MemberNumberFactory();
  
  private final MemberRepository dao = MemberRepository.getInstance();

  private MemberNumberFactory() {
    super(Integer.valueOf(CCNL_MEMBERS), 0);
    initialize();
  }

  private void initialize() {
    super.initialize(dao.getAllMemberNumbers());
  }

}
