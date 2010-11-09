
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
 * Represents the object attribute data of the osp-profile-spec-data-jar.xml
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ObjectAttributeDataXML extends DTDXML {

	/**
	 * @param document
	 * @param root
	 * @param dtd
	 */
	public ObjectAttributeDataXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
	/**
	 * @param objectName the name of the referenced object
	 * @param objectVersion the version of the referenced object
	 * @param objectVendor the vendor of the referenced object
	 * @param attributename the name of the referenced attribute 
	 * @param jarlocation the jar location of the corresponding export.jar
	 * @return
	 */
	public ObjectReferenceDataXML setObjectRefData(String objectName, String objectVersion, String objectVendor,  String attributename, String jarlocation) {
		Element objectRefData[] = getNodes(getRoot(), "object-ref-data");
		Element objectRef = null;
		if (objectRefData.length > 0) {
			// already exists
			objectRef = objectRefData[0];
		} else {
			// does'nt exists yet.
			objectRef = addElement(getRoot(), "object-ref-data");
		}
		addElement(objectRef, "object-name").appendChild(document.createTextNode(objectName));
		addElement(objectRef, "object-version").appendChild(document.createTextNode(objectVersion));
		addElement(objectRef, "object-vendor").appendChild(document.createTextNode(objectVendor));
		addElement(objectRef, "attribute-name").appendChild(document.createTextNode(attributename));
		addElement(objectRef, "jar-location").appendChild(document.createTextNode(jarlocation));
		return new ObjectReferenceDataXML(document, objectRef, dtd);
	}
	
	public ObjectReferenceDataXML getObjectRefData() throws ComponentNotFoundException {
			Element[] objectRefs = getNodes(getRoot(), "object-attribute-data/object-ref-data");
			if(objectRefs.length>0){
				return new ObjectReferenceDataXML(document,objectRefs[0], dtd);
			}else{
				return null;
			}
	}
	
	public boolean isComposePk(){
		String result = getChildText(getRoot(), "isComposePk");
		if(result.equals("true")){
			return true;
		}else{
			return false;
		}
	}
	
	public void setComposePk(boolean compose){
		if(compose)
			setChildText(getRoot(), "isComposePk", "true");
		else
			setChildText(getRoot(), "isComposePk", "false");
	}
	
	

}
