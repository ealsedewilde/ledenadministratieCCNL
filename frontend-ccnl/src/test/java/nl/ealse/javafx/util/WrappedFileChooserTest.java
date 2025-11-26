package nl.ealse.javafx.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import nl.ealse.ccnl.test.FXBase;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WrappedFileChooserTest  extends FXBase {

  @Test
  void performTests() {
    Assertions.assertTrue(runFX(() -> {
      FileChooserException ex = assertThrows(FileChooserException.class, () -> missingDirectoryTest());
      assertEquals("Geen bestandslocatie ingesteld", ex.getMessage());
      ex = assertThrows(FileChooserException.class, () ->invalidDirectoryTest());
      assertEquals("Bestandslocatie instelling C:/? is ongeldig", ex.getMessage());
    }));

  }
  
  private void missingDirectoryTest() {
    WrappedFileChooser sut = new WrappedFileChooser(FileExtension.PDF);
    sut.setInitialDirectory(() -> null);
    sut.showOpenDialog();
  }
  
  private void invalidDirectoryTest() {
    WrappedFileChooser sut = new WrappedFileChooser(FileExtension.PDF);
    sut.setInitialDirectory(() -> "C:/?");
    sut.showOpenDialog();
  }

}
