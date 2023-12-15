package nl.ealse.ccnl.ledenadministratie.util;

import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationOtherRepository;

public class ExternalRelationNumberFactory extends NumberFactory {
  
  @Getter
  private static final ExternalRelationNumberFactory instance = new ExternalRelationNumberFactory();

  private final ExternalRelationOtherRepository dao = ExternalRelationOtherRepository.getInstance();
  
  private ExternalRelationNumberFactory() {
    super(99, 8400);
    initialize();
  }

  private void initialize() {
    super.initialize(dao.getAllRelationNumbers());

  }

}
