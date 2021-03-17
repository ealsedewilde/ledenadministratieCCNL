package nl.ealse.ccnl.test;

import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.core.task.TaskExecutor;

public class TestExecutor<T extends Task<?>> implements TaskExecutor {

  @SuppressWarnings("unchecked")
  @Override
  public void execute(Runnable task) {
    T fxt = (T) task;
    try {
      Object result = MethodUtils.invokeMethod(fxt, true, "call");
      ObjectProperty<Object>  v = (ObjectProperty<Object>) fxt.valueProperty();
      v.set(result);
      EventHandler<WorkerStateEvent> e = fxt.getOnSucceeded();
      WorkerStateEvent evt = new WorkerStateEvent(fxt, WorkerStateEvent.WORKER_STATE_SUCCEEDED);
      e.handle(evt);
    } catch (Exception e) {
      EventHandler<WorkerStateEvent> f = fxt.getOnFailed();
      WorkerStateEvent evt = new WorkerStateEvent(fxt, WorkerStateEvent.WORKER_STATE_FAILED);
      f.handle(evt);
    }
    
  }

}
