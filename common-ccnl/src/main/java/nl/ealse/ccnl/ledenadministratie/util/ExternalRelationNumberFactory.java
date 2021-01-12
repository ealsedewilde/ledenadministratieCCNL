package nl.ealse.ccnl.ledenadministratie.util;

import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationOtherRepository;
import org.springframework.stereotype.Component;

@Component
public class ExternalRelationNumberFactory extends NumberFactory {

  public ExternalRelationNumberFactory(ExternalRelationOtherRepository dao) {
    super(100, 8400);
    initialize(dao);
  }

  private void initialize(ExternalRelationOtherRepository dao) {
    super.initialize(dao.getAllRelationNumbers());

  }

}
