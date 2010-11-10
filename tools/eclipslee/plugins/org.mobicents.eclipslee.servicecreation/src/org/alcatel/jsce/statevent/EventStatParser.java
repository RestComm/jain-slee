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

import org.alcatel.jsce.util.log.SCELogger;
import org.alcatel.jsce.util.xml.ErrorStatus;
import org.alcatel.jsce.util.xml.FileManager;
import org.alcatel.jsce.util.xml.OSPXML;
import org.alcatel.jsce.util.xml.XMLErrorHanlder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Description:
 * <p>
 * This object maneges the access to the stat event XML files and the creation
 * of associeted @link org.alcatel.jsce.statevent.EventCatalog
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 * 
 */
public class EventStatParser extends OSPXML {
	/** Errors occured in the last parsing. */
	private List errors = null;

	/**
	 * 
	 */
	public EventStatParser() {
		errors = new ArrayList();
	}

	/**
	 * Open an stat event catalog XML file and load its content.
	 * 
	 * @param fileLocation
	 *            is the absolute URL path of the stat event file.
	 * @param schemaSource
	 *            the location of the XML schema (can be null)
	 * @return the stat event catalog.
	 */
	public EventCatalog getStatEventCatalog(URL fileLocation, URL schemaSource) {
		errors.clear();
		Document xmlDocument = FileManager.getInstance().openXMLFile(
				fileLocation, schemaSource);
		errors = FileManager.getInstance().getParseError();
		if (xmlDocument != null
				&& !(XMLErrorHanlder.isPresentError(ErrorStatus.FATAL_ERROR,
						errors))) {
			// Fill the catalog recursively
			return process(xmlDocument, fileLocation);
		} else {

		}
		return null;
	}

	// /////////////////////////////////////////
	//
	// XML Node acces methods
	//
	// ////////////////////////////////////////

