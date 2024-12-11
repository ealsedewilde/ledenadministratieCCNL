package nl.ealse.ccnl.ledenadministratie.excelimport;

import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationPartnerRepository;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;

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

  @Override
  protected boolean validRelationNumber(int relatienummer) {
    return 85 == relatienummer / 100;
  }

}
