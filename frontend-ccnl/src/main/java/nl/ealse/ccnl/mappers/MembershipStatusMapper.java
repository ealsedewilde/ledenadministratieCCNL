package nl.ealse.ccnl.mappers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.javafx.mappers.PropertyMapper;

public class MembershipStatusMapper implements PropertyMapper<ChoiceBox<String>, MembershipStatus> {

  public static final String ACTIVE = "actief";
  public static final String LAST_YEAR_MEMBERSHIP = "laatste jaar";
  public static final String OVERDUE = "niet betaald";
  public static final String INACTIVE = "opgezegd";
  public static final String AFTER_APRIL = "na 1 april opgezegd";

  @Getter
  private static final ObservableList<String> statuses = FXCollections.observableArrayList(ACTIVE,
      LAST_YEAR_MEMBERSHIP, OVERDUE, INACTIVE, AFTER_APRIL);

  @Override
  public MembershipStatus getPropertyFromJavaFx(ChoiceBox<String> javaFx) {
    return switch (javaFx.getValue()) {
      case LAST_YEAR_MEMBERSHIP:
        yield MembershipStatus.LAST_YEAR_MEMBERSHIP;
      case OVERDUE:
        yield MembershipStatus.OVERDUE;
      case INACTIVE:
        yield MembershipStatus.INACTIVE;
      case ACTIVE:
        yield MembershipStatus.ACTIVE;
      default:
        yield MembershipStatus.AFTER_APRIL;
    };
  }

  @Override
  public void mapPropertyToJavaFx(MembershipStatus modelProperty, ChoiceBox<String> javaFx) {
    javaFx.setItems(statuses);
    switch (modelProperty) {
      case INACTIVE:
        javaFx.setValue(INACTIVE);
        break;
      case LAST_YEAR_MEMBERSHIP:
        javaFx.setValue(LAST_YEAR_MEMBERSHIP);
        break;
      case OVERDUE:
        javaFx.setValue(OVERDUE);
        break;
      case ACTIVE:
        javaFx.setValue(ACTIVE);
        break;
      case AFTER_APRIL:
      default:
        javaFx.setValue(AFTER_APRIL);
        break;

    }

  }

}
