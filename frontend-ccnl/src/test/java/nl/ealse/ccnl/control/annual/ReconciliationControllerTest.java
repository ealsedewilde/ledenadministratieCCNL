package nl.ealse.ccnl.control.annual;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.stage.Stage;
import nl.ealse.ccnl.control.annual.ReconciliationController.ReconcileTask;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.PaymentFile;
import nl.ealse.ccnl.service.ReconciliationService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.ccnl.test.TestExecutor;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;

class ReconciliationControllerTest extends FXMLBaseTest<ReconciliationController> {
  private static ApplicationContext context;
  private static PageController pageController;
  private static ReconciliationService service;
  private static WrappedFileChooser fileChooser;
  private static TaskExecutor executor = new TestExecutor<ReconcileTask>();

  @TempDir
  File tempDir;

  private ReconciliationController sut;

  @Test
  void testController() {
    sut = new ReconciliationController(pageController, context, executor);
    File reconcileFile = new File(tempDir, "reconcile.xml");
    when(fileChooser.showOpenDialog()).thenReturn(reconcileFile);
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      setFileChooser();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.RECONCILE_PAYMENTS);
    sut.onApplicationEvent(event);
    verify(service).allFiles();

    Button btn = new Button();
    PaymentFile file = new PaymentFile();
    inject(btn, file);
    ActionEvent e = new ActionEvent(btn, null);
    sut.deleteFile(e);
    verify(service, times(2)).allFiles();

    sut.selectFile();
    verify(pageController, never()).showErrorMessage(any(String.class));

    sut.reconcilePayments();
    verify(pageController).showMessage("Betalingen zijn verwerkt");

  }

  private void prepare() {
    try {
      setDialog(true, "messagesStage");
      getPageWithFxController(sut, PageName.RECONCILE_PAYMENTS);
      Parent m = getPageWithFxController(sut, PageName.RECONCILE_MESSAGES);
      when(pageController.loadPage(PageName.RECONCILE_MESSAGES)).thenReturn(m);
      setDialog(false, "messagesStage");
      sut.initialize();
    } catch (FXMLMissingException e) {
      Assertions.fail(e.getMessage());
    }
  }

  @BeforeAll
  static void setup() {
    pageController = mock(PageController.class);
    context = mock(ApplicationContext.class);
    service = mock(ReconciliationService.class);
    when(context.getBean(ReconciliationService.class)).thenReturn(service);

    try {
      when(service.saveFile(any(File.class))).thenReturn(true);
      List<PaymentFile> paymentFiles = new ArrayList<>();
      PaymentFile pf = new PaymentFile();
      pf.setFileName("pf.xml");
      paymentFiles.add(pf);
      when(service.allFiles()).thenReturn(paymentFiles);
    } catch (IOException e) {
      e.printStackTrace();
    }
    fileChooser = mock(WrappedFileChooser.class);
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void inject(Button btn, PaymentFile file) {
    TableCell<PaymentFile, String> cell = new TableCell<>();
    TableRow<PaymentFile> row = new TableRow<>();
    row.setItem(file);
    try {
      ReadOnlyObjectWrapper<TableRow<PaymentFile>> wrappedRow = new ReadOnlyObjectWrapper<>(row);
      FieldUtils.writeField(cell, "tableRow", wrappedRow, true);

      ReadOnlyObjectWrapper<TableCell<PaymentFile, String>> wrappedValue =
          new ReadOnlyObjectWrapper<>(cell);
      FieldUtils.writeField(btn, "parent", wrappedValue, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  private void setDialog(boolean b, String name) {
    try {
      Field f = sut.getClass().getDeclaredField(name);
      f.setAccessible(true);
      if (b) {
        f.set(sut, new Stage());
      } else {
        f.set(sut, null);

      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }


}
