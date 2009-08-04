package org.mobicents.slee.services.sip.location;

/**
 * Hides the management of which impl of location service and if it's a
 * singleton or not
 * 
 * @author martins
 * 
 */
public class LocationServiceFactory {

	public static LocationService getLocationService(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return (LocationService) Class.forName(className).newInstance();
	}
	
}
