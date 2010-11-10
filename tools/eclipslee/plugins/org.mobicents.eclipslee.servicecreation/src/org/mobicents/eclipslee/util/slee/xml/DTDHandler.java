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
package org.mobicents.eclipslee.util.slee.xml;

import java.util.StringTokenizer;
import java.util.Vector;

/**
 * @author cath
 */
public class DTDHandler implements org.xml.sax.ext.DeclHandler {
	
	private Vector elements = new Vector();
	
	public DTDHandler() {		
	}
	
	public void elementDecl(String name,
			java.lang.String model)
	{
		// We do care about elements, and more importantly, their CHILDREN						
		Element element = new Element(name, model);
		elements.add(element);		
	}
	
	public Element getElementDecl(String name) {
		for (int i = 0; i < elements.size(); i++) {
			Element element = (Element) elements.get(i);
			if (element.getName().equals(name))
				return element;
		}		
		
		return null;
	}
	
	public void attributeDecl(String elementName, String attrName, String type, String defaultValue, String value) {}
	public void externalEntityDecl(String name, String publicID, String systemID) {}	
	public void internalEntityDecl(String name, String value) {}	

	public class Element {		
		protected Element(String name, String model) {
			
			this.name = name;
			
			children = new String[0];
			if (model.equalsIgnoreCase("EMPTY"))
				return;

			model = model.replaceAll("\\(", "");
			model = model.replaceAll("\\)", "");
			
			if (model.equalsIgnoreCase("#PCDATA"))
				return;

			model = model.replaceAll("\\?", "");
			model = model.replaceAll("\\+", "");
			model = model.replaceAll("\\*", "");
			
			StringTokenizer tok = new StringTokenizer(model, ",|");
			children = new String[tok.countTokens()];
			int count = 0;
			while (tok.hasMoreTokens()) {
				String token = tok.nextToken();
				children[count++] = token;
			}
		}
		
		public String getName() {
			return name;
		}
		
		public String[] getChildren() {
			return children;
		}
		
		public String toString() {			
			String output = "Element " + name + "\n";
			for (int i = 0 ; i < children.length; i++)
				output += "\t[" + i + "] " + children[i] + "\n";
			
			return output;			
		}
		
		private String name;
		private String children[];
	}
	
}