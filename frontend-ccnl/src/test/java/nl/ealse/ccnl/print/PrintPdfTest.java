package nl.ealse.ccnl.print;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.io.InputStream;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Werkende PDF print.
 * 
 * @author ealse
 *
 */
class PrintPdfTest {

  private static final String PRINTER_NAME = "Brother DCP-L3510CDW series Printer";

  @Disabled("Output goes to real printer")
  @Test
  void pdfPrint() {
    try (InputStream pdfStream = getClass().getResourceAsStream("/MachtigingsformulierSEPA.pdf")) {
      PDDocument document = PDDocument.load(pdfStream);
      PrintService ps = getprintService();
      PrinterJob job = PrinterJob.getPrinterJob();
      job.setPageable(new PDFPageable(document));
      job.setPrintService(ps);
      job.print();
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    } catch (PrinterException e) {
      Assertions.fail(e.getMessage());
    }

  }

  private PrintService getprintService() {
    DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;
    PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, null);
    for (PrintService printService : printServices) {
      if (printService.getName().trim().equals(PRINTER_NAME)) {
        return printService;
      }
    }
    throw new RuntimeException("Printer niet gevonden");
  }

}
