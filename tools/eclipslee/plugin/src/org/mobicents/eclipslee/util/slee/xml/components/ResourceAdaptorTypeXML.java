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

import org.mobicents.eclipslee.util.slee.xml.DTDHandler;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @author cath
 */
public class ResourceAdaptorTypeXML extends DTDXML {

	public ResourceAdaptorTypeXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
	public void setDescription(String desc) {
		setChildText(root, "description", desc);
	}
	
	public String getDescription() {
		return getChildText(root, "description");
	}
	
	public void setName(String name) {
		setChildText(root, "resource-adaptor-type-name", name);
	}
	
	public String getName() {
		return getChildText(root, "resource-adaptor-type-name");
	}

	public void setVendor(String vendor) {
		setChildText(root, "resource-adaptor-type-vendor", vendor);
	}
	
	public String getVendor() {
		return getChildText(root, "resource-adaptor-type-vendor");
	}

	public void setVersion(String version) {
		setChildText(root, "resource-adaptor-type-version", version);
	}
	
	public String getVersion() {
		return getChildText(root, "resource-adaptor-type-version");
	}

	public ResourceAdaptorTypeClassesXML addResourceAdaptorTypeClasses() {
		Element child = addElement(root, "resource-adaptor-type-classes");
		return new ResourceAdaptorTypeClassesXML(document, child, dtd);		
	}
	
	public ResourceAdaptorTypeClassesXML[] getResourceAdaptorTypeClasses() {
		Element nodes[] = getNodes("resource-adaptor-type/resource-adaptor-type-classes");
		ResourceAdaptorTypeClassesXML xml[] = new ResourceAdaptorTypeClassesXML[nodes.length];
		for (int i = 0; i < nodes.length; i++)
			xml[i] = new ResourceAdaptorTypeClassesXML(document, nodes[i], dtd);
		return xml;
	}
	
	public void removeResourceAdaptorTypeClasses(ResourceAdaptorTypeClassesXML xml) {
		xml.getRoot().getParentNode().removeChild(xml.getRoot());
	}
		
	public ResourceAdaptorEventXML addEvent(EventXML event) {
		Element child = addElement(root, "event-type-ref");
		setChildText(child, "event-type-name", event.getName());
		setChildText(child, "event-type-vendor", event.getVendor());
		setChildText(child, "event-type-version", event.getVersion());
		return new ResourceAdaptorEventXML(document, child, dtd);		
	}
	
	public ResourceAdaptorEventXML addEvent(String name, String vendor, String version) {
		Element child = addElement(root, "event-type-ref");
		setChildText(child, "event-type-name", name);
		setChildText(child, "event-type-vendor",vendor);
		setChildText(child, "event-type-version",version);
		return new ResourceAdaptorEventXML(document, child, dtd);		
	}
	
	public ResourceAdaptorEventXML[] getEvents() {
		Element nodes[] = getNodes("resource-adaptor-type/event-type-ref");
		ResourceAdaptorEventXML xml[] = new ResourceAdaptorEventXML[nodes.length];
		for (int i = 0; i < nodes.length; i++)
			xml[i] = new ResourceAdaptorEventXML(document, nodes[i], dtd);		
		return xml;
	}
	
	public void removeEvent(ResourceAdaptorEventXML xml) {
		xml.getRoot().getParentNode().removeChild(xml.getRoot());
	}

	public ResourceAdaptorEventXML getEvents(String name, String vendor, String version) {
		Element nodes[] = getNodes("resource-adaptor-type/event-type-ref");
		ResourceAdaptorEventXML xml[] = new ResourceAdaptorEventXML[nodes.length];
		for (int i = 0; i < nodes.length; i++){	
			xml[i] = new ResourceAdaptorEventXML(document, nodes[i], dtd);		
			if(xml[i].getName().equals(name) && xml[i].getVendor().equals(vendor) && xml[i].getVersion().equals(version)){
				return xml[i];
			}
		}
		return null;
	}
	
}
