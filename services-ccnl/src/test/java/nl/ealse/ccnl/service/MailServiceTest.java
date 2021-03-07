package nl.ealse.ccnl.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {
  
  @Mock
  private JavaMailSender emailSender;

  @Mock
  private DocumentService documentService;

  @InjectMocks
  private MailService sut;

  @Test
  void testSendMail() {
    sut.sendMail("ealsedewilde@gmail.com", "Testbericht", "Dit is een test");
    verify(emailSender).send(any(SimpleMailMessage.class));
  }

  @Test
  void testSaveMail() {
    Member m = new Member();
    m.setMemberNumber(1234);
    sut.saveMail(m, mailMessage());
    verify(documentService).saveDocument(any(Document.class));
  }
  
  private SimpleMailMessage mailMessage() {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("info@test.nl");
    message.setTo("target@test.nl");
    message.setSubject("Test mail");
    message.setText("The content");
    return message;
    
    
  }
  
  @BeforeEach
  void setup() {
    try {
      FieldUtils.writeField(sut, "from", "info@test.nl", true);
    } catch (IllegalAccessException e) {
      Assertions.fail(e.getMessage());
      e.printStackTrace();
    }
  }

}
