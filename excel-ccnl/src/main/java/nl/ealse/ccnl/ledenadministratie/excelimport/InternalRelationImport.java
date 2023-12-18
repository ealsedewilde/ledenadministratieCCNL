package nl.ealse.ccnl.ledenadministratie.excelimport;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import nl.ealse.ccnl.ledenadministratie.dao.InternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.excel.intern.CCNLIntern;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;

public class InternalRelationImport {

  private final InternalRelationRepository repository;

  private final ProcessType processType;

  private final List<Number> existingNumbers;

  public InternalRelationImport(InternalRelationRepository repository, ProcessType processType) {
    this.repository = repository;
    this.processType = processType;
    this.existingNumbers = repository.getAllRelationNumbers();
  }

  public void importInternalRelation(CCNLIntern functie) {
    Optional<InternalRelation> optionalRelation = repository.findById(functie.getRelatienummer());
    InternalRelation relation;
    if (optionalRelation.isEmpty()) {
      relation = new InternalRelation();
      fillRelation(functie, relation);
      repository.save(relation);
    } else if (processType != ProcessType.ADD) {
      relation = optionalRelation.get();
      fillRelation(functie, relation);
      repository.save(relation);
      if (processType == ProcessType.REPLACE) {
        existingNumbers.remove(Integer.valueOf(functie.getRelatienummer()));
      }
    }
  }

  public void finalizeImport() {
    if (processType == ProcessType.REPLACE) {
      for (Number nr : existingNumbers) {
        Integer id;
        if (nr instanceof Integer i) {
          id = i;
        } else {
          id = Integer.valueOf(nr.intValue());
        }
        repository.deleteById(id);
      }

    }
  }

  private void fillRelation(CCNLIntern functie, InternalRelation relation) {
    relation.setRelationNumber(functie.getInternNummer());
    relation.setTitle(functie.getFunctie());
    relation.setContactName(functie.getContactpersoon());
    relation.setTelephoneNumber(functie.getTelefoon());
    relation.setModificationDate(LocalDate.now());

    relation.setAddress(AddressMapping.mapAddress(functie));
  }


}
