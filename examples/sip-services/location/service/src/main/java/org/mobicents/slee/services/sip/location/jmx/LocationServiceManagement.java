package org.mobicents.slee.services.sip.location.jmx;

import java.util.Set;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.services.sip.location.LocationService;
import org.mobicents.slee.services.sip.location.LocationServiceException;

/**
 * Implementation of the Location Service Management JMX interface.
 * @author martins
 *
 */
public class LocationServiceManagement implements LocationServiceManagementMBean {

	private final LocationService locationService;
	
	private LocationServiceManagement(LocationService locationService) {
		this.locationService=locationService;
	}
	
	public Set<String> getContacts(String sipAddress)
			throws LocationServiceException {
		
		return locationService.getContacts(sipAddress);
	}

	public long getExpirationTime(String sipAddress, String contactAddress)
			throws LocationServiceException {
		
		return locationService.getExpirationTime(sipAddress, contactAddress);
	}

	public int getRegisteredUserCount() throws LocationServiceException {
		
		return locationService.getRegisteredUserCount();
	}

	public Set<String> getRegisteredUsers() throws LocationServiceException {
		
		return locationService.getRegisteredUsers();
	}

	/**
	 * Starts the MBean
	 * @return
	 */
	public static boolean create(LocationService locationService) {

		MBeanServer mbs = SleeContainer.lookupFromJndi().getMBeanServer();
		ObjectName on = null;
		try {
			on = new ObjectName(MBEAN_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		} 

		try {
			if (mbs.getObjectInstance(on) != null) {
				mbs.unregisterMBean(on);
			}
		} catch (InstanceNotFoundException e) {
			// ignore
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			mbs.registerMBean(new LocationServiceManagement(locationService), on);
		} catch (InstanceAlreadyExistsException e) {

			e.printStackTrace();
			return false;
		} catch (MBeanRegistrationException e) {

			e.printStackTrace();
			return false;
		} catch (NotCompliantMBeanException e) {

			e.printStackTrace();
			return false;
		}

		return true;

	}
	
	/**
	 * Stops the MBean
	 */
	public static void destroy() {
		try {
			SleeContainer.lookupFromJndi().getMBeanServer().unregisterMBean(new ObjectName(MBEAN_NAME));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
