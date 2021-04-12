package nl.ealse.ccnl.ledenadministratie.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.xmlgraphics.io.Resource;
import org.apache.xmlgraphics.io.ResourceResolver;
import org.springframework.core.io.ClassPathResource;

/**
 * Resolve resource URI's to absolute paths.
 * 
 * @author ealse
 *
 */
public class CustomResourceResolver implements ResourceResolver {

  /**
   * Classpath either on file system or with a jar.
   */
  private final String classpathBase;

  /**
   * 
   * @param baseUri = locations of 'fop.xconf' file
   */
  public CustomResourceResolver(URI baseUri) {
    classpathBase = baseUri.toString();
  }

  /**
   * Resolve the resource for the URI. The resource can be somewhere on the file system or on the
   * classpath in a jar.
   */
  @Override
  public Resource getResource(URI uri) throws IOException {
    if ("file".equals(uri.getScheme())) {
      Path resourcePath = Paths.get(uri);
      return new Resource(Files.newInputStream(resourcePath));
    } else {
      // resource is on the (BOOT-INF) classpath in a jar.
      String path = classpathBase + uri.toString();
      ClassPathResource resource = new ClassPathResource(path);
      return new Resource(resource.getInputStream());
    }
  }

  @Override
  public OutputStream getOutputStream(URI uri) throws IOException {
    throw new UnsupportedOperationException();
  }

}
