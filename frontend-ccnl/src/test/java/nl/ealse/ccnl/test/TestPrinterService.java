package nl.ealse.ccnl.test;

import java.util.Optional;
import javax.print.PrintService;
import nl.ealse.javafx.print.PrinterService;
import nl.ealse.javafx.print.PrintDocument;
import nl.ealse.javafx.print.PrintException;
import org.mockito.Mockito;

public class TestPrinterService extends PrinterService {

  @Override
  public Optional<PrintService> print(PrintDocument pdf) throws PrintException {
    PrintCount.increment();
    PrintService ps = Mockito.mock(PrintService.class);
    return Optional.of(ps);
  }

  @Override
  public void printAttachment(PrintService ps, byte[] attachment) throws PrintException {
    PrintCount.increment();
  }

}
