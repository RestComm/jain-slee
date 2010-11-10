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
 * @author allenc
 */
public class SbbCMPField extends DTDXML {

	protected SbbCMPField(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
	public String getName() {
		return getChildText(getRoot(), "cmp-field-name");		
	}
	
	public void setName(String name) {
		setChildText(getRoot(), "cmp-field-name", name);
	}
	
	public void setSbbAliasRef(SbbRefXML ref) {
		setChildText(getRoot(), "sbb-alias-ref", ref.getAlias());
	}
	
	public void setSbbAliasRef(String ref) {
		setChildText(getRoot(), "sbb-alias-ref", ref);
	}
	
	public String getSbbAliasRef() {		
		return getChildText(getRoot(), "sbb-alias-ref");
	}
	
}
