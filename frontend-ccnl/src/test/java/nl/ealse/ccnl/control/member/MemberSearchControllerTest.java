package nl.ealse.ccnl.control.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.service.relation.SearchItem;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

class MemberSearchControllerTest extends FXMLBaseTest {

  private static ApplicationContext springContext;
  private static MemberService service;
  private static PageController pageController;
  private static Member m;

  private static SearchItem si = SearchItem.NUMBER;
  private static String sv = "1234";

  private MemberSearchController sut;

  @Test
  void testSearch() {
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.AMEND_MEMBER);
      sut.searchMember(event);

      sut.doSearch(si, sv);
      verify(service).searchMember(si, sv);

      MouseEvent me = mock(MouseEvent.class);
      TableRow<Member> row = new TableRow<>();
      row.setItem(m);
      when(me.getSource()).thenReturn(row);
      sut.handleSelected(me);
      verify(springContext).publishEvent(any(MemberSeLectionEvent.class));

      searchField("1234");
      sut.search();
      searchCriterium(3);
      searchField("1234aa");
      sut.search();
      sut.reset();

      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void prepare() {
    sut = new MemberSearchController(springContext, service, pageController);
  }


  @BeforeAll
  static void setup() {
    springContext = mock(ApplicationContext.class);
    service = mock(MemberService.class);
    pageController = mock(PageController.class);
    m = member();
    List<Member> members = new ArrayList<>();
    members.add(m);
    when(service.searchMember(any(SearchItem.class), anyString())).thenReturn(members);
    when(service.searchMemberWithoutSepa(any(SearchItem.class), anyString())).thenReturn(members);
  }

  private static Member member() {
    Member m = new Member();
    m.setMemberNumber(1234);
    return m;
  }


  @SuppressWarnings("unchecked")
  private void searchCriterium(int index) {
    try {
      ChoiceBox<String> searchCriterium =
          (ChoiceBox<String>) FieldUtils.readField(sut, "searchCriterium", true);
      searchCriterium.getSelectionModel().select(index);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private void searchField(String text) {
    try {
      TextField searchField = (TextField) FieldUtils.readField(sut, "searchField", true);
      searchField.setText(text);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
