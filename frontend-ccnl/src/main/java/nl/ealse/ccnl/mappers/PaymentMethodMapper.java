package nl.ealse.ccnl.mappers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.javafx.mappers.PropertyMapper;

public class PaymentMethodMapper implements PropertyMapper<ChoiceBox<String>, PaymentMethod> {
  public static final String DIRECT_DEBIT = "automatische incasso";
  public static final String BANK_TRANSFER = "overboeking";
  public static final String NOT_APPLICABLE = "erelid";
  public static final String UNKNOWN = "onbekend";

  @Getter
  private static final ObservableList<String> values =
      FXCollections.observableArrayList(DIRECT_DEBIT, BANK_TRANSFER, NOT_APPLICABLE, UNKNOWN);

  @Override
  public PaymentMethod getPropertyFromJavaFx(ChoiceBox<String> javaFx) {
    return switch (javaFx.getValue()) {
      case BANK_TRANSFER:
        yield PaymentMethod.BANK_TRANSFER;
      case DIRECT_DEBIT:
        yield PaymentMethod.DIRECT_DEBIT;
      case NOT_APPLICABLE:
        yield PaymentMethod.NOT_APPLICABLE;
      case UNKNOWN:
      default:
        yield PaymentMethod.UNKNOWN;
    };
  }

  @Override
  public void mapPropertyToJavaFx(PaymentMethod modelProperty, ChoiceBox<String> javaFx) {
    switch (modelProperty) {
      case BANK_TRANSFER:
        javaFx.setValue(BANK_TRANSFER);
        break;
      case DIRECT_DEBIT:
        javaFx.setValue(DIRECT_DEBIT);
        break;
      case NOT_APPLICABLE:
        javaFx.setValue(NOT_APPLICABLE);
        break;
      case UNKNOWN:
      default:
        javaFx.setValue(UNKNOWN);
        break;

    }
  }

}
