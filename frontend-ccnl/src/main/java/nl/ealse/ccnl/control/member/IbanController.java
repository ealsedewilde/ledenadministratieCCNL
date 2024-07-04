package nl.ealse.ccnl.control.member;

import java.util.function.Supplier;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nl.ealse.ccnl.MainStage;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.javafx.FXMLLoaderUtil;
import org.apache.commons.validator.routines.IBANValidator;

/**
 * Controls the popup for adding a missing IBAN-number.
 */
public class IbanController {

  @FXML
  private Label memberInfo;

  @FXML
  private TextField ibanNumber;

  @FXML
  private Label ibanNumberE;

  private final IBANValidator ibanValidator = new IBANValidator();

  private Member selectedMember;

  private Stage ibanNumberStage;
  
  public IbanController() {
    setup();
  }
  
  private Supplier<Void> nextAction;

  private void setup() {
    ibanNumberStage = new Stage();
    ibanNumberStage.initModality(Modality.APPLICATION_MODAL);
    ibanNumberStage.setTitle("IBAN-nummer toevoegen");
    ibanNumberStage.getIcons().add(MainStage.getIcon());
    ibanNumberStage.initOwner(MainStage.getStage());
    Parent p = FXMLLoaderUtil.getPage("dialog/addIban", this);
    Scene dialogScene = new Scene(p, 1200, 400);
    ibanNumberStage.setScene(dialogScene);
  }

  @FXML
  void initialize() {
    ibanNumber.textProperty().addListener(new IbanNumberListener(ibanNumberE));
  }

  @FXML
  void save() {
    String iban = ibanNumber.getText().toUpperCase();
    if (ibanValidator.isValid(iban)) {
      ibanNumberE.setVisible(false);
      ibanNumberStage.close();
      selectedMember.setIbanNumber(iban);
      nextAction.get();
    } else if (iban == null || iban.isBlank()) {
      ibanNumberE.setText("IBAN-nummer is verplicht");
      ibanNumberE.setVisible(true);
    } else {
      ibanNumberE.setText("IBAN-nummer onjuist");
      ibanNumberE.setVisible(true);
    }
  }


  @EventListener
  public void onApplicationEvent(AddIbanNumberEvent event) {
    this.nextAction = event.getNextAction();
    this.selectedMember = event.getMember();
    memberInfo.setText(String.format("Lid %d (%s)", selectedMember.getMemberNumber(),
        selectedMember.getFullName()));
    ibanNumber.setText("");
    ibanNumberE.setText(null);
    ibanNumberE.setVisible(false);
    ibanNumberStage.show();
  }

  private static class IbanNumberListener implements ChangeListener<String> {

    private final Label ibanNumberE;

    private IbanNumberListener(Label ibanNumberE) {
      this.ibanNumberE = ibanNumberE;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue,
        String newValue) {
      ibanNumberE.setVisible(false);
    }

  }

}
