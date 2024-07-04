package nl.ealse.ccnl.service;

import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.annual.AnnualRollover;

/**
 * Perform the rollover to the next memebership year.
 */
@Slf4j
public class AnnualRolloverService {

  private final AnnualRollover rollover;

  public AnnualRolloverService(AnnualRollover rollover) {
    log.info("Service created");
    this.rollover = rollover;
  }

  public void annualRollover() {
    rollover.rollover();
  }

}
