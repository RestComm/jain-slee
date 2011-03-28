
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
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *  Description:
 * <p>
 * Represents an attribute-data tag in the osp-profile-spec-data-jar.xml
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class AttributeDataXML extends DTDXML {


	/**
	 * @param document the XML document
	 * @param root the local root
	 * @param dtd
	 */
	public AttributeDataXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}

	public ObjectAttributeDataXML addObjectAttributeData(){
		Element[] attributeData = getNodes(getRoot(), "object-attribute-data");
		if(attributeData.length >0){
			/*Already exists: do nothing*/
			return new ObjectAttributeDataXML(document, attributeData[0], dtd);
		}else{
			Element objectAttribute = addElement(getRoot(), "object-attribute-data");
			return new ObjectAttributeDataXML(document, objectAttribute, dtd);
		}
	}
	
	public ObjectAttributeDataXML getObjrctAttributeData(){
		Element[] attributeData = getNodes(getRoot(), "attribute-data/object-attribute-data");
		if(attributeData.length >0){
			/*Already exists: do nothing*/
			return new ObjectAttributeDataXML(document, attributeData[0], dtd);
		}else throw new IllegalStateException("The tag object-attribute-data does not exist !");
	}
}
