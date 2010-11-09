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
public class AntJavacXML extends XML {

	protected AntJavacXML(Document document, Element root) {
		super(document, root);
	}
	
	public void setDestdir(String dest) {
		root.setAttribute("destdir", dest);		
	}
	
	public String getDestdir() {
		return root.getAttribute("destdir");
	}
	
	public void setSrcdir(String src) {
		root.setAttribute("srcdir", src);
	}
	
	public String getSrcdir() {
		return root.getAttribute("srcdir");
	}
	
	public String[] getIncludes() {
		String includes = root.getAttribute("includes");
		if (includes == null)
			return new String[0];
		
		StringTokenizer tok = new StringTokenizer(includes, ",");
		String output[] = new String[tok.countTokens()];
		for (int i = 0; i < output.length; i++)
			output[i] = tok.nextToken();
		
		return output;
	}
	
	public void setIncludes(String [] includes) {		
		String attr = "";
		for (int i = 0; i < includes.length; i++) {
			if (i > 0)
				attr += ",";
			attr += includes[i];
		}
		
		root.setAttribute("includes", attr);		
	}
	
	public void addPathXML(AntPathXML path) {
		Element classpath = getChild(root, "classpath");
		if (classpath == null)
			classpath = addElement(root, "classpath");
		
		Element pathElement = addElement(classpath, "path");
		pathElement.setAttribute("refid", path.getID());		
	}
	
	public void removePathXML(AntPathXML path) {
		removePathID(path.getID());
	}
	
	public void removePathID(String id) {
		Element elements[] = getNodes("javac/classpath/path");
		
		for (int i = 0; i < elements.length; i++) {
			String attr = elements[i].getAttribute("refid");
			if (id.equals(attr)) {
				elements[i].getParentNode().removeChild(elements[i]);
				return;
			}
		}		
	}
	
	public String[] getPathRefIDs() {
		Element elements[] = getNodes("javac/classpath/path");
		String ids[] = new String[elements.length];
		for (int i = 0; i < ids.length; i++)
			ids[i] = elements[i].getAttribute("refid");
		
		return ids;		
	}
	
}
