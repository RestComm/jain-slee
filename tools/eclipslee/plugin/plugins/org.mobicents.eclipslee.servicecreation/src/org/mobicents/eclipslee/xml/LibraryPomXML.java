/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.eclipslee.xml;

import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.xml.sax.SAXException;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class LibraryPomXML extends org.mobicents.eclipslee.util.slee.xml.components.LibraryPomXML {
  /** The location of the corresponding jar file*/
  private String jarLocation = null;
  /** The location of the <name>export.jar */
  private String  exportJarLocation = null;

  public LibraryPomXML() throws ParserConfigurationException, IOException {
    super(new NullEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
    this.osPath = null;
  }

  public LibraryPomXML(IFile file) throws SAXException, IOException, CoreException, ParserConfigurationException {
    super(file.getContents(), new NullEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
    this.osPath = file.getFullPath().toOSString();
  }

  public LibraryPomXML(JarFile jar, JarEntry entry, String location) throws SAXException, IOException, CoreException, ParserConfigurationException {
    super(jar.getInputStream(entry), new NullEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
    String fname = "jar:" + jar.getName() + "!" + entry.getName();
    this.osPath = fname;
    this.jarLocation = location;
  }

  public String getOSPath() {
    return osPath;
  }

  private final String osPath;

  /**
   * @return Returns the jarLocation.
   */
  public String getJarLocation() {
    return jarLocation;
  }

  /**
   * @param jarLocation The jarLocation to set.
   */
  public void setJarLocation(String jarLocation) {
    this.jarLocation = jarLocation;
  }

  public String getExportJarLocation() {
    return exportJarLocation;
  }

  public void setExportJarLocation(String exportJarLocation) {
    this.exportJarLocation = exportJarLocation;
  }
  
}
