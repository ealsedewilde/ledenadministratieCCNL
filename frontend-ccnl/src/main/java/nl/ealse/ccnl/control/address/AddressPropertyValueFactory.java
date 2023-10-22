package nl.ealse.ccnl.control.address;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.AddressOwner;

/**
 * Adress properties in a TableView.
 *
 * @param <T>
 */
public class AddressPropertyValueFactory<T extends AddressOwner>
    implements Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>> {

  @Getter
  @Setter
  private String property;

  @Override
  public ObservableValue<String> call(CellDataFeatures<T, String> param) {
    Address address = param.getValue().getAddress();

    if ("city".equals(property)) {
      return new SimpleStringProperty(address.getCity());

    } else if ("streetAndNumber".equals(property)) {
      return new SimpleStringProperty(address.getStreetAndNumber());
    }
    return new SimpleStringProperty(address.getPostalCode());
  }

}
