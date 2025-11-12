package nl.ealse.ccnl.control.internal;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import nl.ealse.ccnl.control.SearchField;
import nl.ealse.ccnl.control.SearchTextField;

public class SearchChoiceBox extends ChoiceBox<String> implements SearchField {

  private static final List<String> VALUES = new ArrayList<>();

  static {
    for (RelationNumberValue rnv : RelationNumberValue.values()) {
      VALUES.add(rnv.getLabel());
    }
  }

  public SearchChoiceBox(SearchTextField field) {
    getItems().addAll(VALUES);
    setOnKeyPressed(field.getOnKeyPressed());
    setMaxWidth(field.getMaxWidth());
  }

  @Override
  public String getSearchText() {
    return getValue();
  }

  @Override
  public void reset() {
    getSelectionModel().selectFirst();
  }

  @Override
  public Control getAsControl() {
    return this;
  }

}
