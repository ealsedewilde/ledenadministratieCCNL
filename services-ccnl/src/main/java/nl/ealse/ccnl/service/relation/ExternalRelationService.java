package nl.ealse.ccnl.service.relation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;
import nl.ealse.ccnl.ledenadministratie.util.NumberFactory;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ExternalRelationService<T extends ExternalRelation> {
  {log.info("Service created");}

  private final NumberFactory numberFactory;

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

  public void save(T externalRelation) {
    getDao().save(externalRelation);
  }

  public void deleteExternalRelation(T externalRelation) {
    getDao().delete(externalRelation);
  }

  public abstract ExternalRelationRepository<T> getDao();

}
