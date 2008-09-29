package org.mobicents.slee.resource.http;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tells the HttpServlet RA if an event should be filtered
 * @author martins
 *
 */
public class EventIDFilter {

	/**
	 * Holds mappings String..ServiceID --> int[]..EventIDs
	 */ 
	private Map<String, int[]> eventIDsOfServicesInstalled = new ConcurrentHashMap<String, int[]>(
			31);

	/**
	 * Holds mappings eventID --> Set(ServiceID) which are interested in receiving event
	 */
	private Map<Integer, Set<String>> eventID2serviceIDs = new ConcurrentHashMap<Integer, Set<String>>(
			31);

	/**
	 * checks if event should be filtered or not
	 * @param eventID
	 * @return true is event is to be filtered, false otherwise
	 */
	public boolean filterEvent(int eventID) {
		
		return !eventID2serviceIDs.containsKey(Integer.valueOf(eventID));
	}
	
	/**
	 * tells the filter that a service has been installed, declaring a set of event IDs
	 * @param serviceID
	 * @param eventIDs
	 */
	public void serviceInstalled(String serviceID, int[] eventIDs) {

		// STORE SOME INFORMATION FOR LATER
		eventIDsOfServicesInstalled.put(serviceID, eventIDs);
	}

	/**
	 * tells the filter that a service has been uninstalled
	 * @param serviceID
	 */
	public void serviceUninstalled(String serviceID) {

		// LETS REMOVE INFORMATION OF EVENT IDS OF INTERES OF SERVICE FROM THE
		// RECORD
		eventIDsOfServicesInstalled.remove(serviceID);
	}

	
	/**
	 * tells the filter that a service has been activated, thus its eventIDs should not be filtered
	 * @param serviceID
	 */
	public synchronized void serviceActivated(String serviceID) {

		int[] eventIDs = (int[]) eventIDsOfServicesInstalled.get(serviceID);
		if (eventIDs != null) {
			for (int eventID : eventIDs) {
				Set<String> serviceIDs = eventID2serviceIDs.get(Integer.valueOf(eventID));
				if (serviceIDs == null) {
					serviceIDs = new HashSet<String>();
					eventID2serviceIDs.put(Integer.valueOf(eventID),serviceIDs);
				}
				serviceIDs.add(serviceID);
			}
		}
	}

	/**
	 * tells the filter that a service has been deactivated
	 * @param serviceID
	 */
	public synchronized void serviceDeactivated(String serviceID) {

		int[] eventIDs = (int[]) eventIDsOfServicesInstalled.get(serviceID);
		if (eventIDs != null) {
			for (int eventID : eventIDs) {
				Set<String> serviceIDs = eventID2serviceIDs.get(Integer.valueOf(eventID));
				if (serviceIDs != null) {
					serviceIDs.remove(serviceID);
					if (serviceIDs.isEmpty()) {
						eventID2serviceIDs.remove(Integer.valueOf(eventID));
					}
				}
			}
		}

	}

}
