package nl.ealse.ccnl.test;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.mockito.Mockito;

@UtilityClass
public class MockProvider {
  
  public <T> T mock(Class<T> cls) {
    T mock  = Mockito.mock(cls);
    try {
      FieldUtils.writeDeclaredStaticField(cls, "instance", mock, true);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return mock;
  }

}
