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

import org.mobicents.eclipslee.util.Utils;
import org.mobicents.eclipslee.util.slee.xml.DTDHandler;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @author allenc
 */
public class SbbEventXML extends DTDXML {

	public static final int NONE = 1;
	public static final int FIRE = 2;
	public static final int RECEIVE = 4;
	
	protected SbbEventXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
	public void setInitialEvent(boolean initial) {
		getRoot().setAttribute("initial-event", Utils.capitalize(new Boolean(initial).toString()));		
	}
	
	public boolean getInitialEvent() {
		String attr = getRoot().getAttribute("initial-event");
		if (attr == null)
			return false;
		if (attr.equalsIgnoreCase("true"))
			return true;
		return false;
	}
	
	public void setEventDirection(String direction) {
		getRoot().setAttribute("event-direction", direction);		
	}
	
	public String getEventDirection() {
		String dir = getRoot().getAttribute("event-direction");
		
		return (dir == null ? "Receive" : dir);
	}
	
	public void setEventDirection(int direction) {
		String dir;
		
		if (direction == FIRE)
			dir = "Fire";
		else if (direction == RECEIVE)
			dir = "Receive";
		else if (direction == (FIRE | RECEIVE))
			dir = "FireAndReceive";
		else
			throw new IllegalArgumentException("Direction must be FIRE, RECEIVE or FIRE + RECEIVE");
		
		getRoot().setAttribute("event-direction", dir);		
	}
	
	/*
	
	public int getEventDirection() {
		String attr = getRoot().getAttribute("event-direction");
		if (attr == null) return 0;
		
		if (attr.equals("Fire"))
			return FIRE;
		
		if (attr.equals("Receive"))
			return RECEIVE;
		
		if (attr.equals("FireAndReceive"))
			return FIRE | RECEIVE;
		
		return NONE;
	}
	
	*/
	
	public void setMaskOnAttach(boolean mask) {
		getRoot().setAttribute("mask-on-attach", new Boolean(mask).toString());
	}
	
	public boolean getMaskOnAttach() {
		String attr = getRoot().getAttribute("mask-on-attach");
		if (attr == null)
			return false;
		
		return Boolean.getBoolean(attr);
	}
	
	public void setDescription(String desc) {
		setChildText(getRoot(), "description", desc);
	}
	
	public String getDescription() {
		return getChildText(getRoot(), "description");
	}
	
	public void setName(String name) {
		Element eventTypeRef = getChild(getRoot(), "event-type-ref");
		if (eventTypeRef == null)
			eventTypeRef = addElement(getRoot(), "event-type-ref");
		
		setChildText(eventTypeRef, "event-type-name", name);		
	}
	
	public String getName() {
		Element eventTypeRef = getChild(getRoot(), "event-type-ref");
		if (eventTypeRef == null) return null;
		return getChildText(eventTypeRef, "event-type-name");
	}
	
	public void setVendor(String name) {
		Element eventTypeRef = getChild(getRoot(), "event-type-ref");
		if (eventTypeRef == null)
			eventTypeRef = addElement(getRoot(), "event-type-ref");
		
		setChildText(eventTypeRef, "event-type-vendor", name);		
	}
	
	public String getVendor() {
		Element eventTypeRef = getChild(getRoot(), "event-type-ref");
		if (eventTypeRef == null) return null;
		return getChildText(eventTypeRef, "event-type-vendor");
	}
	
	public void setVersion(String name) {
		Element eventTypeRef = getChild(getRoot(), "event-type-ref");
		if (eventTypeRef == null)
			eventTypeRef = addElement(getRoot(), "event-type-ref");
		
		setChildText(eventTypeRef, "event-type-version", name);		
	}
	
	public String getVersion() {
		Element eventTypeRef = getChild(getRoot(), "event-type-ref");
		if (eventTypeRef == null) return null;
		return getChildText(eventTypeRef, "event-type-version");
	}
	
	public void setScopedName(String name) {
		setChildText(getRoot(), "event-name", name);
	}
	
	public String getScopedName() {
		return getChildText(getRoot(), "event-name");
	}
	
	public String[] getInitialEventSelectors() {
		
		Element nodes[] = getNodes("event/initial-event-select");
		String selectors[] = new String[nodes.length];
		
		for (int i = 0; i < nodes.length; i++)
			selectors[i] = nodes[i].getAttribute("variable");
		
		return selectors;
	}
	
	public boolean isInitialEventSelector(String selector) {
		String selectors[] = getInitialEventSelectors();
		
		for (int i = 0; i < selectors.length; i++)
			if (selector.equals(selectors[i]))
				return true;
			
		return false;
	}
	
	public void addInitialEventSelector(String selector) {
		
		if (isInitialEventSelector(selector))
			return;
		
		Element node = this.addElement(getRoot(), "initial-event-select");
		node.setAttribute("variable", selector);
	}
	
	public void removeInitialEventSelector(String selector) {
		Element nodes[] = getNodes("event/initial-event-select");
		
		for (int i = 0; i < nodes.length; i++) {
			Element iesNode = nodes[i];
			if (iesNode.getAttribute("variable") != null && iesNode.getAttribute("variable").equals(selector)) {
				nodes[i].getParentNode().removeChild(nodes[i]);
				return;
			}
		}
	}
	
	public void setInitialEventSelectorMethod(String methodName) {
		setChildText(getRoot(), "initial-event-selector-method-name", methodName);		
	}
	
	public String getInitialEventSelectorMethod() {
		return getChildText(getRoot(), "initial-event-selector-method-name");
	}
	
	public void removeInitialEventSelectorMethod() {
		Element iesMethod = getChild(getRoot(), "initial-event-selector-method-name");
		iesMethod.getParentNode().removeChild(iesMethod);
	}
	
	public void setEventResourceOption(String option) {
		setChildText(getRoot(), "event-resource-option", option);
	}
	
	public String getEventResourceOption() {
		return getChildText(getRoot(), "event-resource-option");
	}
	
	public void removeEventResourceOption() {
		Element option = getChild(getRoot(), "event-resource-option");
		option.getParentNode().removeChild(option);
	}
	
}
