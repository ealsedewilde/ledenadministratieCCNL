package nl.ealse.javafx.util;

public class PdfPrintDocument implements PrintDocument {
  
  private static final Type type = Type.PDF;
  
  private final byte[] pdf;
  
  
  public PdfPrintDocument(byte[] pdf) {
    this.pdf = pdf;
  }

  @Override
  public byte[] getDocument() {
    return pdf;
  }

  @Override
  public Type getType() {
    return type;
  }

}
