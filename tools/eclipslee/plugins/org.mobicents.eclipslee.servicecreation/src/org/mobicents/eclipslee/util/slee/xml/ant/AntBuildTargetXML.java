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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author cath
 */
public class AntBuildTargetXML extends AntTargetXML {

	protected AntBuildTargetXML(Document document, Element root) {
		super(document, root);
	}
	
	public void addMkdir(String dir) {
		Element child = addElement(root, "mkdir");
		child.setAttribute("dir", dir);		
	}
	
	public void removeMkdir(String dir) {
		Element children[] = getNodes(root, "target/mkdir");
		for (int i = 0; i < children.length; i++) {
			if (dir.equals(children[i].getAttribute("dir"))) {
				children[i].getParentNode().removeChild(children[i]);
				return;
			}
		}
	}
	
	public String[] getMkdirs() {
		Element children[] = getNodes(root, "target/mkdir");
		String dirs[] = new String[children.length];
		
		for (int i = 0; i < children.length; i++)
			dirs[i] = children[i].getAttribute("dir");
		
		return dirs;		
	}
	
	public AntJavacXML createJavac() {
		if (javacXML == null)
			javacXML = new AntJavacXML(document, addElement(root, "javac"));
		return javacXML;
	}
	
	public void removeJavac() {
		javacXML.getRoot().getParentNode().removeChild(javacXML.getRoot());
		javacXML = null;
	}
	
	public AntDeployableJarXML addDeployableJar() {
		return new AntDeployableJarXML(document, addElement(root, "deployablejar"));		
	}
	
	public void removeDeployableJar(AntDeployableJarXML xml) {
		xml.getRoot().getParentNode().removeChild(xml.getRoot());
	}
	
	public AntDeployableJarXML[] getDeployableJars() {
		Element children[] = getNodes("target/deployablejar");
		AntDeployableJarXML[] jars = new AntDeployableJarXML[children.length];
		
		for (int i = 0; i < children.length; i++)
			jars[i] = new AntDeployableJarXML(document, children[i]);
		
		return jars;		
	}
	
	public AntEventJarXML addEventJar() {
		return new AntEventJarXML(document, addElement(root, "eventjar"));		
	}
	
	public void removeEventJar(AntEventJarXML xml) {
		xml.getRoot().getParentNode().removeChild(xml.getRoot());
	}
	
	public AntEventJarXML[] getEventJars() {
		Element children[] = getNodes("target/eventjar");
		AntEventJarXML[] jars = new AntEventJarXML[children.length];
		
		for (int i = 0; i < children.length; i++)
			jars[i] = new AntEventJarXML(document, children[i]);
		
		return jars;		
	}
	
	public AntSbbJarXML addSbbJar() {
		return new AntSbbJarXML(document, addElement(root, "sbbjar"));		
	}
	
	public void removeSbbJar(AntSbbJarXML xml) {
		xml.getRoot().getParentNode().removeChild(xml.getRoot());
	}
	
	public AntSbbJarXML[] getSbbJars() {
		Element children[] = getNodes("target/sbbjar");
		AntSbbJarXML[] jars = new AntSbbJarXML[children.length];
		
		for (int i = 0; i < children.length; i++)
			jars[i] = new AntSbbJarXML(document, children[i]);
		
		return jars;		
	}

	public AntProfileSpecJarXML addProfileSpecJar() {
		return new AntProfileSpecJarXML(document, addElement(root, "profilespecjar"));		
	}
	
	public void removeProfileSpecJar(AntProfileSpecJarXML xml) {
		xml.getRoot().getParentNode().removeChild(xml.getRoot());
	}
	
	public AntProfileSpecJarXML[] getProfileSpecJars() {
		Element children[] = getNodes("target/profilespecjar");
		AntProfileSpecJarXML[] jars = new AntProfileSpecJarXML[children.length];
		
		for (int i = 0; i < children.length; i++)
			jars[i] = new AntProfileSpecJarXML(document, children[i]);
		
		return jars;		
	}

	public AntJarXML addJar() {
		Element child = addElement(root, "jar");
		return new AntJarXML(document, child);	
	}
	
	public AntJarXML[] getJars() {
		Element nodes[] = getNodes(root, "target/jar");
		AntJarXML xml[] = new AntJarXML[nodes.length];
		for (int i = 0; i < xml.length; i++)
			xml[i] = new AntJarXML(document, nodes[i]);
		return xml;
	}
	
	private AntJavacXML javacXML = null;
	
}
