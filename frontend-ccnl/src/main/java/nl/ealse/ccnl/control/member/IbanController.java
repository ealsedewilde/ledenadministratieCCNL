package nl.ealse.ccnl.control.member;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import org.apache.commons.validator.routines.IBANValidator;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class IbanController {

  private final SepaAuthorizarionController parentController;

  @FXML
  private Label memberInfo;

  @FXML
  private TextField ibanNumber;

  @FXML
  private Label ibanNumberE;

  private final IBANValidator ibanValidator = new IBANValidator();

  @FXML
  public void initialize() {
    ibanNumber.textProperty().addListener(new IbanNumberListener(ibanNumberE));
    parentController.getIbanNumberStage().setOnCloseRequest(e -> {
      e.consume();
      save();
    });
  }

  @FXML
  public void save() {
    String iban = ibanNumber.getText().toUpperCase();
    if (ibanValidator.isValid(iban)) {
      ibanNumberE.setVisible(false);
      selectedMember.setIbanNumber(iban);
      parentController.getIbanNumberStage().close();
      parentController.selectSepaAuthorization();
    } else if (iban == null || iban.isBlank()) {
      ibanNumberE.setText("IBAN-nummer is verplicht");
      ibanNumberE.setVisible(true);
    } else {
      ibanNumberE.setText("IBAN-nummer onjuist");
      ibanNumberE.setVisible(true);
    }
  }

  private Member selectedMember;

  public IbanController(SepaAuthorizarionController parentController) {
    this.parentController = parentController;

  }

  @EventListener(condition = "#event.name('PAYMENT_AUTHORIZATION')")
  public void onApplicationEvent(MemberSeLectionEvent event) {
    this.selectedMember = event.getSelectedEntity();
    memberInfo.setText(String.format("Lid %d (%s)", selectedMember.getMemberNumber(),
        selectedMember.getFullName()));
    ibanNumberE.setText(null);
    ibanNumberE.setVisible(false);
  }

  private static class IbanNumberListener implements ChangeListener<String> {

    private final Label ibanNumberE;

    private IbanNumberListener(Label ibanNumberE) {
      this.ibanNumberE = ibanNumberE;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue,
        String newValue) {
      ibanNumberE.setVisible(true);
    }

  }

}
