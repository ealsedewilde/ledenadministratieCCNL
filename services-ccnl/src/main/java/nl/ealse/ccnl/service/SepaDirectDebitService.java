package nl.ealse.ccnl.service;

import jakarta.persistence.EntityManager;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoException;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoProperties;
import nl.ealse.ccnl.ledenadministratie.dd.SepaIncassoGenerator;
import nl.ealse.ccnl.ledenadministratie.dd.SepaIncassoResult;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigAmountEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigBooleanEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigDateEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigStringEntry;
import nl.ealse.ccnl.ledenadministratie.util.AmountFormatter;
import nl.ealse.ccnl.ledenadministratie.util.EntityManagerProvider;

@Slf4j
public class SepaDirectDebitService {

  @Getter
  private static SepaDirectDebitService instance = new SepaDirectDebitService();

  private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  private final SepaIncassoGenerator generator;

  private  SepaDirectDebitService() {
    log.info("Service created");
    this.generator = SepaIncassoGenerator.getInstance();
  }

  public List<String> generateSepaDirectDebitFile(File targetFile) throws IncassoException {
    String fileName = targetFile.getAbsolutePath();
    int end = fileName.length() - 4;
    File controlExcelFile = new File(fileName.substring(0, end) + ".xlsx");
    SepaIncassoResult result = generator.generateSepaDirectDebitFile(targetFile, controlExcelFile);
    return result.getMessages();
  }

  public List<FlatProperty> getProperties() {
    List<FlatProperty> propertyList = new ArrayList<>();
    DirectDebitConfig config = IncassoProperties.getConfig();
    if (config != null) {
      propertyList.add(new FlatProperty(FlatPropertyKey.DD_DIR, config.getDirectDebitDir()));
      propertyList.add(new FlatProperty(FlatPropertyKey.DD_AMOUNT, config.getDirectDebitAmount()));
      propertyList.add(new FlatProperty(FlatPropertyKey.ACC_NR, config.getIbanNumber()));
      propertyList.add(new FlatProperty(FlatPropertyKey.ACC_NAME, config.getClubName()));
      propertyList.add(new FlatProperty(FlatPropertyKey.DD_ID, config.getDirectDebitId()));
      propertyList.add(new FlatProperty(FlatPropertyKey.DD_DATE, config.getDirectDebitDate()));
      propertyList
          .add(new FlatProperty(FlatPropertyKey.DD_REASON, config.getDirectDebitDescription()));
      propertyList.add(new FlatProperty(FlatPropertyKey.MSG_ID, config.getMessageId()));
      propertyList.add(new FlatProperty(FlatPropertyKey.AUTH_REF, config.getAuthorization()));
      propertyList.add(new FlatProperty(FlatPropertyKey.AUTH_TYPE, config.getAuthorizationType()));
      propertyList.add(new FlatProperty(FlatPropertyKey.TEST, config.getTestRun()));
    }
    return propertyList;
  }

