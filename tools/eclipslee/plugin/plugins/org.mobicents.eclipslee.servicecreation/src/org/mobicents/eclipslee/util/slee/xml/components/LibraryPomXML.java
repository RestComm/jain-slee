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

package org.mobicents.eclipslee.util.slee.xml.components;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The root XML for a library pom XML file.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class LibraryPomXML extends DTDXML {

  public static final String QUALIFIED_NAME = "project";

  public static final String PUBLIC_ID = "Maven POM File (ignore this)";
  public static final String SYSTEM_ID = "Maven POM File (ignore this)";

  /**
   * Create a new LibraryJar with the specified EntityResolver.
   * 
   * @param resolver
   * @throws ParserConfigurationException
   */

  public LibraryPomXML(EntityResolver resolver, InputSource dummyXML) throws ParserConfigurationException {
    super(QUALIFIED_NAME, PUBLIC_ID, SYSTEM_ID, resolver);
  }

  /**
   * Parse the provided InputStream as though it contains JAIN SLEE Library XML Data.
   * @param stream
   */
  public LibraryPomXML(InputStream stream, EntityResolver resolver, InputSource dummyXML) throws SAXException, IOException, ParserConfigurationException {
    super(stream, resolver);	

    boolean found = false;
    // Verify that this is really a library POM XML file.
    Element[] elements = getNodes("project/build/plugins/plugin");
    for (int i = 0; i < elements.length; i++) {
      if(getChildText(elements[i], "artifactId").equals("maven-library-plugin")) {
        found = true;
        break;
      }
    }

    if (!found) {
      throw new SAXException("This was not a library pom XML file.");
    }
  }

  public LibraryXML[] getLibraries() {
    Element[] elements = getNodes("project/build/plugins/plugin");
    for (int i = 0; i < elements.length; i++) {
      if(getChildText(elements[i], "artifactId").equals("maven-library-plugin")) {
        return new LibraryXML[]{new LibraryXML(document, getChild(elements[i], "configuration"), dtd)};
      }
    }

    return new LibraryXML[0];		
  }

  public LibraryXML getLibrary(String name, String vendor, String version) throws ComponentNotFoundException {

    if (name == null) throw new NullPointerException("Name cannot be null.");
    if (vendor == null) throw new NullPointerException("Vendor cannot be null.");
    if (version == null) throw new NullPointerException("Version cannot be null.");

    LibraryXML libraries[] = getLibraries();
    for (int i = 0; i < libraries.length; i++) {
      if (libraries[i].getName().equals(name)
          && libraries[i].getVendor().equals(vendor)
          && libraries[i].getVersion().equals(version))
        return libraries[i];			
    }

    throw new ComponentNotFoundException("Specified library could not be found.");
  }

  public LibraryXML addLibrary(String name, String vendor, String version, String description) throws DuplicateComponentException {
    boolean found = true;
    try {
      getLibrary(name, vendor, version);			
    }
    catch (ComponentNotFoundException e) {
      found = false;
    }
    finally {
      if (found) {
        throw new DuplicateComponentException("A library with the same name, vendor and version combination already exists.");
      }
    }

    Element elements[] = getNodes("library-jar");
    Element newLibrary = addElement(elements[0], "library");
    addElement(newLibrary, "description").appendChild(document.createTextNode(description));
    addElement(newLibrary, "library-name").appendChild(document.createTextNode(name));
    addElement(newLibrary, "library-vendor").appendChild(document.createTextNode(vendor));
    addElement(newLibrary, "library-version").appendChild(document.createTextNode(version));

    return new LibraryXML(document, newLibrary, dtd);
  }

  public void removeLibrary(String name, String vendor, String version) throws ComponentNotFoundException {
    LibraryXML library = getLibrary(name, vendor, version);
    removeLibrary(library);
  }

  public void removeLibrary(LibraryXML library) {
    if (library == null) throw new NullPointerException("library must be non-null");

    library.getRoot().getParentNode().removeChild(library.getRoot());		
  }

  public String toString() {
    String output = "";
    LibraryXML libraries[] = getLibraries();
    for (int i = 0; i < libraries.length; i++) {
      if (i > 0) {
        output += ", ";
      }
      output += "[" + libraries[i].toString() + "]";
    }
    return output;
  }


  //	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  //	  ...
  //  <build>
  //    <plugins>
  //      <plugin>
  //        <groupId>org.mobicents.tools</groupId>
  //          <artifactId>maven-library-plugin</artifactId>
  //          <executions>
  //            <execution>
  //                <goals>
  //                    <goal>copy-dependencies</goal>
  //                      <goal>generate-descriptor</goal>
  //                    </goals>
  //                </execution>
  //            </executions>
  //          <configuration>
  //            <library-name>smack</library-name>
  //              <library-vendor>org.mobicents</library-vendor>
  //              <library-version>2.0</library-version>
  //            </configuration>
  //        </plugin>
  //      </plugins>
  //    </build>
  //	</project>
}
