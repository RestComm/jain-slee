
/**
 *   Copyright 2006 Alcatel, OSP.
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
package org.alcatel.jsce.servicecreation.wizards.ra.xml;

import org.mobicents.eclipslee.util.slee.xml.DTDHandler;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *  Description:
 * <p>
 * This class represents the resource-adaptor-jar.xml
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ResourceAdaptorXML extends DTDXML {

	protected ResourceAdaptorXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
	public void setDescription(String desc) {
		setChildText(root, "description", desc);
	}
	
	public String getDescription() {
		return getChildText(root, "description");
	}
	
	public void setName(String name) {
		setChildText(root, "resource-adaptor-name", name);
	}
	
	public String getName() {
		return getChildText(root, "resource-adaptor-name");
	}

	public void setVendor(String vendor) {
		setChildText(root, "resource-adaptor-vendor", vendor);
	}
	
	public String getVendor() {
		return getChildText(root, "resource-adaptor-vendor");
	}

	public void setVersion(String version) {
		setChildText(root, "resource-adaptor-version", version);
	}
	
	public String getVersion() {
		return getChildText(root, "resource-adaptor-version");
	}

	public void setResourceAdaptorTypeRef(ResourceAdaptorTypeXML ref) {
		Element child = getChild(root, "resource-adaptor-type-ref");
		if (child == null)
			child = addElement(root, "resource-adaptor-type-ref");
		
		setChildText(child, "resource-adaptor-type-name", ref.getName());
		setChildText(child, "resource-adaptor-type-vendor", ref.getVendor());
		setChildText(child, "resource-adaptor-type-version", ref.getVersion());		
	}
	
	public void setResourceAdaptorTypeRef(String name, String vendor, String version) {
		Element child = getChild(root, "resource-adaptor-type-ref");
		if (child == null)
			child = addElement(root, "resource-adaptor-type-ref");
		
		setChildText(child, "resource-adaptor-type-name", name);
		setChildText(child, "resource-adaptor-type-vendor", vendor);
		setChildText(child, "resource-adaptor-type-version",version);		
	}
	
	public ResourceAdaptorTypeXML getResourceAdaptorTypeRef() {
		Element child = getChild(root, "resource-adaptor-type-ref");
		if (child == null)
			return null;
		ResourceAdaptorTypeXML node = new ResourceAdaptorTypeXML(document, child,dtd);
		return node;
	}


	public String getResourceAdaptorTypeName() {
		Element child = getChild(root, "resource-adaptor-type-ref");
		if (child == null)
			return null;
		
		return getChildText(child, "resource-adaptor-type-name");
	}

	public String getResourceAdaptorTypeVendor() {
		Element child = getChild(root, "resource-adaptor-type-ref");
		if (child == null)
			return null;
		
		return getChildText(child, "resource-adaptor-type-vendor");
	}

	public String getResourceAdaptorTypeVersion() {
		Element child = getChild(root, "resource-adaptor-type-ref");
		if (child == null)
			return null;
		
		return getChildText(child, "resource-adaptor-type-version");
	}

	public void setResourceAdacptorClasses(String name) {
		if (name == null) {
			Element child = getChild(root, "resource-adaptor-classes");
			if (child != null)
				child.getParentNode().removeChild(child);
			return;
		}
		setChildText(root, "resource-adaptor-classes", name);
	}
	
	public String getResourceAdacptorClasses() {
		return getChildText(root, "resource-adaptor-classes");		
	}
	
	
	public void removeResourceAdaptorTypeRef(ResourceAdaptorTypeXML xml) {
		xml.getRoot().getParentNode().removeChild(xml.getRoot());
	}

}
