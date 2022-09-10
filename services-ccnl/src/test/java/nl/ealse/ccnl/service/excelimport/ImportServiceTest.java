package nl.ealse.ccnl.service.excelimport;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.excel.base.SheetNotFoundException;
import nl.ealse.ccnl.ledenadministratie.excelimport.ImportHandler;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationClubRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationOtherRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationPartnerRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.InternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import nl.ealse.ccnl.service.excelimport.ImportService.ImportSelection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class ImportServiceTest {

  private static ExternalRelationPartnerRepository commercialPartnerRepository;
  private static ExternalRelationClubRepository externalRelationClubRepository;
  private static ExternalRelationOtherRepository externalRelationOtherRepository;
  private static InternalRelationRepository internalRelationRepository;
  private static MemberRepository memberRepository;
  private static ImportHandler importHandler;

  private ImportService sut;
  private CCNLColumnProperties excelProperties;

  @Test
  void testImport() {
    initExcelProperties();
    sut = new ImportService(importHandler, excelProperties);
    
    Resource r = new ClassPathResource("leden.xlsx");
    ImportSelection selection = new ImportSelection(true, true, true, true, true, ImportType.REPLACE);
    
    try {
      sut.importFromExcel(r.getFile(), selection);
      verify(memberRepository, times(16)).save(any(Member.class));
    } catch (IOException | SheetNotFoundException e) {
      Assertions.fail(e.getMessage());
    }
  }

  @BeforeAll
  static void setup() {
    commercialPartnerRepository = mock(ExternalRelationPartnerRepository.class);
    externalRelationClubRepository = mock(ExternalRelationClubRepository.class);
    externalRelationOtherRepository = mock(ExternalRelationOtherRepository.class);
    internalRelationRepository = mock(InternalRelationRepository.class);
    memberRepository = mock(MemberRepository.class);
    importHandler = new ImportHandler(memberRepository, externalRelationClubRepository,
        internalRelationRepository, externalRelationOtherRepository, commercialPartnerRepository);
  }



  private void initExcelProperties() {
    ConfigurableEnvironment environment = new StandardEnvironment();
    MutablePropertySources propertySources = environment.getPropertySources();
    Properties props = new Properties();
    Map<String, Object> map = new HashMap<>();
    propertySources.addFirst(new MapPropertySource("excel.properties", map));
    Resource r = new ClassPathResource("excel.properties");
    try {
      props.load(r.getInputStream());
      props.forEach((key, value) -> map.put((String) key, value));
    } catch (IOException e) {
      e.printStackTrace();
    }

    excelProperties = new CCNLColumnProperties(environment);

  }

}
