package nl.ealse.ccnl.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.annual.AnnualRollover;

/**
 * Perform the rollover to the next memebership year.
 */
@Slf4j
@AllArgsConstructor
public class AnnualRolloverService {
  {log.info("Service created");}

  private final AnnualRollover rollover;

  public void annualRollover() {
    rollover.rollover();
  }

}
