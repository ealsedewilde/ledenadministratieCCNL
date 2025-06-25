package nl.ealse.ccnl.ledenadministratie.dao;

import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CommercialPartnerTest {
  
  @BeforeAll
  private static void setup() {
    ApplicationContext.start();
  }

  private ExternalRelationPartnerRepository repository = new ExternalRelationPartnerRepository();

  @Test
  void newCommercialpartner() {
    ExternalRelationPartner partner = new ExternalRelationPartner();
    partner.setRelationNumber(8210);
    partner.setRelationName("2CVGB");
    partner.setContactName("Mark Lewis");
    Address address = new Address();
    address.setStreet("Helena Hoeve");
    address.setAddressNumber("26");
    address.setCity("Gouda");
    partner.setAddress(address);
    repository.save(partner);
    long count = repository.count();
    Assertions.assertEquals(1L, count);
  }

}
