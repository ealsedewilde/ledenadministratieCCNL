package nl.ealse.ccnl.control.document;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.model.Document;

/**
 * Format a document type in a TableColumn.
 * 
 * @author ealse
 *
 */
@Slf4j
public class DocumentTypePropertyValueFactory
    implements Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>> {

  @Override
  public ObservableValue<String> call(CellDataFeatures<Document, String> param) {
    try {
      Document document = param.getValue();
      String description = document.getDescription();
      if (description == null) {
        description = document.getDocumentType().getDescription();
      }
      return new SimpleStringProperty(description);
    } catch (Exception e) {
      log.warn("Error retrieving property", e);
    }
    return null;
  }

}
