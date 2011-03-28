
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
package org.alcatel.jsce.statevent;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.alcatel.jsce.util.log.SCELogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *  Description:
 * <p>
 *  Describes an stat event catalog.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class EventCatalog {
	/** name of the catalog*/
	private String catalogName = "not provided";
	/** name of the documentation*/
	private String docName = "not Provided";
	/** List of sub features, @link EventSubFeature*/
	private List subFeatureEvent = null;
	/** Feature ID is the feature ID of the catalog*/
	private String featureID = "1";
	/** Represents all the events present in each type of each 
	 * subfeature*/
	private List allEvents = null;
	/** The absolute fileLocation of the file*/
	private URL fileLocation = null;
	/** The associeted XML document*/
	private Document document = null;

	
	/**
	 * Constructor.
	 */
	public EventCatalog() {
		subFeatureEvent = new ArrayList();
	}
	
	///////////////////////////////////////////
	//
	// Access Methods
	//
	//////////////////////////////////////////

	/**
	 * @return Returns the catalogName.
	 */
	public String getCatalogName() {
		return catalogName;
	}


	public String getFeatureID() {
		return featureID;
	}

	public void setFeatureID(String featureID) {
		this.featureID = featureID;
	}

	/**
	 * @param catalogName The catalogName to set.
	 */
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}


	/**
	 * @return Returns the docName.
	 */
	public String getDocName() {
		return docName;
	}


	/**
	 * @param docName The docName to set.
	 */
	public void setDocName(String docName) {
		this.docName = docName;
	}


	/**
	 * @return Returns the subFeatureEvent.
	 */
	public List getSubFeatureEvent() {
		return subFeatureEvent;
	}


	/**
	 * @param subFeatureEvent The subFeatureEvent to set.
	 */
	public void setSubFeatureEvent(List subFeatureEvent) {
		this.subFeatureEvent = subFeatureEvent;
	}
	
	/**
	 * @return Returns the document.
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * @param document The document to set.
	 */
	public void setDocument(Document document) {
		this.document = document;
	}

	/**
	 * @return Returns the fileLocation.
	 */
	public URL getFileLocation() {
		return fileLocation;
	}

	/**
	 * @param fileLocation The fileLocation to set.
	 */
	public void setFileLocation(URL location) {
		this.fileLocation = location;
	}

	/**
	 * @return all events of each type of each subfeature. 
	 */
	public List getAllStatEvent() {
		allEvents = new ArrayList();
		for (Iterator iter = getSubFeatureEvent().iterator(); iter.hasNext();) {
			EventSubFeature subFeature = (EventSubFeature) iter.next();
			for (Iterator iterator = subFeature.getEventTypes().iterator(); iterator.hasNext();) {
				EventType type = (EventType) iterator.next();
				allEvents.addAll(type.getEvents());
			}
		}
		return allEvents;
	}
	
	// /////////////////////////////////////////
	//
	// Creation
	//
	//////////////////////////////////////////

	/**
	 * @param name the name of the file
	 * @param docname the doc name of the catalog
	 * @param fileLocation hte fileLocation of the file
	 * @param subDirNumber number of subdirectory between the file and the external fileLocation root
	 * @param integer the feature ID of the catalog
	 * @return an new empty stat event catalog and its associted xml document. 
	 */
	public static EventCatalog createEmptyCatalog(String name, String docname, URL location, int subDirNumber, String id) {
		EventCatalog catalog = new EventCatalog();
		catalog.setCatalogName(name);
		catalog.setDocName(docname);
		catalog.setFileLocation(location);
		catalog.setFeatureID(id);
		String schemaLocation = "";
		for (int i = 0; i <subDirNumber; i++) {
			schemaLocation +="../";
		}
		schemaLocation +="StatEvent.xsd";
		/*Document building*/
		try {
			Document doc;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			doc = db.newDocument();
			
			/* Create component root */
			Element root = doc.createElement("component");
			root.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
			root.setAttribute("xsi:noNamespaceSchemaLocation", schemaLocation);
			doc.appendChild(root);
			
			  Element nameNode = doc.createElement("name");
			  nameNode.appendChild(doc.createTextNode(name));
			  root.appendChild(nameNode);
			  
			  Element docNode = doc.createElement("docname");
			  docNode.appendChild(doc.createTextNode(docname));
			  root.appendChild(docNode);
			  			  
			  Element depNode = doc.createElement("SubFeats");
			  root.appendChild(depNode);
			  
			  catalog.setDocument(doc);

		} catch (ParserConfigurationException e) {
			SCELogger.logError("Parsing error while trying to build a new XML catalog file", e);
			e.printStackTrace();
		}
		return catalog;
	}

	/**
	 * Remove the event from the catalog (in memory).
	 * @param eventSelected the event to remove.
	 */
	public void removeEvent(StatEvent eventSelected) {
		for (Iterator iter = getSubFeatureEvent().iterator(); iter.hasNext();) {
			EventSubFeature subFeature = (EventSubFeature) iter.next();
			for (Iterator iterator = subFeature.getEventTypes().iterator(); iterator.hasNext();) {
				EventType type = (EventType) iterator.next();
				type.getEvents().remove(eventSelected);
			}
		}
		
	}
	
	

}
