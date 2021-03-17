package nl.ealse.ccnl.ledenadministratie.excelimport;

import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationOtherRepository;

public class OtherExternalRelationImport extends ExternalRelationImport<ExternalRelationOther> {

  public OtherExternalRelationImport(ExternalRelationOtherRepository repository,
      ProcessType processType) {
    super(repository, processType);
    setExistingNumbers(repository.getAllRelationNumbers());
  }

  @Override
  protected ExternalRelationOther newInstance() {
    return new ExternalRelationOther();
  }

}
