package nl.ealse.ccnl.test;

import static org.mockito.Mockito.mock;
import java.util.HashMap;
import java.util.Map;
import nl.ealse.ccnl.TaskExecutor;
import nl.ealse.ccnl.TestExecutor;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.ioc.ComponentProvider;

public class TestComponentProvider implements ComponentProvider {

  private final Map<Class<?>, Object> mocks = new HashMap<>();

  public TestComponentProvider() {
    mocks.put(PageController.class, mock(PageController.class));
    mocks.put(TaskExecutor.class, new TestExecutor());
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getComponent(Class<T> clazz) {
    Object mockComponent = mocks.get(clazz);
    if (mockComponent == null) {
      mockComponent = mock(clazz);
      mocks.put(clazz, mockComponent);
    }
    return (T) mockComponent;

  }

}
