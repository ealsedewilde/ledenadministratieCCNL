package nl.ealse.ccnl.control.button;

public class SaveButton extends ImageButton {

  public SaveButton(String text) {
    super("save.png", text);
  }

  public SaveButton() {
    super("save.png", "Opslaan");
  }

}