  public MappingResult saveProperty(FlatProperty prop) {
    EntityManager em = EntityManagerProvider.getEntityManager();
    DirectDebitConfig config = em.find(DirectDebitConfig.class, 1);
    MappingResult result = new MappingResult();
    switch (prop.fpk) {
      case ACC_NAME:
        DDConfigStringEntry e = config.getClubName();
        e.setValue(prop.getValue());
        e.setDescription(prop.getDescription());
        break;
      case ACC_NR:
        e = config.getIbanNumber();
        e.setValue(prop.getValue());
        e.setDescription(prop.getDescription());
        break;
      case AUTH_REF:
        e = config.getAuthorization();
        e.setValue(prop.getValue());
        e.setDescription(prop.getDescription());
        break;
      case AUTH_TYPE:
        e = config.getAuthorizationType();
        e.setValue(prop.getValue());
        e.setDescription(prop.getDescription());
        break;
      case DD_AMOUNT:
        DDConfigAmountEntry a = config.getDirectDebitAmount();
        // Value is in unknown charset.
        char[] cs = prop.getValue().toCharArray();
        // We must remove all characters before the first digit
        int ix = 0;
        while (!Character.isDigit(cs[ix])) {
          ix++;
        }
        // The substring method will produce a normal String
        String amount = prop.getValue().substring(ix);
        try {
          a.setValue(BigDecimal.valueOf(AmountFormatter.parse(amount)));
        } catch (NumberFormatException nfe) {
          result.setValid(false);
          result.setErrorMessage(
              String.format("'%s' kan niet worden omgezet naar een bedrag", amount));
        }
        a.setDescription(prop.getDescription());
        break;
      case DD_DATE:
        DDConfigDateEntry d = config.getDirectDebitDate();
        try {
          d.setValue(LocalDate.parse(prop.getValue(), DT));
        } catch (DateTimeParseException dtpe) {
          result.setValid(false);
          result.setErrorMessage("Geef datum op als dd-mm-jjjj");
        }
        d.setDescription(prop.getDescription());
        break;
      case DD_DIR:
        e = config.getDirectDebitDir();
        File f = new File(prop.getValue());
        if (!f.exists()) {
          result.setValid(false);
          result.setErrorMessage(String.format("Opgegeven map '%s' bestaat niet", prop.getValue()));
        } else {
          e.setValue(prop.getValue());
        }
        e.setDescription(prop.getDescription());
        break;
      case DD_ID:
        e = config.getDirectDebitId();
        e.setValue(prop.getValue());
        e.setDescription(prop.getDescription());
        break;
      case DD_REASON:
        e = config.getDirectDebitDescription();
        e.setValue(prop.getValue());
        e.setDescription(prop.getDescription());
        break;
      case MSG_ID:
        e = config.getMessageId();
        e.setValue(prop.getValue());
        e.setDescription(prop.getDescription());
        break;
      case TEST:
        DDConfigBooleanEntry b = config.getTestRun();
        b.setValue("Ja".equalsIgnoreCase(prop.getValue()));
        b.setDescription(prop.getDescription());
        break;
      default:
        break;
    }
    if (!result.isValid()) {
      em.clear();
    }
    return result;
  }

  @Data
  public static class MappingResult {
    private boolean valid = true;
    private String errorMessage;
  }

  @Data
  public static class FlatProperty {
    private FlatPropertyKey fpk;
    private String value;
    private String description;

    public String getKey() {
      return fpk.getKeyDescription();
    }

    public FlatProperty(FlatPropertyKey key, DDConfigStringEntry entry) {
      this.fpk = key;
      this.value = entry.getValue();
      this.description = entry.getDescription();
    }

    public FlatProperty(FlatPropertyKey key, DDConfigBooleanEntry entry) {
      this.fpk = key;
      this.value = entry.isValue() ? "Ja" : "Nee";
      this.description = entry.getDescription();
    }

    public FlatProperty(FlatPropertyKey key, DDConfigDateEntry entry) {
      this.fpk = key;
      this.value = entry.getValue().format(DT);
      this.description = entry.getDescription();
    }

    public FlatProperty(FlatPropertyKey key, DDConfigAmountEntry entry) {
      this.fpk = key;
      this.value = AmountFormatter.format(entry.getValue());
      this.description = entry.getDescription();
    }
  }

  public enum FlatPropertyKey {
    DD_DIR("Map voor incassobestand"), DD_AMOUNT("Incassobedrag"), ACC_NR("IBAN-nummer"), ACC_NAME(
        "Naam bij IBAN-nummer"), DD_ID("Incassant Id"), DD_DATE("Incassodatum"), DD_REASON(
            "Incassoreden"), MSG_ID("Bericht Id"), AUTH_REF(
                "Machtiging referentie"), AUTH_TYPE("Incasso type"), TEST("Testrun");

    @Getter
    private final String keyDescription;

    private FlatPropertyKey(String keyDescription) {
      this.keyDescription = keyDescription;
    }
  }



}
