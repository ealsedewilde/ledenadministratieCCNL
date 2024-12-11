package nl.ealse.ccnl.ledenadministratie.excelimport;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationClubRepository;
import nl.ealse.ccnl.ledenadministratie.excel.club.CCNLClub;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ExternalImportTest {
  
  ExternalClubImport sut;
  
  private static ExternalRelationClubRepository repository;
  
  private static ExternalRelationClub clubRelation = new ExternalRelationClub();
  
  private static CCNLClub club;
  
  @Test
  void testImportClub() {
    sut = new ExternalClubImport(repository, ProcessType.REPLACE);
    sut.importRelation(club);
    verify(repository).save(clubRelation);
    sut.finalizeImport();
    verify(repository).deleteById(Integer.valueOf(1111));
    verify(repository, never()).deleteById(Integer.valueOf(1234));
    
  }
  
  @BeforeAll
  static void setup() {
    repository = mock(ExternalRelationClubRepository.class);
    when(repository.findById(any(Integer.class))).thenReturn(Optional.of(clubRelation));

    List<Number> clubs = new ArrayList<>();
    clubs.add(Integer.valueOf(8288));
    clubs.add(Integer.valueOf(1111));
    when(repository.getAllRelationNumbers()).thenReturn(clubs);

    club = mock(CCNLClub.class);
    when(club.getClubNummer()).thenReturn(8288);
    when(club.getRelatienummer()).thenReturn(8288);
    when(club.getPlaats()).thenReturn("Ons Dorp");
    when(club.getPostcode()).thenReturn("1234 AA");
    when(club.getStraat()).thenReturn("Brink 1");
  }


}
