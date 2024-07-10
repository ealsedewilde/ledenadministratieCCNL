package nl.ealse.javafx.util;

import java.util.Optional;
import java.util.ServiceLoader;
import javax.print.PrintService;
import lombok.experimental.UtilityClass;

/**
 * Utiltiy for printing.
 *
 * @author ealse
 *
 */
@UtilityClass
public class PrintUtil {

  private static final ServiceLoader<PrintServiceProvider> printServiceLoader =
      ServiceLoader.load(PrintServiceProvider.class);

  private static final PrintServiceProvider printServiceProvider;

  static {
    Optional<PrintServiceProvider> first = printServiceLoader.findFirst();
    if (first.isPresent()) {
      printServiceProvider = first.get();
    } else {
      printServiceProvider = new DefaultPrintServiceProvider();
    }
  }

  /**
   * Print a document.
   * <p>
   * The printer is selected via a native printer selection dialog.
   * </p>
   *
   * @param document - the document to print
   * @return - the printer used for printing
   * @throws PrintException - when printing fails
   */
  public Optional<PrintService> print(PrintDocument document) throws PrintException {
    return printServiceProvider.print(document);
  }

  /**
   * Print an attachment after the related document.
   *
   * @param ps - printer to use
   * @param attachment - the document to print
   * @throws PrintException - when printing fails
   */
  public void printAttachment(PrintService ps, byte[] attachment) throws PrintException {
    printServiceProvider.printAttachment(ps, attachment);
  }

}
