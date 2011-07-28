/**
 *   Copyright 2005 Open Cloud Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.mobicents.eclipslee.util.slee.xml.components;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.xml.SLEEEntityResolver;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * @author allenc
 *
 * The root XML for a profile specification jar XML file.
 */
public class ProfileSpecJarXML extends DTDXML {

	public static final String QUALIFIED_NAME = "profile-spec-jar";
	public static final String PUBLIC_ID_1_0 = "-//Sun Microsystems, Inc.//DTD JAIN SLEE Profile Specification 1.0//EN";
	public static final String SYSTEM_ID_1_0 = "http://java.sun.com/dtd/slee-profile-spec-jar_1_0.dtd";

    public static final String PUBLIC_ID_1_1 = "-//Sun Microsystems, Inc.//DTD JAIN SLEE Profile Specification 1.1//EN";
    public static final String SYSTEM_ID_1_1 = "http://java.sun.com/dtd/slee-profile-spec-jar_1_1.dtd";

    public static final String PUBLIC_ID = PUBLIC_ID_1_1;
    public static final String SYSTEM_ID = SYSTEM_ID_1_1;

    /**
	 * Create a new ProfileSpecJar with the specified EntityResolver.
	 * 
	 * @param resolver
	 * @throws ParserConfigurationException
	 */
	
	public ProfileSpecJarXML(EntityResolver resolver, InputSource dummyXML) throws ParserConfigurationException {
		super(QUALIFIED_NAME, PUBLIC_ID, SYSTEM_ID, resolver);
		readDTDVia(resolver, dummyXML);
	}
	
	/**
	 * Parse the provided InputStream as though it contains JAIN SLEE Profile Specification XML Data.
	 * @param stream
	 */
	
	public ProfileSpecJarXML(InputStream stream, EntityResolver resolver, InputSource dummyXML) throws SAXException, IOException, ParserConfigurationException {
		super(stream, resolver);	
		
		// Verify that this is really a profile-spec-jar XML file.
		if (!getRoot().getNodeName().equals(QUALIFIED_NAME))
			throw new SAXException("This was not a profile specification XML file.");

		readDTDVia(resolver, dummyXML);
	}

	/**@OSP modification by Sabri Skhiri*/
	public ProfileSpecJarXML(InputStream stream, SLEEEntityResolver resolver, InputSource dummyXML, String qualified_name)  throws SAXException, IOException, ParserConfigurationException{
	super(stream, resolver);	
		
		// Verify that this is really a osp-profile-spec-jar XML file.
		if (!getRoot().getNodeName().equals(qualified_name))
			throw new SAXException("This was not an OSP  profile specification XML file.");

		readDTDVia(resolver, dummyXML);
	}

	public ProfileSpecXML[] getProfileSpecs() {
		Element[] elements = getNodes("profile-spec-jar/profile-spec");		
		ProfileSpecXML specs[] = new ProfileSpecXML[elements.length];
		
		for (int i = 0; i < elements.length; i++)
			specs[i] = new ProfileSpecXML(document, elements[i], dtd);
		
		return specs;		
	}
		
	public ProfileSpecXML getProfileSpec(String name, String vendor, String version) throws ComponentNotFoundException {

		if (name == null) throw new NullPointerException("Name cannot be null.");
		if (vendor == null) throw new NullPointerException("Vendor cannot be null.");
		if (version == null) throw new NullPointerException("Version cannot be null.");
	
		ProfileSpecXML specs[] = getProfileSpecs();
		for (int i = 0; i < specs.length; i++) {
			if (specs[i].getName().equals(name)
					&& specs[i].getVendor().equals(vendor)
					&& specs[i].getVersion().equals(version))
				return specs[i];			
		}
		
		throw new ComponentNotFoundException("Specified profile specification could not be found.");
	}

	public ProfileSpecXML getProfileSpec(String cmpClassName) throws ComponentNotFoundException, SAXException {
		
		if (cmpClassName == null) throw new NullPointerException("Class name may not be null.");

		ProfileSpecXML specs[] = getProfileSpecs();
		for (int i = 0; i < specs.length; i++) {
			
			if (cmpClassName.equals(specs[i].getCMPInterfaceName()))
				return specs[i];
			
			if (cmpClassName.equals(specs[i].getManagementInterfaceName()))
				return specs[i];
			
			if (cmpClassName.equals(specs[i].getManagementAbstractClassName()))
				return specs[i];
		}			
		
		throw new ComponentNotFoundException("Specified profile specification could not be found.");
	}
	
	public ProfileSpecXML addProfileSpec(String name, String vendor, String version, String description) throws DuplicateComponentException {
		
		boolean found = true;
		try {
			getProfileSpec(name, vendor, version);			
		} catch (ComponentNotFoundException e) {
			found = false;
		} finally {
			if (found)
				throw new DuplicateComponentException("A profile specification with the same name, vendor and version combination already exists.");
		}

		Element elements[] = getNodes("profile-spec-jar");
		Element newSpec = addElement(elements[0], "profile-spec");
		addElement(newSpec, "description").appendChild(document.createTextNode(description));
		addElement(newSpec, "profile-spec-name").appendChild(document.createTextNode(name));
		addElement(newSpec, "profile-spec-vendor").appendChild(document.createTextNode(vendor));
		addElement(newSpec, "profile-spec-version").appendChild(document.createTextNode(version));
		
		return new ProfileSpecXML(document, newSpec, dtd);
	}
	
	public void removeProfileSpec(String name, String vendor, String version) throws ComponentNotFoundException {
		ProfileSpecXML spec = getProfileSpec(name, vendor, version);
		removeProfileSpec(spec);
	}
	
	public void removeProfileSpec(ProfileSpecXML spec) {
		if (spec == null) throw new NullPointerException("spec must be non-null");
	
		spec.getRoot().getParentNode().removeChild(spec.getRoot());		
	}

	public String toString() {
		String output = "";
		ProfileSpecXML profiles[] = getProfileSpecs();
		for (int i = 0; i < profiles.length; i++) {
			if (i > 0)
				output += ", ";
			output += "[" + profiles[i].toString() + "]";
		}
		return output;
	}

	
	// profile-spec-jar/profile-spec/profile-spec-name
	// profile-spec-vendor
	// profile-spec-version
	// profile-classes/profile-cmp-interface-name
	//                 profile-management-interface-name
	//                 profile-management-abstract-class-name
	// profile-index (unique=Foo)

}
