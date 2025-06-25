package nl.ealse.ccnl.view;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import lombok.Getter;
import nl.ealse.ccnl.mappers.MembershipStatusMapper;
import nl.ealse.javafx.mapping.Mapping;

/**
 * View for handling the cancelation of a membership.
 * 
 * @author ealse
 *
 */
@Getter
public class MemberCancelView {

  @FXML
  private TextField memberNumber;

  @FXML
  private TextField initials;

  @FXML
  private TextField lastNamePrefix;

  @FXML
  private TextField lastName;

  @FXML
  private TextField email;

  @FXML
  private TextField telephoneNumber;

  @FXML
  @Mapping(propertyMapper = MembershipStatusMapper.class)
  private ChoiceBox<String> memberStatus;

  @FXML
  private TextField memberSince;

  /**
   * Initialize the view before the page is shown.
   */
  public void initializeView() {
    if (memberStatus != null) {
      for (String status : MembershipStatusMapper.getStatuses()) {
        if (!MembershipStatusMapper.ACTIVE.equals(status)) {
          memberStatus.getItems().add(status);
        }
      }
    }
  }

}
