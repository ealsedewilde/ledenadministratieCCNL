package nl.ealse.ccnl.ledenadministratie.model.dao;

import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommercialPartnerTest extends JpaTestBase {

  @Autowired
  private ExternalRelationPartnerRepository repository;

  @Test
  void newCommercialpartner() {
    ExternalRelationPartner partner = new ExternalRelationPartner();
    partner.setRelationNumber(8210);
    partner.setRelationName("2CVGB");
    partner.setContactName("Mark Lewis");
    Address address = new Address();
    address.setAddress("Helena Hoeve");
    address.setAddressNumber("26");
    address.setCity("Gouda");
    partner.setAddress(address);
    repository.save(partner);
    long count = repository.count();
    Assertions.assertEquals(1L, count);
  }

}
