package nl.ealse.ccnl.service.relation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import nl.ealse.ccnl.ledenadministratie.dao.InternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.test.MockProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class InternalRelationServiceTest {
  
  private static InternalRelationRepository dao;
  
  private static InternalRelationService sut;
  
  @Test
  void testGetAllTitles() {
    sut.getAllTitles();
    verify(dao).getAllTitles();
  }
  
  @Test
  void testPersistInternalRelation() {
    InternalRelation relation = new InternalRelation();
    sut.save(relation);
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
    sut.searchInternalRelation(SearchItem.CITY, searchValue);
    verify(dao).findInternalRelationsByCity(searchValue);
  }
  
  @Test
  void testSearchName() {
    String searchValue ="foo";
    sut.searchInternalRelation(SearchItem.NAME, searchValue);
    verify(dao).findInternalRelationsByTitle(searchValue);
  }
  
  @Test
  void testSearchNumber() {
    String searchValue ="1234";
    sut.searchInternalRelation(SearchItem.NUMBER, searchValue);
    verify(dao).findById(any(Integer.class));
  }
  
  @Test
  void testSearchPC() {
    String searchValue ="foo";
    sut.searchInternalRelation(SearchItem.POSTAL_CODE, searchValue);
    verify(dao).findInternalRelationsByPostalCode(searchValue);
  }
  
  @Test
  void testSearchStreet() {
    String searchValue ="foo";
    sut.searchInternalRelation(SearchItem.STREET, searchValue);
    verify(dao).findInternalRelationsByAddress(searchValue);
  }
  
  @BeforeAll
  static void setup() {
    dao = MockProvider.mock(InternalRelationRepository.class);
    sut = InternalRelationService.getInstance();
    
  }

}
