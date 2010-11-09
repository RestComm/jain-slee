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
public class AntJarXML extends XML {

	protected AntJarXML(Document document, Element root) {
		super(document, root);
	}
	
	public AntFileSet addFileSet() {
		Element child = addElement(root, "fileset");
		return new AntFileSet(document, child);		
	}
	
	public void removeFileSet(AntFileSet fileSet) {
		fileSet.getRoot().getParentNode().removeChild(fileSet.getRoot());		
	}
	
	public AntFileSet[] getFileSets() {
		Element nodes[] = getNodes("jar/fileset");
		AntFileSet sets[] = new AntFileSet[nodes.length];
		for (int i = 0; i < nodes.length; i++)
			sets[i] = new AntFileSet(document, nodes[i]);
		return sets;
	}
	
	public void setJarFile(String file) {
		root.setAttribute("jarfile", file);
	}
	
	public String getJarFile() {
		return root.getAttribute("jarfile");
	}	
	
	public AntFileSet addMetaInf() {
		Element child = addElement(root, "metainf");
		return new AntFileSet(document, child);
	}
	
	public void removeMetaInf(AntFileSet metaInf) {
		metaInf.getRoot().getParentNode().removeChild(metaInf.getRoot());
	}

	public AntFileSet[] getMetaInfs() {
		Element nodes[] = getNodes("jar/metainf");
		AntFileSet sets[] = new AntFileSet[nodes.length];
		for (int i = 0; i < nodes.length; i++)
			sets[i] = new AntFileSet(document, nodes[i]);
		return sets;
	}

}
