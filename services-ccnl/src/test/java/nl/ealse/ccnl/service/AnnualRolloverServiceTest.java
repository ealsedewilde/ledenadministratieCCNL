package nl.ealse.ccnl.service;

import static org.mockito.Mockito.verify;
import nl.ealse.ccnl.ledenadministratie.annual.AnnualRollover;
import nl.ealse.ccnl.test.MockProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnnualRolloverServiceTest {
  
  private AnnualRollover rollover;
  
  private AnnualRolloverService sut;
  
  @Test
  void testRollover() {
    rollover = MockProvider.mock(AnnualRollover.class);
    sut = AnnualRolloverService.getInstance();
    sut.annualRollover();
    verify(rollover).rollover();
  }

}
