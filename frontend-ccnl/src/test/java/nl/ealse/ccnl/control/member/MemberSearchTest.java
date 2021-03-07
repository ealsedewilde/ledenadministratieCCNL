package nl.ealse.ccnl.control.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.MemberService;
import nl.ealse.ccnl.service.SearchItem;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.FXMLMissingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

class MemberSearchTest extends FXMLBaseTest<MemberSearchController> {

  private static ApplicationContext springContext;
  private static MemberService service;
  private static Member m;

  private static SearchItem si = SearchItem.NUMBER;
  private static String sv = "1234";

  private MemberSearch p;


  private MemberSearchController sut;

  @Test
  void testSearch() {
    sut = new MemberSearchController(springContext, service);
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.AMEND_MEMBER);
      sut.onApplicationEvent(event);

      sut.doSearch(si, sv);
      verify(service).searchMember(si, sv);

      MouseEvent me = mock(MouseEvent.class);
      TableRow<Member> row = new TableRow<>();
      row.setItem(m);
      when(me.getSource()).thenReturn(row);
      sut.handleSelected(me);
      verify(springContext).publishEvent(any(MemberSeLectionEvent.class));

      searchField("1234");
      p.search();
      searchCriterium(3);
      searchField("1234aa");
      p.search();
      p.reset();

      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void prepare() {
    try {
      p = (MemberSearch) getPage(sut, PageName.MEMBER_SEARCH);
    } catch (FXMLMissingException e) {
      Assertions.fail(e.getMessage());
      e.printStackTrace();
    }
  }


  @BeforeAll
  static void setup() {
    springContext = mock(ApplicationContext.class);
    service = mock(MemberService.class);
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
      Field f = MemberSearch.class.getSuperclass().getDeclaredField("searchCriterium");
      f.setAccessible(true);
      ChoiceBox<String> searchCriterium = (ChoiceBox<String>) f.get(p);
      searchCriterium.getSelectionModel().select(index);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private void searchField(String text) {
    try {
      Field f = MemberSearch.class.getSuperclass().getDeclaredField("searchField");
      f.setAccessible(true);
      TextField searchField = (TextField) f.get(p);
      searchField.setText(text);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
