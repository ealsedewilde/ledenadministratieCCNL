package nl.ealse.ccnl.control.external;

import java.util.List;
import nl.ealse.ccnl.control.SearchController;
import nl.ealse.ccnl.event.EntitySelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;
import nl.ealse.ccnl.service.relation.ExternalRelationService;
import nl.ealse.ccnl.service.relation.SearchItem;
import org.springframework.context.ApplicationEventPublisher;

public abstract class ExternalRelationSearchController<T extends ExternalRelation>
    extends SearchController<T, EntitySelectionEvent<T>> {

  private final ExternalRelationService<T> externalRelationService;

  protected ExternalRelationSearchController(ApplicationEventPublisher eventPublisher,
      ExternalRelationService<T> externalRelationService) {
    super(eventPublisher);
    this.externalRelationService = externalRelationService;
  }

  @Override
  public List<T> doSearch(SearchItem searchItem, String value) {
    return externalRelationService.searchExternalRelation(searchItem, value);
  }



}
