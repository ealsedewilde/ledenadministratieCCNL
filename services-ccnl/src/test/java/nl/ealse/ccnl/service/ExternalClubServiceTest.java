package nl.ealse.ccnl.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationClubRepository;
import nl.ealse.ccnl.ledenadministratie.util.ClubNumberFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExternalClubServiceTest {
  
  @Mock
  private ExternalRelationClubRepository clubDao;
  
  @Mock
  private ClubNumberFactory numberFactory;
  
  @InjectMocks
  private ExternalClubService sut;
  
  @Test
  void testGetFreeNumber() {
    sut.getFreeNumber();
    verify(numberFactory).getNewNumber();
  }
  
  @Test
  void testPersistExternalRelation() {
    ExternalRelationClub club = new ExternalRelationClub();
    sut.persistExternalRelation(club);
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

}
