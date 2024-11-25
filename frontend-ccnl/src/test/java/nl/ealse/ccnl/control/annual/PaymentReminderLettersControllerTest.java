package nl.ealse.ccnl.control.annual;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.control.DocumentViewer;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ioc.ComponentProviderUtil;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateType;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.output.LetterData;
import nl.ealse.ccnl.ledenadministratie.pdf.content.FOContent;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PaymentReminderLettersControllerTest extends FXMLBaseTest {

  private static DocumentService documentService;
  private static MemberService memberService;
  private static WrappedFileChooser fileChooser;


  @TempDir
  File tempDir;

  private PaymentReminderLettersController sut;


  @Test
  void test() {
    File exportFile = new File(tempDir, "test.docx");
    when(fileChooser.showSaveDialog()).thenReturn(exportFile);
    Assertions.assertTrue(runFX(() -> {
      prepare();
      doTest();
    }));
  }

  private void doTest() {
    MenuChoiceEvent btEvent = new MenuChoiceEvent(this, MenuChoice.PRODUCE_REMINDER_LETTERS_BT);
    MenuChoiceEvent ddEvent = new MenuChoiceEvent(this, MenuChoice.PRODUCE_REMINDER_LETTERS_DD);
    sut.remindersBT(btEvent);
    sut.remindersDD(ddEvent);

    sut.showLetterExample();
    verify(documentService).generatePDF(any(LetterData.class));
    sut.saveLettersToFile();
    verify(documentService).generatePDF(any(FOContent.class), any(LetterData.class));
    verify(getPageController()).showMessage("Bestand is aangemaakt");
    sut.printLetters();
    verify(getPageController()).showMessage("");
    sut.printPDF();
  }

  private void prepare() {
    sut = getTestSubject(PaymentReminderLettersController.class);
    getPageWithFxController(sut, PageName.PAYMENT_REMINDER_LETTERS);
    setPdfViewer();
    setFileChooser();
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setPdfViewer() {
    DocumentViewer documentViewer = DocumentViewer.builder().build();
    documentViewer.setWindowTitle("Herinneringsbrief voor lid: %d (%s)");
    try {
      FieldUtils.writeField(sut, "documentViewer", documentViewer, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @BeforeAll
  static void setup() {
    documentService = ComponentProviderUtil.getComponent(DocumentService.class);
    List<Document> letters = new ArrayList<>();
    when(documentService.findDocuments(any(Member.class), any(DocumentType.class)))
        .thenReturn(letters);
    List<DocumentTemplate> templates = new ArrayList<>();
    templates.add(documentTemplate(true));
    templates.add(documentTemplate(false));
    when(documentService.findDocumentTemplates(any(DocumentTemplateType.class), any(boolean.class)))
        .thenReturn(templates);
    byte[] pdf = getBlob("/welkom.pdf");
    when(documentService.generatePDF(anyString())).thenReturn(pdf);
    when(documentService.generatePDF(any(LetterData.class))).thenReturn(pdf);
    when(documentService.generatePDF(any(FOContent.class), any(LetterData.class))).thenReturn(pdf);
    when(documentService.generateFO(any(LetterData.class))).thenReturn(new FOContent());

    List<Member> members = new ArrayList<>();
    members.add(member());
    memberService = ComponentProviderUtil.getComponent(MemberService.class);
    when(memberService.findMembersCurrentYearNotPaid(any(PaymentMethod.class))).thenReturn(members);

    fileChooser = mock(WrappedFileChooser.class);
  }

  private static DocumentTemplate documentTemplate(boolean sepa) {
    DocumentTemplate template = new DocumentTemplate();
    template.setIncludeSepaForm(sepa);
    template.setModificationDate(LocalDate.of(2020, 12, 5));
    template.setTemplate("template");
    DocumentTemplateID id = new DocumentTemplateID();
    id.setDocumentTemplateType(DocumentTemplateType.PAYMENT_REMINDER);
    id.setName("tp" + sepa);
    template.setTemplateID(id);
    return template;
  }

  private static Member member() {
    Member m = new Member();
    m.setMemberNumber(1234);
    m.setInitials("T.");
    m.setLastName("Tester");
    return m;
  }

  private static byte[] getBlob(String name) {
    byte[] b = null;
    try (InputStream is = PaymentReminderLettersControllerTest.class.getResourceAsStream(name)) {
      b = is.readAllBytes();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return b;
  }
}
