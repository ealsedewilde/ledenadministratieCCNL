package nl.ealse.ccnl.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoException;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoProperties;
import nl.ealse.ccnl.ledenadministratie.dd.SepaIncassoGenerator;
import nl.ealse.ccnl.ledenadministratie.dd.SepaIncassoResult;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatProperty;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatPropertyKey;
import nl.ealse.ccnl.service.SepaDirectDebitService.MappingResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SepaDirectDebitServiceTest {
  
  private static DirectDebitConfig config;
  
  private static EntityManager em;
  
  private static SepaIncassoGenerator generator;
  
  private static SepaDirectDebitService sut;
  
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
  
  @BeforeEach
  void resetEntityManager() {
    reset(em);
    EntityTransaction t = mock(EntityTransaction.class);
    when(em.getTransaction()).thenReturn(t);

  }
  
  @BeforeAll
  static void initConfig() {
    config = IncassoProperties.getProperties();
    config.getDirectDebitDir().setValue("C:/xtemp");
    em = EntityManagerProvider.getEntityManager();
    generator = mock(SepaIncassoGenerator.class);
    sut = new SepaDirectDebitService(generator);
  }

}
