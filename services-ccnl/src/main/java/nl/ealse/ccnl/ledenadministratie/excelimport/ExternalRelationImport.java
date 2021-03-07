package nl.ealse.ccnl.ledenadministratie.excelimport;

import java.time.LocalDate;
import nl.ealse.ccnl.ledenadministratie.excel.partner.CCNLPartner;
import nl.ealse.ccnl.ledenadministratie.excelimport.ImportService.ProcessType;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.util.DateUtil;

public abstract class ExternalRelationImport<T extends ExternalRelation>
    extends ExternalImport<T, CCNLPartner> {

  protected ExternalRelationImport(ExternalRelationRepository<T> repository,
      ProcessType processType) {
    super(repository, processType);
  }

  protected void fillEntity(CCNLPartner ccnlPartner, T relation) {
    relation.setEmail(ccnlPartner.getEmail());
    relation.setContactName(ccnlPartner.getContactpersoon());
    relation.setContactNamePrefix(ccnlPartner.getAanhef());
    relation.setRelationName(ccnlPartner.getPartnerNaam());
    relation.setRelationNumber(ccnlPartner.getPartnerNummer());
    relation.setRelationSince(DateUtil.toLocaleDate(ccnlPartner.getPartnerVanaf()));
    relation.setTelephoneNumber(ccnlPartner.getTelefoon());
    relation.setRelationInfo(ccnlPartner.getOpmerking());
    relation.setModificationDate(LocalDate.now());

    relation.setAddress(AddressMapping.mapAddress(ccnlPartner));
  }

  @Override
  protected boolean process(int relatienummer) {
    return relatienummer < 8500;
  }

}
