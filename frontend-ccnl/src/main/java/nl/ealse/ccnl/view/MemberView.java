package nl.ealse.ccnl.view;

import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import lombok.Getter;
import lombok.Setter;
import nl.ealse.ccnl.control.button.SearchButton;
import nl.ealse.ccnl.mappers.MembershipStatusMapper;
import nl.ealse.ccnl.mappers.PaymentMethodMapper;
import nl.ealse.javafx.mapping.Mapping;
import nl.ealse.javafx.util.ContentUpdate;

@Getter
@Setter
public abstract class MemberView extends AddressView {

  @FXML
  private Label memberNumber;

  @FXML
  private TextField initials;
  @FXML
  @Mapping(ignore = true)
  private ToggleGroup rbGroup;
  @FXML
  @Mapping(ignore = true)
  private Label initialsE;

  @FXML
  private TextField lastNamePrefix;

  @FXML
  private TextField lastName;

  @FXML
  @Mapping(ignore = true)
  private Label lastNameE;

  @FXML
  private TextField email;
  @FXML
  @Mapping(ignore = true)
  private Label emailE;

  @FXML
  private TextField telephoneNumber;

  @FXML
  @Mapping(propertyMapper = MembershipStatusMapper.class)
  private ChoiceBox<String> memberStatus;

  @FXML
  private TextField ibanNumber;

  @FXML
  @Mapping(ignore = true)
  private Label ibanOwnerNameL;

  @FXML
  @Mapping(ignore = true)
  private TextField ibanOwnerName;

  @FXML
  @Mapping(ignore = true)
  private Label ibanNumberE;

  @FXML
  @Mapping(ignore = true)
  private Label bicCodeL;

  @FXML
  private TextField bicCode;

  @FXML
  @Mapping(propertyMapper = PaymentMethodMapper.class)
  private ChoiceBox<String> paymentMethod;

  @FXML
  private CheckBox currentYearPaid;

  @FXML
  @Mapping(ignore = true)
  private Label amountPaidL;

  @FXML
  private TextField amountPaid;

  @FXML
  @Mapping(ignore = true)
  private Label amountPaidE;

  @FXML
  private DatePicker paymentDate;

  @FXML
  private CheckBox membercardIssued;

  @FXML
  private TextArea paymentInfo;

  @FXML
  @Mapping(ignore = true)
  private Label sepaLabel;

  @FXML
  @Mapping(ignore = true)
  private SearchButton sepaButton;

  @FXML
  private CheckBox noMagazine;

  @FXML
  private TextArea memberInfo;

  @FXML
  private DatePicker memberSince;

  protected void initializeView() {
    if (memberStatus != null) {
      memberStatus.setItems(MembershipStatusMapper.getStatuses());
    }
    if (paymentMethod != null) {
      paymentMethod.setItems(PaymentMethodMapper.getValues());
      paymentMethod.setValue(PaymentMethodMapper.BANK_TRANSFER);
    }
  }

  /**
   * Perform some formatting before processing the form data.
   */
  protected void enrich() {
    formatName();
    LocalDate d = memberSince.getValue();
    if (d == null) {
      memberSince.setValue(LocalDate.now());
    }
  }

  public void formatName() {
    String id = ((RadioButton) rbGroup.getSelectedToggle()).getId();
    if ("voorletters".equals(id)) {
      ContentUpdate.formatInitials(initials);
    } else {
      ContentUpdate.firstCapital(initials);
    }
    ContentUpdate.firstCapital(lastName);
  }

}
