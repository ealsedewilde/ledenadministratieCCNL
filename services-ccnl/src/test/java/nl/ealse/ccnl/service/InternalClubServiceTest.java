package nl.ealse.ccnl.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.ledenadministratie.model.dao.InternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.util.ClubNumberFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InternalClubServiceTest {
  
  @Mock
  private InternalRelationRepository dao;
  
  @Mock
  private ClubNumberFactory numberFactory;
  
  @InjectMocks
  private InternalRelationService sut;
  
  @Test
  void testGetAllTitles() {
    sut.getAllTitles();
    verify(dao).getAllTitles();
  }
  
  @Test
  void testPersistInternalRelation() {
    InternalRelation relation = new InternalRelation();
    sut.persistInternalRelation(relation);
    verify(dao).save(relation);
  }
  
  @Test
  void testDeleteInternalRelation() {
    InternalRelation relation = new InternalRelation();
    sut.deleteInternalRelation(relation);
    verify(dao).delete(relation);
  }
  
  @Test
  void testSearchCity() {
    String searchValue ="foo";
    sut.searchExternalRelation(SearchItem.CITY, searchValue);
    verify(dao).findInternalRelationsByCity(searchValue);
  }
  
  @Test
  void testSearchName() {
    String searchValue ="foo";
    sut.searchExternalRelation(SearchItem.NAME, searchValue);
    verify(dao).findInternalRelationsByTitle(searchValue);
  }
  
  @Test
  void testSearchNumber() {
    String searchValue ="1234";
    sut.searchExternalRelation(SearchItem.NUMBER, searchValue);
    verify(dao).findById(any(Integer.class));
  }
  
  @Test
  void testSearchPC() {
    String searchValue ="foo";
    sut.searchExternalRelation(SearchItem.POSTAL_CODE, searchValue);
    verify(dao).findInternalRelationsByPostalCode(searchValue);
  }
  
  @Test
  void testSearchStreet() {
    String searchValue ="foo";
    sut.searchExternalRelation(SearchItem.STREET, searchValue);
    verify(dao).findInternalRelationsByAddress(searchValue);
  }

}
