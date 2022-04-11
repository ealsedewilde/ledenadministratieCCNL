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
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.Parent;
import nl.ealse.ccnl.control.annual.PaymentReminderLettersController.PdfToFile;
import nl.ealse.ccnl.control.annual.PaymentReminderLettersController.PdfToPrint;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
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
import nl.ealse.ccnl.test.TestExecutor;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;

class PaymentReminderLettersControllerTest extends FXMLBaseTest<PaymentReminderLettersController> {

  private static PageController pageController;
  private static DocumentService documentService;
  private static MemberService memberService;
  private static MemberLetterHandler memberLetterHandler;
  private static WrappedFileChooser fileChooser;
  private static TaskExecutor toFileEexecutor = new TestExecutor<PdfToFile>();
  private static TaskExecutor toPrintEexecutor = new TestExecutor<PdfToPrint>();
  private static TaskExecutor executor = (task -> {
    if (task instanceof PdfToFile) {
      toFileEexecutor.execute(task);
    } else {
      toPrintEexecutor.execute(task);
    }
  });


  @TempDir
  File tempDir;

  private PaymentReminderLettersController sut;


  @Test
  void test() {

    File exportFile = new File(tempDir, "test.docx");
    when(fileChooser.showSaveDialog()).thenReturn(exportFile);

    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      sut = new PaymentReminderLettersController(documentService, memberService, pageController,
          memberLetterHandler, executor);
      prepare();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());

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
    verify(pageController).showMessage("Bestand is aangemaakt");
    sut.printLetters();
    verify(pageController).showMessage("");
    sut.printPDF();
  }

  private void prepare() {
    try {
      Parent p = getPage(sut, PageName.REMINDER_TEXT_HELP);
      when(pageController.loadPage(PageName.REMINDER_TEXT_HELP)).thenReturn(p);
      getPage(sut, PageName.PAYMENT_REMINDER_LETTERS);
      getPage(sut, PageName.PAYMENT_REMINDER_LETTER_SHOW);
      setFileChooser();
    } catch (FXMLMissingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @BeforeAll
  static void setup() {
    pageController = mock(PageController.class);

    documentService = mock(DocumentService.class);
    List<Document> letters = new ArrayList<>();
    when(documentService.findDocuments(any(Member.class), any(DocumentType.class)))
        .thenReturn(letters);
    List<DocumentTemplate> templates = new ArrayList<>();
    templates.add(documentTemplate(true));
    templates.add(documentTemplate(false));
    when(documentService.findDocumentTemplates(any(DocumentTemplateType.class), any(boolean.class)))
        .thenReturn(templates);
    byte[] pdf = getBlob("welkom.pdf");
    when(documentService.generatePDF(anyString())).thenReturn(pdf);
    when(documentService.generatePDF(any(LetterData.class))).thenReturn(pdf);
    when(documentService.generatePDF(any(FOContent.class), any(LetterData.class))).thenReturn(pdf);
    when(documentService.generateFO(any(LetterData.class))).thenReturn(new FOContent());

    List<Member> members = new ArrayList<>();
    members.add(member());
    memberService = mock(MemberService.class);
    when(memberService.findMembersCurrentYearNotPaid(any(PaymentMethod.class))).thenReturn(members);
    memberLetterHandler = new MemberLetterHandler(documentService);
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
    Resource r = new ClassPathResource(name);
    try (InputStream is = r.getInputStream()) {
      b = is.readAllBytes();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return b;
  }


}
