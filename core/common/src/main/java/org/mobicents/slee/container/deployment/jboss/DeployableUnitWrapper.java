package org.mobicents.slee.container.deployment.jboss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.logging.Logger;

/**
 * 
 * DeployableUnit Wrapper for providing some useful URL manipulation and VFS hacks.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class DeployableUnitWrapper {
  // The logger.
  private static Logger logger = Logger.getLogger(DeployableUnitWrapper.class);

  private URL url;

  private String fileName;

  private String fullPath;

  public DeployableUnitWrapper(URL url) {
    try {
      gatherInfoFromURL(url);
    }
    catch (MalformedURLException e) {
      logger.error(e.getLocalizedMessage(), e);
    }
  }

  // Private ------------------------------------------------------- 

  private void gatherInfoFromURL(URL url) throws MalformedURLException {
    // Weird VFS behavior... returns jar:file:...jar!/
    if(url.getProtocol().equals("jar")) {
      this.url = new URL(url.getFile().replaceFirst("!/", "/"));
    }
    else {
      this.url = url;
    }

    this.fullPath = this.url.getFile();

    this.fileName = getFileNameInternal(fullPath);
  }

  private String getFileNameInternal(String fullPath) {
    String fileName = fullPath;

    if (fileName.endsWith("/")) {
      fileName = fileName.substring(0, fileName.length() - 1);
    }

    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);

    return fileName;
  }

  // Getters and Setters -------------------------------------------

  public URL getUrl() {
    return url;
  }

  public String getFileName() {
    return fileName;
  }

  public String getFullPath() {
    return fullPath;
  }

  public URL getEntry(String path) {
    URL retURL = null;

    try {
      retURL = new URL(this.url + "/" + path);

      try {
        retURL.openStream().close();
      }
      catch ( IOException e ) {
        retURL = null;
      }
    }
    catch ( MalformedURLException e ) {
      logger.error(e.getLocalizedMessage(), e);
    }

    return  retURL;
  }
}
