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
public class SbbResourceAdaptorTypeBindingXML extends DTDXML {

	protected SbbResourceAdaptorTypeBindingXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
	public void setDescription(String desc) {
		setChildText(root, "description", desc);
	}
	
	public String getDescription() {
		return getChildText(root, "description");
	}
	
	public void setResourceAdaptorTypeRef(ResourceAdaptorTypeXML ref) {
		Element child = getChild(root, "resource-adaptor-type-ref");
		if (child == null)
			child = addElement(root, "resource-adaptor-type-ref");
		
		setChildText(child, "resource-adaptor-type-name", ref.getName());
		setChildText(child, "resource-adaptor-type-vendor", ref.getVendor());
		setChildText(child, "resource-adaptor-type-version", ref.getVersion());		
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

	public void setActivityContextInterfaceFactoryName(String name) {
		if (name == null) {
			Element child = getChild(root, "activity-context-interface-factory-name");
			if (child != null)
				child.getParentNode().removeChild(child);
			return;
		}
		setChildText(root, "activity-context-interface-factory-name", name);
	}
	
	public String getActivityContextInterfaceFactoryName() {
		return getChildText(root, "activity-context-interface-factory-name");		
	}
	
	public SbbResourceAdaptorEntityBindingXML addResourceAdaptorEntityBinding() {
		Element child = addElement(root, "resource-adaptor-entity-binding");
		return new SbbResourceAdaptorEntityBindingXML(document, child, dtd);
	}
	
	public SbbResourceAdaptorEntityBindingXML[] getResourceAdaptorEntityBindings() {
		Element nodes[] = getNodes("resource-adaptor-type-binding/resource-adaptor-entity-binding");
		SbbResourceAdaptorEntityBindingXML xml[] = new SbbResourceAdaptorEntityBindingXML[nodes.length];
			
		for (int i = 0; i < nodes.length; i++)		
			xml[i] = new SbbResourceAdaptorEntityBindingXML(document, nodes[i], dtd);
		return xml;		
	}
	
	public void removeResourceAdaptorEntityBinding(SbbResourceAdaptorEntityBindingXML xml) {
		xml.getRoot().getParentNode().removeChild(xml.getRoot());
	}
	
}
