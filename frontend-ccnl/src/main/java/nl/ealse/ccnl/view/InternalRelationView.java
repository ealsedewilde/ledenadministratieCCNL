package nl.ealse.ccnl.view;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;
import nl.ealse.javafx.mapping.Mapping;

@Getter
public abstract class InternalRelationView extends AddressView {

  @FXML
  private ChoiceBox<String> title;
  @FXML
  @Mapping(ignore = true)
  private Label titleE;

  @FXML
  private TextField contactName;
  @FXML
  @Mapping(ignore = true)
  private Label contactNameE;

  @FXML
  private TextField telephoneNumber;
  
  @FXML
  private CheckBox noMagazine;

}
