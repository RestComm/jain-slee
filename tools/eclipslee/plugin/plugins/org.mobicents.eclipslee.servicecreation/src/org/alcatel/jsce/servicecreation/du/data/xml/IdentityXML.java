
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
package org.alcatel.jsce.servicecreation.du.data.xml;

import org.mobicents.eclipslee.util.slee.xml.DTDHandler;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *  Description:
 * <p>
 * Represents the identity XML node of the osp-deployable-unit-data xml file.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class IdentityXML extends DTDXML {


	/**
	 * @param document the XML document
	 * @param root the root of the SBB
	 * @param dtd the corresponding DTD
	 */
	public IdentityXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	public String getName() {
		return getChildText(getRoot(), "name");		
	}
	
	public void setName(String name) {
		setChildText(getRoot(), "name", name);
	}
	
	public String getVendor() {
		return getChildText(getRoot(), "vendor");
	}
	
	public void setVendor(String vendor) {
		setChildText(getRoot(), "vendor", vendor);
	}
	
	public String getVersion() {
		return getChildText(getRoot(), "version");
	}
	
	public void setVersion(String version) {
		setChildText(getRoot(), "version", version);
	}
	
	public String getDescription() {
		return getChildText(getRoot(), "description");
	}
	
	public void setDescription(String description) {
		setChildText(getRoot(), "description", description);
	}	
	

}
