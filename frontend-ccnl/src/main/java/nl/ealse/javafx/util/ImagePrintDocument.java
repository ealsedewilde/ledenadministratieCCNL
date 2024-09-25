package nl.ealse.javafx.util;

import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class ImagePrintDocument implements PrintDocument {
  
  private static final Type type = Type.IMAGE;
  
  private final BufferedImage image;
  
  
  public ImagePrintDocument(Image image) {
    this.image = SwingFXUtils.fromFXImage(image, null);
  }

  @Override
  public BufferedImage getDocument() {
    return image;
  }

  @Override
  public Type getType() {
    return type;
  }

}
