
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
package org.alcatel.jsce.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.alcatel.jsce.statevent.EventCatalog;
import org.alcatel.jsce.statevent.EventSubFeature;
import org.alcatel.jsce.statevent.EventType;
import org.alcatel.jsce.statevent.StatEvent;
import org.alcatel.jsce.util.xml.ErrorStatus;
import org.alcatel.jsce.util.log.SCELogger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;
import org.mobicents.eclipslee.util.Utils;

/**
 *  Description:
 * <p>
 * This object is an faclity class used for event stat feature.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class EventStatUtil {

	/**
	 * 
	 */
	public EventStatUtil() {
		super();
	}

	/**
	 * @param usageFile
	 * @return the list of the selected stat events in the sepcified java file. The list 
	 * contains the name id of each stat event (E1_0_6_1_n = number stat event).
	 */
	public static List getSelectedEvents(IFile usageFile) {
			List usageData = new ArrayList();
			try {
				
				ICompilationUnit unit = (ICompilationUnit) JavaCore.create(usageFile);		
				if(unit != null){
					//The class exists
					IType clazz = unit.getTypes()[0];
					IMethod methods[] = clazz.getMethods();
					
					for (int i = 0; i < methods.length; i++) {
						
						if (methods[i].getElementName().startsWith("increment")) {
							IMethod method = methods[i];
							String name = Utils.uncapitalize(method.getElementName().substring(9));
							usageData.add(name);
							continue;
						}
						
						/**@OSP modification: we must add the other type of UP: the tag event*/
						if (methods[i].getElementName().startsWith("tag")) {
							IMethod method = methods[i];
							String name = Utils.uncapitalize(method.getElementName().substring(3));
							usageData.add(name);
							continue;
						}
						// Ignore this non-compliant method.
						}
					}
			} catch (JavaModelException e) {
				// Ignore to return what has been successfully entered.
				SCELogger.logError("Java model error while reading the UP (stat event) interface "+ usageFile.getName(), e);
			}
			return usageData;
		}
	
	
	/**
	 * @param usageClass the usage parameter class
	 * @return the list of the selected stat events in the sepcified class file. The list 
	 * contains the name id of each stat event (E1_0_6_1_n = number stat event).
	 */
	public static List getSelectedEvents(Class usageClass, ErrorStatus status) {
			List usageData = new ArrayList();
				
				String errorMessage = "";
				boolean errorFound = false;
				Method[] methods = usageClass.getMethods();
					
					for (int i = 0; i < methods.length; i++) {
						
						if (methods[i].getName().startsWith("incrementE")) {
							Method method = methods[i];
							//the name id of each stat event (1_0_6_1_n = number stat event)
							String id = Utils.uncapitalize(method.getName().substring(10));
							StatEvent statEvent = getStatEventFromId(id);
							if(statEvent!=null){
								errorMessage+= "\n\t The Stat event "+ statEvent.getName() + "  has been found in "+usageClass.getName();
								usageData.add(statEvent);
							}else{
								errorFound = true;
								errorMessage+= "\n\t The stat event corresponding to "+ method.getName() + " has not been found";
							}
						}
						
						/**@OSP modification: we must add the other type of UP: the tag event*/
						if (methods[i].getName().startsWith("tagT")) {
							Method method = methods[i];
							String id = Utils.uncapitalize(method.getName().substring(4));
							StatEvent statEvent = getStatEventFromId(id);
							if(statEvent!=null){
								errorMessage+= "\n\t The Stat event "+ statEvent.getName() + "  has been found";
								usageData.add(statEvent);
							}else{
								errorFound = true;
								errorMessage+= "\n\t The stat event corresponding to "+ method.getName() + "() has not been found";
							}
						}
					}
					if(errorFound){
						status.setType(ErrorStatus.ERROR);
						status.setException(new IllegalStateException( errorMessage));
					}else{
						status.setType(ErrorStatus.INFO);
						status.setMessage( errorMessage);
					}

			return usageData;
		}
	
	/**
	 * @return the selected stat event based in thename id of 1_0_6_1_n or null.
	 */
	public static StatEvent getStatEventFromId(String id) throws NumberFormatException {
		List catalogs = ServiceCreationPlugin.getDefault().getMainControl().getAllStatEventCatalogs();
		String[] footPrint = new String[4];
		//Extraction of subfeature, type, stat
		String rule="[0-9]{1,}_";
		 Pattern p = Pattern.compile(rule);
		 Matcher matcher = p.matcher(id);
		 int i=0;
		 while(matcher.find()&& i<4){
			String field = matcher.group();
			footPrint[i] = field.substring(0, field.length()-1);
			i++;
		 }
		 //Now in footPrint we have [feature, subfeature, type, subtype]
		 for (Iterator iter = catalogs.iterator(); iter.hasNext();) {
			EventCatalog catalog_i = (EventCatalog) iter.next();
			if(catalog_i.getFeatureID().equals(footPrint[0])){
				//It is our catalog
				List subFeatureList = catalog_i.getSubFeatureEvent();
				for (Iterator iterator = subFeatureList.iterator(); iterator.hasNext();) {
					EventSubFeature eventSubFeature_i = (EventSubFeature) iterator.next();
					if(eventSubFeature_i.getValue() == Integer.parseInt(footPrint[1])){
						//It is our sub feature
						List eventTypeList = eventSubFeature_i.getEventTypes();
						for (Iterator iteratorType = eventTypeList.iterator(); iteratorType.hasNext();) {
							EventType type_i = (EventType) iteratorType.next();
							if(type_i.getValue() == Integer.parseInt(footPrint[2])){
								//It is our type
								List statEventList = type_i.getEvents();
								for (Iterator iteratorEvent = statEventList.iterator(); iteratorEvent.hasNext();) {
									StatEvent statEvent_i = (StatEvent) iteratorEvent.next();
									if(statEvent_i.getValue() == Integer.parseInt(footPrint[3])){
										//It is our stat, woohou, happy end !
										return statEvent_i;
									}
								}
							}
						}
					}
				}
				
			}
		}
		return null;
	}
	
	/**
	 * @return the selected stat event based in thename id of 1_0_6_1_n or null.
	 */
	public static EventType getStatEventTypeFromId(String id) throws NumberFormatException {
		List catalogs = ServiceCreationPlugin.getDefault().getMainControl().getAllStatEventCatalogs();
		String[] footPrint = getStatEventFootPrintFromId(id);
		 //Now in footPrint we have [feature, subfeature, type, subtype]
		 for (Iterator iter = catalogs.iterator(); iter.hasNext();) {
			EventCatalog catalog_i = (EventCatalog) iter.next();
			if(catalog_i.getFeatureID().equals(footPrint[0])){
				//It is our catalog
				List subFeatureList = catalog_i.getSubFeatureEvent();
				for (Iterator iterator = subFeatureList.iterator(); iterator.hasNext();) {
					EventSubFeature eventSubFeature_i = (EventSubFeature) iterator.next();
					if(eventSubFeature_i.getValue() == Integer.parseInt(footPrint[1])){
						//It is our sub feature
						List eventTypeList = eventSubFeature_i.getEventTypes();
						for (Iterator iteratorType = eventTypeList.iterator(); iteratorType.hasNext();) {
							EventType type_i = (EventType) iteratorType.next();
							if(type_i.getValue() == Integer.parseInt(footPrint[2])){
								return type_i;
							}
						}
					}
				}
				
			}
		}
		return null;
	}
	
	/**
	 * @return the selected stat event based in the name id of 1_0_6_1_n, and we return  [feature, subfeature, type, subtype]
	 */
	public static String[] getStatEventFootPrintFromId(String id) throws NumberFormatException {
		String[] footPrint = new String[4];
		//Extraction of subfeature, type, stat
		String rule="[0-9]{1,}_";
		 Pattern p = Pattern.compile(rule);
		 Matcher matcher = p.matcher(id);
		 int i=0;
		 while(matcher.find()&& i<4){
			String field = matcher.group();
			footPrint[i] = field.substring(0, field.length()-1);
			i++;
		 }
		 return footPrint;
		 }
	

	/** 
	 * @param file the file in which we are looking for stat event method name
	 * @return the list of stat event method names.
	 */
	public static List getStatEventMethodNames(IFile file) {
			List usageData = new ArrayList();
			try {
				
				ICompilationUnit unit = (ICompilationUnit) JavaCore.create(file);						
				IType clazz = unit.getTypes()[0];
				IMethod methods[] = clazz.getMethods();
				
				for (int i = 0; i < methods.length; i++) {
					
					if (methods[i].getElementName().startsWith("increment")) {
						IMethod method = methods[i];
						String name = Utils.uncapitalize(method.getElementName());
						usageData.add(name);
						continue;
					}
					
					/**@OSP modification: we must add the other type of UP: the tag event*/
					if (methods[i].getElementName().startsWith("tag")) {
						IMethod method = methods[i];
						String name = Utils.uncapitalize(method.getElementName());
						usageData.add(name);
						continue;
					}
					// Ignore this non-compliant method.
				}
			} catch (JavaModelException e) {
				// Ignore to return what has been successfully entered.
				SCELogger.logError("Java model error while reading the UP (stat event) interface "+ file.getName(), e);
			}
			return usageData;
		}


	public static List copyToList(StatEvent[] selectedStatEvents) {
		List copy = new ArrayList();
		for (int i = 0; i < selectedStatEvents.length; i++) {
			StatEvent event = selectedStatEvents[i];
			copy.add(event);
		}
		return copy;
	}

	public static StatEvent[] getArray(List selected_stat_old) {
		StatEvent[]  array=new StatEvent[selected_stat_old.size()];
		int i = 0;
		for (Iterator iter = selected_stat_old.iterator(); iter.hasNext(); i++) {
			StatEvent item = (StatEvent) iter.next();
			array[i] = item;
		}
		return array;
	}

	public static List copy(List toCopy) {
		List copy = new ArrayList();
		for (Iterator iter = toCopy.iterator(); iter.hasNext();) {
			Object item = (Object) iter.next();
			copy.add(item);
		}
		return copy;
	}

}
