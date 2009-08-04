/*
 * Created on Jan 18, 2005
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.services.sip.location.nonha;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.mobicents.slee.services.sip.location.LocationService;
import org.mobicents.slee.services.sip.location.LocationServiceException;
import org.mobicents.slee.services.sip.location.RegistrationBinding;
import org.mobicents.slee.services.sip.location.jmx.LocationServiceManagement;


/**
 * 
 * Simple location service without high availability or faul tolerance.
 * 
 * @author martins
 */
public class NonHALocationService implements LocationService {
		
	private ConcurrentHashMap<String, RegistrationBindings> bindingsMap = new ConcurrentHashMap<String, RegistrationBindings>(); 
	
	private static final Logger logger = Logger.getLogger(LocationService.class);
	
	public Map<String,RegistrationBinding> getBindings(String sipAddress) throws LocationServiceException {
		RegistrationBindings bindings = bindingsMap.get(sipAddress);
		if (bindings != null) {
			Map<String,RegistrationBinding> resultMap = new HashMap<String,RegistrationBinding>();
			for (String key:bindings.getBindings().keySet()) {
				resultMap.put(key, bindings.getBindings().get(key));
			}
			return resultMap;
		}
		else {
			return new HashMap<String,RegistrationBinding>();
		}
	}

	public RegistrationBinding addBinding(String sipAddress, String contactAddress, String comment, long expires, long registrationDate, float qValue, String callId, long cSeq)
			throws LocationServiceException {
		RegistrationBinding registrationBinding = new RegistrationBindingImpl(
				sipAddress, contactAddress, expires, registrationDate, qValue,
				callId, cSeq, comment);
		RegistrationBindings bindings = bindingsMap.get(sipAddress);
		if (bindings == null) {
			bindings = new RegistrationBindings();
			RegistrationBindings otherBindings = bindingsMap.putIfAbsent(sipAddress, bindings);
			if (otherBindings != null) {
				bindings = otherBindings;
			}
		}
		
		if (bindings.getBindings().putIfAbsent(contactAddress, registrationBinding) != null) {
			throw new LocationServiceException("Binding already exists");
		}
		else {
			return registrationBinding;
		}
	}

	public void removeBinding(String sipAddress, String sipContact)
			throws LocationServiceException {
		RegistrationBindings bindings = bindingsMap.get(sipAddress);
		if (bindings != null) {
			bindings.getBindings().remove(sipContact);
			if (bindings.getBindings().isEmpty()) {
				bindingsMap.remove(sipAddress, bindings);
			}		
		}
	}

	public void updateBinding(RegistrationBinding registrationBinding)
			throws LocationServiceException {
		RegistrationBindings bindings = bindingsMap.get(registrationBinding.getSipAddress());
		if (bindings != null) {			
			bindings.getBindings().put(registrationBinding.getContactAddress(), registrationBinding);
		}
	}
	
	/**
	 * Returns set of sip addresses of registered users.
	 * @throws LocationServiceException 
	 */
	public Set<String> getRegisteredUsers() throws LocationServiceException {
		return bindingsMap.keySet();
	}
	
	public void init() {
		// starts MBean
		LocationServiceManagement.create(this);	
		logger.info("Non HA Location Service started.");
	}
	
	public void shutdown() {
		// stop MBean
		LocationServiceManagement.destroy();
		logger.info("Non HA Location Service shutdown.");
	}

	// MBEAN RELATED METHODS
	
	public Set<String> getContacts(String sipAddress) throws LocationServiceException {
		return getBindings(sipAddress).keySet();		
	}

	public long getExpirationTime(String sipAddress, String contactAddress) throws LocationServiceException {
		RegistrationBindings bindings = bindingsMap.get(sipAddress);
		if (bindings != null) {	
			for (String address : bindings.getBindings().keySet()) {
				if (address.equals(contactAddress)) {
					return bindings.getBindings().get(address).getExpiresDelta();
				}
			}
		}
		return -1;
	}

	public int getRegisteredUserCount() throws LocationServiceException {
		return getRegisteredUsers().size();
	}
	
}
