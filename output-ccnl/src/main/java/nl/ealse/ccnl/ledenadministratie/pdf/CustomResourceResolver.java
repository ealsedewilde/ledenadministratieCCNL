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
   * Resolve the resource for the URI. The resource can be somewhere on the file system or on the
   * classpath in a jar.
   */
  @Override
  public Resource getResource(URI uri) throws IOException {
    if ("file".equals(uri.getScheme())) {
      Path resourcePath = Paths.get(uri);
      return new Resource(Files.newInputStream(resourcePath));
    } else {
      ClassPathResource resourcex = new ClassPathResource(uri.toString());
      return new Resource(resourcex.getInputStream());
    }
  }

  @Override
  public OutputStream getOutputStream(URI uri) throws IOException {
    throw new UnsupportedOperationException();
  }

}
