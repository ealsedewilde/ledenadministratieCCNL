package nl.ealse.ccnl.service.relation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dao.InternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;

@Slf4j
public class InternalRelationService {
  
  @Getter
  private static InternalRelationService instance = new InternalRelationService();

  private final InternalRelationRepository dao;

  private InternalRelationService() {
    log.info("Service created");
    this.dao = InternalRelationRepository.getInstance();
  }

  public List<String> getAllTitles() {
    return dao.getAllTitles();
  }

  public List<InternalRelation> searchInternalRelation(SearchItem searchItem, String searchValue) {
    List<InternalRelation> result = new ArrayList<>();
    switch (searchItem) {
      case CITY:
        result.addAll(dao.findInternalRelationsByCity(searchValue));
        break;
      case NAME:
        result.addAll(dao.findInternalRelationsByTitle(searchValue));
        break;
      case NUMBER:
        Integer id = Integer.parseInt(searchValue);
        Optional<InternalRelation> m = dao.findById(id);
        if (m.isPresent()) {
          result.add(m.get());
        }
        break;
      case POSTAL_CODE:
        result.addAll(dao.findInternalRelationsByPostalCode(searchValue));
        break;
      case STREET:
        result.addAll(dao.findInternalRelationsByAddress(searchValue));
        break;
      default:
        break;

    }
    return result;
  }

  public void save(InternalRelation internalRelation) {
    dao.save(internalRelation);
  }

  public void deleteInternalRelation(InternalRelation internalRelation) {
    dao.delete(internalRelation);
  }

}
