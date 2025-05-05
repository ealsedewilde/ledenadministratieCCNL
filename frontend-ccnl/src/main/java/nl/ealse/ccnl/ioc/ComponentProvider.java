package nl.ealse.ccnl.ioc;

import java.util.Optional;
import java.util.ServiceLoader;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ComponentProvider {
  
  private final ComponentFactory factory;
  
  static {
    ServiceLoader<ComponentFactory> serviceLoader = ServiceLoader.load(ComponentFactory.class);
    Optional<ComponentFactory> first = serviceLoader.findFirst();
    if (first.isPresent()) {
      factory = first.get();
    } else {
      factory = new DefaultComponentFactory();
    }
  }
  
  public <T> T getComponent(Class<T> clazz) {
    return factory.getComponent(clazz);
  }

}
