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

import java.util.StringTokenizer;

import org.mobicents.eclipslee.util.slee.xml.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @author cath
 */
public class AntTargetXML extends XML {

	protected AntTargetXML(Document document, Element root) {
		super(document, root);
	}

	public void setName(String name) {
		root.setAttribute("name", name);
	}
	
	public String getName() {
		return root.getAttribute("name");
	}
	
	public void setDepends(String [] depends) {
		String str = "";
		for (int i = 0; i < depends.length; i++) {
			if (i > 0)
				str += ",";
			str += depends[i];
		}
		root.setAttribute("depends", str);
	}
	
	public String[] getDepends() {
		String depends = root.getAttribute("depends");
		if (depends == null) return new String[0];
		
		StringTokenizer tok = new StringTokenizer(depends, ",");
		String output[] = new String[tok.countTokens()];
		for (int i = 0; i < output.length; i++) {
			output[i] = tok.nextToken().trim();
		}
		return output;
	}
	
	public void addDepends(String dep) {
		String[] oldDepends = getDepends();
		String[] newDepends = new String[oldDepends.length + 1];
		System.arraycopy(oldDepends, 0, newDepends, 0, oldDepends.length);
		newDepends[oldDepends.length] = dep;
		setDepends(newDepends);
	}
	
	public void addAntTarget(AntTargetXML targetXML) {
		Element ant = addElement(root, "ant");
		ant.setAttribute("target", targetXML.getName());		
	}

	public void removeAntTarget(AntTargetXML targetXML) {
		Element ant[] = getNodes("target/ant");
		for (int i = 0; i < ant.length; i++) {
			if (targetXML.getName().equals(ant[i].getAttribute("target"))) {
				ant[i].getParentNode().removeChild(ant[i]);
				return;
			}
		}
	}

	public AntCopyXML addCopyTarget() {
		Element copy = addElement(root, "copy");
		return new AntCopyXML(document, copy);		
	}
	
	public void removeCopyTarget(AntCopyXML xml) {
		xml.getRoot().getParentNode().removeChild(xml.getRoot());
	}
	
	public AntCopyXML[] getCopyTargets() {
		Element nodes[] = getNodes("target/copy");
		AntCopyXML xml[] = new AntCopyXML[nodes.length];
		
		for (int i = 0; i < nodes.length; i++)
			xml[i] = new AntCopyXML(document, nodes[i]);
		
		return xml;
	}
	
}
