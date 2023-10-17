package nl.ealse.ccnl.control.member;

import jakarta.annotation.PostConstruct;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.javafx.FXMLLoaderBean;
import nl.ealse.javafx.ImagesMap;
import org.apache.commons.validator.routines.IBANValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

/**
 * Controls the popup for adding a missing IBAN-number.
 */
@Controller
public class IbanController {

  private final ApplicationEventPublisher eventPublisher;

  private final PageController pageController;

  @FXML
  private Label memberInfo;

  @FXML
  private TextField ibanNumber;

  @FXML
  private Label ibanNumberE;

  private final IBANValidator ibanValidator = new IBANValidator();

  private Member selectedMember;

  private Stage ibanNumberStage;

  public IbanController(ApplicationEventPublisher eventPublisher, PageController pageController) {
    this.eventPublisher = eventPublisher;
    this.pageController = pageController;
  }

  @PostConstruct
  void setup() {
    ibanNumberStage = new Stage();
    ibanNumberStage.initModality(Modality.APPLICATION_MODAL);
    ibanNumberStage.setTitle("IBAN-nummer toevoegen");
    ibanNumberStage.getIcons().add(ImagesMap.get("Citroen.png"));
    ibanNumberStage.initOwner(pageController.getPrimaryStage());
    Parent p = FXMLLoaderBean.getPage("dialog/addIban", this);
    Scene dialogScene = new Scene(p, 1200, 400);
    ibanNumberStage.setScene(dialogScene);
  }

  @FXML
  void initialize() {
    ibanNumber.textProperty().addListener(new IbanNumberListener(ibanNumberE));
  }

  public void show() {
    ibanNumberStage.show();
  }

  @FXML
  void save() {
    String iban = ibanNumber.getText().toUpperCase();
    if (ibanValidator.isValid(iban)) {
      ibanNumberE.setVisible(false);
      ibanNumberStage.close();
      selectedMember.setIbanNumber(iban);
      eventPublisher.publishEvent(new IbanNumberAddedEvent(this));
    } else if (iban == null || iban.isBlank()) {
      ibanNumberE.setText("IBAN-nummer is verplicht");
      ibanNumberE.setVisible(true);
    } else {
      ibanNumberE.setText("IBAN-nummer onjuist");
      ibanNumberE.setVisible(true);
    }
  }


  @EventListener(condition = "#event.name('PAYMENT_AUTHORIZATION')")
  public void onApplicationEvent(MemberSeLectionEvent event) {
    this.selectedMember = event.getSelectedEntity();
    memberInfo.setText(String.format("Lid %d (%s)", selectedMember.getMemberNumber(),
        selectedMember.getFullName()));
    ibanNumber.setText("");
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
      ibanNumberE.setVisible(false);
    }

  }

}
