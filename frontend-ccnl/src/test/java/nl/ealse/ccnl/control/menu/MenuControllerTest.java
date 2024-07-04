package nl.ealse.ccnl.control.menu;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mockStatic;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventPublisher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

class MenuControllerTest {

  private MockedStatic<EventPublisher> context;

  private static MenuController sut;

  private ArgumentCaptor<MenuChoiceEvent> menu = ArgumentCaptor.forClass(MenuChoiceEvent.class);

  @BeforeEach
  void init() {
    context = mockStatic(EventPublisher.class);
  }

  @AfterEach
  void clear() {
    context.close();
  }

  @Test
  void action() {
    MenuItem item = new MenuItem();
    item.setId("NEW_MEMBER");
    ActionEvent actionEvent = new ActionEvent(item, item);
    sut.action(actionEvent);
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.NEW_MEMBER, event.getMenuChoice());
  }

  @BeforeAll
  static void setup() {
    sut = new MenuController();
  }

}
