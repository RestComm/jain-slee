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
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * @author cath
 */
public class ResourceAdaptorTypeJarXML extends DTDXML {

	public static final String QUALIFIED_NAME = "resource-adaptor-type-jar";
	public static final String PUBLIC_ID_1_0 = "-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor Type 1.0//EN";
	public static final String SYSTEM_ID_1_0 = "http://java.sun.com/dtd/slee-resource-adaptor-type-jar_1_0.dtd";

    public static final String PUBLIC_ID_1_1 = "-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor Type 1.1//EN";
    public static final String SYSTEM_ID_1_1 = "http://java.sun.com/dtd/slee-resource-adaptor-type-jar_1_1.dtd";

    public static final String PUBLIC_ID = PUBLIC_ID_1_1;
    public static final String SYSTEM_ID = SYSTEM_ID_1_1;

    public ResourceAdaptorTypeJarXML(EntityResolver resolver, InputSource dummyXML) throws ParserConfigurationException {
		super(QUALIFIED_NAME, PUBLIC_ID, SYSTEM_ID, resolver);
		readDTDVia(resolver, dummyXML);
	}
	
	/**
	 * Parse the provided InputStream as though it contains JAIN SLEE Profile Specification XML Data.
	 * @param stream
	 */
	
	public ResourceAdaptorTypeJarXML(InputStream stream, EntityResolver resolver, InputSource dummyXML) throws SAXException, IOException, ParserConfigurationException {
		super(stream, resolver);			

		// Verify that this is really an ratype-jar XML file.
		if (!getRoot().getNodeName().equals(QUALIFIED_NAME))
			throw new SAXException("This was not a resource adaptor type jar XML file.");

		readDTDVia(resolver, dummyXML);
	}

	
	public ResourceAdaptorTypeXML[] getResourceAdaptorTypes() {
		Element elements[] = getNodes("resource-adaptor-type-jar/resource-adaptor-type");
		ResourceAdaptorTypeXML ratypes[] = new ResourceAdaptorTypeXML[elements.length];
		for (int i = 0; i < elements.length; i++)
			ratypes[i] = new ResourceAdaptorTypeXML(document, elements[i], dtd);
		
		return ratypes;		
	}
	
	public ResourceAdaptorTypeXML getResourceAdaptorType(String name, String vendor, String version) throws ComponentNotFoundException {
		ResourceAdaptorTypeXML ratypes[] = getResourceAdaptorTypes();
		for (int i = 0; i < ratypes.length; i++) {
			ResourceAdaptorTypeXML ratype = ratypes[i];
			
			if (name.equals(ratype.getName())
					&& vendor.equals(ratype.getVendor())
					&& version.equals(ratype.getVersion()))
					return ratype;
		}

		throw new ComponentNotFoundException("Unable to find specified RA Type.");

	}
	
	public ResourceAdaptorTypeXML addResourceAdaptorType() {	
		Element child = addElement(getRoot(), "resource-adaptor-type");
		return new ResourceAdaptorTypeXML(document, child, dtd);
	}
	
	public void removeResourceAdaptorType(ResourceAdaptorTypeXML ratype) {
		ratype.getRoot().getParentNode().removeChild(ratype.getRoot());
	}
	
	public String toString() {
		String output = "";
		ResourceAdaptorTypeXML ratypes[] = getResourceAdaptorTypes();
		for (int i = 0; i < ratypes.length; i++) {
			if (i > 0)
				output += ", ";
			output += "[" + ratypes[i].toString() + "]";
		}
		return output;
	}

}
