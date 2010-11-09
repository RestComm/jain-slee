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
public class ServiceXML extends DTDXML {

	
	public static final String QUALIFIED_NAME = "service-xml";
	public static final String PUBLIC_ID_1_0 = "-//Sun Microsystems, Inc.//DTD JAIN SLEE Service 1.0//EN";
	public static final String SYSTEM_ID_1_0 = "http://java.sun.com/dtd/slee-service_1_0.dtd";
	
    public static final String PUBLIC_ID_1_1 = "-//Sun Microsystems, Inc.//DTD JAIN SLEE Service 1.1//EN";
    public static final String SYSTEM_ID_1_1 = "http://java.sun.com/dtd/slee-service_1_1.dtd";

    public static final String PUBLIC_ID = PUBLIC_ID_1_1;
    public static final String SYSTEM_ID = SYSTEM_ID_1_1;

    public ServiceXML(EntityResolver resolver, InputSource dummyXML) throws ParserConfigurationException {
		super(QUALIFIED_NAME, PUBLIC_ID, SYSTEM_ID, resolver);
		readDTDVia(resolver, dummyXML);
	}
		
	/**
	 * Parse the provided InputStream as though it contains JAIN SLEE Event XML Data.
	 * @param stream
	 */
	
	public ServiceXML(InputStream stream, EntityResolver resolver, InputSource dummyXML) throws SAXException, IOException, ParserConfigurationException {
		super(stream, resolver);
		
		// Verify that this is really a service XML file.
		if (!getRoot().getNodeName().equals(QUALIFIED_NAME))
			throw new SAXException("This was not a service XML file.");		
		
		readDTDVia(resolver, dummyXML);
	}

	public Service addService() {
		return new Service(document, addElement(root, "service"), dtd);		
	}
	
	public Service[] getServices() {
		Element nodes[] = getNodes(root, "service-xml/service");
		Service services[] = new Service[nodes.length];
		
		for (int i = 0; i < nodes.length; i++) {
			services[i] = new Service(document, nodes[i], dtd);
		}
		
		return services;
	}
	
	public Service getService(String name, String vendor, String version) throws ComponentNotFoundException {
		Service services[] = getServices();
		for (int i = 0; i < services.length; i++) {
			if (services[i].getName().equals(name)
					&& services[i].getVendor().equals(vendor)
					&& services[i].getVersion().equals(version))
				return services[i];
		}
		
		throw new ComponentNotFoundException("specified service does not exist");
	}
	
	public void removeService(Service service) {
		service.getRoot().getParentNode().removeChild(service.getRoot());
	}
	
	public String toString() {
		
		String output = "";
		Service[] services = getServices();
		for (int i = 0; i < services.length; i++) {
			if (i > 0)
				output += ", ";
			output += "[" + services[i].toString() + "]";			
		}
		return output;
	}
}
