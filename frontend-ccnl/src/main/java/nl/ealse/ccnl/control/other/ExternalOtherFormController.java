package nl.ealse.ccnl.control.other;

import nl.ealse.ccnl.control.external.ExternalRelationFormController;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;

/**
 * Control the form pages for an external relation.
 */
public class ExternalOtherFormController
    extends ExternalRelationFormController<ExternalRelationOther, ExternalOtherController> {

  public ExternalOtherFormController(ExternalOtherController controller) {
    super(controller);
  }

  @Override
  protected void initializePages() {
    initialize("external/form/pageRelation", "Relatiegegevens");
  }

}
