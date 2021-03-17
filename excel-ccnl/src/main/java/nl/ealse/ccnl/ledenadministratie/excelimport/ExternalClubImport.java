package nl.ealse.ccnl.ledenadministratie.excelimport;

import java.time.LocalDate;
import nl.ealse.ccnl.ledenadministratie.excel.club.CCNLClub;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationClubRepository;

public class ExternalClubImport extends ExternalImport<ExternalRelationClub, CCNLClub> {

  public ExternalClubImport(ExternalRelationClubRepository repository, ProcessType processType) {
    super(repository, processType);
    setExistingNumbers(repository.getAllRelationNumbers());
  }

  protected void fillEntity(CCNLClub ccnlPartner, ExternalRelationClub relation) {
    relation.setContactName(ccnlPartner.getContactpersoon());
    relation.setContactNamePrefix(ccnlPartner.getAanhef());
    relation.setRelationName(ccnlPartner.getClubNaam());
    relation.setRelationNumber(ccnlPartner.getClubNummer());
    relation.setModificationDate(LocalDate.now());

    relation.setAddress(AddressMapping.mapAddress(ccnlPartner));
  }

  @Override
  protected ExternalRelationClub newInstance() {
    return new ExternalRelationClub();
  }

  @Override
  protected boolean process(int relatienummer) {
    return true;
  }

}
