package nl.ealse.ccnl.control.settings;

import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class TemplateSelectionEvent extends ApplicationEvent {

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
