package nl.ealse.ccnl.service;

import jakarta.mail.Address;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseProperties;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.pdf.MailFOGenerator;
import nl.ealse.ccnl.ledenadministratie.pdf.PDFGenerator;
import nl.ealse.ccnl.ledenadministratie.pdf.content.FOContent;
import nl.ealse.ccnl.mail.support.MailMessage;

@Slf4j
public class MailService {

  private static final String FILE_NAME = "OpzegMailLid%d.pdf";

  private final Session session;

  private final DocumentService documentService;

  public MailService(DocumentService documentService) {
    this.documentService = documentService;
    this.session = initialize();
  }

  private Session initialize() {
    Properties props = new Properties();
    props.put("mail.smtp.host", DatabaseProperties.getProperty("ccnl.mail.host"));
    props.put("mail.smtp.port", DatabaseProperties.getProperty("ccnl.mail.port"));
    props.put("mail.smtp.auth",
        DatabaseProperties.getProperty("ccnl.mail.properties.mail.smtp.auth", "false"));
    props.put("mail.smtp.starttls.enable",
        DatabaseProperties.getProperty("ccnl.mail.properties.mail.smtp.starttls.enable", "false"));
    Authenticator auth = new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(DatabaseProperties.getProperty("ccnl.mail.from"),
            DatabaseProperties.getProperty("ccnl.mail.password"));
      }
    };
    return Session.getInstance(props, auth);
  }

  /**
   * Send an email.
   * 
   * @param to - for the email
   * @param subject - of the email
   * @param text - content of the mail
   * @return the email ad was sent
   * @throws MessagingException 
   */
  public MailMessage sendMail(String to, String subject, String text) throws MessagingException {
    MimeMessage message = new MimeMessage(session);
    try {
      message.setFrom(DatabaseProperties.getProperty("ccnl.mail.from"));
      Address[] recipients = {new InternetAddress(to)};
      message.setRecipients(Message.RecipientType.TO, recipients);
      message.setSubject(subject);
      message.setText(text);
      Transport.send(message);
    } catch (MessagingException e) {
      log.error("Failed to send mail", e);
      throw e;
    }
    return new MailMessage(to, subject, text);
  }

  /**
   * Save the content of an email with the related member.
   * 
   * @param member - to whom the email belongs
   * @param message - the email
   */
  public void saveMail(Member member, MailMessage message) {
    FOContent content =
        MailFOGenerator.generateFO(message.to(), null, message.subject(), message.text());
    byte[] pdf = PDFGenerator.generateMailPDF(content);
    List<Document> letters =
        documentService.findDocuments(member, DocumentType.MEMBERSHIP_CANCELATION_MAIL);
    Document document;
    if (letters.isEmpty()) {
      document = new Document();
      document.setDocumentType(DocumentType.MEMBERSHIP_CANCELATION_MAIL);
      document.setDocumentName(String.format(FILE_NAME, member.getMemberNumber()));
      document.setDescription(DocumentType.MEMBERSHIP_CANCELATION_MAIL.getDescription());
      document.setOwner(member);
    } else {
      document = letters.get(0);
    }
    document.setPdf(pdf);
    documentService.saveDocument(document);

  }

}
