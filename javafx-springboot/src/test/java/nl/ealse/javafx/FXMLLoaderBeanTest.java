package nl.ealse.javafx;

import static org.mockito.Mockito.mock;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

class FXMLLoaderBeanTest {

  private static ApplicationContext springContext;

  @Test
  void getPage() {
    Parent p = FXMLLoaderBean.getPage("logo");
    Assertions.assertTrue(p instanceof VBox);
  }

  @Test
  void pageNotFound() {
    Parent p = FXMLLoaderBean.getPage("dummy");
    Assertions.assertTrue(p instanceof Label);
  }

  @Test
  void pageInError() {
    Assertions.assertThrows(FXMLLoadException.class, () -> FXMLLoaderBean.getPage("empty"));
  }
  
  @BeforeAll
  static void setup() {
    new JFXPanel();
    springContext = mock(ApplicationContext.class);
    fxmlDirectory(new FXMLLoaderBean(springContext));
  }
  
  @AfterAll
  static void cleanup() {
    Platform.exit();
  }
  
  private static void fxmlDirectory(FXMLLoaderBean flb) {
    try {
      FieldUtils.writeField(flb, "fxmlDirectory", "fxml/", true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
