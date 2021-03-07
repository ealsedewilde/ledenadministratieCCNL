package nl.ealse.ccnl.control.settings;

import lombok.Getter;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.EntitySelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;

@SuppressWarnings("serial")
public class TemplateSelectionEvent extends EntitySelectionEvent<DocumentTemplate> {

  @Getter
  private final boolean newTemplate;

  public TemplateSelectionEvent(Object source, DocumentTemplate template, boolean newTemplate) {
    super(source, MenuChoice.TEMPLATES_OVERVIEW, template);
    this.newTemplate = newTemplate;
  }

}
