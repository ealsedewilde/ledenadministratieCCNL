package nl.ealse.ccnl.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.dao.PaymentFileRepository;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentFile;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.payment.PaymentHandler;
import nl.ealse.ccnl.ledenadministratie.util.XmlValidator;

@Slf4j
public class ReconciliationService {

  private static final Set<MembershipStatus> STATUSES =
      EnumSet.of(MembershipStatus.ACTIVE, MembershipStatus.LAST_YEAR_MEMBERSHIP);

  private final PaymentFileRepository dao;
  private final MemberRepository memberDao;
  private final PaymentHandler reconciliationHandler;

  private static final String XSD = "/camt.053.001.02.xsd";

  public ReconciliationService(PaymentFileRepository dao, MemberRepository memberDao, PaymentHandler reconciliationHandler) {
    log.info("Service created");
    this.dao = dao;
    this.memberDao = memberDao;
    this.reconciliationHandler = reconciliationHandler;
  }

  public void deleteAllFiles() {
    dao.deleteAll();
  }

  public boolean saveFile(File file) throws IOException {
    String xml = getXml(file);
    if (XmlValidator.validate(XSD, xml)) {
      PaymentFile paymentFile = new PaymentFile();
      paymentFile.setFileName(file.getName());
      paymentFile.setXml(xml);
      dao.save(paymentFile);
      return true;
    }
    return false;
  }

  private String getXml(File file) throws IOException {
    StringBuilder sb = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line = reader.readLine();
      while (line != null) {
        sb.append(line);
        line = reader.readLine();
      }
    } catch (IOException e) {
      log.error("Error reading file", e);
      throw e;
    }
    return sb.toString();
  }

  public void deleteFile(PaymentFile file) {
    dao.delete(file);
  }

  public List<PaymentFile> allFiles() {
    return dao.findAllByOrderByFileName();
  }

  public List<String> reconcilePayments(LocalDate referenceDate, boolean includeDD) {
    return reconciliationHandler.handlePayments(allFiles(), referenceDate, includeDD);
  }

  public void resetPaymentStatus() {
    List<Member> members = memberDao.findMembersByStatuses(STATUSES);
    members.forEach(member -> {
      member.setDirectDebitExecuted(false);
      member.setCurrentYearPaid(PaymentMethod.NOT_APPLICABLE == member.getPaymentMethod());
      member.setPaymentDate(null);
      member.setPaymentInfo(null);
      member.setMembercardIssued(false);
    });
    dao.deleteAll();
  }

}
