
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

import java.util.ArrayList;
import java.util.List;

import org.alcatel.jsce.util.log.SCELogger;
import org.mobicents.eclipslee.util.slee.xml.DTDHandler;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *  Description:
 * <p>
 *  Represents the <i>Service</i> xml tag in the osp-deployable-unti.xml file.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ServiceXML extends DTDXML {

	/**
	 * @param document the XML document
	 * @param root the root of the SBB
	 * @param dtd the corresponding DTD
	 */
	public ServiceXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
	
	public void setServiceFile(String fileLocation) {
		setChildText(getRoot(),"service-xml-file", fileLocation);
	}
	
	public String getServiceFile() {
		return getChildText(getRoot(), "service-xml-file");
	}
	
	public ObjectXML[] getObjectXML() {
		Element objects[] = getNodes("service/database/objects/object");
		List objectsData = new ArrayList();
		for (int i = 0; i < objects.length; i++) {
			Element object_i = objects[i];
			objectsData.add(new ObjectXML(document, object_i, dtd));
		}
		return (ObjectXML[]) objectsData.toArray(new ObjectXML[objectsData.size()]);
	}
	
	public ObjectXML addObjectXML() {		
		Element objects[] = getNodes("service/database/objects");
		if (objects.length > 0) {
			//There is only one such a node
			Element objectNode = addElement(objects[0], "object");
			return new ObjectXML(document, objectNode, dtd);
		} else {
			/*We must create the objects node, but first verrify if the database node has laready been created*/
			Element database[] = getNodes("service/database");
			if (database.length > 0) {
				//There is only one such a node
				Element objectsNode = addElement(database[0], "objects");
				Element object = addElement(objectsNode, "object");
				return new ObjectXML(document, object, dtd);
			} else {
				/*We must create the objects node*/
				Element databaseNode = addElement(getRoot(), "database");
				Element objecstNode = addElement(databaseNode, "objects");
				Element object = addElement(objecstNode, "object");
				return new ObjectXML(document, object, dtd);
			}
			
		}
	}
	
	public DefaultUserXML getDefautUser(){
		Element nodes[] = getNodes("service/default-user");
		if(nodes.length>0){
			//There is only one such a node
			return new DefaultUserXML(document, nodes[0], dtd);
		}else{
			String msg = "The node default-user was not found as child of "+ getRoot();
			SCELogger.logError(msg, new IllegalStateException(msg));
			return null;
		}
	}
	
	public DefaultUserXML setDefaultUser(String name, String type){
		Element nodes[] = getNodes("service/default-user");
		if(nodes.length>0){
			//There is only one such a node
			DefaultUserXML defaultUserXML =  new DefaultUserXML(document, nodes[0], dtd);
			defaultUserXML.setUserName(name);
			defaultUserXML.setUserType(type);
			return defaultUserXML;
		}else{
			Element node = addElement(getRoot(), "default-user");
			DefaultUserXML defaultUserXML = new DefaultUserXML(document, node, dtd);
			defaultUserXML.setUserName(name);
			defaultUserXML.setUserType(type);
			return defaultUserXML;
		}
		
	}


	public ObjectXML getObjectXML(String name, String version, String vendor) {
		Element objects[] = getNodes("service/database/objects/object");
		for (int i = 0; i < objects.length; i++) {
			Element object_i = objects[i];
			ObjectXML objectXML = new ObjectXML(document, object_i, dtd);
			try {
				if(objectXML.getObjectcIdXML().getName().equals(name) && objectXML.getObjectcIdXML().getVersion().equals(version) && objectXML.getObjectcIdXML().getVendor().equals(vendor) ){
					return objectXML;
				}
			} catch (ComponentNotFoundException e) {
				SCELogger.logError(e);
			}
		}
		return null;
	}
	

}
