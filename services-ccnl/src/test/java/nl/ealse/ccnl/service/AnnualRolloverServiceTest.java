package nl.ealse.ccnl.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import nl.ealse.ccnl.ledenadministratie.annual.AnnualRollover;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnnualRolloverServiceTest {
  
  private AnnualRollover rollover;
  
  private AnnualRolloverService sut;
  
  @Test
  void testRollover() {
    rollover = mock(AnnualRollover.class);
    sut = new AnnualRolloverService(rollover);
    sut.annualRollover();
    verify(rollover).rollover();
  }

}
