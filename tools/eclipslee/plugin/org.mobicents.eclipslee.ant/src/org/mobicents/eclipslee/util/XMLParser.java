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

package org.mobicents.eclipslee.util;

import java.net.URL;

import java.io.IOException;
import java.io.InputStream;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.mobicents.eclipslee.util.exception.XMLException;
import org.w3c.dom.Document;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Element;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public final class XMLParser {
    /**
     * Parse an XML document.  Equivalent to <tt>getDocument(url, null)</tt>.
     * @param url the url of the document to parse.
     * @return a parsed document tree.
     * @exception IllegalArgumentException if the supplied url is null.
     * @exception IOException if an IO exception or parse error occurs with the URL.
     */
    public static Document getDocument(URL url) throws IllegalArgumentException, IOException {
	return getDocument(url, null);
    }

    /**
     * Parse an XML document using a given resolver for DTDs.
     * @param url the url of the document to parse.
     * @param resolver the entity resolver to use.  If null, no specific resolver
     *        is used.
     * @return a parsed document tree.
     * @exception IllegalArgumentException if the supplied url is null.
     * @exception IOException if an IO exception occurs with the URL.
     */
    public static Document getDocument(URL url, EntityResolver resolver) throws IllegalArgumentException, IOException {
	return getDocument(url, resolver, false);
    }

    private static class ParseErrorHandler implements ErrorHandler {
	public void error(SAXParseException e) throws SAXException {
	    throw e;
	}

	public void fatalError(SAXParseException e) throws SAXException {
	    throw e;
	}

	public void warning(SAXParseException e) throws SAXException {
	    System.err.println("Warning: " + e);
	}
    }

    public static Document getDocument(URL url, EntityResolver resolver, boolean validating) throws IllegalArgumentException, IOException {
        if (url == null) throw new IllegalArgumentException("URL is null");

        InputStream is = null;
        try {
            is = url.openStream();
	    InputSource source = new InputSource(is);
	    source.setSystemId(url.toString());	    
	    return getDocument(source, resolver, validating);
	} finally {
            try { if (is != null) is.close(); } catch (IOException ioe) {}
        }
    }

    public static Document getDocument(InputSource source, EntityResolver resolver, boolean validating) throws IOException {
	try {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setValidating(validating);	    
            DocumentBuilder builder = factory.newDocumentBuilder();
            if (resolver != null) builder.setEntityResolver(resolver);
	    builder.setErrorHandler(new ParseErrorHandler());
            return builder.parse(source);
        } catch (SAXParseException e) {
	    throw new XMLException("parse error at " + e.getSystemId() + ":" + e.getLineNumber(), e);
        } catch (SAXException e) {
	    throw new XMLException("error reading document", e);
	} catch (ParserConfigurationException e) {
	    throw new XMLException("config error", e);
        }
    }

    /**
     * Get all the descendent element nodes of a given parent node where the
     * name of each descendent node is that given by the specified tag name.
     * Note that only direct child nodes are examined.
     *
     * @param parent the parent element node containing the descendants.
     * @param tagName the name of the tag to retrieve elements for.
     * @return a set of <tt>org.w3c.dom.Element</tt> objects, where each object in the
     *        set corresponds to an element node of the parent node whose tag name is
     *        that specified by <tt>tagName</tt>
     * @exception IllegalArgumentException if either of the supplied
     *        parameters is null, or <tt>tagName</tt> is zero-length..
     */
    public static List getElementsByTagName(Element parent, String tagName) throws IllegalArgumentException {
        if (parent == null) throw new IllegalArgumentException("parent is null");
        if (tagName == null) throw new IllegalArgumentException("tagName is null");
        if (tagName.length() == 0) throw new IllegalArgumentException("tagName is zero-length");

        NodeList nodelist = parent.getChildNodes();
        ArrayList elements = new ArrayList(nodelist.getLength());
        for (int i=0; i<nodelist.getLength(); i++) {
            Node node = nodelist.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && tagName.equals(((Element)node).getTagName())) {
                elements.add((Element)node);
            }
        }
        return elements;
    }

    /**
     * Get a unique descendant element node from a given parent node.  The
     * descendant element is expected to occur once and only once.
     * @param parent the parent element node containing the descendant.
     * @param tagName the name of the tag to retrieve elements for.
     * @return the descendant element noe.
     * @exception IllegalArgumentException if either of the supplied
     *        parameters is null, or <tt>tagName</tt> is zero-length..
     * @exception XMLException if zero or more than one descendant
     *        element with the given name is found.
     */
    public static Element getUniqueElement(Element parent, String tagName) throws IllegalArgumentException, XMLException {
        Iterator elements = getElementsByTagName(parent, tagName).iterator();
        if (elements.hasNext()) {
            Element element = (Element)elements.next();
            if (elements.hasNext()) {
                throw new XMLException("Only one \"" + tagName + "\" element expected");
            }
            return element;
        }
        else {
            throw new XMLException("The \"" + tagName + "\" element was not found");
        }
    }

    /**
     * Get an optional descendant element node from a given parent node.  The
     * descendant may occur at most one time.
     * @param parent the parent element containing the descendant.
     * @param tagName the name of the tag to retrieve elements for.
     * @return the descendant element, or <tt>null</tt> if no descendant
     *        was found.
     * @exception IllegalArgumentException if either of the supplied
     *        parameters is null, or <tt>tagName</tt> is zero-length..
     * @exception XMLException if more than one element was found.
     */
    public static Element getOptionalElement(Element parent, String tagName) throws IllegalArgumentException, XMLException {
        Iterator elements = getElementsByTagName(parent, tagName).iterator();
        if (elements.hasNext()) {
            Element element = (Element)elements.next();
            if (elements.hasNext()) {
                throw new XMLException("At most one \"" + tagName + "\" element expected");
            }
            return element;
        }
        else {
            return null;
        }
    }

    /**
     * Get the contents of an text element.  A text element contains a string of
     * text only, not further XML tags.
     * @param element the text element.
     * @return the text contents of the element as a string.  Whitespace from both
     *        ends of the string is trimmed.
     * @throws IllegalStateException if the element is null
     * @throws XMLException if the element is not a text node.
     */
    public static String getElementContent(Element element) throws IllegalArgumentException, XMLException {
        return getRawElementContent(element).trim();
    }

    /**
     * Get the contents of an text element.  A text element contains a string of
     * text only, not further XML tags.
     * @param element the text element.
     * @return the text contents of the element as a string
     * @throws IllegalStateException if the element is null
     * @throws XMLException if the element is not a text node.
     */
    private static String getRawElementContent(Element element) throws IllegalArgumentException, XMLException {
        if (element == null) throw new IllegalArgumentException("Element is null");

        NodeList nodelist = element.getChildNodes();
        if (nodelist.getLength() == 0)
            return "";

        if ((nodelist.getLength() != 1) || (nodelist.item(0).getNodeType() != Node.TEXT_NODE))
            throw new XMLException("Not a text node: " + element);

        return nodelist.item(0).getNodeValue();
    }

    /**
     * Get the contents of a text element that is a child of another node.
     *
     * @param parent the parent element of the text element
     * @param name the tag name of the text element
     * @return the text contents of the element as a string.  Whitespace from both
     *        ends of the string is trimmed.
     * @throws XMLException if the element is not found, or is not a text node
     */
    public static String getTextElement(Element parent, String name) throws XMLException {
        return getElementContent(getUniqueElement(parent, name));
    }

    /**
     * Get the contents of a text element that is a child of another node.
     *
     * @param parent the parent element of the text element
     * @param name the tag name of the text element
     * @return the text contents of the element as a string.  Whitespace from both
     *        ends of the string is trimmed.
     * @throws XMLException if the element is not found, or is not a text node
     */
    public static String getRawTextElement(Element parent, String name) throws XMLException {
        return getRawElementContent(getUniqueElement(parent, name));
    }

    public static String getOptionalTextElement(Element parent, String name) throws XMLException {
        Element e = getOptionalElement(parent, name);
        if (e == null) return null;
        return getElementContent(e);
    }

    /**
     * Create a new tag as a child of an existing element. Fill the tag with
     * the given text string.
     *
     * @param parent the parent element to create the tag as a child of
     * @param tagName the tag name of the child to create
     * @param text the text content to give the newly created tag     
     */
    public static void createTextElement(Element parent, String tagName, String text) {
        Document doc = parent.getOwnerDocument();
        Element newElement = doc.createElement(tagName);
        newElement.appendChild(doc.createTextNode(text));
        parent.appendChild(newElement);
    }

    
    /**
     * Merge two documents. Elements from the toMerge document are copied into the toModify
     * document. Elements with the special attribute 'id' are matched between documents based
     * on the value of the attribute (which is expected to be unique across all instances in
     * a document -- i.e. it is an XML ID attribute).
     *<p>
     * For example, if this is the toModify document:
     *<pre>
     * &lt;doc1&gt
     *   &lt;tag1&gt abc &lt;/tag1&gt
     *   &lt;tag2 id="123"&gt 
     *     &lt;tag3&gt; def &lt;/tag3&gt;
     *   &lt;/tag2&gt
     * &lt;/doc1&gt
     *</pre>
     * .. and this is the toMerge document:
     *<pre>
     * &lt;doc2&gt
     *   &lt;new-tag&gt ghi &lt;/new-tag&gt
     *   &lt;tag2 id="123"&gt
     *     &lt;new-tag-2&gt; jkl &lt;/new-tag-2&gt;
     *   &lt;/tag2&gt
     * &lt;/doc2&gt
     *</pre>
     *</pre>
     * .. then toModify will become:
     *<pre>
     * &lt;doc1&gt
     *   &lt;tag1&gt abc &lt;/tag1&gt
     *   &lt;tag2 id="123"&gt 
     *     &lt;tag3&gt; def &lt;/tag3&gt;
     *     &lt;new-tag-2&gt; jkl &lt;/new-tag-2&gt;
     *   &lt;/tag2&gt
     *   &lt;new-tag&gt ghi &lt;/new-tag&gt
     * &lt;/doc1&gt
     *</pre>
     *
     * @param toModify the document to merge data into
     * @param toMerge the document to merge data from
     * @param tagsToMerge a non-null array of tags where collisions should be merged
     * @throws XMLException if an ID was found in toMerge that could not be matched up with toModify
     */
    public static void mergeDocuments(Document toModify, Document toMerge, String[] tagsToMerge) throws XMLException {
	mergeTree(toModify.getDocumentElement(), toModify.getDocumentElement(), toMerge.getDocumentElement(), tagsToMerge);
    }

    /**
     * As {@link #mergeDocuments(Document,Document,String[])}, but no collisions are merged
     * @param toModify the document to merge data into
     * @param toMerge the document to merge data from
     */
    public static void mergeDocuments(Document toModify, Document toMerge) throws XMLException {
	mergeDocuments(toModify, toMerge, new String[0]);
    }

    private static Element findID(Node top, String id) {
	NodeList childList = top.getChildNodes();
	for (int i = 0; i < childList.getLength(); ++i) {
	    Node child = childList.item(i);
	    if (child.getNodeType() == Node.ELEMENT_NODE) {
		Element elem = (Element)child;
		Attr idAttr = elem.getAttributeNode("id");
		if (idAttr != null) {
		    if (idAttr.getValue().equals(id))
			return elem;
		    else
			continue;  // don't inspect past an existing ID attribute!
		}
	    }
	    
	    // Check subtree
	    Element found = findID(child, id);
	    if (found != null)
		return found;
	}

	return null;
    }

    // Merge children of 'source' to be children of 'dest'.
    // Also do ID merging.
    private static void mergeTree(Node top, Node dest, Node source, String[] tagsToMerge) throws XMLException {
	NodeList sourceList = source.getChildNodes();
	for (int i = 0; i < sourceList.getLength(); ++i) {
	    Node sourceChild = sourceList.item(i);
	    
	    if (sourceChild.getNodeType() == Node.ELEMENT_NODE) {
		Element elem = (Element)sourceChild;
		Attr idAttr = elem.getAttributeNode("id");
		if (idAttr != null) {
		    Element mergeDest = findID(top, idAttr.getValue());
		    if (mergeDest == null)
			throw new XMLException("Missing ID attribute: " + idAttr.getValue());
		    mergeTree(mergeDest, mergeDest, elem, tagsToMerge);
		    continue;
		}

                // Handle node collisions -- merge children.
                boolean found = false;
                NodeList destChildren = dest.getChildNodes();
                for (int j = 0; j < destChildren.getLength() && !found; ++j) {
                    Node destNode = destChildren.item(j);
                    if (destNode.getNodeType() == Node.ELEMENT_NODE && ((Element)destNode).getTagName().equals(elem.getTagName())) {
                        for (int k = 0; k < tagsToMerge.length && !found; ++k) {
                            if (elem.getTagName().equals(tagsToMerge[k])) {
                                // OK to merge, do it.
                                found = true;
                                mergeTree(top, destNode, sourceChild, tagsToMerge);
                            }
                        }
                    }
                }
                
                if (found)
                    continue;
            }

            switch (sourceChild.getNodeType()) {
            default:    
                // Don't copy other nodes (attributes, etc).
                // nb: attributes are actually copied automatically when we import their
                // owning element (see Document.importNode() javadoc)
                break;

            case Node.ELEMENT_NODE:
                {
                    // Import and recurse.
                    Node newNode = dest.getOwnerDocument().importNode(sourceChild, false);
                    dest.appendChild(newNode);	    
                    mergeTree(top, newNode, sourceChild, tagsToMerge);
                    break;
                }

            case Node.PROCESSING_INSTRUCTION_NODE:
            case Node.TEXT_NODE:
            case Node.CDATA_SECTION_NODE:
            case Node.COMMENT_NODE:
            case Node.ENTITY_REFERENCE_NODE:
                {
                    // Do a deep copy import as these nodes cannot contain Elements
                    Node newNode = dest.getOwnerDocument().importNode(sourceChild, true);
                    dest.appendChild(newNode);	    
                    break;
                }
            }
	}
    }    
}
