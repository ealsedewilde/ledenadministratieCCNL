package nl.ealse.javafx.util;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.Optional;
import javax.print.PrintService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

@Slf4j
public class DefaultPrintServiceProvider implements PrintServiceProvider {

  /**
   * Print a document.
   * <p>
   * The printer is selected via a native printer selection dialog.
   * </p>
   * @param pdf - the document to print
   * @return - the printer used for printing
   * @throws PrintException - when printing fails
   */
  @Override
  public Optional<PrintService> print(byte[] pdf) throws PrintException {
    try (PDDocument document = PDDocument.load(pdf)) {
      PrinterJob job = PrinterJob.getPrinterJob();
      job.setPageable(new PDFPageable(document));
      if (job.printDialog()) {
        job.print();
        return Optional.of(job.getPrintService());
      }
      return Optional.empty();
    } catch (PrinterException | IOException e) {
      log.error("Print error", e);
      throw new PrintException(e);
    }
  }

  /**
   * Print an attachment after the related document.
   * @param ps - printer to use
   * @param attachment - the document to print
   * @throws PrintException  - when printing fails
   */
  @Override
  public void printAttachment(PrintService ps, byte[] attachment) throws PrintException {
    try (PDDocument document = PDDocument.load(attachment)) {
      PrinterJob job = PrinterJob.getPrinterJob();
      job.setPageable(new PDFPageable(document));
      job.setPrintService(ps);
      job.print();
    } catch (PrinterException | IOException e) {
      log.error("Print error", e);
      throw new PrintException(e);
    }

  }

}
