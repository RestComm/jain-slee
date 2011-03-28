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
public class Service extends DTDXML {

	public Service(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);		
	}
	
	public void setDescription(String desc) {
		setChildText(root, "description", desc);
	}
	
	public String getDescription() {
		return getChildText(root, "description");
	}
	
	public void setName(String name) {
		setChildText(root, "service-name", name);
	}
	
	public String getName() {
		return getChildText(root, "service-name");
	}
	
	public void setVendor(String vendor) {
		setChildText(root, "service-vendor", vendor);
	}
	
	public String getVendor() {
		return getChildText(root, "service-vendor");
	}

	public void setVersion(String version) {
		setChildText(root, "service-version", version);
	}
	
	public String getVersion() {
		return getChildText(root, "service-version");
	}

	public void setRootSbb(SbbXML sbb) {
		Element rootSbb = getChild(root, "root-sbb");
		if (rootSbb == null)
			rootSbb = addElement(root, "root-sbb");

		setChildText(rootSbb, "sbb-name", sbb.getName());
		setChildText(rootSbb, "sbb-vendor", sbb.getVendor());
		setChildText(rootSbb, "sbb-version", sbb.getVersion());
		setChildText(rootSbb, "description", sbb.getDescription());		
	}
	
	public String getRootSbbName() {
		Element rootSbb = getChild(root, "root-sbb");
		if (rootSbb == null)
			return null;
		
		return getChildText(rootSbb, "sbb-name");
	}
	
	public String getRootSbbVendor() {
		Element rootSbb = getChild(root, "root-sbb");
		if (rootSbb == null)
			return null;
		
		return getChildText(rootSbb, "sbb-vendor");
	}

	public String getRootSbbVersion() {
		Element rootSbb = getChild(root, "root-sbb");
		if (rootSbb == null)
			return null;
		
		return getChildText(rootSbb, "sbb-version");
	}

	public void setDefaultPriority(int priority) {
		setChildText(root, "default-priority", "" + priority);
	}

	public int getDefaultPriority() {
		String priority = getChildText(root, "default-priority");
		if (priority == null)
			return 0;
		
		return Integer.parseInt(priority);		
	}
	
	public void setAddressProfileTable(String table) {
		if (table == null) {
			Element child = getChild(root, "address-profile-table");
			if (child != null)
				child.getParentNode().removeChild(child);
			return;
		}

		setChildText(root, "address-profile-table", table);
	}
	
	public String getAddressProfileTable() {
		return getChildText(root, "address-profile-table");
	}
	
	public void setResourceInfoProfileTable(String table) {
		if (table == null) {
			Element child = getChild(root, "resource-info-profile-table");
			if (child != null)
				child.getParentNode().removeChild(child);
			return;
		}
		
		setChildText(root, "resource-info-profile-table", table);
	}
	
	public String toString() {
		return getName() + "," + getVersion() + "," + getVendor();
	}
}
