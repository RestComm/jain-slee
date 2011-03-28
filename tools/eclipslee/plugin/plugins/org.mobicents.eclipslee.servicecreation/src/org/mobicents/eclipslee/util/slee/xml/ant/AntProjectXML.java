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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.mobicents.eclipslee.util.slee.xml.XML;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


/**
 * @author cath
 */
public class AntProjectXML extends XML {

	public AntProjectXML() throws ParserConfigurationException {
		super();
		root = document.createElement("project");		
		document.appendChild(root);
	}
	
	public AntProjectXML(InputStream stream) throws ParserConfigurationException, SAXException, IOException {
		super(stream);
	}
	
	protected AntProjectXML(Document document, Element root) {
		super(document, root);
	}
	
	public void setName(String name) {
		root.setAttribute("name", name);
	}
	
	public void setDefault(String def) {
		root.setAttribute("default", def);
	}
	
	public void setBasedir(String basedir) {
		root.setAttribute("basedir", basedir);
	}
	
	public String getName() {
		return root.getAttribute("name");
	}
	
	public String getDefault() {
		return root.getAttribute("default");
	}
	
	public String getBasedir() {
		return root.getAttribute("basedir");
	}

	public AntTargetXML[] getTargets() {
		Element elements[] = getNodes("project/target");
		AntTargetXML targets[] = new AntTargetXML[elements.length];
		for (int i = 0; i < elements.length; i++) {
			String name = elements[i].getAttribute("name");
			
			if (name.equals("init"))
				targets[i] = new AntInitTargetXML(document, elements[i]);
			else
				if (name.startsWith("clean"))
					targets[i] = new AntCleanTargetXML(document, elements[i]);
				else
					if (name.startsWith("build"))
						targets[i] = new AntBuildTargetXML(document, elements[i]);
					else
						targets[i] = new AntTargetXML(document, elements[i]);
		}
		return targets;
	}
	
	public AntTargetXML getTarget(String name) throws ComponentNotFoundException {
		AntTargetXML[] targets = getTargets();
		for (int i = 0; i < targets.length; i++) {
			if (targets[i].getName().equals(name))
				return targets[i];
		}
		throw new ComponentNotFoundException("target '" + name + "' does not exist in this project.");
	}

	public AntTargetXML addTarget() {
		Element child = addElement(root, "target");
		return new AntTargetXML(document, child);
	}
	
	public void removeTarget(AntTargetXML target) {
		target.getRoot().getParentNode().removeChild(target.getRoot());
	}

	public AntInitTargetXML addInitTarget() {
		Element child = addElement(root, "target");
		return new AntInitTargetXML(document, child);
	}
	
	public AntCleanTargetXML addCleanTarget() {
		Element child = addElement(root, "target");
		return new AntCleanTargetXML(document, child);		
	}
	
	public AntBuildTargetXML addBuildTarget() {
		Element child = addElement(root, "target");
		return new AntBuildTargetXML(document, child);
	}
	
}
