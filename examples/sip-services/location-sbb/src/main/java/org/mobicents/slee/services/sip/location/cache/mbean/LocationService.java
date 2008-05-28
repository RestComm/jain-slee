package org.mobicents.slee.services.sip.location.cache.mbean;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.services.sip.common.LocationServiceException;
import org.mobicents.slee.services.sip.common.RegistrationBinding;
import org.mobicents.slee.services.sip.location.cache.LocationSbb;

public class LocationService extends Object implements LocationServiceMBean {

	private String name = "v1LocationService";
	private final static org.mobicents.slee.services.sip.location.cache.LocationService ls = new org.mobicents.slee.services.sip.location.cache.LocationService();

	public Set<String> getContacts(String record) {

		try {
			return new HashSet<String>(ls.getBindings(record).keySet());
		} catch (LocationServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new HashSet<String>();
	}

	public long getExpirationTime(String record, String contact) {
		
		try {
			Map bindings=ls.getBindings(record);
			RegistrationBinding rb=(RegistrationBinding) bindings.get(contact);
			if(rb!=null)
				return rb.getExpiryDelta();
		} catch (LocationServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Long.MIN_VALUE;
		}
		return 0;
	}

	public Set<String> getRegisteredUsers() {

		return new HashSet<String>(ls.getRegistered());
	}

	public int getRegisteredUserCount()
	{
		return getRegisteredUsers().size();
	}
	
	public boolean startService() {

		MBeanServer mbs = SleeContainer.lookupFromJndi().getMBeanServer();
		ObjectName on = null;
		try {
			on = new ObjectName(MBEAN_NAME_PREFIX + name);

		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (mbs.getObjectInstance(on) != null) {
				// LOG
				return false;
			}
		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			Logger.getLogger(LocationSbb.class.getCanonicalName()).info("Binding LocationMBean to["+on+"]");
			mbs.registerMBean(this, on);
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
