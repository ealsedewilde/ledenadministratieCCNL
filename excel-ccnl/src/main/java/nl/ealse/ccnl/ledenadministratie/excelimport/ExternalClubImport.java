package nl.ealse.ccnl.ledenadministratie.excelimport;

import java.time.LocalDate;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationClubRepository;
import nl.ealse.ccnl.ledenadministratie.excel.club.CCNLClub;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;

public class ExternalClubImport extends BaseImport<ExternalRelationClub, CCNLClub> {

  public ExternalClubImport(ExternalRelationClubRepository repository, ProcessType processType) {
    super(repository, processType);
    setExistingNumbers(repository.getAllRelationNumbers());
  }

  public void fillRelation(CCNLClub ccnlPartner, ExternalRelationClub relation) {
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
  protected boolean validRelationNumber(int relatienummer) {
    return 82 == relatienummer / 100;
  }

}
