
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
 * Represents the index-data tag in the osp-profile-spec-data-jar.xml
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class IndexDataXML extends DTDXML{

	protected IndexDataXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
	public ObjectIndexDataXML getObjectIndexData(){
		Element[] objectIndexElement = getNodes(getRoot(), "object-index-data");
		if(objectIndexElement.length>0){
			return new ObjectIndexDataXML(document, objectIndexElement[0], dtd);
		}else{
			throw new IllegalStateException("The tag object-index-data does not exists");
		}
	}

	public ObjectIndexDataXML setObjectIndexData(boolean isSlee, boolean isSmf){
		Element[] objectIndexElement = getNodes(getRoot(), "object-index-data");
		ObjectIndexDataXML objectIndexDataXML = null;
		if(objectIndexElement.length>0){
			objectIndexDataXML =  new ObjectIndexDataXML(document, objectIndexElement[0], dtd);
		}else{
			Element indexElement = addElement(getRoot(),"object-index-data");
			objectIndexDataXML =  new ObjectIndexDataXML(document, indexElement, dtd);
		}
		objectIndexDataXML.setSlee(isSlee);
		objectIndexDataXML.setSmf(isSmf);
		return objectIndexDataXML;
	}
}
