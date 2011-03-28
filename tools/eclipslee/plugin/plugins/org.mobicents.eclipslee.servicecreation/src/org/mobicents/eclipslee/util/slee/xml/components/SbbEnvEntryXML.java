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
public class SbbEnvEntryXML extends DTDXML {

	protected SbbEnvEntryXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}

	public void setName(String name) {
		setChildText(root, "env-entry-name", name);
	}
	
	public void setValue(String value) {
		setChildText(root, "env-entry-value", value);
	}
	
	public void setType(String type) {
		setChildText(root, "env-entry-type", type);
	}
	
	public void setDescription(String desc) {
		setChildText(root, "description", desc);
	}
	
	public String getName() {
		return getChildText(root, "env-entry-name");
	}
	
	public String getValue() {
		return getChildText(root, "env-entry-value");
	}
	
	public String getType() {
		return getChildText(root, "env-entry-type");
	}
	
	public String getDescription() {
		return getChildText(root, "description");
	}
	
}
