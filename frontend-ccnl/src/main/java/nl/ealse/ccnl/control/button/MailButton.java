package nl.ealse.ccnl.control.button;

public class MailButton extends ImageButton {

  public MailButton(String text) {
    super("email.png", text);
  }

  public MailButton() {
    super("email.png", "Mail verzenden");
  }

}
