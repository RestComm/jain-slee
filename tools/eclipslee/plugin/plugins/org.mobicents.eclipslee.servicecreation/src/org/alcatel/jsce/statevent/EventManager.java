
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.alcatel.jsce.util.log.SCELogger;
import org.alcatel.jsce.util.xml.ErrorStatus;
import org.alcatel.jsce.util.xml.FileManager;
import org.alcatel.jsce.util.xml.XMLErrorHanlder;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.mobicents.eclipslee.util.Utils;

/**
 *  Description:
 * <p>
 * Manages any kind of actions on Stat event.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class EventManager {
	/** XML Schema which validates the XML alarm file.*/
	private URL schemaSource = null;
	/** Errors which occured in XML file parsing*/
	private List  parseError = null;
	/** List of directories containing stat events catalogs. List of URL*/
	private List externalStatEventCatalogURLs = null;
	/** URL of the plugin installation directory*/
	/** The stat event parser*/
	private EventStatParser parser = null;

	/**
	 * Constructor.
	 */
	public EventManager() {
		/*List initialization*/
		parseError = new ArrayList();
		externalStatEventCatalogURLs = new ArrayList();
		/*Parser*/
		parser = new EventStatParser();
	}
	
	/**
	 * @return the list of all stat event catalogs in the configuration directory 
	 * and in all external  catalog directories. A list of  @link EventCatalog
	 */
	public List  getAllStatEventCatalogs(URL configDirectory) {
		try {
			List all = new ArrayList();
			URL catalogDir =  new URL(configDirectory, "catalog/stats/");
			List internal =  this.getAllCatalog(catalogDir);
			all.addAll(internal);
			for (Iterator iter = this.externalStatEventCatalogURLs.iterator(); iter.hasNext();) {
				URL catalogUrl_i = (URL) iter.next();
				List external_i = this.getAllCatalog(catalogUrl_i);
				all.addAll(external_i);
			}
			return all;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return new ArrayList();

	}
	
	///////////////////////////////////////////
	//
	// XML Management
	//
	//////////////////////////////////////////
	
	/**
     * Open the stat event catalog XML file and load its content.
     * 
     * @param fileLocation
     *                  is the absolute URL path of the catalog  file.
	 * @param schemaLocation the location of the XML schema
     * @return the corresponding @link EventCatalog
     */
    public EventCatalog getStatEventCatalog(URL fileLocation, URL schemaLocation) {
    	EventCatalog catalog = parser.getStatEventCatalog(fileLocation, schemaSource);
    	this.setParseError(parser.getParseErrors());
    	return catalog;
    }
    
    /**
     * Opens all  the stat event catalog XML files in the sepcified directory and load its content.
     * 
     * @param fileLocation
     *                  is the absolute URL path of the catalog directory.
     * @return a list of  @link EventCatalog
     */
    public List getAllCatalog(URL directory) {
    	List catalogs = new ArrayList();
     	File dir = new File(directory.getFile());
     	if(!dir.isDirectory()){
     		throw new IllegalStateException("The "+ directory.getFile() + " does not exist !");
     	}else{
			extractValidCatalogs(FileManager.getInstance().getAllXMLFile(directory), directory, catalogs);
     	}
    	return catalogs;
    }

	/**
	 * This method extracts all catalog which doesn't contain any @link ErrorStatus#FATAL_ERROR.
	 * @param allXMLFile the list of all xml files present in the parent directory (relative names)
	 * @param eventStatDir the parent directory in which we are looking for catalog.
	 * @param catalogs the final list of all valid catalogs.
	 */
	private void extractValidCatalogs(List allXMLFile, URL eventStatDir, List catalogs) {
		for (Iterator iiter = allXMLFile.iterator(); iiter.hasNext();) {
			String relativepath = (String) iiter.next();
			try {
				URL locationCatalog_i = new URL(eventStatDir, relativepath);
				EventCatalog catalog_i = getStatEventCatalog(locationCatalog_i, null);
				if(!XMLErrorHanlder.isPresentError(ErrorStatus.FATAL_ERROR, getParseError())&& catalog_i!=null){
					//No fatal error where found, the warnin are accepted.
					catalogs.add(catalog_i);
				}else{
					//The catalog is not added.
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	///////////////////////////////////////////
	//
	// Access
	//
	//////////////////////////////////////////

	/**
	 * @return Returns the parseError.
	 */
	public List getParseError() {
		return parseError;
	}

	/**
	 * @param parseError The parseError to set.
	 */
	public void setParseError(List parseError) {
		this.parseError = parseError;
	}
	
	/**
	 * @return Returns the externalStatEventCatalogURLs.
	 */
	public List getExternalStatEventCatalogURLs() {
		return externalStatEventCatalogURLs;
	}
	/**
	 * @param externalStatEventCatalogURLs The externalStatEventCatalogURLs to set.
	 */
	public void setExternalStatEventCatalogURLs(List externalStatEventCatalogs) {
		this.externalStatEventCatalogURLs = externalStatEventCatalogs;
	}
	
	///////////////////////////////////////////
	//
	// Utilities
	//
	//////////////////////////////////////////

	/**
	 * @param statEvents the list of stat events
	 * @param methods will be the list of the method name
	 * @return the string of interface method concataned.
	 */
	public static String getStatEventMethods(StatEvent[] statEvents, List methods) {
		String result = "";
		for (int i = 0; i < statEvents.length; i++) {
			StatEvent event = statEvents[i];
			if(event.getDump_ind().equals("n")){
				/*It's a increment counter*/
				String parent = event.getParent().replace('.','_');
				String comment = "\t/** \n\t* OSP Stat event method generated from the <i>"+event.getName()+"</i> event \n\t" +
						" <br> Description: " + event.getDescription()+" </br>\n\t*/\n";
				String methodName_i = "incrementE"+parent+"_"+event.getValue()+"_"+event.getInc_type();
				methods.add(methodName_i);
				String method_i = comment + "\tpublic void "+methodName_i+"(long value); \n";
				result+= method_i;
			}else{
				/*It's a dump stat eventr*/
				String parent = event.getParent().replace('.','_');
				String comment = "\t/** \n\t* OSP Stat event method generated from the <i>"+event.getName()+"</i> event \n\t" +
						" <br> Description: " + event.getDescription()+" </br>\n\t*/\n";
				String methodName_i = "tagT"+parent+"_"+event.getValue()+"_"+event.getInc_type();
				methods.add(methodName_i);
				String method_i = comment + "\tpublic void "+methodName_i+"(String value); \n";
				result+= method_i;
			}
			
		}
		return result;
	}
	
	/**
	 * @param statEvents
	 * @param methods
	 * @return a concatenated string with the method calling the increment UP method
	 */
	public static String getStatEventCallMethods(StatEvent[] statEvents, List methods) {
		String result = "";
		if(statEvents.length>0){
			result+="////////////////////////////////////////////\n" +
					"//\n" +
					"// OSP Stat events \n" +
					"//\n" +
					"////////////////////////////////////////////\n\n";
		}
		for (int i = 0; i < statEvents.length; i++) {
			StatEvent event_i = statEvents[i];
			String up_method_i = (String) methods.get(i);
			if(event_i.getDump_ind().equals("n")){
				String comment = "\t/** \n\t* OSP Stat event call method generated from  <i>"+event_i.getName()+"</i> \n\t" +"*/" ;
				String signature = "\n\t public void increment"+Utils.capitalize(event_i.getName())+"(long value){\n";
				String body = "\t\t try{ \n\t\t \tgetDefaultSbbUsageParameterSet()."+up_method_i+"(value);\n"+ 
				"\t\t}catch (Exception e){ \n \t\t\t e.printStackTrace();\n\t\t}\n\t}\n\n ";
				result += comment + signature+body;
			}else{
				String comment = "\t/** \n\t* OSP Stat event call method generated from  <i>"+event_i.getName()+"</i> \n\t" +"*/" ;
				String signature = "\n\t public void tag"+Utils.capitalize(event_i.getName())+"(String value){\n";
				String body = "\t\t try{ \n\t\t \tgetDefaultSbbUsageParameterSet()."+up_method_i+"(value);\n"+ 
				"\t\t}catch (Exception e){ \n \t\t\t e.printStackTrace();\n\t\t}\n\t}\n\n ";
				result += comment + signature+body;
			}
		}
		return result;
	}


	///////////////////////////////////////////
	//
	// Creational
	//
	//////////////////////////////////////////
	
	/**
	 * Creates a new event stat in a catalog. If needed, it will also create a new catalog.
	 * @param event the event to create
	 * @param catalog the catalog to write
	 */
	public void createNewEventStat( EventCatalog catalog) {
		/*1. Delete and  create the file specified by the catalog with the new XML tree*/
		File file = new File(catalog.getFileLocation().getFile());
		try {
			if (file.exists()) {
				/* Remove it and recreate empty file */
				file.delete();
				file.createNewFile();
			} else {
				/* Just create it, but we need to create the subdirectory file system*/
				createParentFile(file);
				file.createNewFile();
			}
		} catch (IOException e) {
			SCELogger.logError("Create XML Alarm file failed ! : Error while creating empty file", e);
			e.printStackTrace();
		}
		try {
			/*TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer	.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			FileOutputStream out = new FileOutputStream(file, true);
			transformer.transform(new DOMSource(catalog.getDocument()),
					new StreamResult(out));*/
			FileOutputStream out = new FileOutputStream(file, false);
			XMLSerializer serializer = new XMLSerializer(out, new OutputFormat(
					catalog.getDocument(), "utf-8", true));
			serializer.serialize(catalog.getDocument());
		/*} catch (TransformerConfigurationException e) {
				SCELogger.logError("Create XML Stat event file failed !", e);
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				SCELogger.logError("Create XML Stat event file failed !: File not found", e);
			e.printStackTrace();
		} catch (TransformerException e) {
			SCELogger.logError("Create XML Stat event file failed !: XML error", e);
				e.printStackTrace();*/
			}      
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 /*Notie: as we re-load, on each call, all alarm catalog we don't need to refresh any list of catalogs.*/
	}
	
	private void createParentFile(File file) throws IOException{
		File parent = file.getParentFile();
		if(parent.exists()){
			return;
		}else{
			createParentFile(parent);
			parent.mkdir();
		}
	}

	/**
	 * @param selected_stat the list of stat ID (OSP-flavoured)
	 * @param catalogs the lsit of all present stat event catalog (local +external)
	 * @return the list of stat events corresponding to the ID cotnained in the list. The list returns 
	 * an HashMap [event, catalog]
	 */
	public List extractStatEventFromId(List selected_stat, List catalogs) {
		List statEvents = new ArrayList();
		/* for each event ID, search in the catalog list the corresponding event*/
		for (Iterator iter = selected_stat.iterator(); iter.hasNext();) {
			String eventid_i = (String) iter.next();
			//1. Extract properties [feat, subfeat, type, event]
			String[] prop_i = extractEvent(eventid_i);
			boolean findedEvent = false;
			//2. In each event stat catalog we are looking for the corresponding event
			for (Iterator iterator = catalogs.iterator(); iterator.hasNext() && !findedEvent;) {
				EventCatalog catalog_j = (EventCatalog) iterator.next();
				StatEvent event_i = this.parser.findStatEventInCatalog(prop_i, catalog_j);
				if(event_i!=null){
					HashMap data = new HashMap(1);
					data.put(event_i, catalog_j);
					statEvents.add(data);
					findedEvent = true;
				}
			}
			if(!findedEvent){
				SCELogger.logError("No corresponding stat event was found ", new IllegalStateException("The stat event was not found catalogs !"));
			}
			
		}
		return statEvents;
	}

	/**
	 * @param eventid_i the event ID OSP code (E1_0_6_1_n)
	 * @return thefeature, Subfeature,Type, event values for this event 
	 */
	private String[] extractEvent(String eventid_i) {
		/*Add an _ after E: (E1_0_6_1_n) -> (_1_0_6_1_n)*/
		String id = "_"+eventid_i.substring(1);
		String rule ="_[0-9]{1,}";
		String result[] =new String[4];
		for (int i = 0; i < result.length; i++) {
			result[i] = "0";
		}
		Pattern pattern = Pattern.compile(rule);
		Matcher matcher = pattern.matcher(id);
		int i=0;
		while (matcher.find()) {
			String match_i = matcher.group();
			if(match_i.indexOf("_")> -1){
				result [i] =matcher.group().substring(1);
			}
			i++;
		}
		return result;
	}

	/**
	 * @param source the source code of the usage parameter
	 * @param catalog the stat events catalog
	 * @return the list of all event found
	 */
	public List extractAllStatEvents(String source, EventType type) {

		List events = new ArrayList();
		// Compile the regex.
		String regex = "increment.*?\\(";
		Pattern pattern = Pattern.compile(regex);
		// Get a Matcher based on the target string.
		Matcher matcher = pattern.matcher(source);
		// Find all the matches.
		int i = 0;
		while (matcher.find()) {
			String eventMethod = matcher.group();
			SCELogger.logInfo("Found Stat event  match: "+ eventMethod);
			int index = eventMethod.indexOf("(");
			eventMethod = eventMethod.substring(0, index);
			if(eventMethod.length()>9) eventMethod = eventMethod.substring(9);
			StatEvent statEvent = new StatEvent();
			statEvent.setName(Utils.uncapitalize(eventMethod));
			statEvent.setParent("86.1."+type.getValue());
			statEvent.setValue(type.getEvents().size()+i);
			statEvent.setInc_type("n");
			statEvent.setDump_ind("n");
			statEvent.setDescription("This statistic event was automatically generated by the Alcatel SCE-SE while importing SBB");
			statEvent.setSmp_inc_type("a");
			statEvent.setMacro("none");
			events.add(statEvent);
			i++;
		}
		return events;
	}
	
}
