
/**
 *   Copyright 2005 Alcatel, OSP.
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
package org.alcatel.jsce.servicecreation.ospobject.data.xml;

import org.mobicents.eclipslee.util.slee.xml.DTDHandler;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *  Description:
 * <p>
 * Represents the object reference data tag in the osp-profile-spec-data-jar.xml
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ObjectReferenceDataXML extends DTDXML {

	/**
	 * @param document the maion document
	 * @param root the root (object-ref-data)
	 * @param dtd the correpsonding dtd
	 */
	public ObjectReferenceDataXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
	public String getObjecRefName(){
		return getChildText(getRoot(), "object-name");
	}
	
	public void setObjectRefName(String refName){
		setChildText(getRoot(), "object-name", refName);
	}
	
	public String getObjecRefNVersion(){
		return getChildText(getRoot(), "object-version");
	}
	
	public void setObjectRefVersion(String refName){
		setChildText(getRoot(), "object-version", refName);
	}
	
	public String getObjecRefNVendor(){
		return getChildText(getRoot(), "object-vendor");
	}
	
	public void setObjectRefVendor(String refName){
		setChildText(getRoot(), "object-vendor", refName);
	}
	
	public String getAttributeName(){
		return getChildText(getRoot(), "attribute-name");
	}
	
	public void setAttributeName(String attributeName){
		setChildText(getRoot(), "attribute-name", attributeName);
	}

	public void setJarLocation(String jarLocation){
		setChildText(getRoot(), "jar-location", jarLocation);
	}
	
	public String getJarLocation(){
		return getChildText(getRoot(), "jar-location");
	}
}
