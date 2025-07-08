package nl.ealse.ccnl.ioc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.test.FXBase;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.Index;
import org.jboss.jandex.IndexReader;
import org.jboss.jandex.MethodInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class ControllerProviderTest extends FXBase {
  
  private final ComponentFactory provider = new DefaultComponentFactory();
  private final Map<Class<?>, Object> componentMap = new HashMap<>();

  
  @Test
  void testAllController() {
    Assertions.assertTrue(runFX(() -> {
      try (InputStream input = getClass().getResourceAsStream("/META-INF/jandex.idx")) {
        IndexReader reader = new IndexReader(input);
        Index index = reader.read();
        Collection<AnnotationInstance> annotations = index.getAnnotations(EventListener.class);
        annotations.forEach(this::processAnnotation);
      } catch (IOException e) {
        log.error("Unable to initialize", e);
      }
    }));
    assertEquals(42, componentMap.size());
 }

  private void processAnnotation(AnnotationInstance annotation) {
    MethodInfo method = annotation.target().asMethod();
    ClassInfo classInfo = method.declaringClass();
    String className = classInfo.name().toString();
    try {
      Class<?> clazz = Class.forName(className);
      log.info("controller {}",clazz.getName());
      componentMap.put(clazz, provider.getComponent(clazz));
    } catch (Exception e) {
      log.error("Unable to get Component", e);
      fail();
    }
    
  }


}
