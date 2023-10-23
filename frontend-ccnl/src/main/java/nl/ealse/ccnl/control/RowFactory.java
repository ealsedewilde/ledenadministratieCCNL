package nl.ealse.ccnl.control;

import javafx.event.EventHandler;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;

/**
 * Add onMouseClicked EventHandler to TableRow.
 *
 * @param <T> The type of the item contained within the Cell.
 */
public class RowFactory<T extends Object> implements Callback<TableView<T>, TableRow<T>> {

  @Getter
  @Setter
  private EventHandler<? super MouseEvent> onMouseClicked;

  @Override
  public TableRow<T> call(TableView<T> param) {
    TableRow<T> row = new TableRow<>();
    row.setOnMouseClicked(onMouseClicked);
    return row;
  }

}
