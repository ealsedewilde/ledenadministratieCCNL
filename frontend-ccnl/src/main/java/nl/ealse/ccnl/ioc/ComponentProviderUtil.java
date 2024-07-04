package nl.ealse.ccnl.ioc;

import java.util.Optional;
import java.util.ServiceLoader;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ComponentProviderUtil {
  
  private final ComponentProvider provider;
  
  static {
    ServiceLoader<ComponentProvider> serviceLoader = ServiceLoader.load(ComponentProvider.class);
    Optional<ComponentProvider> first = serviceLoader.findFirst();
    if (first.isPresent()) {
      provider = first.get();
    } else {
      throw new ExceptionInInitializerError("No ComponentProvider available");
    }
  }
  
  public <T> T getComponent(Class<T> clazz) {
    return provider.getComponent(clazz);
  }

}
