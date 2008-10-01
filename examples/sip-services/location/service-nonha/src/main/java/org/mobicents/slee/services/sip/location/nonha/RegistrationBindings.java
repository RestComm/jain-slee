package org.mobicents.slee.services.sip.location.nonha;

import java.rmi.server.UID;
import java.util.concurrent.ConcurrentHashMap;

import javax.sip.address.Address;

import org.mobicents.slee.services.sip.location.RegistrationBinding;

public class RegistrationBindings {

	private ConcurrentHashMap<Address,RegistrationBinding> bindings = new ConcurrentHashMap<Address,RegistrationBinding>();
	private String uid = new UID().toString();
	
	public ConcurrentHashMap<Address, RegistrationBinding> getBindings() {
		return bindings;
	}
	
	@Override
	public int hashCode() {
		return uid.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((RegistrationBindings)obj).uid.equals(this.uid);
		}
		else {
			return false;
		}
	}
}
