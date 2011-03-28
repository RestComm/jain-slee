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
 * @author allenc
 */
public class SbbJarXML extends DTDXML {

	public static final String QUALIFIED_NAME = "sbb-jar";
	public static final String PUBLIC_ID_1_0 = "-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.0//EN";
	public static final String SYSTEM_ID_1_0 = "http://java.sun.com/dtd/slee-sbb-jar_1_0.dtd";

    public static final String PUBLIC_ID_1_1 = "-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.1//EN";
    public static final String SYSTEM_ID_1_1 = "http://java.sun.com/dtd/slee-sbb-jar_1_1.dtd";

    public static final String PUBLIC_ID = PUBLIC_ID_1_1;
    public static final String SYSTEM_ID = SYSTEM_ID_1_1;

    public SbbJarXML(EntityResolver resolver, InputSource dummyXML) throws ParserConfigurationException {
		super(QUALIFIED_NAME, PUBLIC_ID, SYSTEM_ID, resolver);
		readDTDVia(resolver, dummyXML);
	}
	
	/**
	 * Parse the provided InputStream as though it contains JAIN SLEE Profile Specification XML Data.
	 * @param stream
	 */
	
	public SbbJarXML(InputStream stream, EntityResolver resolver, InputSource dummyXML) throws SAXException, IOException, ParserConfigurationException {
		super(stream, resolver);			

		// Verify that this is really an sbb-jar XML file.
		if (!getRoot().getNodeName().equals(QUALIFIED_NAME))
			throw new SAXException("This was not an SBB Jar XML file.");

		readDTDVia(resolver, dummyXML);
	}

	
	public SbbXML[] getSbbs() {
		Element elements[] = getNodes("sbb-jar/sbb");
		SbbXML sbbs[] = new SbbXML[elements.length];
		for (int i = 0; i < elements.length; i++)
			sbbs[i] = new SbbXML(document, elements[i], dtd);
		
		return sbbs;		
	}
	
	public SbbXML getSbb(String className) throws ComponentNotFoundException {
		SbbXML sbbs[] = getSbbs();
		for (int i = 0; i < sbbs.length; i++) {
			if (sbbs[i].getAbstractClassName().equals(className))
				return sbbs[i];
			
			if (className.equals(sbbs[i].getUsageInterfaceName()))
				return sbbs[i];
			
			if (className.equals(sbbs[i].getActivityContextInterfaceName()))
				return sbbs[i];
			
			if (className.equals(sbbs[i].getLocalInterfaceName()))
				return sbbs[i];

		}
		
		throw new ComponentNotFoundException("Unable to find specified SBB.");
	}
	
	public SbbXML getSbb(String name, String vendor, String version) throws ComponentNotFoundException {
		SbbXML sbbs[] = getSbbs();
		for (int i = 0; i < sbbs.length; i++) {
			SbbXML sbb = sbbs[i];
			
			if (name.equals(sbb.getName())
					&& vendor.equals(sbb.getVendor())
					&& version.equals(sbb.getVersion()))
					return sbb;
		}

		throw new ComponentNotFoundException("Unable to find specified SBB.");

	}
	
	public SbbXML addSbb() {	
		Element child = addElement(getRoot(), "sbb");
		return new SbbXML(document, child, dtd);
	}
	
	public void removeSbb(SbbXML sbb) {
		sbb.getRoot().getParentNode().removeChild(sbb.getRoot());
	}
	
	public String toString() {
		String output = "";
		SbbXML sbbs[] = getSbbs();
		for (int i = 0; i < sbbs.length; i++) {
			if (i > 0)
				output += ", ";
			output += "[" + sbbs[i].toString() + "]";
		}
		return output;
	}

}
