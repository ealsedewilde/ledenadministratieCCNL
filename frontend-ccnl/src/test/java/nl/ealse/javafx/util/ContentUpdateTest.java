package nl.ealse.javafx.util;

import javafx.scene.control.TextField;
import nl.ealse.ccnl.test.FXBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ContentUpdateTest extends FXBase {

  @Test
  void performTests() {
    Assertions.assertTrue(runFX(() -> {
      firstCapitalTest();
      formatInitialsTest();
      formatPostalCodeTest();
    }));

  }

  private void firstCapitalTest() {
    TextField tf = new TextField();
    tf.setText("wilde");
    ContentUpdate.firstCapital(tf);
    Assertions.assertEquals("Wilde", tf.getText());
  }

  private void formatInitialsTest() {
    TextField tf = new TextField();
    tf.setText("erw");
    ContentUpdate.formatInitials(tf);
    Assertions.assertEquals("E.R.W.", tf.getText());
  }

  private void formatPostalCodeTest() {
    TextField tf = new TextField();
    tf.setText("1234aa");
    ContentUpdate.formatPostalCode(tf);
    Assertions.assertEquals("1234 AA", tf.getText());
  }
}

