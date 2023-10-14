package nl.ealse.ccnl.ledenadministratie.model.dao;

import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ExternalRelationRepository<T extends ExternalRelation>
    extends JpaRepository<T, Integer> {

  @Query("SELECT M FROM #{#entityName} M WHERE LOWER(M.address.street) LIKE LOWER(concat(?1, '%'))")
  List<T> findExternalRelationsByAddress(String searchValue);

  @Query("SELECT M FROM #{#entityName} M WHERE LOWER(M.address.city) LIKE LOWER(concat('%', ?1, '%'))")
  List<T> findExternalRelationsByCity(String searchValue);

  @Query("SELECT M FROM #{#entityName} M WHERE LOWER(M.relationName) LIKE LOWER(concat('%', ?1, '%'))")
  List<T> findExternalRelationsByName(String searchValue);

  @Query("SELECT M FROM #{#entityName} M WHERE LOWER(M.address.postalCode) = LOWER(?1)")
  List<T> findExternalRelationsByPostalCode(String searchValue);


}
