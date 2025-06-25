package nl.ealse.ccnl;

import java.lang.reflect.InvocationTargetException;
import javafx.beans.property.ObjectProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import nl.ealse.ccnl.control.HandledTask;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import org.apache.commons.lang3.reflect.MethodUtils;

public class TestExecutor extends TaskExecutor {

  @Override
  public void execute(Runnable task) {
    HandledTask fxt = (HandledTask) task;
    try {
      String result = (String) MethodUtils.invokeMethod(fxt, true, "call");
      ObjectProperty<String> v = (ObjectProperty<String>) fxt.valueProperty();
      v.set(result);
      EventHandler<WorkerStateEvent> e = fxt.getOnSucceeded();
      WorkerStateEvent evt = new WorkerStateEvent(fxt, WorkerStateEvent.WORKER_STATE_SUCCEEDED);
      e.handle(evt);
    } catch (InvocationTargetException e) {
      ObjectProperty<Throwable> t = (ObjectProperty<Throwable>) fxt.exceptionProperty();
      t.setValue(e.getTargetException());
      EventHandler<WorkerStateEvent> f = fxt.getOnFailed();
      WorkerStateEvent evt = new WorkerStateEvent(fxt, WorkerStateEvent.WORKER_STATE_FAILED);
      f.handle(evt);
    } catch (Exception e) {
      // not possible
    }
    ApplicationContext.getEntityManagerProvider().getEntityManager().clear();
  }

}
