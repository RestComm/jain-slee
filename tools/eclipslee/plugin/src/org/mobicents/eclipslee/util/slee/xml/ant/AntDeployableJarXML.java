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

package org.mobicents.eclipslee.util.slee.xml.ant;

import org.mobicents.eclipslee.util.slee.xml.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @author cath
 */
public class AntDeployableJarXML extends XML {

	public AntDeployableJarXML(Document document, Element root) {
		super(document, root);
	}

	public void setDestfile(String dest) {
		root.setAttribute("destfile", dest);
	}
	
	public String getDestfile() {
		return root.getAttribute("destfile");
	}
	
	public void setServiceXML(String xml) {
		root.setAttribute("servicexml", xml);
	}
	
	public String getServiceXML() {
		return root.getAttribute("servicexml");
	}
	
	public AntSbbJarXML addSbbJar() {
		return new AntSbbJarXML(document, addElement(root, "sbbjar"));		
	}
	
	public AntSbbJarXML[] getSbbJars() {
		Element elements[] = getNodes("deployablejar/sbbjar");
		AntSbbJarXML[] xml = new AntSbbJarXML[elements.length];
		for (int i = 0; i < elements.length; i++)
			xml[i] = new AntSbbJarXML(document, elements[i]);
		return xml;
	}
	
	public void removeSbbJar(AntSbbJarXML xml) {
		xml.getRoot().getParentNode().removeChild(xml.getRoot());
	}
	
	public AntProfileSpecJarXML addProfileSpecJar() {
		return new AntProfileSpecJarXML(document, addElement(root, "profilespecjar"));
	}

	public AntProfileSpecJarXML[] getProfileSpecJars() {
		Element elements[] = getNodes("deployablejar/profilespecjar");
		AntProfileSpecJarXML[] xml = new AntProfileSpecJarXML[elements.length];
		for (int i = 0; i < elements.length; i++)
			xml[i] = new AntProfileSpecJarXML(document, elements[i]);
		return xml;
	}

	public void removeProfileSpecJar(AntProfileSpecJarXML xml) {
		xml.getRoot().getParentNode().removeChild(xml.getRoot());
	}
	
	public AntEventJarXML addEventJar() {
		return new AntEventJarXML(document, addElement(root, "eventjar"));
	}
	
	public AntEventJarXML[] getEventJars() {
		Element elements[] = getNodes("deployablejar/eventjar");
		AntEventJarXML[] xml = new AntEventJarXML[elements.length];
		for (int i = 0; i < elements.length; i++)
			xml[i] = new AntEventJarXML(document, elements[i]);
		return xml;
	}

	public void removeEventJar(AntEventJarXML xml) {
		xml.getRoot().getParentNode().removeChild(xml.getRoot());
	}
	
}
