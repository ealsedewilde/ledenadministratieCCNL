package nl.ealse.ccnl.ledenadministratie.excelimport;

import java.time.LocalDate;
import nl.ealse.ccnl.ledenadministratie.dao.InternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.excel.intern.CCNLIntern;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;

public class InternalRelationImport extends BaseImport<InternalRelation, CCNLIntern> {

  public InternalRelationImport(InternalRelationRepository repository, ProcessType processType) {
    super(repository, processType);
    setExistingNumbers(repository.getAllRelationNumbers());
  }

  public void fillRelation(CCNLIntern functie, InternalRelation relation) {
    relation.setRelationNumber(functie.getInternNummer());
    relation.setTitle(functie.getFunctie());
    relation.setContactName(functie.getContactpersoon());
    relation.setTelephoneNumber(functie.getTelefoon());
    relation.setModificationDate(LocalDate.now());

    relation.setAddress(AddressMapping.mapAddress(functie));
  }

  @Override
  protected InternalRelation newInstance() {
    return new InternalRelation();
  }
  @Override
  protected boolean validRelationNumber(int relatienummer) {
    return 80 == relatienummer / 100;
  }



}
