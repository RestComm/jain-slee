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
public class AntSbbJarXML extends XML {

	protected AntSbbJarXML(Document document, Element root) {
		super(document, root);
	}
	
	public void setXML(String xml) {
		root.setAttribute("sbbjarxml", xml);
	}
	
	public String getXML() {
		return root.getAttribute("sbbjarxml");
	}
	
	public void setClasspath(String classpath) {
		root.setAttribute("classpath", classpath);
	}
	
	public String getClasspath() {
		return root.getAttribute("classpath");
	}	
	
	public void setDestfile(String dest) {
		root.setAttribute("destfile", dest);
	}
	
	public String getDestfile() {
		return root.getAttribute("destfile");
	}
}
