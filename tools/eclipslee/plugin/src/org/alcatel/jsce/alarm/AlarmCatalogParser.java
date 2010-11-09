
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

import org.alcatel.jsce.util.log.SCELogger;
import org.alcatel.jsce.util.xml.ErrorStatus;
import org.alcatel.jsce.util.xml.FileManager;
import org.alcatel.jsce.util.xml.XMLErrorHanlder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *  Description:
 * <p>
 * This object is specialized in the acces of the alarm catalog XML file.  
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class AlarmCatalogParser {
	/** Errors occured in the last parsing.*/
	private List errors = null;

	/**
	 * 
	 */
	public AlarmCatalogParser() {
		errors = new ArrayList();
	}
	
	/**
     * Open the alarm catalog XML file and load its content.
     * 
     * @param fileLocation
     *                  is the absolute URL path of the Alarm file.
     * @return the list of alarms contained in the catalog.
     */
    private AlarmsCatalog loadAlarmsFromXMl(URL fileLocation, URL schemaSource) {
        Document xmlDocument = FileManager.getInstance().openXMLFile(fileLocation, schemaSource);
        errors = FileManager.getInstance().getParseError();
        if (xmlDocument != null
                && !(XMLErrorHanlder.isPresentError(ErrorStatus.FATAL_ERROR, errors))) {
            return process(xmlDocument, fileLocation);
        } else {
        	return null;
        }
    }
    
    ///////////////////////////////////////////
	//
	// XML Node acces methods
	//
	//////////////////////////////////////////
    
    /**
    * @param node
    * @param constraintSet
    */
   private boolean processNameNode(Node node, AlarmsCatalog catalog) {
       if ((node.getNodeType() == Node.ELEMENT_NODE)
               && (node.getNodeName().equals("name"))) {
    	   String name = node.getFirstChild().getNodeValue();
    	   if(name.substring(0,5).equals("alarm")){
    		   catalog.setCatalogName(name);
    		   return true;
    	   }else{
    		   return false;
    	   }
           
       } else {
           /* Else we go on the recursivity */
           NodeList child = node.getChildNodes();
           boolean test = true;
           for (int i = 0; i < child.getLength(); i++) {
        	   test = processNameNode(child.item(i),catalog);
           }
           return test;
       }

   }

   /**
    * @param node
    * @param constraintSet
    */
   private void processDocNameNode(Node node, AlarmsCatalog catalog) {
       if ((node.getNodeType() == Node.ELEMENT_NODE)
               && (node.getNodeName().equals("docname"))) {
    	   catalog.setDocName(node.getFirstChild().getNodeValue());
       } else {
           /* Else we go on the recursivity */
           NodeList child = node.getChildNodes();
           for (int i = 0; i < child.getLength(); i++) {
        	   processDocNameNode(child.item(i),catalog);
           }
       }

   }
    
    /**
	 * Load the alarms contained in the XML document intoa list of @link Alarm
     * @param fileLocation the location of the file
	 * @param xmlDocument the associeted XML document
	 */
	private AlarmsCatalog process(Document doc, URL fileLocation) {
		AlarmsCatalog catalog = new AlarmsCatalog();
		catalog.setFileLocation(fileLocation);
		catalog.setDocument(doc);
		 /*
         * Part 0: Extract name and doc name (if exist) + description
         */
        NodeList catName = doc.getElementsByTagName("name");
        if (catName.getLength() > 0) {
            if(processNameNode(catName.item(0),catalog)){
            	 NodeList docName = doc.getElementsByTagName("docname");
                 if (docName.getLength() > 0) {
                     processDocNameNode(docName.item(0),catalog);
                 }
                 NodeList description = doc.getElementsByTagName("description");
                 if (description.getLength() > 0) {
                     processDescriptionNode(description.item(0),catalog);
                 }
                 
                 /*
                  * Part 1: calatog extraction
                  */
                 NodeList eventSet = doc.getElementsByTagName("events");
                 if (eventSet.getLength() > 0) {
                     processEventsNode(eventSet.item(0), catalog);
                 }
                 NodeList dep = doc.getElementsByTagName("dependencies");
                 if (dep.getLength() > 0) {
                     processdependecies(dep.item(0), catalog.getDependecies());
                 }
            }else{
            catalog = null;
            }
        }
       return catalog;
	}

	private void processdependecies(Node node, List depds) {
        if ((node.getNodeType() == Node.ELEMENT_NODE)
                && (node.getNodeName().equals("Component"))) {
          depds.add(node.getFirstChild().getNodeValue());
        }

        /* Else we go on the recursivity */
        NodeList child = node.getChildNodes();
        for (int i = 0; i < child.getLength(); i++) {
        	processdependecies(child.item(i), depds);
        }

	
		
	}

	private void processEventsNode(Node node, AlarmsCatalog catalog) {
        if ((node.getNodeType() == Node.ELEMENT_NODE)
                && (node.getNodeName().equals("event"))) {
            Alarm eventAlarm = new Alarm();
            eventAlarm.setCatalog(catalog);
            processEvent(node,eventAlarm);
            catalog.addEvent(eventAlarm);
            
        }

        /* Else we go on the recursivity */
        NodeList child = node.getChildNodes();
        for (int i = 0; i < child.getLength(); i++) {
        	processEventsNode(child.item(i), catalog);
        }

	}

	private void processEvent(Node node, Alarm eventAlarm) {
		if ((node.getNodeType() == Node.ELEMENT_NODE)
                && (node.getNodeName().equals("Name"))) {
			if(node.getFirstChild() !=null){
				eventAlarm.setName(node.getFirstChild().getNodeValue());
			}
        }
		if ((node.getNodeType() == Node.ELEMENT_NODE)
                && (node.getNodeName().equals("Number"))) {
			if(node.getFirstChild() !=null){
				int value = Integer.parseInt(node.getFirstChild().getNodeValue());
		           eventAlarm.setAlarmNumber(value);
			}
        }
		if ((node.getNodeType() == Node.ELEMENT_NODE)
                && (node.getNodeName().equals("Level"))) {
	           eventAlarm.setLevel(node.getFirstChild().getNodeValue());
        }
		if ((node.getNodeType() == Node.ELEMENT_NODE)
                && (node.getNodeName().equals("Problem"))) {
			if(node.getFirstChild() !=null){
				eventAlarm.setProblem(node.getFirstChild().getNodeValue());
			}
        }
		if ((node.getNodeType() == Node.ELEMENT_NODE)
                && (node.getNodeName().equals("Type"))) {
			if(node.getFirstChild() !=null){
				if(node.getFirstChild() !=null){
					eventAlarm.setType(node.getFirstChild().getNodeValue());
				}
			}
        }
		if ((node.getNodeType() == Node.ELEMENT_NODE)
                && (node.getNodeName().equals("Cause"))) {
			if(node.getFirstChild() !=null){
				eventAlarm.setCause(node.getFirstChild().getNodeValue());
			}
        }
		if ((node.getNodeType() == Node.ELEMENT_NODE)
                && (node.getNodeName().equals("Effect"))) {
			if(node.getFirstChild() !=null){
				eventAlarm.setEffect(node.getFirstChild().getNodeValue());
			}
        }
		if ((node.getNodeType() == Node.ELEMENT_NODE)
                && (node.getNodeName().equals("Action"))) {
			if(node.getFirstChild() !=null){
				eventAlarm.setAction(node.getFirstChild().getNodeValue());
			}
        }

        /* Else we go on the recursivity */
        NodeList child = node.getChildNodes();
        for (int i = 0; i < child.getLength(); i++) {
        	processEvent(child.item(i), eventAlarm);
        }
		
	}

	private void processDescriptionNode(Node node, AlarmsCatalog catalog) {
		 if ((node.getNodeType() == Node.ELEMENT_NODE)
	               && (node.getNodeName().equals("short"))) {
			 if(node.getFirstChild() !=null){
				 catalog.setShortDescription(node.getFirstChild().getNodeValue());
			 }
	       }  else {
	    	   if ((node.getNodeType() == Node.ELEMENT_NODE)
		               && (node.getNodeName().equals("long"))) {
	    		   if(node.getFirstChild() !=null){
	    			   catalog.setLongDescription(node.getFirstChild().getNodeValue());
	    		   }
		       } else{
		    	   /* Else we go on the recursivity */
		           NodeList child = node.getChildNodes();
		           for (int i = 0; i < child.getLength(); i++) {
		        	   processDescriptionNode(child.item(i),catalog);
		           }
		       }
	       }		
	}

	/**
	 * @param fileLocation the location of the XML catalog file.
	 * @param schemaLocation the location of the XML schema
	 * @return the @link AlarmsCatalog referenced by 
	 * the file location.
	 */
	public AlarmsCatalog getCatalog(URL fileLocation, URL schemaLocation) {
		errors.clear();
		return loadAlarmsFromXMl(fileLocation, schemaLocation);
		
	}

	/**
	 * @return the list of @link ErrorStatus
	 */
	public List getParseErrors() {
		return errors;
	}

	/**
	 * Add an alarm in the XML tree.
	 * @param alarm
	 * @param document
	 */
	public void addAlarm(Alarm alarm, Document document) {
		 NodeList eventsNode = document.getElementsByTagName("events");
	     if (eventsNode.getLength() > 0) {
	    	 Node events = eventsNode.item(0);
	    	 /*1. Create event node*/
	    	   Element eventElement = document.createElement("event");
	    	   events.appendChild(eventElement);
	    	   /*2. Create the event parameter*/
	    	     Element name = document.createElement("Name");
	    	     name.appendChild(document.createTextNode(alarm.getName()));
	    	     eventElement.appendChild(name);
	    	     
	    	     Element number = document.createElement("Number");
	    	     number.appendChild(document.createTextNode((new Integer(alarm.getAlarmNumber()).toString())));
	    	     eventElement.appendChild(number);
	    	     
	    	     Element level = document.createElement("Level");
	    	     level.appendChild(document.createTextNode(alarm.getLevel()));
	    	     eventElement.appendChild(level);
	    	     
	    	     Element problem = document.createElement("Problem");
	    	     problem.appendChild(document.createTextNode(alarm.getProblem()));
	    	     eventElement.appendChild(problem);
	    	     
	    	     Element type = document.createElement("Type");
	    	     type.appendChild(document.createTextNode("0"));
	    	     eventElement.appendChild(type);
	    	     
	    	     Element cause = document.createElement("Cause");
	    	     cause.appendChild(document.createTextNode(alarm.getCause()));
	    	     eventElement.appendChild(cause);
	    	     
	    	     Element effect = document.createElement("Effect");
	    	     effect.appendChild(document.createTextNode(alarm.getEffect()));
	    	     eventElement.appendChild(effect);
	    	     
	    	     Element action = document.createElement("Action");
	    	     action.appendChild(document.createTextNode(alarm.getAction()));
	    	     eventElement.appendChild(action);
	     }else{
	    	 SCELogger.logError("The catalog xml file does not contain any Events node", new IllegalStateException("No event node"));
	     }
	}
	
	/**
	 * Replaces the corresponding alarmin the XML tree of the alarm catalog
	 * @param oldName the old name of the alarm (could be modified)
	 * @param alarm the alarm to copy
	 */
	public static void replaceAlarm(String oldName, Alarm alarm, Document document ){
		 NodeList eventsNode = document.getElementsByTagName("event");
	     if (eventsNode.getLength() > 0) {
	    	 for (int i = 0; i < eventsNode.getLength(); i++) {
				Node node = eventsNode.item(i);
				/*1. Find the node event hagving a node name = to old name*/
				String name = getAlarmName(node, "Name");
				if(name.equals(oldName)){
					/*2. Set all node of this event in accordance with the new alarm*/
					Node namTxt = getNode(node, "Name");
					if(namTxt!=null){
						namTxt.setNodeValue(alarm.getName());
					}
					Node number = getNode(node, "Number");
					if(number!=null){
						number.setNodeValue(new Integer(alarm.getAlarmNumber()).toString());
					}
					Node level = getNode(node, "Level");
					if(level!=null){
						level.setNodeValue(alarm.getLevel());
					}
					Node problem = getNode(node, "Problem");
					if(problem!=null){
						problem.setNodeValue(alarm.getProblem());
					}
					Node type = getNode(node, "Type");
					if(type!=null){
						type.setNodeValue(alarm.getType());
					}
					Node cause = getNode(node, "Cause");
					if(cause!=null){
						cause.setNodeValue(alarm.getCause());
					}
					Node effect = getNode(node, "Effect");
					if(effect!=null){
						effect.setNodeValue(alarm.getEffect());
					}
					Node action = getNode(node, "Action");
					if(action!=null){
						action.setNodeValue(alarm.getAction());
					}
					return;
				}
			}
	     }
	}
	
	private static String getAlarmName(Node node, String field) {
		NodeList child = node.getChildNodes();
		for (int i = 0; i < child.getLength(); i++) {
			Node node_i = child.item(i);
			if ((node_i.getNodeType() == Node.ELEMENT_NODE)
					&& (node_i.getNodeName().equals(field))) {			
				if(node_i.getFirstChild()!=null){
					return node_i.getFirstChild().getNodeValue();
				}
			}
		}
		return "";
	}
	
	private static Node getNode(Node node, String field) {
		NodeList child = node.getChildNodes();
		for (int i = 0; i < child.getLength(); i++) {
			Node node_i = child.item(i);
			if ((node_i.getNodeType() == Node.ELEMENT_NODE)
					&& (node_i.getNodeName().equals(field))) {			
				if(node_i.getFirstChild()!=null){
					return node_i.getFirstChild();
				}
				}
			}
		return null;
	}

	/**
	 * Removes the specified alarm form the XML tree of the document (from the catalog xml tree)*/
	public static void removeAlarm(Alarm alarmSelected, Document document) {
		 NodeList eventsNode = document.getElementsByTagName("event");
	     if (eventsNode.getLength() > 0) {
	    	 for (int i = 0; i < eventsNode.getLength(); i++) {
				Node node = eventsNode.item(i);
				/*1. Find the node event hagving a node name = to old name*/
				String name = getAlarmName(node, "Name");
				if(name.equals(alarmSelected.getName())){
					node.getParentNode().removeChild(node);
				}
	    	 }
	     }
	}

}
