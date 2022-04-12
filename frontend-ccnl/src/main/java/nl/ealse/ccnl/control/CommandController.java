package nl.ealse.ccnl.control;

import nl.ealse.ccnl.control.annual.PaymentReminderReportCommand;
import nl.ealse.ccnl.control.annual.ResetPaymentCommand;
import nl.ealse.ccnl.control.excel.ExcelExportCommand;
import nl.ealse.ccnl.control.settings.BackupRestoreCommand;
import nl.ealse.ccnl.control.settings.SepaAuthorizationFormCommand;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Entrypoint for menu choices without fxml.
 * Only instantiated components can receive events.
 * Since lazy instantiation is the default eventing will not work in all cases.  
 * For menuchoices with fxml, the fxml is loaded before the event is fired.
 * Loading the fxml will instantiate the controller, so there eventing will work.
 * For menu choices without fxml eventing will not work.
 * This class is explicitly eargerly instantiated, so it can handle events and
 * instantiate the correct command.
 * @author ealse
 */
@Component
@Lazy(false) 
public class CommandController {

  private final ApplicationContext springContext;
  
  public CommandController(ApplicationContext springContext) {
    this.springContext = springContext;
  }
  
  @EventListener(condition = "#event.group('REPORTS')")
  public void onReport(MenuChoiceEvent event) {
    springContext.getBean(ExcelExportCommand.class).executeCommand(event);
  }
  
  @EventListener(condition = "#event.group('COMMANDS')")
  public void onCommand(MenuChoiceEvent event) {
    switch(event.getMenuChoice()) {
      case PRODUCE_REMINDER_REPORT:
        springContext.getBean(PaymentReminderReportCommand.class).executeCommand();
        break;
      case RESET_PAYMENTS:
        springContext.getBean(ResetPaymentCommand.class).executeCommand();
        break;
      case MANAGE_BACKUP_DATABASE:
        springContext.getBean(BackupRestoreCommand.class).executeCommand();
        break;      
      case MANAGE_SEPA_FORM:
        springContext.getBean(SepaAuthorizationFormCommand.class).executeCommand();
        break;      
      default:  
    }
    
  }


}
