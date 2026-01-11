package nl.ealse.ccnl.control.member;

import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.NAME;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.NUMBER;
import jakarta.mail.MessagingException;
import java.util.StringJoiner;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;
import nl.ealse.ccnl.control.DocumentTemplateController;
import nl.ealse.ccnl.control.HandledTask;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.mail.support.MailMessage;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.MailService;
import nl.ealse.ccnl.service.relation.MemberService;

public class CancelationMailController extends DocumentTemplateController {

  private final PageController pageController;

  private final MemberService memberService;

  private final DocumentService documentService;

  private final MailService mailService;

  private Member selectedMember;

  @FXML
  @Getter
  private TextField toMailAddress;

  @FXML
  @Getter
  private Label toMailAddressE;

  @FXML
  private CheckBox saveMailAddress;

  @FXML
  private Button sendButton;

  private CancelMailValidation validation;

  public CancelationMailController(PageController pageController, MemberService memberService,
      DocumentService documentService, MailService mailService) {
    super(DocumentTemplateContext.MEMBERSHIP_CANCELATION_MAIL);
    this.pageController = pageController;
    this.memberService = memberService;
    this.documentService = documentService;
    this.mailService = mailService;
  }

  @FXML
  protected void initialize() {
    initializeTemplates();
    validation = new CancelMailValidation(this);
    validation.setCallback(valid -> sendButton.setDisable(!valid));
  }

  @EventListener
  public void onApplicationEvent(CancelMailEvent event) {
    selectedMember = event.getMember();
    pageController.setActivePage(PageName.MEMBER_CANCEL_MAIL);
    toMailAddress.setText(selectedMember.getEmail());
    saveMailAddress.setSelected(false);
    validation.validate();
  }

  @FXML
  void sendMail() {
    String mailAddress = toMailAddress.getText();
    if (saveMailAddress.isSelected()) {
      selectedMember.setEmail(mailAddress);
      memberService.save(selectedMember);
    }
    SendMailTask sendMailTask = new SendMailTask(mailAddress);
    pageController.showPermanentMessage("Email wordt verzonden naar: " + mailAddress);
    sendMailTask.executeTask();
    pageController.activateLogoPage();
  }

  @FXML
  void noMail() {
    pageController.activateLogoPage();
    pageController.showMessage("Geen Email verzonden");
  }

  /**
   * Construct the content of the mail.
   *
   * @return - mail content
   */
  private String generateText() {
    String[] lines = getLetterText().getText().split("\\r?\\n");
    // tab (\t) is a workaround for "Auto Remove Line Breaks" in Outlook.
    StringJoiner j = new StringJoiner("\t\n");
    String number = Integer.toString(selectedMember.getMemberNumber());
    String name;
    if (selectedMember.hasFirstName()) {
      name = selectedMember.getInitials();
    } else {
      name = selectedMember.getFullName();
    }
    for (String line : lines) {
      line = line.replaceAll(NAME.symbol(), name);
      line = line.replaceAll(NUMBER.symbol(), number);
      j.add(line);
    }
    return j.toString();
  }

  @Override
  protected PageController getPageController() {
    return pageController;
  }

  @Override
  protected DocumentService getDocumentService() {
    return documentService;
  }

  @Override
  protected void reInitialize() {
    initializeTemplates();
  }
  
  private class SendMailTask extends HandledTask {

    private final String mailSubject = ApplicationContext.getPreference("ccnl.mail.subject");
    private final String mailAddress;
    
    public SendMailTask(String mailAddress) {
      this.mailAddress = mailAddress;
    }

    @Override
    protected String call() throws Exception {
      String mailContent = generateText();
      try {
        MailMessage mailMessage = mailService.sendMail(mailAddress, mailSubject, mailContent);
        mailService.saveMail(selectedMember, mailMessage);
        return "Email is verzonden";
      } catch (MessagingException e) {
        return "Email is verzending is mislukt: " + e.getMessage();
      }
    }
    
  }

}
