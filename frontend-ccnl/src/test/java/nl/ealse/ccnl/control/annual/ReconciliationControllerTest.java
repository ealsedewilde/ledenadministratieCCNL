package nl.ealse.ccnl.control.annual;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.model.PaymentFile;
import nl.ealse.ccnl.service.ReconciliationService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ReconciliationControllerTest extends FXMLBaseTest {
  private static ReconciliationService service;
  private static WrappedFileChooser fileChooser;

  @TempDir
  File tempDir;

  private ReconciliationController sut;

  @Test
  void testController() {
    File reconcileFile = new File(tempDir, "reconcile.xml");
    when(fileChooser.showOpenDialog()).thenReturn(reconcileFile);
    Assertions.assertTrue(runFX(() -> {
      prepare();
      setFileChooser();
      doTest();
    }));
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
    verify(getPageController(), never()).showErrorMessage(any(String.class));

    sut.reconcilePayments();
    verify(getPageController()).showMessage("Betalingen zijn verwerkt");

  }

  private void prepare() {
    sut = getTestSubject(ReconciliationController.class);
    getPageWithFxController(sut, PageName.RECONCILE_PAYMENTS);

  }

  @BeforeAll
  static void setup() {
    service = ApplicationContext.getComponent(ReconciliationService.class);


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

}
