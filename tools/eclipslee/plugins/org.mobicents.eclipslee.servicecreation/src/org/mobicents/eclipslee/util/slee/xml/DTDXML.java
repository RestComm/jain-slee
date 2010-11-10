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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.parsers.SAXParser;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author cath
 *
 * Basic XML file reading class.  It's likely that you'll want to use
 * the component-specific version of this class.
 */
public class DTDXML extends XML {

	
	/**
	 * Creates an empty XML file.
	 *
	 */
	
	public DTDXML(String qualifiedName, String publicID, String systemID, EntityResolver resolver) throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setEntityResolver(resolver);
		builder.setErrorHandler(new XMLErrorHandler());
		
		DOMImplementation impl = builder.getDOMImplementation();
		DocumentType docType = impl.createDocumentType(qualifiedName, publicID, systemID);
		document = impl.createDocument(systemID, qualifiedName, docType);
		
		root = document.getDocumentElement();
	}
	
	/**
	 * Parse the provided InputStream as though it contains JAIN SLEE Event XML Data.
	 * @param stream
	 */
	
	public DTDXML(InputStream stream, EntityResolver resolver) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setEntityResolver(resolver);
		builder.setErrorHandler(new XMLErrorHandler());
		document = builder.parse(stream);
		root = document.getDocumentElement();		
	}
	
	protected void readDTDVia(EntityResolver resolver, InputSource dummyXML) {
		try {
			SAXParser parser = new SAXParser();
			dtd = new DTDHandler();
			parser.setEntityResolver(resolver);
			parser.setProperty("http://xml.org/sax/properties/declaration-handler", dtd);
			parser.parse(dummyXML);
		} catch (Exception e) {
			System.err.println("Exception thrown trying to read DTD.");
			e.printStackTrace();
		}
	}
		
	/**
	 * Creates a new XML object rooted at the specified element.
	 * 
	 * @param document
	 * @param root
	 */
	protected DTDXML(Document document, Element root, DTDHandler dtd) {
		super(document, root);
		this.dtd = dtd;
	}

	/**
	 * Creates a new child element with the specified name and adds it to the parent
	 * element at the location specified in the document's DTD.
	 * @param parent
	 * @param childName
	 * @return
	 */
	
	protected Element addElement(Element parent, String childName) {
		
		Element child = document.createElement(childName);

		// Should be able to use the DTD to determine which elements to insert this before/after		
		// Returns children in order.
		
		DTDHandler.Element ele = dtd.getElementDecl(parent.getNodeName());
		String validChildren[] = ele.getChildren();
		int childIndex = -1;
		for (int i = 0; i < validChildren.length; i++) {
			if (validChildren[i].equals(childName)) {
				childIndex = i;
				break;
			}
		}
		if (childIndex == -1) {
			System.err.println("Element " + childName + " is not a valid child of " + parent.getNodeName());
			return null;
//			throw new Exception("Not a valid child of this node.");
		}		
		
		NodeList children = parent.getChildNodes();
		
		for (int i = childIndex + 1; i < validChildren.length; i++) {
			// Look for a current child with the specified index.
			// If found insert the new node before it.
			// If not found continue
			
			for (int j = 0; j < children.getLength(); j++) {
				if (children.item(j).getNodeName().equals(validChildren[i])) {
					parent.insertBefore(child, children.item(j));
					return child;					
				}
			}
		}	

		// If still not found append to the parent element.
		parent.appendChild(child);
		return child;
	}

	protected DTDHandler dtd;
}
