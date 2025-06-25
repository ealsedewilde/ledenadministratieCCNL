package nl.ealse.javafx.util;

import javafx.scene.control.TextField;
import lombok.experimental.UtilityClass;

/**
 * Helper for formatting user input before saving.
 * 
 * @author ealse
 *
 */
@UtilityClass
public class ContentUpdate {

  /**
   * Assure that the text begins with a capital symbol.
   * 
   * @param textField - to format
   */
  public void firstCapital(TextField textField) {
    String string = textField.getText();
    if (string.length() > 1) {
      textField.setText(string.substring(0, 1).toUpperCase() + string.substring(1));
    } else {
      textField.setText(string.toUpperCase());
    }
  }

  /**
   * Place dots between each letter.
   * 
   * @param initials - to format
   */
  public void formatInitials(TextField initials) {
    StringBuilder sb = new StringBuilder();
    String text = initials.getText();
    for (char ch : text.toCharArray()) {
      if (Character.isLetter(ch)) {
        sb.append(ch);
        sb.append('.');
      }
    }
    initials.setText(sb.toString().toUpperCase());
  }

  /**
   * Format a Dutch postalcode.
   * 
   * @param postalCode - to format
   */
  public void formatPostalCode(TextField postalCode) {
    if (postalCode.getText().length() != 7) {
      String pc = postalCode.getText();
      String s1 = pc.substring(0, 4);
      String s2 = pc.substring(pc.length() - 2);
      postalCode.setText(s1 + " " + s2.toUpperCase());
    }
  }


}
