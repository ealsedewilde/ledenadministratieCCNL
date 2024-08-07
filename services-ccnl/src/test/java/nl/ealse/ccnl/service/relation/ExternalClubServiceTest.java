package nl.ealse.ccnl.service.relation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationClubRepository;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.ledenadministratie.util.ClubNumberFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ExternalClubServiceTest {
  
  private static ExternalRelationClubRepository clubDao;
  
  private static ClubNumberFactory clubNumberFactory;
  
  private static ExternalClubService sut;
  
  @Test
  void testGetFreeNumber() {
    sut.getFreeNumber();
    verify(clubNumberFactory).getNewNumber();
  }
  
  @Test
  void testPersistExternalRelation() {
    ExternalRelationClub club = new ExternalRelationClub();
    sut.save(club);
    verify(clubDao).save(club);
  }
  
  @Test
  void testDeleteExternalRelation() {
    ExternalRelationClub club = new ExternalRelationClub();
    sut.deleteExternalRelation(club);
    verify(clubDao).delete(club);
  }
  
  @Test
  void testSearchCity() {
    String searchValue ="foo";
    sut.searchExternalRelation(SearchItem.CITY, searchValue);
    verify(clubDao).findExternalRelationsByCity(searchValue);
  }
  
  @Test
  void testSearchName() {
    String searchValue ="foo";
    sut.searchExternalRelation(SearchItem.NAME, searchValue);
    verify(clubDao).findExternalRelationsByName(searchValue);
  }
  
  @Test
  void testSearchNumber() {
    String searchValue ="1234";
    sut.searchExternalRelation(SearchItem.NUMBER, searchValue);
    verify(clubDao).findById(any(Integer.class));
  }
  
  @Test
  void testSearchPC() {
    String searchValue ="foo";
    sut.searchExternalRelation(SearchItem.POSTAL_CODE, searchValue);
    verify(clubDao).findExternalRelationsByPostalCode(searchValue);
  }
  
  @Test
  void testSearchStreet() {
    String searchValue ="foo";
    sut.searchExternalRelation(SearchItem.STREET, searchValue);
    verify(clubDao).findExternalRelationsByAddress(searchValue);
  }
  
  @BeforeAll
  static void setup() {
    clubNumberFactory = mock(ClubNumberFactory.class);
    clubDao = mock(ExternalRelationClubRepository.class);
    sut = new ExternalClubService(clubDao, clubNumberFactory);
    
  }

}
