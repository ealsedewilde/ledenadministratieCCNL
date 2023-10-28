package nl.ealse.javafx.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.Optional;
import javax.print.PrintService;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.javafx.util.PrintDocument.Type;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

@Slf4j
public class DefaultPrintServiceProvider implements PrintServiceProvider {

  private static final String ERROR = "Print error";

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
  @Override
  public Optional<PrintService> print(PrintDocument document) throws PrintException {
    if (document.getType() == Type.PDF) {
      return printPdf((PdfPrintDocument) document);
    }
    return printImage((ImagePrintDocument) document);
  }

  private Optional<PrintService> printPdf(PdfPrintDocument document) throws PrintException {
    try (PDDocument pdDocument = Loader.loadPDF(document.getDocument())) {
      PrinterJob job = PrinterJob.getPrinterJob();
      job.setPageable(new PDFPageable(pdDocument));
      return doPrint(job);
    } catch (IOException e) {
      log.error(ERROR, e);
      throw new PrintException(e);
    }
  }

  private Optional<PrintService> printImage(ImagePrintDocument document) throws PrintException {
    PrinterJob job = PrinterJob.getPrinterJob();
    job.setPrintable(new ImagePrintable(document.getDocument()));
    return doPrint(job);
  }

  private Optional<PrintService> doPrint(PrinterJob job) throws PrintException {
    try {
      if (job.printDialog()) {
        job.print();
        return Optional.of(job.getPrintService());
      }
      return Optional.empty();
    } catch (PrinterException e) {
      log.error(ERROR, e);
      throw new PrintException(e);
    }
  }

  /**
   * Print an attachment after the related document.
   *
   * @param ps - printer to use
   * @param attachment - the document to print
   * @throws PrintException - when printing fails
   */
  @Override
  public void printAttachment(PrintService ps, byte[] attachment) throws PrintException {
    try (PDDocument document = Loader.loadPDF(attachment)) {
      PrinterJob job = PrinterJob.getPrinterJob();
      job.setPageable(new PDFPageable(document));
      job.setPrintService(ps);
      job.print();
    } catch (PrinterException | IOException e) {
      log.error(ERROR, e);
      throw new PrintException(e);
    }

  }

  private static class ImagePrintable implements Printable {

    private final BufferedImage image;

    private ImagePrintable(BufferedImage image) {
      this.image = image;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
        throws PrinterException {
      // Check the pageIndex to avoid endless printing.
      if (pageIndex == 0) {
        double widthRatio = pageFormat.getImageableWidth() / image.getWidth();
        double heightRatio = pageFormat.getImageableHeight() / image.getHeight();
        double ratio = Math.min(widthRatio, heightRatio);
        int pageWidth = (int) (image.getWidth() * ratio);
        int pageHeight = (int) (image.getHeight() * ratio);
        graphics.drawImage(image, (int) pageFormat.getImageableX(),
            (int) pageFormat.getImageableY(), pageWidth, pageHeight, null);
        return Printable.PAGE_EXISTS;
      }
      return Printable.NO_SUCH_PAGE;
    }

  }

}
