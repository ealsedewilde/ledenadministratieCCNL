package nl.ealse.ccnl.ledenadministratie.model.dao;

import java.util.List;
import java.util.Optional;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalRelationRepository extends JpaRepository<InternalRelation, Integer> {

  Optional<InternalRelation> findInternalRelationByTitleIgnoreCase(String title);


  @Query("Select m.relationNumber from InternalRelation m")
  List<Number> getAllRelationNumbers();

  @Query("Select m.title from InternalRelation m")
  List<String> getAllTitles();

  @Query("SELECT M FROM InternalRelation M WHERE LOWER(M.address.street) LIKE LOWER(concat(?1, '%'))")
  List<InternalRelation> findInternalRelationsByAddress(String searchValue);

  @Query("SELECT M FROM InternalRelation M WHERE LOWER(M.address.city) LIKE LOWER(concat(?1, '%'))")
  List<InternalRelation> findInternalRelationsByCity(String searchValue);

  @Query("SELECT M FROM InternalRelation M WHERE LOWER(M.title) LIKE LOWER(concat(?1, '%'))")
  List<InternalRelation> findInternalRelationsByTitle(String searchValue);

  @Query("SELECT M FROM InternalRelation M WHERE LOWER(M.address.postalCode) = LOWER(?1)")
  List<InternalRelation> findInternalRelationsByPostalCode(String searchValue);

}
