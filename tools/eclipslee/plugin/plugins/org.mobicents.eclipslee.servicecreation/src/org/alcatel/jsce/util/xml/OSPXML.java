
/**
 *   Copyright 2005 Alcatel, OSP.
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
package org.alcatel.jsce.util.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 *  Description:
 * <p>
 * 
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class OSPXML {
	
	/**
	 * Returns an InputStream containing this XML data in UTF-8 format.
	 * 
	 * @return the InputStream containing this XML data
	 */
	
	public InputStream getInputStreamFromXML(Document document) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		XMLSerializer serializer = new XMLSerializer(baos, new OutputFormat(
				document, "utf-8", true));
		
		serializer.serialize(document);
		baos.close();

		return new ByteArrayInputStream(baos.toByteArray());		
	}
	
	/**
	 * Gets the text data of the specified element.
	 * 
	 * @param element
	 * @return
	 */
	
	public String getTextData(Node element) {		
		Text text = (Text) element.getFirstChild();
		if (text == null) return null;
		return text.getData();
		
	}
	
	public void setTextData(Node element, String newText, Document document) {
		Text text = (Text) element.getFirstChild();
		if (text == null)
			element.appendChild(document.createTextNode(newText));
		else
			text.setData(newText);
	}	
	
		
	/**
	 * Creates a new child element with the specified name and adds it to the parent
	 * element.
	 * @param parent
	 * @param childName
	 * @return
	 */
	
	protected Element addElement(Element parent, String childName, Document document) {
		
		Element child = document.createElement(childName);
		parent.appendChild(child);
		return child;
	}
	
	
	/**
	 * Gets the text at the child element specified, relative to the current root.
	 * 
	 * @param path
	 * @return
	 */
	
	public String getChildText(Element parent, String childElement) {
		Element child = getChild(parent, childElement);
		if (child == null)
			return null;
		
		return getTextData(child);
	}
	
	public void save(OutputStream stream, Document document) throws IOException {
		XMLSerializer serializer = new XMLSerializer(stream, new OutputFormat(
				document, "utf-8", true));
		
		serializer.serialize(document);
	}
	
	public void setChildText(Element parent, String childElement, String text, Document document) {
		Element child = getChild(parent, childElement);
		if (child == null)
			child = addElement(parent, childElement, document);
		
		setTextData(child, text, document);		
	}

	public Element getChild(Element parent, String child) {
		
		if (parent == null)
			throw new NullPointerException("parent cannot be null");
		
		if (child == null)
			throw new NullPointerException("child cannot be null");
		
		NodeList children = parent.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node childNode = children.item(i);
			
			if (childNode.getNodeName().equals(child))
				return (Element) childNode;
		}
		
		return null;
	}
	
	/**
	 * Gets the nodes located at the specified path.  For example "event-xml/event".
	 * 
	 * @param path
	 * @return
	 */
	protected Element[] getNodes(Element rootNode, String path) {
		
		if (path == null && path.length() == 0)
			throw new NullPointerException("Path cannot be null or empty.");
		
		StringTokenizer tokenizer = new StringTokenizer(path, "/");
		Vector nodes = new Vector();
		
		String pathRoot = tokenizer.nextToken();
		if (pathRoot == null)
			throw new NullPointerException("Path cannot be empty");

		if (rootNode.getNodeName().equals(pathRoot)) {			
			if (tokenizer.hasMoreElements())
				findElements(rootNode, tokenizer, nodes);
			else
				nodes.add(rootNode);
		}
		
		Element elements[] = new Element[nodes.size()];
		elements = (Element []) nodes.toArray(elements);
		return elements;
	}
	
	protected Element[] getNodes(String path, Document document) {
		return getNodes(document.getDocumentElement(), path);
	}
	
	protected void findElements(Element thisRoot, StringTokenizer tokenizer, Vector results) {		
		
		if (!tokenizer.hasMoreElements()) {// Sanity check.
			return;
		}
			
		String element = tokenizer.nextToken();
		boolean middle = tokenizer.hasMoreTokens();
		
		NodeList children = thisRoot.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			
			if (child.getNodeName().equals(element) && child instanceof Element) {
				if (middle) { // If more elements go and look for them.
					findElements((Element) child, tokenizer, results);
				}
				else { // This is the last element in the chain.
					results.add(child);
				}
			}
		}		
	}

}
