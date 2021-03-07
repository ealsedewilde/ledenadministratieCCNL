package nl.ealse.ccnl.control.button;

public class MailButton extends ImageButton {

  public MailButton(String text) {
    super("EMAIL.png", text);
  }

  public MailButton() {
    super("EMAIL.png", "Mail verzenden");
  }

}
