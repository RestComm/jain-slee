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

import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @author cath
 */
public class AntInitTargetXML extends AntTargetXML {

	protected AntInitTargetXML(Document document, Element root) {
		super(document, root);
	}
	
	public AntPathXML addPath(String name) {		
		Element child = addElement(root, "path");
		child.setAttribute("id", name);
		return new AntPathXML(document, child);
	}
	
	public void removePath(AntPathXML pathID) {		
		pathID.getRoot().getParentNode().removeChild(pathID.getRoot());		
	}
	
	public AntPathXML[] getPaths() {
		
		Element children[] = getNodes("target/path");
		AntPathXML [] paths = new AntPathXML[children.length];
		for (int i = 0; i < children.length; i++)
			paths[i] = new AntPathXML(document, children[i]);
		
		return paths;		
	}
	
	public AntPathXML getPathID(String name) throws ComponentNotFoundException {
		AntPathXML paths[] = getPaths();
		for (int i = 0; i < paths.length; i++) {
			if (paths[i].getID().equals(name)) {
				return paths[i];				
			}
		}
		throw new ComponentNotFoundException("This path ID does not exist in this target.");
	}
	
	public AntTaskdefXML addTaskdef() {
		Element child = addElement(root, "taskdef");
		return new AntTaskdefXML(document, child);		
	}

	public AntTaskdefXML getTaskdef(String name) throws ComponentNotFoundException {
		AntTaskdefXML tasks[] = getTaskdefs();
		for (int i = 0; i < tasks.length; i++)
			if (tasks[i].getName().equals(name))
				return tasks[i];
				
		throw new ComponentNotFoundException("Cannot find specified taskdef in this task.");
	}
				
	public AntTaskdefXML[] getTaskdefs() {
		
		Element children[] = getNodes("target/taskdef");
		AntTaskdefXML tasks[] = new AntTaskdefXML[children.length];
		
		for (int i = 0; i < children.length; i++)
			tasks[i] = new AntTaskdefXML(document, children[i]);
		
		return tasks;
	}

	public void removeTaskdef(AntTaskdefXML taskdef) {
		taskdef.getRoot().getParentNode().removeChild(taskdef.getRoot());		
	}
}

