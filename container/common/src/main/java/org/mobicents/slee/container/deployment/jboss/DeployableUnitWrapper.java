/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.deployment.jboss;

import org.jboss.logging.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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

  public DeployableUnitWrapper(URL url, String name) {
    try {
      gatherInfoFromURL(url);

      // Only for WildFly
      if (this.fileName.contentEquals("content")) {
        this.fileName = name;
      }
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
