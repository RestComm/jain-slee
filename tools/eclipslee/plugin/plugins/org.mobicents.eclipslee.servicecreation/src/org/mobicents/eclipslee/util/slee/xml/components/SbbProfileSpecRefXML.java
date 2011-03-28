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
public class SbbProfileSpecRefXML extends DTDXML {
	
	protected SbbProfileSpecRefXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
	public String getName() {
		return getChildText(getRoot(), "profile-spec-name");		
	}
	
	public void setName(String name) {
		setChildText(getRoot(), "profile-spec-name", name);
	}
	
	public String getVendor() {
		return getChildText(getRoot(), "profile-spec-vendor");
	}
	
	public void setVendor(String vendor) {
		setChildText(getRoot(), "profile-spec-vendor", vendor);
	}
	
	public String getVersion() {
		return getChildText(getRoot(), "profile-spec-version");
	}
	
	public void setVersion(String version) {
		setChildText(getRoot(), "profile-spec-version", version);
	}
	
	public String getDescription() {
		return getChildText(getRoot(), "description");
	}
	
	public void setDescription(String description) {
		setChildText(getRoot(), "description", description);
	}	

	public void setAlias(String alias) {
		setChildText(getRoot(), "profile-spec-alias", alias);
	}
	
	public String getAlias() {
		return getChildText(getRoot(), "profile-spec-alias");
	}
	
}
