package nl.ealse.ccnl.mail.support;

import lombok.Data;

@Data
public class MailMessage {
  
  private final String to;
  private final String subject;
  private final String text;

}
