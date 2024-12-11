package nl.ealse.ccnl.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import jakarta.mail.MessagingException;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.mail.support.MailMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MailServiceTest {

  private static DocumentService documentService;

  private static MailService sut;

  @Test
  void testSendMail() {
    try {
      sut.sendMail("ealsedewilde@gmail.com", "Testbericht", "Dit is een test");
    } catch (MessagingException e) {
      String error = e.getMessage();
      assertEquals("Couldn't connect to host, port: localhost, 25; timeout -1", error);
    }
  }

  @Test
  void testSaveMail() {
    Member m = new Member();
    m.setMemberNumber(1234);
    try {
      sut.saveMail(m, mailMessage());
    } catch (Throwable t) {
        t.printStackTrace();
    }
    verify(documentService).saveDocument(any(Document.class));
  }

  private MailMessage mailMessage() {
    return new MailMessage("target@test.nl", "Test mail", "The content");
  }

  @BeforeEach
  void setup() {
    documentService = mock(DocumentService.class);
    sut = new MailService(documentService);
  }

}
