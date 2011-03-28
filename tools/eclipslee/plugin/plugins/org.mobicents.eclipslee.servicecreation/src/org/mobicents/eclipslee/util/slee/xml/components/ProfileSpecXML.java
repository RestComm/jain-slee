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
public class ProfileSpecXML extends DTDXML {

	/**
	 * Can only be created by classes in the same package.
	 * 
	 * @param document the parent Document
	 * @param root the profile-spec element relating to this profile specification
	 */
	
	protected ProfileSpecXML(Document document, Element root, DTDHandler dtd) {
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
	
	public void setCMPInterfaceName(String name) {
		
		Element classes = getChild(getRoot(), "profile-classes");
		if (classes == null)
			classes = addElement(getRoot(), "profile-classes");

		setChildText(classes, "profile-cmp-interface-name", name);
	}
	
	public String getCMPInterfaceName() {
		
		Element classes = getChild(getRoot(), "profile-classes");
		if (classes == null) return null;
		
		return getChildText(classes, "profile-cmp-interface-name");		
	}
	
	/**
	 * 
	 * @param name the name of the management interface, null to remove
	 */
	
	public void setManagementInterfaceName(String name) {

		Element classes = getChild(getRoot(), "profile-classes");
		if (classes == null)
			classes = addElement(getRoot(), "profile-classes");

		if (name == null) {
			Element child = this.getChild(classes, "profile-management-interface-name");
			if (child != null)
				classes.removeChild(child);
			return;
		}
		
		setChildText(classes, "profile-management-interface-name", name);		
	}

	public String getManagementInterfaceName() {

		Element classes = getChild(getRoot(), "profile-classes");
		if (classes == null) return null;
		
		return getChildText(classes, "profile-management-interface-name");		
	}
		
	/**
	 * 
	 * @param name the name of the management abstract class, null to remove
	 */
	
	public void setManagementAbstractClassName(String name) {

		Element classes = getChild(getRoot(), "profile-classes");
		if (classes == null)
			classes = addElement(getRoot(), "profile-classes");

		if (name == null) {
			Element child = this.getChild(classes, "profile-management-abstract-class-name");
			if (child != null)
				classes.removeChild(child);
			return;
		}
			
		setChildText(classes, "profile-management-abstract-class-name", name);	
	}
	
	public String getManagementAbstractClassName() {

		Element classes = getChild(getRoot(), "profile-classes");
		if (classes == null) return null;
		
		return getChildText(classes, "profile-management-abstract-class-name");		
	}
	
	public Element[] getIndexedAttributes() {
		return getNodes(getRoot(), "profile-spec/profile-index");
	}
	
	public Element getIndexedAttribute(String cmpName) {
		
		Element attrs[] = getIndexedAttributes();
		
		for (int i = 0; i < attrs.length; i++) {
			Element attr = attrs[i];
			
			if (cmpName.equals(getTextData(attr)))
				return attr;
		}
		
		return null;		
	}

	public Element addIndexedAttribute(String name, boolean unique) throws DuplicateComponentException {
		
		if (getIndexedAttribute(name) != null)
			throw new DuplicateComponentException("CMP field is already indexed.");
		
		Element attr = addElement(getRoot(), "profile-index");
		this.setTextData(attr, name);
		attr.setAttribute("unique", (unique ? "True" : "False"));
		return attr;
	}
	
	public void removeIndexedAttribute(String name) throws ComponentNotFoundException {
		
		Element attr = getIndexedAttribute(name);
		if (attr == null)
			throw new ComponentNotFoundException("Attribute not found.");
		
		attr.getParentNode().appendChild(attr);
			
	}

	public String toString() {
		return "Profile: " + getName() + "," + getVersion() + "," + getVendor();
	}

}
