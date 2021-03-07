package nl.ealse.ccnl.service;

import static org.mockito.Mockito.verify;
import nl.ealse.ccnl.ledenadministratie.annual.AnnualRollover;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnnualRolloverServiceTest {
  
  @Mock
  private AnnualRollover rollover;
  
  @InjectMocks
  private AnnualRolloverService sut;
  
  @Test
  void testRollover() {
    sut.annualRollover();
    verify(rollover).rollover();
  }

}
