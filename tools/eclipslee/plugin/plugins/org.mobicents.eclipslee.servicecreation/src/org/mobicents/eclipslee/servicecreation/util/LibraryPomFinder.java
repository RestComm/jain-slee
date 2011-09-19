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

package org.mobicents.eclipslee.servicecreation.util;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IFile;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.xml.LibraryPomXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class LibraryPomFinder extends BaseFinder {

  @Override
  protected DTDXML loadJar(JarFile file, JarEntry entry, String jarLocation) throws Exception {
    return new LibraryPomXML(file, entry, jarLocation);
  }

  private static LibraryPomFinder libraryFinder = new LibraryPomFinder();

  public static LibraryPomFinder getDefault() {
    return libraryFinder;
  }

  public DTDXML loadJar(JarFile jar, JarEntry entry) throws Exception {
    return new LibraryPomXML(jar, entry, (String)null);
  }

  public DTDXML loadFile(IFile file) throws Exception {
    return new LibraryPomXML(file);
  }

  protected DTDXML getInnerXML(DTDXML outerXML, String className) throws Exception {
    return null;
  }

  public static LibraryPomXML getLibraryJarXML(IFile file) {
    try {
      LibraryPomXML xml = new LibraryPomXML(file);
      return xml;
    }
    catch (Exception e) {
      return null;
    }
  }

}
