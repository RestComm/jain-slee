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

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author cath
 */
public class AntCleanTargetXML extends AntTargetXML {

	protected AntCleanTargetXML(Document document, Element root) {
		super(document, root);
	}
	
	public void addDir(String dir) {
		Element child = addElement(root, "delete");
		child.setAttribute("dir", dir);
	}
	
	public void addFile(String file) {
		Element child = addElement(root, "delete");
		child.setAttribute("file", file);		
	}
	
	public void removeDir(String dir) {
		Element nodes[] = getNodes(root, "target/delete");
		for (int i = 0; i < nodes.length; i++) {
			String attr = nodes[i].getAttribute("dir");
			if (attr != null && attr.equals(dir)) {
				nodes[i].getParentNode().removeChild(nodes[i]);
				return;	
			}
		}		
	}
	
	public void removeFile(String file) {
		Element nodes[] = getNodes(root, "target/delete");
		for (int i = 0; i < nodes.length; i++) {
			String attr = nodes[i].getAttribute("file");
			if (attr != null && attr.equals(file)) {
				nodes[i].getParentNode().removeChild(nodes[i]);
				return;	
			}
		}		
	}

	public String[] getDirs() {
		Element nodes[] = getNodes(root, "target/delete");
		Vector tmp = new Vector();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i].getAttribute("dir") != null)
				tmp.add(nodes[i].getAttribute("dir"));			
		}
		
		String array[] = new String[tmp.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = (String) tmp.get(i);

		return array;		
	}
	
	public String[] getFiles() {
		Element nodes[] = getNodes(root, "target/delete");
		Vector tmp = new Vector();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i].getAttribute("file") != null)
				tmp.add(nodes[i].getAttribute("file"));			
		}
		
		String array[] = new String[tmp.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = (String) tmp.get(i);

		return array;
	}
	
}


