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
public class ResourceAdaptorEventXML extends DTDXML {

	protected ResourceAdaptorEventXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
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
}
