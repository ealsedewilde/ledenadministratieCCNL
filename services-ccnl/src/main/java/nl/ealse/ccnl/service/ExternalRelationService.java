package nl.ealse.ccnl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.util.NumberFactory;

@Slf4j
public abstract class ExternalRelationService<T extends ExternalRelation> {

  private final NumberFactory numberFactory;

  protected ExternalRelationService(NumberFactory numberFactory) {
    log.info("Service created");
    this.numberFactory = numberFactory;
  }

  public Integer getFreeNumber() {
    return numberFactory.getNewNumber();
  }

  public List<T> searchExternalRelation(SearchItem searchItem, String searchValue) {
    List<T> result = new ArrayList<>();
    switch (searchItem) {
      case CITY:
        result.addAll(getDao().findExternalRelationsByCity(searchValue));
        break;
      case NAME:
        result.addAll(getDao().findExternalRelationsByName(searchValue));
        break;
      case NUMBER:
        Integer id = Integer.parseInt(searchValue);
        Optional<T> m = getDao().findById(id);
        if (m.isPresent()) {
          result.add(m.get());
        }
        break;
      case POSTAL_CODE:
        result.addAll(getDao().findExternalRelationsByPostalCode(searchValue));
        break;
      case STREET:
        result.addAll(getDao().findExternalRelationsByAddress(searchValue));
        break;
      default:
        break;

    }
    return result;
  }

  public void persistExternalRelation(T externalRelation) {
    getDao().save(externalRelation);
  }

  public void deleteExternalRelation(T externalRelation) {
    getDao().delete(externalRelation);
  }

  public abstract ExternalRelationRepository<T> getDao();

}
