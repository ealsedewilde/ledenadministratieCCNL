package nl.ealse.javafx;

import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import nl.ealse.ccnl.test.FXBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FXMLLoaderUtilTest extends FXBase {
  
  @Test
  void testFxmlLoading() {
    AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      getPage();
      pageNotFound();
      pageInError();
      
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }


  //@Test
  void getPage() {
    Parent p = FXMLLoaderUtil.getPage("logo");
    Assertions.assertTrue(p instanceof VBox);
  }

  //@Test
  void pageNotFound() {
    Parent p = FXMLLoaderUtil.getPage("dummy");
    Assertions.assertTrue(p instanceof Label);
  }

  //@Test
  void pageInError() {
    Assertions.assertThrows(FXMLLoadException.class, () -> FXMLLoaderUtil.getPage("empty"));
  }


}
