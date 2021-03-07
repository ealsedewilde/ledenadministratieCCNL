package nl.ealse.javafx;

import static org.mockito.Mockito.mock;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

class FXMLNodeMapTest {

  private static ApplicationContext springContext;
  private static FXMLNodeMap sut;

  @Test
  void getPage() {
     try {
      Parent p = sut.get(new PageId("LOGO", "logo"));
      Assertions.assertTrue(p instanceof VBox);
    } catch (FXMLMissingException e) {
      e.printStackTrace();
    }
  }

  @Test
  void pageNotFound() {
    Assertions.assertThrows(FXMLMissingException.class,
        () -> sut.get(new PageId("DUMMY", "dummy")));
  }

  @Test
  void pageInError() {
    PageId id = new PageId("EMPTY", "empty");
    Assertions.assertThrows(FXMLLoadException.class, () -> sut.get(id));
  }
  
  @BeforeAll
  static void setup() {
    new JFXPanel();
    springContext = mock(ApplicationContext.class);
    sut = new FXMLNodeMap(springContext);
    fxmlDirectory();
  }
  
  @AfterAll
  static void cleanup() {
    Platform.exit();
  }
  
  private static void fxmlDirectory() {
    try {
      FieldUtils.writeField(sut, "fxmlDirectory", "fxml/", true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
