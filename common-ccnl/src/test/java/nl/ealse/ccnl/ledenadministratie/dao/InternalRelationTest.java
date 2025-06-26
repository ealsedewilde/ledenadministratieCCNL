package nl.ealse.ccnl.ledenadministratie.dao;

import java.util.Optional;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InternalRelationTest {

  private InternalRelationRepository repository = new InternalRelationRepository();

  @Test
  void testByTitle() {
    InternalRelation relation = new InternalRelation();
    Address address = new Address();
    address.setStreet("Helena Hoeve");
    address.setAddressNumber("26");
    address.setCity("Gouda");
    relation.setAddress(address);

    relation.setRelationNumber(8086);
    relation.setContactName("Ealse de Wilde");
    relation.setTitle("Ledenadministratie");

    repository.save(relation);
    long count = repository.count();
    Assertions.assertEquals(1L, count);

    Optional<InternalRelation> result =
        repository.findInternalRelationByTitleIgnoreCase("ledenadministraTIE");
    Assertions.assertTrue(result.isPresent());
  }

}
