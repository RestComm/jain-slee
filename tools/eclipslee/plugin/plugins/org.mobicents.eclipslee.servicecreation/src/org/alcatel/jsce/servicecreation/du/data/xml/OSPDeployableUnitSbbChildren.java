/**
 *   Copyright 2007 Alcatel-Lucent, Convergence, OSP.
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

import org.mobicents.eclipslee.util.slee.xml.DTDHandler;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *  Description:
 * <p>
 * This class represents the sbb children node of the OSP deployable unit.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class OSPDeployableUnitSbbChildren extends DTDXML {

	/**
	 * @param document the XML doc
	 * @param root the root node
	 * @param dtd the corresponding DTD
	 */
	protected OSPDeployableUnitSbbChildren(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}

	public IdentityXML addSbbChild(String name, String vendor, String version, String description) {

		// already exists
		Element newSpec = addElement(root, "identity");
		addElement(newSpec, "description").appendChild(document.createTextNode(description));
		addElement(newSpec, "name").appendChild(document.createTextNode(name));
		addElement(newSpec, "vendor").appendChild(document.createTextNode(vendor));
		addElement(newSpec, "version").appendChild(document.createTextNode(version));
		return new IdentityXML(document, newSpec, dtd);

	}
	
	public IdentityXML[] getAllChildren(){
		List childrenXML = new ArrayList();
		Element[] children = getNodes("children/identity");
		for (int i = 0; i < children.length; i++) {
			Element element_i = children[i];
			IdentityXML childi_i = new IdentityXML(document, element_i, dtd);
			childrenXML.add(childi_i);
		}
		return (IdentityXML[]) childrenXML.toArray(new IdentityXML[childrenXML.size()]);
	}

}