	/**
	 * @param node
	 * @param constraintSet
	 */
	private boolean processNameNode(Node node, EventCatalog catalog) {
		 if ((node.getNodeType() == Node.ELEMENT_NODE)
	               && (node.getNodeName().equals("name"))) {
	    	   String name = node.getFirstChild().getNodeValue();
	    	   if(name.substring(0,4).equals("stat")){
	    		   catalog.setCatalogName(name);
	    		   return true;
	    	   }else{
	    		   return false;
	    	   }
		 }else {
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
	private void processDocNameNode(Node node, EventCatalog catalog) {
		if ((node.getNodeType() == Node.ELEMENT_NODE)
				&& (node.getNodeName().equals("docname"))) {
			catalog.setDocName(node.getFirstChild().getNodeValue());
		} else {
			/* Else we go on the recursivity */
			NodeList child = node.getChildNodes();
			for (int i = 0; i < child.getLength(); i++) {
				processDocNameNode(child.item(i), catalog);
			}
		}
	}

	/**
	 * Load the alarms contained in the XML document intoa list of
	 * @link Alarm
	 * @param xmlDocument the associeted XML document
	 * @param fileLocation the URL of the file
	 */
	private EventCatalog process(Document doc, URL fileLocation) {
		EventCatalog catalog = new EventCatalog();
		catalog.setFileLocation(fileLocation);
		catalog.setDocument(doc);
		/*
		 * Part 0: Extract name and doc name (if exist) + description
		 */
		NodeList catName = doc.getElementsByTagName("name");
		if (catName.getLength() > 0) {
			if(processNameNode(catName.item(0), catalog)){
				NodeList docName = doc.getElementsByTagName("docname");
				if (docName.getLength() > 0) {
					processDocNameNode(docName.item(0), catalog);
				}
				NodeList description = doc.getElementsByTagName("SubFeats");
				if (description.getLength() > 0) {
					processSubFeatsNode(description.item(0), catalog);
				}
				return catalog;
			}else{
				catalog = null;
			};
		}
		return null;
	}

	/**
	 * @param node
	 *            the sub features XML node
	 * @param catalog
	 *            the catlog to fill
	 */
	private void processSubFeatsNode(Node node, EventCatalog catalog) {
		if ((node.getNodeType() == Node.ELEMENT_NODE)
				&& (node.getNodeName().equals("SubFeat"))) {
			EventSubFeature subFeature = new EventSubFeature();
			processSubFeatNode(node, catalog, subFeature);
			catalog.getSubFeatureEvent().add(subFeature);
		} else {
			/* Else we go on the recursivity */
			NodeList child = node.getChildNodes();
			for (int i = 0; i < child.getLength(); i++) {
				processSubFeatsNode(child.item(i), catalog);
			}
		}

	}

	private void processSubFeatNode(Node node, EventCatalog catalog,
			EventSubFeature subFeature) {
		if ((node.getNodeType() == Node.ELEMENT_NODE)
				&& (node.getNodeName().equals("Parent"))) {
			String tmp = node.getFirstChild().getNodeValue();
			if (tmp != null) {
				subFeature.setParent(tmp);
				catalog.setFeatureID(tmp);
			}
		} else {
			if ((node.getNodeType() == Node.ELEMENT_NODE)
					&& (node.getNodeName().equals("Name"))) {
				String tmp = node.getFirstChild().getNodeValue();
				if (tmp != null) {
					subFeature.setName(tmp);
				}
			} else {
				if ((node.getNodeType() == Node.ELEMENT_NODE)
						&& (node.getNodeName().equals("Value"))) {
					String tmp = node.getFirstChild().getNodeValue();
					if (tmp != null) {
						subFeature.setValue(Integer.parseInt(tmp));
					}
				} else {
					if ((node.getNodeType() == Node.ELEMENT_NODE)
							&& (node.getNodeName().equals("Type"))) {
						EventType type = new EventType();
						processTypeNode(node, type);
						subFeature.getEventTypes().add(type);
					} else {
						/* Else we go on the recursivity */
						NodeList child = node.getChildNodes();
						for (int i = 0; i < child.getLength(); i++) {
							processSubFeatNode(child.item(i), catalog,
									subFeature);
						}
					}
				}
			}
		}
	}

	private void processTypeNode(Node node, EventType type) {
		if ((node.getNodeType() == Node.ELEMENT_NODE)
				&& (node.getNodeName().equals("Parent"))) {
			if (node.getFirstChild() != null) {
				String tmp = node.getFirstChild().getNodeValue();
				if (tmp != null) {
					type.setParent(tmp);
				}
			}
		} else {
			if ((node.getNodeType() == Node.ELEMENT_NODE)
					&& (node.getNodeName().equals("Name"))) {
				if (node.getFirstChild() != null) {
					String tmp = node.getFirstChild().getNodeValue();
					if (tmp != null) {
						type.setName(tmp);
					}
				}
			} else {
				if ((node.getNodeType() == Node.ELEMENT_NODE)
						&& (node.getNodeName().equals("Value"))) {
					if (node.getFirstChild() != null) {
						String tmp = node.getFirstChild().getNodeValue();
						if (tmp != null) {
							type.setValue(Integer.parseInt(tmp));
						}
					}
				} else {
					if ((node.getNodeType() == Node.ELEMENT_NODE)
							&& (node.getNodeName().equals("RAP_NAME"))) {
						if (node.getFirstChild() != null) {
							String tmp = node.getFirstChild().getNodeValue();
							if (tmp != null) {
								type.setRap_name(tmp);
							}
						}
					} else {
						if ((node.getNodeType() == Node.ELEMENT_NODE)
								&& (node.getNodeName().equals("REPTYPE"))) {
							if (node.getFirstChild() != null) {
								String tmp = node.getFirstChild()
										.getNodeValue();
								if (tmp != null) {
									type.setRep_type(tmp);
								}
							}
						} else {
							if ((node.getNodeType() == Node.ELEMENT_NODE)
									&& (node.getNodeName().equals("UNITS"))) {
								if (node.getFirstChild() != null) {
									String tmp = node.getFirstChild()
											.getNodeValue();
									if (tmp != null) {
										type.setUnit(tmp);
									}
								}

							} else {
								if ((node.getNodeType() == Node.ELEMENT_NODE)
										&& (node.getNodeName()
												.equals("COM_STAT"))) {
									if (node.getFirstChild() != null) {
										String tmp = node.getFirstChild()
												.getNodeValue();
										if (tmp != null) {
											if (tmp.equals("false")) {
												type.setCom_stat(false);
											} else {
												type.setCom_stat(true);
											}
										}
									}

								} else {
									if ((node.getNodeType() == Node.ELEMENT_NODE)
											&& (node.getNodeName()
													.equals("DESC"))) {
										if (node.getFirstChild() != null) {
											String tmp = node.getFirstChild()
													.getNodeValue();
											if (tmp != null) {
												type.setDescription(tmp);
											}
										}
									} else {
										if ((node.getNodeType() == Node.ELEMENT_NODE)
												&& (node.getNodeName()
														.equals("Event"))) {
											StatEvent event = new StatEvent();
											processEventNode(node, event);
											type.getEvents().add(event);
										} else{
											/* Else we go on the recursivity */
											NodeList child = node
													.getChildNodes();
											for (int i = 0; i < child
													.getLength(); i++) {
												processTypeNode(child.item(i),
														type);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

	
	
	}

	private void processEventNode(Node node, StatEvent event) {
		if ((node.getNodeType() == Node.ELEMENT_NODE)
				&& (node.getNodeName().equals("Parent"))) {
			if (node.getFirstChild() != null) {
				String tmp = node.getFirstChild().getNodeValue();
				if (tmp != null) {
					event.setParent(tmp);
				}
			}
		} else {
			if ((node.getNodeType() == Node.ELEMENT_NODE)
					&& (node.getNodeName().equals("Name"))) {
				if (node.getFirstChild() != null) {
					String tmp = node.getFirstChild().getNodeValue();
					if (tmp != null) {
						event.setName(tmp);
					}
				}
			} else {
				if ((node.getNodeType() == Node.ELEMENT_NODE)
						&& (node.getNodeName().equals("Value"))) {
					if (node.getFirstChild() != null) {
						String tmp = node.getFirstChild().getNodeValue();
						if (tmp != null) {
							event.setValue(Integer.parseInt(tmp));
						}
					}
				} else {
					if ((node.getNodeType() == Node.ELEMENT_NODE)
							&& (node.getNodeName().equals("INCTYPE"))) {
						if (node.getFirstChild() != null) {
							String tmp = node.getFirstChild().getNodeValue();
							if (tmp != null) {
								event.setInc_type(tmp);
							}
						}
					} else {
						if ((node.getNodeType() == Node.ELEMENT_NODE)
								&& (node.getNodeName().equals("DUMPIND"))) {
							if (node.getFirstChild() != null) {
								String tmp = node.getFirstChild()
										.getNodeValue();
								if (tmp != null) {
									event.setDump_ind(tmp);
								}
							}
						} else {
							if ((node.getNodeType() == Node.ELEMENT_NODE)
									&& (node.getNodeName().equals("SMPINCTYPE"))) {
								if (node.getFirstChild() != null) {
									String tmp = node.getFirstChild()
											.getNodeValue();
									if (tmp != null) {
										event.setSmp_inc_type(tmp);
									}
								}

							} else {
								if ((node.getNodeType() == Node.ELEMENT_NODE)
										&& (node.getNodeName().equals("MACRO"))) {
									if (node.getFirstChild() != null) {
										String tmp = node.getFirstChild()
												.getNodeValue();
										if (tmp != null) {
											event.setMacro(tmp);
										}
									}
								} else {
									if ((node.getNodeType() == Node.ELEMENT_NODE)
											&& (node.getNodeName()
													.equals("DESC"))) {
										if (node.getFirstChild() != null) {
											String tmp = node.getFirstChild()
													.getNodeValue();
											if (tmp != null) {
												event.setDescription(tmp);
											}
										}
									} else {

										/* Else we go on the recursivity */
										NodeList child = node.getChildNodes();
										for (int i = 0; i < child.getLength(); i++) {
											processEventNode(child.item(i),
													event);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @return Returns the errors.
	 */
	public List getParseErrors() {
		return errors;
	}

	/**
	 * @param errors
	 *            The errors to set.
	 */
	public void setErrors(List errors) {
		this.errors = errors;
	}
	/**
	 * Add a Sub feature in the XML tree.
	 * @param subFeature the subfeature to add
	 * @param document the XML document in wich we wil add the subfeature
	 */
	public static void addSubFeature(EventSubFeature subFeature, Document document) {
		 NodeList eventsNode = document.getElementsByTagName("SubFeats");
	     if (eventsNode.getLength() > 0) {
	    	 Node subfeats = eventsNode.item(0);
	    	 /*1. Create event node*/
	    	   Element subfeat = document.createElement("SubFeat");
	    	   subfeats.appendChild(subfeat);
	    	   /*2. Create the event parameter*/
	    	     Element parent = document.createElement("Parent");
	    	     parent.appendChild(document.createTextNode(subFeature.getParent()));
	    	     subfeat.appendChild(parent);
	    	     
	    	     Element name = document.createElement("Name");
	    	     name.appendChild(document.createTextNode(subFeature.getName()));
	    	     subfeat.appendChild(name);
	    	     
	    	     Element value = document.createElement("Value");
	    	     value.appendChild(document.createTextNode((new Integer(subFeature.getValue()).toString())));
	    	     subfeat.appendChild(value);
	    	     
	    	     Element types = document.createElement("Types");
	    	     subfeat.appendChild(types);
	     }else{
	    	 SCELogger.logError("The catalog xml file does not contain any SubFeats node", new IllegalStateException("No SubFeats node"));
	     }
	}
	
	/**
	 * Add a new type in the XML tree.
	 * @param eventType the subfeature to add
	 * @param document the XML document in wich we wil add the subfeature
	 * @param subfeautreValue  the value of the parent subfeature
	 * @param subfeatureParent the value of the parent
	 */
	public static void addType(EventType eventType, Document document, String subfeatureParent, int subfeautreValue) {
		 NodeList eventsNode = document.getElementsByTagName("SubFeat");
	     if (eventsNode.getLength() > 0) {
	    	 for (int i = 0; i < eventsNode.getLength(); i++) {
				Node node_i = eventsNode.item(i);
				String parentName = getSubfeaturName(node_i, "Parent");
				if(parentName.equals(subfeatureParent)){
					String valueName = getSubfeaturName(node_i, "Value");
					if(valueName.equals((new Integer(subfeautreValue)).toString())){
						/*In node_i we have got the right subfeature.
						 * Now we have to find the types node in order to to add our new type.*/
						
						 Node types = findNode("Types", node_i);
				    	 /*1. Create type node  node*/
				    	   Element type = document.createElement("Type");
				    	   types.appendChild(type);
				    	   /*2. Create the type  parameter*/
				    	     Element parent = document.createElement("Parent");
				    	     parent.appendChild(document.createTextNode(eventType.getParent()));
				    	     type.appendChild(parent);
				    	     
				    	     Element name = document.createElement("Name");
				    	     name.appendChild(document.createTextNode(eventType.getName()));
				    	     type.appendChild(name);
				    	     
				    	     Element value = document.createElement("Value");
				    	     value.appendChild(document.createTextNode((new Integer(eventType.getValue()).toString())));
				    	     type.appendChild(value);
				    	     
				    	     Element rap_name = document.createElement("RAP_NAME");
				    	     rap_name.appendChild(document.createTextNode(eventType.getRap_name()));
				    	     type.appendChild(rap_name);
				    	     
				    	     Element reptype = document.createElement("REPTYPE");
				    	     reptype.appendChild(document.createTextNode(eventType.getRep_type()));
				    	     type.appendChild(reptype);
				    	     
				    	     Element units = document.createElement("UNITS");
				    	     units.appendChild(document.createTextNode(eventType.getUnit()));
				    	     type.appendChild(units);
				    	     
				    	     Element com = document.createElement("COM_STAT");
				    	     com.appendChild(document.createTextNode("false"));
				    	     type.appendChild(com);
				    	     
				    	     Element desc = document.createElement("DESC");
				    	     desc.appendChild(document.createTextNode(eventType.getDescription()));
				    	     type.appendChild(desc);
				    	     
				    	     
				    	     Element events = document.createElement("Events");
				    	     type.appendChild(events);
				    	     return;
				     }else{
				    	 SCELogger.logError("The catalog xml file does not contain any Types node", new IllegalStateException("No Types node"));
				     }
					}
				}
			}

	}
	
	private static Node findNode(String field, Node node) {
		if ((node.getNodeType() == Node.ELEMENT_NODE)
				&& (node.getNodeName().equals(field))) {
			return node;
		} else {
			/* Else we go on the recursivity */
			NodeList child = node.getChildNodes();
			Node tmp = null;
			for (int i = 0; i < child.getLength(); i++) {
				 tmp =  findNode(field, child.item(i));
				 if(tmp!=null) return tmp;
			}
			return tmp;
		}
	}

	private static String getSubfeaturName(Node node, String field) {
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
	 * Add a new Stat event  in the XML tree.
	 * @param event the event to add
	 * @param document the XML document in wich we wil add the subfeature
	 *  @param subfeautreValue  the value of the parent subfeature
	 * @param subfeatureParent the value of the parent
	 */
	public static void addEvent(StatEvent event, Document document,String subfeatureParent, int subfeautreValue) {
		 NodeList eventsNode = document.getElementsByTagName("Type");
	     if (eventsNode.getLength() > 0) {
		    	 for (int i = 0; i < eventsNode.getLength(); i++) {
					Node node_i = eventsNode.item(i);
					String parentName = getSubfeaturName(node_i, "Parent");
					if(parentName.equals(subfeatureParent)){
						String valueName = getSubfeaturName(node_i, "Value");
						if(valueName.equals((new Integer(subfeautreValue)).toString())){
							/*In node_i we have got the right subfeature.
							 * Now we have to find the types node in order to to add our new type.*/
							
							 Node events = findNode("Events", node_i);
							 /*1. Create event node*/
					    	   Element eventNode = document.createElement("Event");
					    	   events.appendChild(eventNode);
					    	   /*2. Create the event parameter*/
					    	     Element parent = document.createElement("Parent");
					    	     parent.appendChild(document.createTextNode(event.getParent()));
					    	     eventNode.appendChild(parent);
					    	     
					    	     Element name = document.createElement("Name");
					    	     name.appendChild(document.createTextNode(event.getName()));
					    	     eventNode.appendChild(name);
					    	     
					    	     Element value = document.createElement("Value");
					    	     value.appendChild(document.createTextNode((new Integer(event.getValue()).toString())));
					    	     eventNode.appendChild(value);
					    	     
					    	     Element inctype = document.createElement("INCTYPE");
					    	     inctype.appendChild(document.createTextNode(event.getInc_type()));
					    	     eventNode.appendChild(inctype);
					    	     
					    	     Element dump = document.createElement("DUMPIND");
					    	     dump.appendChild(document.createTextNode(event.getDump_ind()));
					    	     eventNode.appendChild(dump);
					    	     
					    	     Element desc = document.createElement("DESC");
					    	     desc.appendChild(document.createTextNode(event.getDescription()));
					    	     eventNode.appendChild(desc);
					    	     
					    	     Element smp = document.createElement("SMPINCTYPE");
					    	     smp.appendChild(document.createTextNode(event.getSmp_inc_type()));
					    	     eventNode.appendChild(smp);
					    	     
					    	     Element macro = document.createElement("MACRO");
					    	     macro.appendChild(document.createTextNode(event.getMacro()));
					    	     eventNode.appendChild(macro);
					    	     return;
					    	  
						}
					}
		    	 }	
	     }else{
	    	 SCELogger.logError("The catalog xml file does not contain any Events node", new IllegalStateException("No Events node"));
	     }
	}

	/**
	 * Removes a  Stat event  from the XML tree.
	 * @param event the event to remove
	 * @param document the XML document in wich we wil remove the subfeature
	 */
	public static void removeStatEvent(StatEvent eventSelected, EventCatalog catalog) {
		Document document = catalog.getDocument();
		// Remove the stat from subfeature
		catalog.removeEvent(eventSelected);
		NodeList eventsNode = document.getElementsByTagName("Event");
		if (eventsNode.getLength() > 0) {
			for (int i = 0; i < eventsNode.getLength(); i++) {
				Node node_i = eventsNode.item(i);
				String parentName = getSubfeaturName(node_i, "Parent");
				if (parentName.equals(eventSelected.getParent())) {
					String valueName = getSubfeaturName(node_i, "Value");
					if (valueName.equals((new Integer(eventSelected.getValue())).toString())) {
						/*
						 * In node_i we have got the right event. Now we have to find the types node in order to to add
						 * our new type.
						 */
						node_i.getParentNode().removeChild(node_i);
					}
				}
			}
		}
	}

	/**
	 * Removes a stat type from the XML tree.
	 * 
	 * @param event
	 *            the event to remove
	 * @param document
	 *            the XML document in wich we wil remove the subfeature
	 */
	public static void removeStatEventType(EventType type, Document document) {
		 NodeList eventsNode = document.getElementsByTagName("Types");
	     if (eventsNode.getLength() > 0) {
		    	 for (int i = 0; i < eventsNode.getLength(); i++) {
					Node node_i = eventsNode.item(i);
					String parentName = getSubfeaturName(node_i, "Parent");
					if(parentName.equals(type.getParent())){
						String valueName = getSubfeaturName(node_i, "Value");
						if(valueName.equals((new Integer(type.getValue())).toString())){
							/*In node_i we have got the right event.
							 * Now we have to find the types node in order to to add our new type.*/
							node_i.getParentNode().removeChild(node_i);
						}
					}
					}
		    	 }
	     }

	/**
	 * Replaces the  Stat event defined by the oldValue by the new event in  the XML tree.
	 * @param event the new event
	 * @param oldValue the value field of the event to replace
	 * @param document the XML document in wich we wil remove the subfeature
	 */
	public static void replaceStatEvent(String oldValue, StatEvent event, Document document) {
		 NodeList eventsNode = document.getElementsByTagName("Event");
	     if (eventsNode.getLength() > 0) {
		    	 for (int i = 0; i < eventsNode.getLength(); i++) {
					Node node_i = eventsNode.item(i);
					String parentName = getSubfeaturName(node_i, "Parent");
					if(parentName.equals(event.getParent())){
						String valueName = getSubfeaturName(node_i, "Value");
						if(valueName.equals(oldValue)){
							/*In node_i we have got the right event.
							 * Now we have to find the types node in order to to add our new type.*/
							Node name = getNode(node_i,"Name");
							if(name!=null){
								name.setNodeValue(event.getName());
							}
							Node value = getNode(node_i,"Value");
							if(value!=null){
								value.setNodeValue((new Integer(event.getValue())).toString());
							}
							Node inc = getNode(node_i,"INCTYPE");
							if(inc!=null){
								inc.setNodeValue(event.getInc_type());
							}
							Node dump = getNode(node_i,"DUMPIND");
							if(dump!=null){
								dump.setNodeValue(event.getDump_ind());
							}
							Node desc = getNode(node_i,"DESC");
							if(desc!=null){
								desc.setNodeValue(event.getDescription());
							}
							Node smp = getNode(node_i,"SMPINCTYPE");
							if(smp!=null){
								smp.setNodeValue(event.getSmp_inc_type());
							}
							Node macro = getNode(node_i,"MACRO");
							if(macro!=null){
								macro.setNodeValue(event.getMacro());
							}
							return;
						}
					}
					}
		    	 }
	     
		
	}
	
	/**
	 * @param prop_i properties of the event [feat, subfeat, type, event]
	 * @param catalog_j the event catlog in which we want to look-up
	 * @return the event corresponding to the specified properties
	 */
	public StatEvent findStatEventInCatalog(String[] prop_i, EventCatalog catalog_j) {
		String key = "";
		for (int i = 0; i < prop_i.length-1; i++) {
			String key_i = prop_i[i];
			key = key +  "."+ key_i;
		}
		key = key.substring(1);
		String value = prop_i[3];
		List eventS = catalog_j.getAllStatEvent();
		for (Iterator iter = eventS.iterator(); iter.hasNext();) {
			StatEvent event_i = (StatEvent) iter.next();
			if(event_i.getParent().equals(key) && (new Integer(event_i.getValue())).toString().equals(value)){
				return event_i;
			}
		}
		return null;
	}

	/**
	 * @param prop_i properties of the event [feat, subfeat, type, event]
	 * @param catalog_j the event catlog in which we want to look-up
	 * @return the event corresponding to the specified properties
	 */
	public StatEvent findStatEvent(String[] prop_i, EventCatalog catalog_j) {
		StatEvent result = null;
		Element[] subfeatList = getNodes(catalog_j.getDocument().getDocumentElement(), "component/SubFeats/SubFeat");
		for (int i = 0; i < subfeatList.length; i++) {
			Element subfeat_i = subfeatList[i];
			String parent_i = getChildText(subfeat_i,"Parent");
			if(parent_i!=null){
				parent_i = parent_i.trim();
				if(parent_i.equals(prop_i[0])){
					
					/*We have got the right catalog*/
					String subfeat_value_i = getChildText(subfeat_i,"Value");
					if(subfeat_value_i!=null){
						subfeat_value_i = subfeat_value_i.trim();
						if(subfeat_value_i.equals(prop_i[1])){
							
							/*We have got the right subfeature */
							Element[] typeList = getNodes(subfeat_i, "Types/Type");
							for (int j = 0; j < typeList.length; j++) {
								Element type_i = typeList[j];
								String type_value_i = getChildText(type_i,"Value");
								if(type_value_i !=null){
									type_value_i  =type_value_i .trim();
									if(type_value_i.equals(prop_i[2])){
										
										/*We have got the right type*/
										Element[] eventList = getNodes(subfeat_i, "Events/StatEvent");
										for (int k = 0; k < eventList.length; k++) {
											Element event_k = typeList[k];
											String event_value_k = getChildText(event_k,"Value");
											if(event_value_k !=null){
												event_value_k  =event_value_k .trim();
												if(event_value_k.equals(prop_i[3])){
													
													/*We have got the right event*/
													
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

}
