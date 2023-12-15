package nl.ealse.ccnl.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.annual.AnnualRollover;

/**
 * Perform the rollover to the next memebership year.
 */
@Slf4j
public class AnnualRolloverService {
  
  @Getter
  private static AnnualRolloverService instance = new AnnualRolloverService();

  private final AnnualRollover rollover;

  private AnnualRolloverService() {
    log.info("Service created");
    this.rollover = AnnualRollover.getInstance();
  }

  public void annualRollover() {
    rollover.rollover();
  }

}
