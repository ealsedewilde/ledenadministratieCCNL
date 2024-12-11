package nl.ealse.ccnl.ledenadministratie.excelimport;

import java.util.List;
import java.util.Optional;
import lombok.Setter;
import nl.ealse.ccnl.ledenadministratie.dao.BaseRepository;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLAdres;

public abstract class BaseImport<T extends Object, S extends CCNLAdres> {
  
  private final BaseRepository<T> repository;
  private final ProcessType processType;
  
  @Setter
  List<Number> existingNumbers;
  
  protected BaseImport(BaseRepository<T> repository, ProcessType processType) {
    this.repository = repository;
    this.processType = processType;
  }
  
  /**
   * Import a relation from Excel into the database;
   *
   * @param source - Excel representation of the relation
   */
  public void importRelation(S source) {
    if (validRelationNumber(source.getRelatienummer())) {
      Optional<T> optionalRelation = repository.findById(source.getRelatienummer());
      T relation;
      if (optionalRelation.isEmpty()) {
        relation = newInstance();
        fillRelation(source, relation);
        repository.save(relation);
      } else if (processType != ProcessType.ADD) {
        relation = optionalRelation.get();
        fillRelation(source, relation);
        repository.save(relation);
        if (processType == ProcessType.REPLACE) {
          existingNumbers.remove(Integer.valueOf(source.getRelatienummer()));
        }
      }
    }
  }

  /**
   * When the Excel contains all relations, remove obsolete relations from the database.
   */
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
  
  protected abstract boolean validRelationNumber(int relatienummer);
  
  protected abstract T newInstance();
  
  public abstract void fillRelation(S source, T relation);

}
