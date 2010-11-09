
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
 * Represents the <i>default-user</i> XML tag in osp-deployable-unit.xml
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class DefaultUserXML extends DTDXML {

	/**
	 * @param document the XML document
	 * @param root the root of the SBB
	 * @param dtd the corresponding DTD
	 */
	public  DefaultUserXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}

	public void setUserName(String name) {
		setChildText(getRoot(),"user-name", name);
	}
	
	public String getUserName() {
		return getChildText(getRoot(), "user-name");
	}
	
	public void setUserType(String type) {
		setChildText(getRoot(),"user-type", type);
	}
	
	public String getUserType() {
		return getChildText(getRoot(), "user-type");
	}

}
