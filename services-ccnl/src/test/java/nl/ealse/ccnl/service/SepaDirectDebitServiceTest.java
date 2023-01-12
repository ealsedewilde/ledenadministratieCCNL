package nl.ealse.ccnl.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.EntityManager;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoException;
import nl.ealse.ccnl.ledenadministratie.dd.SepaIncassoGenerator;
import nl.ealse.ccnl.ledenadministratie.dd.SepaIncassoResult;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigAmountEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigBooleanEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigDateEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigStringEntry;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatProperty;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatPropertyKey;
import nl.ealse.ccnl.service.SepaDirectDebitService.MappingResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SepaDirectDebitServiceTest {
  
  private static DirectDebitConfig config;
  
  @Mock
  private SepaIncassoGenerator generator;
  
  @Mock
  private EntityManager em;
  
  @InjectMocks
  private SepaDirectDebitService sut;
  
  @Test
  void getPropertiesTest() {
    when(em.find(DirectDebitConfig.class, 1)).thenReturn(config);
    List<FlatProperty> result = sut.getProperties();
    Assertions.assertEquals(11, result.size());
    
  }
  
  @Test
  void generateSepaDirectDebitFileTest() {
    File targetFile = new File("target.xlsx");
    try {
      when(generator.generateSepaDirectDebitFile(any(File.class), any(File.class))).thenReturn(
          new SepaIncassoResult(1, new ArrayList<String>()));
      sut.generateSepaDirectDebitFile(targetFile);
      verify(generator).generateSepaDirectDebitFile(any(File.class), any(File.class));
    } catch (IncassoException e) {
      Assertions.fail(e.getMessage());
    }
  }
  
  @Test
  void getDirectDebitsDirectoryTest() {
    when(em.find(DirectDebitConfig.class, 1)).thenReturn(config);
    File result = sut.getDirectDebitsDirectory();
    Assertions.assertEquals("ddDir", result.getName());
  }
  
  @Test
  void savePropertyAccName() {
    when(em.find(DirectDebitConfig.class, 1)).thenReturn(config);
    FlatProperty prop = new FlatProperty(FlatPropertyKey.ACC_NAME, config.getClubName());
    sut.saveProperty(prop);
    verify(em, never()).clear();
  }
  
  @Test
  void savePropertyAccNr() {
    when(em.find(DirectDebitConfig.class, 1)).thenReturn(config);
    FlatProperty prop = new FlatProperty(FlatPropertyKey.ACC_NR, config.getIbanNumber());
    sut.saveProperty(prop);
    verify(em, never()).clear();
  }
  
  @Test
  void savePropertyAuthRef() {
    when(em.find(DirectDebitConfig.class, 1)).thenReturn(config);
    FlatProperty prop = new FlatProperty(FlatPropertyKey.AUTH_REF, config.getAuthorization());
    sut.saveProperty(prop);
    verify(em, never()).clear();
  }
  
  @Test
  void savePropertyAuthType() {
    when(em.find(DirectDebitConfig.class, 1)).thenReturn(config);
    FlatProperty prop = new FlatProperty(FlatPropertyKey.AUTH_TYPE, config.getAuthorizationType());
    sut.saveProperty(prop);
    verify(em, never()).clear();
  }
  
  @Test
  void savePropertyAmount() {
    when(em.find(DirectDebitConfig.class, 1)).thenReturn(config);
    FlatProperty prop = new FlatProperty(FlatPropertyKey.DD_AMOUNT, config.getDirectDebitAmount());
    sut.saveProperty(prop);
    verify(em, never()).clear();
  }
  
  @Test
  void savePropertyDate() {
    when(em.find(DirectDebitConfig.class, 1)).thenReturn(config);
    FlatProperty prop = new FlatProperty(FlatPropertyKey.DD_DATE, config.getDirectDebitDate());
    sut.saveProperty(prop);
    verify(em, never()).clear();
  }
  
  @Test
  @DisplayName("invalid directory")
  void savePropertyDir() {
    when(em.find(DirectDebitConfig.class, 1)).thenReturn(config);
    FlatProperty prop = new FlatProperty(FlatPropertyKey.DD_DIR, config.getDirectDebitDir());
    MappingResult result = sut.saveProperty(prop);
    Assertions.assertFalse(result.isValid());
    verify(em).clear();
  }
  
  @Test
  void savePropertyReason() {
    when(em.find(DirectDebitConfig.class, 1)).thenReturn(config);
    FlatProperty prop = new FlatProperty(FlatPropertyKey.DD_REASON, config.getDirectDebitDescription());
    sut.saveProperty(prop);
    verify(em, never()).clear();
  }
  
  @Test
  void savePropertyMessage() {
    when(em.find(DirectDebitConfig.class, 1)).thenReturn(config);
    FlatProperty prop = new FlatProperty(FlatPropertyKey.MSG_ID, config.getMessageId());
    sut.saveProperty(prop);
    verify(em, never()).clear();
  }
  
  @Test
  void savePropertyTestrun() {
    when(em.find(DirectDebitConfig.class, 1)).thenReturn(config);
    FlatProperty prop = new FlatProperty(FlatPropertyKey.TEST, config.getTestRun());
    sut.saveProperty(prop);
    verify(em, never()).clear();
  }
  
  @BeforeAll
  static void initConfig() {
    config = new DirectDebitConfig();
    DDConfigStringEntry auth = new DDConfigStringEntry();
    auth.setValue("value");
    auth.setDescription("description");
    config.setAuthorization(auth);
    
    DDConfigStringEntry authType = new DDConfigStringEntry();
    authType.setValue("value");
    authType.setDescription("description");
    config.setAuthorizationType(authType);
    
    DDConfigStringEntry clubName = new DDConfigStringEntry();
    clubName.setValue("value");
    clubName.setDescription("description");
    config.setClubName(clubName);
    
    DDConfigAmountEntry amount = new DDConfigAmountEntry();
    amount.setValue(BigDecimal.TEN);
    amount.setDescription("description");
    config.setDirectDebitAmount(amount);
    
    DDConfigDateEntry date = new DDConfigDateEntry();
    date.setValue(LocalDate.of(2020, 12, 5));
    date.setDescription("description");
    config.setDirectDebitDate(date);
    
    DDConfigStringEntry desc = new DDConfigStringEntry();
    desc.setValue("value");
    desc.setDescription("description");
    config.setDirectDebitDescription(desc);
    
    DDConfigStringEntry ddDir = new DDConfigStringEntry();
    ddDir.setValue("ddDir");
    ddDir.setDescription("description");
    config.setDirectDebitDir(ddDir);
    
    DDConfigStringEntry ddId = new DDConfigStringEntry();
    ddId.setValue("value");
    ddId.setDescription("description");
    config.setDirectDebitId(ddId);
    
    DDConfigStringEntry iban = new DDConfigStringEntry();
    iban.setValue("value");
    iban.setDescription("description");
    config.setIbanNumber(iban);
    
    DDConfigStringEntry msg = new DDConfigStringEntry();
    msg.setValue("value");
    msg.setDescription("description");
    config.setMessageId(msg);
    
    DDConfigBooleanEntry test = new DDConfigBooleanEntry();
    test.setValue(true);
    test.setDescription("description");
    config.setTestRun(test);
    
    
  }

}
