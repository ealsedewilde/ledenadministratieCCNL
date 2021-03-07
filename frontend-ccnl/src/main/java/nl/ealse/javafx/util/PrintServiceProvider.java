package nl.ealse.javafx.util;

import java.util.Optional;
import javax.print.PrintService;

public interface PrintServiceProvider {
  
  Optional<PrintService> print(byte[] pdf) throws PrintException;
  
  void printAttachment(PrintService ps, byte[] attachment) throws PrintException;

}
