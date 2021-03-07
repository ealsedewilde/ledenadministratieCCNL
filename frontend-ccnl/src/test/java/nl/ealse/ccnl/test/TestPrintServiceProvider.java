package nl.ealse.ccnl.test;

import java.util.Optional;
import javax.print.PrintService;
import nl.ealse.javafx.util.PrintException;
import nl.ealse.javafx.util.PrintServiceProvider;
import org.mockito.Mockito;

public class TestPrintServiceProvider implements PrintServiceProvider {

  @Override
  public Optional<PrintService> print(byte[] pdf) throws PrintException {
    PrintCount.increment();
    PrintService ps = Mockito.mock(PrintService.class);
    return Optional.of(ps);
  }

  @Override
  public void printAttachment(PrintService ps, byte[] attachment) throws PrintException {
    PrintCount.increment();
  }

}
