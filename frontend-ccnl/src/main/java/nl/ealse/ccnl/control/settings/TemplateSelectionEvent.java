package nl.ealse.ccnl.control.settings;

import java.util.EventObject;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;

@SuppressWarnings("serial")
public class TemplateSelectionEvent extends EventObject {

  @Getter
  private final boolean newTemplate;

  @Getter
  private final transient DocumentTemplate selectedTemplate;

  public TemplateSelectionEvent(Object source, DocumentTemplate template, boolean newTemplate) {
    super(source);
    this.selectedTemplate = template;
    this.newTemplate = newTemplate;
  }

}
