package nl.ealse.ccnl.control.magazine;

import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Setter;
import nl.ealse.ccnl.control.button.ImageCell;

public class ImageCellPropertyValueFactory extends 
PropertyValueFactory<InvalidAddressData, ImageCell<InvalidAddressData>>{
  
  @Setter
  private String property = "reset";

  public ImageCellPropertyValueFactory() {
    super("reset");
  }

 
}
