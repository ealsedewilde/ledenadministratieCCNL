package nl.ealse.ccnl.ledenadministratie.excelimport;

import nl.ealse.ccnl.ledenadministratie.excelimport.ImportService.ProcessType;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationPartnerRepository;

public class CommercialPartnerImport extends ExternalRelationImport<ExternalRelationPartner> {

  public CommercialPartnerImport(ExternalRelationPartnerRepository repository,
      ProcessType processType) {
    super(repository, processType);
    setExistingNumbers(repository.getAllRelationNumbers());
  }

  @Override
  protected ExternalRelationPartner newInstance() {
    return new ExternalRelationPartner();
  }

}
