package nl.ealse.ccnl.control.member;

import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.NAME;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.NUMBER;
import java.util.StringJoiner;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;
import nl.ealse.ccnl.control.DocumentTemplateController;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.MailService;
import nl.ealse.ccnl.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;

@Controller
public class CancelationMailController extends DocumentTemplateController
    implements ApplicationListener<MemberSeLectionEvent> {

  private final PageController pageController;

  private final MemberService memberService;

  private final MailService mailService;

  @Value("${ccnl.mail.subject:}")
  private String mailSubject;

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


  public CancelationMailController(PageController pageController, DocumentService documentService,
      MailService mailService, MemberService memberService) {
    super(pageController, documentService, DocumentTemplateContext.MEMBERSHIP_CANCELATION_MAIL);
    this.pageController = pageController;
    this.memberService = memberService;
    this.mailService = mailService;
  }

  @FXML
  @Override
  public void initialize() {
    super.initialize();
    validation = new CancelMailValidation(this);
    validation.setCallback(valid -> sendButton.setDisable(!valid));
  }

  @Override
  public void onApplicationEvent(MemberSeLectionEvent event) {
    if (MenuChoice.CANCEL_MEMBERSHIP == event.getMenuChoice()) {
      selectedMember = event.getSelectedEntity();
      toMailAddress.setText(selectedMember.getEmail());
      saveMailAddress.setSelected(false);
      validation.validate();

      initializeTemplates();
    }
  }

  @FXML
  public void sendMail() {
    if (saveMailAddress.isSelected()) {
      selectedMember.setEmail(toMailAddress.getText());
      memberService.persistMember(selectedMember);
    }
    String mailContent = generateText();
    SimpleMailMessage mailMessage =
        mailService.sendMail(toMailAddress.getText(), mailSubject, mailContent);
    mailService.saveMail(selectedMember, mailMessage);
    pageController.setMessage("Email is verzonden");
    pageController.setActivePage(PageName.LOGO);
  }

  @FXML
  public void noMail() {
    pageController.setMessage("Geen Email verzonden");
    pageController.setActivePage(PageName.LOGO);
  }

  /**
   * Construct the content of the mail.
   * 
   * @return
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

}
