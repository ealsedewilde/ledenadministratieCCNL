package nl.ealse.ccnl.ledenadministratie.excelimport;

import java.util.List;
import java.util.Optional;
import lombok.Setter;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLAdres;
import nl.ealse.ccnl.ledenadministratie.excelimport.ImportService.ProcessType;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationRepository;

public abstract class ExternalImport<T extends ExternalRelation, S extends CCNLAdres> {

  private final ExternalRelationRepository<T> repository;

  private final ProcessType processType;

  @Setter
  List<Number> existingNumbers;

  protected ExternalImport(ExternalRelationRepository<T> otherRepository, ProcessType processType) {
    this.repository = otherRepository;
    this.processType = processType;
  }

  public void importExternalRelation(S source) {
    if (process(source.getRelatienummer())) {
      Optional<T> optionalRelation = repository.findById(source.getRelatienummer());
      T relation;
      if (optionalRelation.isEmpty()) {
        relation = newInstance();
        fillExternalRelation(source, relation);
        repository.save(relation);
      } else if (processType != ProcessType.ADD) {
        relation = optionalRelation.get();
        fillExternalRelation(source, relation);
        repository.save(relation);
        if (processType == ProcessType.REPLACE) {
          existingNumbers.remove(indexOf(source.getRelatienummer()));
        }
      }
    }
  }

  public void finalizeImport() {
    if (processType == ProcessType.REPLACE) {
      for (Number nr : existingNumbers) {
        Integer id;
        if (nr instanceof Integer) {
          id = (Integer) nr;
        } else {
          id = Integer.valueOf(nr.intValue());
        }
        repository.deleteById(id);
      }

    }
  }

  private void fillExternalRelation(S source, T relation) {
    fillEntity(source, relation);
    relation.setAddress(AddressMapping.mapAddress(source));
  }

  private int indexOf(int relatienummer) {
    for (int ix = 0; ix < existingNumbers.size(); ix++) {
      if (existingNumbers.get(ix).intValue() == relatienummer) {
        return ix;
      }
    }
    return -1;
  }

  protected abstract boolean process(int relatienummer);

  protected abstract void fillEntity(S source, T relation);

  protected abstract T newInstance();

}
