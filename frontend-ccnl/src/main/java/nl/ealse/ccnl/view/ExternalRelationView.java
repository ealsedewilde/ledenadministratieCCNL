package nl.ealse.ccnl.view;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Getter;
import nl.ealse.javafx.mapping.Mapping;
import nl.ealse.javafx.util.ContentUpdate;

@Getter
public class ExternalRelationView extends AddressView {

  @FXML
  private Label relationNumber;

  @FXML
  private TextField relationName;
  @FXML
  @Mapping(ignore = true)
  private Label relationNameE;

  @FXML
  private TextField contactName;
  @FXML
  @Mapping(ignore = true)
  private Label contactNameE;

  @FXML
  private TextField contactNamePrefix;

  @FXML
  @Getter
  private TextField email;
  @FXML
  @Mapping(ignore = true)
  private Label emailE;

  @FXML
  private TextField telephoneNumber;

  @FXML
  private TextArea relationInfo;

  @FXML
  private DatePicker relationSince;

  @FXML
  private DatePicker modificationDate;

  protected void enrich() {
    ContentUpdate.firstCapital(relationName);
  }


}
