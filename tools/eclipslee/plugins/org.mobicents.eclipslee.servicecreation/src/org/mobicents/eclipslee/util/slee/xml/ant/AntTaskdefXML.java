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
public class AntTaskdefXML extends XML {

	protected AntTaskdefXML(Document document, Element root) {
		super(document, root);
	}
	
	public void setName(String name) {
		root.setAttribute("name", name);
	}
	
	public String getName() {
		return root.getAttribute("name");
	}
	
	public void setClassname(String className) {
		root.setAttribute("classname", className);
	}
	
	public String getClassname() {
		return root.getAttribute("classname");
	}
	
	public void setClasspath(String classpath) {
		root.setAttribute("classpath", classpath);
	}
	
	public String getClasspath() {
		return root.getAttribute("classpath");
	}
		
}

