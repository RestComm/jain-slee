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
 * This object manages any kind of alarm manipulation, but also the access via the xml alarms 
 * catalog.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class AlarmManager {
	/** XML Schema which validates the XML alarm file.*/
	private URL schemaSource = null;
	/** Errors which occured in XML file parsing*/
	private List  parseError = null;
	/** List of directories containing alarm catalogs. List of URL*/
	private List externalAlarmCatalogURL = null;
	/**The alarm parser*/
	private AlarmCatalogParser parser = null;

	/**
	 * Constructor.
	 */
	public AlarmManager() {
		/*List initialization*/
		parseError = new ArrayList();
		externalAlarmCatalogURL = new ArrayList();
		parser = new AlarmCatalogParser();
	}
	
	/**
	 * @return the list of all alarm catalogs in the configuration directory 
	 * and in all external catalog directories.
	 */
	public List  getAllAlarmCatalogs(URL configDirectory) {
		try {
			List all = new ArrayList();
			URL catalogDir =  new URL(configDirectory, "catalog/alarms/");
			List internal =  getAllCatalog(catalogDir);
			all.addAll(internal);
			for (Iterator iter = this.externalAlarmCatalogURL.iterator(); iter.hasNext();) {
				URL catalogUrl_i = (URL) iter.next();
				List external_i = getAllCatalog(catalogUrl_i);
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
     * Open the alarm catalog XML file and load its content.
     * 
     * @param fileLocation
     *                  is the absolute URL path of the catalog  file.
	 * @param schemaLocation the location of the XML schema
     * @return the corresponding @link AlarmsCatalog
     */
    public AlarmsCatalog getAlarmCatalog(URL fileLocation, URL schemaLocation) {
    	AlarmsCatalog catalog = parser.getCatalog(fileLocation, schemaSource);
    	this.setParseError(parser.getParseErrors());
    	return catalog;
    }
    
    /**
     * Opens all  the alarm catalog XML files in the sepcifesd directory and loads its content.
     * 
     * @param fileLocation
     *                  is the absolute URL path of the catalog directory.
     * @return a list of  @link AlarmsCatalog
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
	 * This method extracts all catalogs which doesn't contain any @link ErrorStatus#FATAL_ERROR.
	 * @param allXMLFile the list of all xml files present in the parent directory (relative names)
	 * @param alarmDir the parent directory in which we are looking for catalog.
	 * @param catalogs the final list of all valid catalogs.
	 */
	private void extractValidCatalogs(List allXMLFile, URL alarmDir, List catalogs) {
		for (Iterator iiter = allXMLFile.iterator(); iiter.hasNext();) {
			String relativepath = (String) iiter.next();
			try {
				URL locationCatalog_i = new URL(alarmDir, relativepath);
				AlarmsCatalog catalog_i = getAlarmCatalog(locationCatalog_i, null);
				if(!XMLErrorHanlder.isPresentError(ErrorStatus.FATAL_ERROR, getParseError()) && catalog_i!=null){
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
	 * @return Returns the externalAlarmCatalogURL.
	 */
	public List getExternalAlarmCatalogURL() {
		return externalAlarmCatalogURL;
	}
	/**
	 * @param externalAlarmCatalogURL The externalAlarmCatalogURL to set.
	 */
	public void setExternalAlarmCatalogURL(List externalAlarmCatalogs) {
		this.externalAlarmCatalogURL = externalAlarmCatalogs;
	}
	
	///////////////////////////////////////////
	//
	// Utility methods
	//
	//////////////////////////////////////////

	/**
	 * @param name the catalog name
	 * @param number the alarm number (id)
	 * @return the associeted type of the alarm: osp.\<catSubLocation\>.number
	 */
	public static String getTypeAlarm(String name, int number) {
		String result = "osp.";
		int index = name.lastIndexOf("alarm:");
	   if(index>-1){
		   result = result + name.substring(index+6);
	   }else{
		   result = result + "common";
	   }
	   result = result+"." + number;
		return result;
	}

	/**
	 * @param level
	 * @return the right @ SLEE level.
	 */
	public static String getLevel(String level) {
	        if (level.equals("noaction"))
	            return "Level.OFF";
	        if (level.equals("display"))
	        	return "Level.FINEST";
	        if (level.equals("warning"))
	        	 return "Level.FINER";
	        if( level.equals("threshold"))
	        	return "Level.FINE";
	        if (level.equals("benign"))
	        	 return "Level.CONFIG";
	        if (level.equals("minor"))
	        	return "Level.INFO";
	        if (level.equals("blocking"))
	        	return "Level.WARNING";
	        if (level.equals("major"))
	            return "Level.SEVERE";
	        return "Level.OFF";
	}
	
	/**
	 * @param level
	 * @return the right @ SLEE level.
	 */
	public static String getOSPLevel(String level) {
	        if (level.equals("Level.OFF"))
	            return "noaction";
	        if (level.equals("Level.FINEST"))
	        	return "display";
	        if (level.equals("Level.FINER"))
	        	 return "warning";
	        if( level.equals("Level.FINE"))
	        	return "threshold";
	        if (level.equals("Level.CONFIG"))
	        	 return "benign";
	        if (level.equals("Level.INFO"))
	        	return "minor";
	        if (level.equals("Level.WARNING"))
	        	return "blocking";
	        if (level.equals("Level.SEVERE"))
	            return "major";
	        return "noaction";
	}
	
	/**
	 * Creates a new alarm in the specified catalog.
	 * @param alarm the OSP alarm to create
	 * @param catalog the catalog in which we must create the new alarm
	 */
	public void createAlarmInCatalog(Alarm alarm, AlarmsCatalog catalog) {
		/*1. Add the alarm in the XML tree*/
		this.parser.addAlarm(alarm, catalog.getDocument());
		/*2. Delete and  create the file specified by the catalog with the new XML tree*/
		createCatalog(catalog);
	}
	
	/**
	 * Writes the alarm catalog xml tree in an xml file, and creates it if it's needed.
	 * @param catalog
	 */
	public void createCatalog(AlarmsCatalog catalog){
		File file = new File(catalog.getFileLocation().getFile());
		try {
			if (file.exists()) {
				/* Remove it and recreate empty file */
				file.delete();
				file.createNewFile();
			} else {
				/* Just create it */
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
				SCELogger.logError("Create XML Alarm file failed !", e);
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				SCELogger.logError("Create XML Alarm file failed !: File not found", e);
			e.printStackTrace();
		} catch (TransformerException e) {
			SCELogger.logError("Create XML Alarm file failed !: XML error", e);
				e.printStackTrace();*/
			}      
		catch (IOException e) {
			SCELogger.logError("Create XML Alarm file failed !: File not found", e);
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
	 * Extract the corresponding alarm.
	 * @param id the OSP id of the Alarm : "osp.\<catalogSubLocation/name\>.alarm number
	 * @param catalogs the list of all @link AlarmsCatalog
	 */
	public Alarm extractAlarmFromId(String id, List catalogs) {
		int index = id.indexOf("osp.");
		if(index>-1){
			String nameID = id.substring(index+4);
			index = nameID.indexOf(".");
			if(index>0){
				String catName = nameID.substring(0, index);
				String alarmNumber = nameID.substring(index+1);
				Alarm alarm = lookupAlarm(catName, alarmNumber,catalogs);
				return alarm;
			}else{
				String error = "The alarm string ID is not valid: "+ id;
				SCELogger.logError(error, new IllegalStateException(error));
			}
			
		}else{
			String error = "The alarm string ID is not valid: "+ id;
			SCELogger.logError(error, new IllegalStateException(error));
		}
		return null;
	}

	private Alarm lookupAlarm(String catName, String alarmNumber, List catalogs) {
		for (Iterator iter = catalogs.iterator(); iter.hasNext();) {
			AlarmsCatalog catalog_i = (AlarmsCatalog) iter.next();
			//1. We extract the catalog name from alarm:<name>
			String result = null, name = catalog_i.getCatalogName();
			int index = name.lastIndexOf("alarm:");
			if (index > -1) {
				result =  name.substring(index + 6);
			} else {
				result ="common";
			}
			if(result.equals(catName)){
				//2. We have got the right catalog, now we are looking for the alarm
				for (Iterator iterator = catalog_i.getAlarmEvents().iterator(); iterator.hasNext();) {
					Alarm alarm_i = (Alarm) iterator.next();
					if(alarm_i.getAlarmNumber() == Integer.parseInt(alarmNumber)){
						//3. We have got our alarm
						return alarm_i;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @param alarms the list of selected alarms
	 * @return the string of all methods which must be wtritten in the Sbb abstract clas in a 2D array [ method, catalog].
	 */
	public static String[] getAlarmsMethods(Alarm[] alarms) {
		String alarmToWrite = " ";
		String alarmMethods[] = new String[alarms.length * 2];
		for (int i = 0; i < alarmMethods.length; i++) {
			alarmMethods[i] ="";
			
		}
		String catalogToWrite="\t/** \n\t* Static method used to validate the Sbb in the packaging step.\n" +
				"\t*  Do not edit this code\n" +
				"\t*/"+
				"\n\t public static String getAlarmCatalogName(String alarmName){";
		
		int index = 0;
		if(alarms.length>0){
			alarmToWrite+="\t////////////////////////////////////////////\n" +
					"\t//\n" +
					"\t// OSP Alarms \n" +
					"\t//\n" +
					"\t////////////////////////////////////////////\n\n";
		}
		for (int i = 0; i < alarms.length; i++) {
			Alarm alarm_i = alarms[i];
			String name = (String) alarm_i.getName();
			String message = alarm_i.getProblem();
			String catalogName = AlarmManager.getTypeAlarm(alarm_i.getCatalog().getCatalogName(), alarm_i.getAlarmNumber());
			String comment = "/** \n\t* OSP Alarm method generated from the <i>" + alarm_i.getName()
					+ "</i> alarm \n\t" + "* <br> Problem: " + alarm_i.getProblem() + "</br>\n\t* <br> Cause: "
					+ alarm_i.getCause() + "</br>\n\t" + "* <br> Effect: " + alarm_i.getEffect() + "</br>\n\t"
					+ "* <br> Action: " + alarm_i.getAction() + " </br>\n\t" + "* */";
			String body = "try{ \n\t" + "\t\t" + "String msg= \" " + message + " \" + dump;\n"
					+ "\t\t\tgetAlarmFacility().createAlarm(getSbbId(), " + AlarmManager.getLevel(alarm_i.getLevel())
					+ ", \""
					+ catalogName
					+ "\" ,msg,0);\n  " + "\t\t}catch (Exception e){ \n\r \t\t\t e.printStackTrace();\n\t\t}\n ";
			String bodyCause = "try{ \n\t" + "\t\t" + "String msg= \" " + message + " \" + dump;\n"
					+ "\t\t\tgetAlarmFacility().createAlarm(getSbbId(), " + AlarmManager.getLevel(alarm_i.getLevel())
					+ ", \""
					+ catalogName
					+ "\" ,msg, cause,0);\n  " + "\t\t}catch (Exception e){ \n\r  \t\t\t e.printStackTrace();\n\t\t}"
					+ "\n ";
			alarmMethods[index++] = "\t" + comment + "\n\tpublic  void error" + Utils.capitalize(name)
					+ "(String dump) {\n\t\t" + body + "\t}\n";

			alarmMethods[index++] = "\t" + comment + "\n\tpublic  void error" + Utils.capitalize(name)
					+ "(String dump, Throwable cause) {\n \t\t" + bodyCause + "\t}\n\n";
			
			//catalog name
			catalogToWrite+="\n\t\tcatalogMap.put(\""+ alarm_i.getName()+"\", \""+catalogName+"\"); ";
		}
		catalogToWrite+="\n\t\t return (String) catalogMap.get(alarmName);" +
				"\n\t}";

		for (int i = 0; i < alarmMethods.length; i++)
			alarmToWrite += alarmMethods[i];
		
		return new String[]{alarmToWrite, catalogToWrite};
	}
	
	/**
	 * @param alarm_i
	 * @return the createAlarm attribute: "(getSBBId, ...);"
	 */
	public static String getAlarmMethodParameters(Alarm alarm_i){
		String catalogName = AlarmManager.getTypeAlarm(alarm_i.getCatalog().getCatalogName(), alarm_i.getAlarmNumber());
	
		String attribute = "(getSbbId(), " + AlarmManager.getLevel(alarm_i.getLevel())
				+ ", \""
				+ catalogName
				+ "\" , \""+alarm_i.getProblem()+"\", null, 0);//TODO set the throwable attribute\n ";
		return attribute;
	}
	
	/**
	 * @param source the string representing the source code.
	 * @param catalog the catalog in which we must add new alarms
	 * @return the list of alarm calls in the source, a hashMpa containing [alarm name, alarmRefactoring DO] 
	 */
	public HashMap extractAllAlarmsfromSources(String source, AlarmsCatalog catalog) {
		HashMap alarmCallMap = new HashMap();
		// Compile the regex.
		String regex = ".createAlarm(.*?,.*?,.*?,.*?,.*?,.*)|.createAlarm(.*?,.*?,.*?,.*?,.*)";
		Pattern pattern = Pattern.compile(regex);
		// Get a Matcher based on the target string.
		Matcher matcher = pattern.matcher(source);
		// Find all the matches.
		while (matcher.find()) {
			String alarmSet = matcher.group();
			String alarmCall = alarmSet;
			SCELogger.logInfo("Found alarm  match: "+ alarmSet);
			int index = alarmSet.indexOf("(");
			int lastIndex = alarmSet.lastIndexOf(")");
			alarmSet = alarmSet.substring(index+1, lastIndex).replaceAll("\"", "");
			String[] parameters =alarmSet.split(",");
			Alarm alarm_i = new Alarm();
			alarm_i.setCause(parameters[3].trim());
			alarm_i.setMsg3(parameters[3].trim());
			alarm_i.setLevel(getOSPLevel(parameters[1].trim()));
			alarm_i.setAlarmNumber(catalog.getAlarmEvents().size());
			String alarmName = parameters[2];
			int slashIndex = parameters[2].lastIndexOf("/");
			if(slashIndex>-1) alarmName = alarmName.substring(slashIndex+1);
			//The name will be used as a java method name in SBB: error<AlarmName>
			alarmName = alarmName.replaceAll("\\.", "_");
			alarmName = alarmName.replaceAll("\\?", "_");
			alarmName = alarmName.replaceAll("/", "_");
			alarm_i.setName(alarmName.trim());
			alarm_i.setCatalog(catalog);
			//if(parameters.length >5){
				if(notExistINcatalog(alarmName.trim(), catalog.getAlarmEvents())){
					//This operation will realized in the perform finish of the wizard
					//createAlarmInCatalog(alarm_i, catalog);
					catalog.addEvent(alarm_i);
				}
		//	}
			
			//the list of "call creatalarm(...,...);"
			AlarmRefactoringDO alarmRefactoringDO = (AlarmRefactoringDO) alarmCallMap.get(alarm_i.getName());
			if(alarmRefactoringDO == null){
				alarmRefactoringDO = new AlarmRefactoringDO();
				alarmCallMap.put(alarm_i.getName(), alarmRefactoringDO);
				alarmRefactoringDO.setAlarm(alarm_i);
			}
			alarmRefactoringDO.addMethodCall(alarmCall.substring(1), parameters); // we forgot the "."
		}
		return alarmCallMap;

	}
	


	private boolean notExistINcatalog(String alarmName, List alarmEvents) {
		for (Iterator iter = alarmEvents.iterator(); iter.hasNext();) {
			Alarm alarm_i = (Alarm) iter.next();
			if(alarm_i.getName().equals(alarmName)){
				return false;
			}
		}
		return true;
	}
	

}
