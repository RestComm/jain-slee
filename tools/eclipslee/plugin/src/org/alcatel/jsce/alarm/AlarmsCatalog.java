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
package org.alcatel.jsce.alarm;

import java.net.URL;
import java.util.ArrayList;
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
 *  Represents a catalog of alarms.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class AlarmsCatalog {
	/** List of alarms (@link Alarm)*/
	private List alarmEvents = null;
	/** name of the catalog*/
	private String catalogName = "not provided";
	/** name of the documentation*/
	private String docName = "not Provided";
	/** long description*/
	private String  longDescription = "not Provided";
	/** short description*/
	private String shortDescription = "not Provided";
	/** list of strings which describe the depencies */
	private List dependecies = null;
	
	//File information
	/** The file location*/
	private URL fileLocation = null;
	/** The associted XML document*/
	private Document	document = null;

	/**
	 * Constructor.
	 */
	public AlarmsCatalog() {
		alarmEvents = new ArrayList();
		dependecies = new ArrayList();
	}
	
	
	///////////////////////////////////////////
	//
	// Access methods
	//
	//////////////////////////////////////////
	

	/**
	 * @return Returns the catalogName.
	 */
	public String getCatalogName() {
		return catalogName;
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
	 * @return Returns the longDescription.
	 */
	public String getLongDescription() {
		return longDescription;
	}

	/**
	 * @param longDescription The longDescription to set.
	 */
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	/**
	 * @return Returns the shortDescription.
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * @param shortDescription The shortDescription to set.
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * @return Returns the alarmEvents.
	 */
	public List getAlarmEvents() {
		return alarmEvents;
	}

	/**
	 * @return Returns the dependecies.
	 */
	public List getDependecies() {
		return dependecies;
	}
	
	public void addEvent(Alarm alarm) {
		getAlarmEvents().add(alarm);
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
	public void setFileLocation(URL fileLocation) {
		this.fileLocation = fileLocation;
	}
	
	///////////////////////////////////////////
	//
	// Creationnal section
	//
	//////////////////////////////////////////
	
	/**
	 * Creates an empty Alarm catalog and the associted XML document.
	 * @param name the name of the catalog
	 * @param docname the documentation name
	 * @param longDesc  long descripton
	 * @param shortDesc short description
	 * @param location the absolute location of the file
	 * @param author the author's name
	 * @param mail the author's e-mail
	 * @return an empty alarm catalog and the associted XML document.
	 */
	public static AlarmsCatalog createEmptyCatalog(String name, String docname,String longDesc, String shortDesc, URL location, String author, String mail, int subDirNumber){
		AlarmsCatalog catalog = new AlarmsCatalog();
		catalog.setCatalogName(name);
		catalog.setDocName(docname);
		catalog.setLongDescription(longDesc);
		catalog.setShortDescription(shortDesc);
		catalog.setFileLocation(location);
		String schemaLocation = "";
		for (int i = 0; i <subDirNumber; i++) {
			schemaLocation +="../";
		}
		schemaLocation +="AlarmSchema.xsd";
		/*Document building*/
		try {
			Document doc;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			doc = db.newDocument();
			
			/* Create component root */
			Element root = doc.createElement("component");
			root.setAttribute("author", author);
			root.setAttribute("authoremail", mail);
			root.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
			root.setAttribute("xsi:noNamespaceSchemaLocation", schemaLocation);
			doc.appendChild(root);
			
			  Element nameNode = doc.createElement("name");
			  nameNode.appendChild(doc.createTextNode(name));
			  root.appendChild(nameNode);
			  
			  Element docNode = doc.createElement("docname");
			  docNode.appendChild(doc.createTextNode(docname));
			  root.appendChild(docNode);
			  
			  Element decNode = doc.createElement("description");
			  root.appendChild(decNode);
			  
			  Element longDescNode = doc.createElement("long");
			  decNode.appendChild(longDescNode);
			  longDescNode.appendChild(doc.createTextNode(longDesc));
			  
			  Element shortDescNode = doc.createElement("short");
			  decNode.appendChild(shortDescNode);
			  shortDescNode.appendChild(doc.createTextNode(shortDesc));
			  
			  Element events = doc.createElement("events");
			  root.appendChild(events);
			  
			  Element depNode = doc.createElement("dependencies");
			  root.appendChild(depNode);
			  
			  catalog.setDocument(doc);

		} catch (ParserConfigurationException e) {
			SCELogger.logError("Parsing error while trying to build a new XML catalog file", e);
			e.printStackTrace();
		}
		return catalog;
	}


	/**
	 * @param alarmEvents The alarmEvents to set.
	 */
	public void setAlarmEvents(List alarmEvents) {
		this.alarmEvents = alarmEvents;
	}


	/**
	 * @param dependecies The dependecies to set.
	 */
	public void setDependecies(List dependecies) {
		this.dependecies = dependecies;
	}

}
