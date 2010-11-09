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
public class ResourceAdaptorTypeClassesXML extends DTDXML {

	protected ResourceAdaptorTypeClassesXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
	public void setDescription(String desc) {
		setChildText(root, "description", desc);
	}
	
	public String getDescription() {
		return getChildText(root, "description");
	}
	
	public void addActivityType(String name) {
		Element child = addElement(root, "activity-type");
		setChildText(child, "activity-type-name", name);		
	}
	
	public String[] getActivityTypes() {
		Element nodes[] = getNodes("resource-adaptor-type/activity-type");
		String types[] = new String[nodes.length];
		for (int i = 0; i < nodes.length; i++)
			types[i] = getChildText(nodes[i], "activity-type-name");
		return types;		
	}
	
	public void removeActivityType(String name) {
		Element nodes[] = getNodes("resource-adaptor-type/activity-type");
		for (int i = 0; i < nodes.length; i++) {
			if (getChildText(nodes[i], "activity-type-name").equals(name)) {
				nodes[i].getParentNode().removeChild(nodes[i]);
				return;
			}
		}
	}
	
	public void setActivityContextInterfaceFactoryInterface(String name) {
		Element child = getChild(root, "activity-context-interface-factory-interface");
		if (name == null) {
			if (child != null)
				child.getParentNode().removeChild(child);
			return;			
		}
		
		if (child == null)
			child = addElement(root, "activity-context-interface-factory-interface");
		
		setChildText(child, "activity-context-interface-factory-interface-name", name);
	}
	
	public String getActivityContextInterfaceFactoryInterface() {		
		Element child = getChild(root, "activity-context-interface-factory-interface");
		if (child == null)
			return null;
		
		return getChildText(child, "activity-context-interface-factory-interface-name");
	}

	public void setResourceAdaptorInterface(String name) {
		Element child = getChild(root, "resource-adaptor-interface");
		if (name == null) {
			if (child != null)
				child.getParentNode().removeChild(child);
			return;			
		}
		
		if (child == null)
			child = addElement(root, "resource-adaptor-interface");
		
		setChildText(child, "resource-adaptor-interface-name", name);
	}
	
	public String getResourceAdaptorInterface() {		
		Element child = getChild(root, "resource-adaptor-interface");
		if (child == null)
			return null;
		
		return getChildText(child, "resource-adaptor-interface-name");
	}

	
}
