package nl.ealse.ccnl.control.other;

import nl.ealse.ccnl.control.external.ExternalRelationFormPages;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.javafx.PageId;

/**
 * Control the form pages for an external relation.
 */
public class ExternalOtherFormpages extends ExternalRelationFormPages<ExternalRelationOther, ExternalOtherController> {

  public ExternalOtherFormpages(ExternalOtherController controller) {
    super(controller);
    initialize(controller, new PageId("EXTERNAL_RELATION", "external/form/pageRelation"));
  }

}
