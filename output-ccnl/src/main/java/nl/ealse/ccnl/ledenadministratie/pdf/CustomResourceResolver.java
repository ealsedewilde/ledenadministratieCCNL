package nl.ealse.ccnl.ledenadministratie.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import org.apache.xmlgraphics.io.Resource;
import org.apache.xmlgraphics.io.ResourceResolver;

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
    return new Resource(uri.toURL().openStream());
  }

  @Override
  public OutputStream getOutputStream(URI uri) throws IOException {
    throw new UnsupportedOperationException();
  }

}
