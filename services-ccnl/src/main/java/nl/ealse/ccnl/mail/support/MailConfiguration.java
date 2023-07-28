package nl.ealse.ccnl.mail.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Provide the standard JavaMailSender.
 * The default Spring configuration doesn't work in our case.
 * The required properties come from the database instead of a file.
 * From some reason Spring Boot can't handle that properly.
 */
@Configuration
public class MailConfiguration {
  
  @Value("${spring.mail.host}")
  private String host;
  
  @Value("${spring.mail.port}")
  private String port;
  
  @Value("${spring.mail.username}")
  private String userName;
  
  @Value("${spring.mail.password}")
  private String password;
  
  @Value("${spring.mail.properties.mail.smtp.auth}")
  private String mailSmtpAuth;
  
  @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
  private String starttlsEnable;
  
  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl bean = new JavaMailSenderImpl();
    bean.setHost(host);
    bean.setPort(Integer.parseInt(port));
    bean.setUsername(userName);
    bean.setPassword(password);
    bean.getJavaMailProperties().put("mail.smtp.auth", mailSmtpAuth);
    bean.getJavaMailProperties().put("mail.smtp.starttls.enable", starttlsEnable);
    return bean;
  }


}
