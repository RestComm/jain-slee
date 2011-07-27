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

import org.mobicents.eclipslee.util.slee.xml.DTDHandler;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class LibraryXML extends DTDXML {

	/**
	 * Can only be created by classes in the same package.
	 * 
	 * @param document the parent Document
	 * @param root the profile-spec element relating to this profile specification
	 */
	
	protected LibraryXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
	public String getName() {
		return getChildText(getRoot(), "library-name");		
	}
	
	public void setName(String name) {
		setChildText(getRoot(), "library-name", name);
	}
	
	public String getVendor() {
		return getChildText(getRoot(), "library-vendor");
	}
	
	public void setVendor(String vendor) {
		setChildText(getRoot(), "library-vendor", vendor);
	}
	
	public String getVersion() {
		return getChildText(getRoot(), "library-version");
	}
	
	public void setVersion(String version) {
		setChildText(getRoot(), "library-version", version);
	}
	
	public String getDescription() {
		return getChildText(getRoot(), "description");
	}
	
	public void setDescription(String description) {
		setChildText(getRoot(), "description", description);
	}	
	
  public JarXML addJar(String jarName) {
    Element child = getChild(root, "jar");

    if(child == null) {
      child = addElement(root, "jar");
    }

    setChildText(child, "jar-name", jarName.trim());

    return new JarXML(document, child, dtd);   
  }

  public JarXML getJar() {
    Element child = getChild(root, "jar");

    return new JarXML(document, child, dtd);   
  }

  public LibraryRefXML addLibrary(LibraryXML library) {
    Element child = addElement(root, "library-ref");
    setChildText(child, "library-name", library.getName());
    setChildText(child, "library-vendor", library.getVendor());
    setChildText(child, "library-version", library.getVersion());
    return new LibraryRefXML(document, child, dtd);
  }

  public LibraryRefXML addLibrary(String name, String vendor, String version) {
    Element child = addElement(root, "library-ref");
    setChildText(child, "library-name", name.trim());
    setChildText(child, "library-vendor", vendor.trim());
    setChildText(child, "library-version", version.trim());
    return new LibraryRefXML(document, child, dtd);
  }

  public LibraryRefXML[] getLibraries() {
    //Element nodes[] = getNodes("resource-adaptor-type/event-type-ref");
    Element nodes[] = getNodes(root, "library-ref");
    LibraryRefXML xml[] = new LibraryRefXML[nodes.length];
    for (int i = 0; i < nodes.length; i++)
      xml[i] = new LibraryRefXML(document, nodes[i], dtd);    
    return xml;
  }
  
  public void removeLibrary(LibraryXML xml) {
    xml.getRoot().getParentNode().removeChild(xml.getRoot());
  }

  public LibraryRefXML getLibrary(String name, String vendor, String version) {
    LibraryRefXML libraries[] = getLibraries();

    for (int i = 0; i < libraries.length; i++) {
      if (name.equals(libraries[i].getName()) && vendor.equals(libraries[i].getVendor()) && version.equals(libraries[i].getVersion())) {
        return libraries[i];
      }
    }

    return null;
  }

  public String toString() {
		return "Library: " + getName() + ", " + getVersion() + ", " + getVendor();
	}

}
