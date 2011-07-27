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
 * The root XML for a library jar XML file.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class LibraryJarXML extends DTDXML {

	public static final String QUALIFIED_NAME = "library-jar";

    public static final String PUBLIC_ID_1_1 = "-//Sun Microsystems, Inc.//DTD JAIN SLEE Library 1.1//EN";
    public static final String SYSTEM_ID_1_1 = "http://java.sun.com/dtd/slee-library-jar_1_1.dtd";

    public static final String PUBLIC_ID = PUBLIC_ID_1_1;
    public static final String SYSTEM_ID = SYSTEM_ID_1_1;

    /**
	 * Create a new LibraryJar with the specified EntityResolver.
	 * 
	 * @param resolver
	 * @throws ParserConfigurationException
	 */
	
	public LibraryJarXML(EntityResolver resolver, InputSource dummyXML) throws ParserConfigurationException {
		super(QUALIFIED_NAME, PUBLIC_ID, SYSTEM_ID, resolver);
		readDTDVia(resolver, dummyXML);
	}
	
	/**
	 * Parse the provided InputStream as though it contains JAIN SLEE Library XML Data.
	 * @param stream
	 */
	public LibraryJarXML(InputStream stream, EntityResolver resolver, InputSource dummyXML) throws SAXException, IOException, ParserConfigurationException {
		super(stream, resolver);	
		
		// Verify that this is really a library-jar XML file.
		if (!getRoot().getNodeName().equals(QUALIFIED_NAME))
			throw new SAXException("This was not a library XML file.");

		readDTDVia(resolver, dummyXML);
	}

	public LibraryXML[] getLibraries() {
		Element[] elements = getNodes("library-jar/library");		
		LibraryXML libraries[] = new LibraryXML[elements.length];
		
		for (int i = 0; i < elements.length; i++)
			libraries[i] = new LibraryXML(document, elements[i], dtd);
		
		return libraries;		
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

	
//	<library-jar>
//	  ...
//	  <library>
//	    <description> ... </description>
//	    <library-name> JCC </library-name>
//      <library-vendor> javax.csapi.cc.jcc </library-vendor>
//	    <library-version> 1.1 </library-version>
//	    <jar>
//	      <description> ... </description>
//	      <jar-name> jars/jcc-1.1.jar </jar-name>
//	    </jar>
//	  </library>
//	</library-jar>
}
