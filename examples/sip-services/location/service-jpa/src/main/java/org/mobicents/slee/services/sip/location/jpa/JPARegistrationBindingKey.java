package org.mobicents.slee.services.sip.location.jpa;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.sip.address.Address;

@Embeddable
public class JPARegistrationBindingKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1690457764225957427L;
	
	/**
	 * a real contact for the sipAddress 
	 */
	private Address contactAddress;
	
	/**
	 * a sip public identity
	 */
	private String sipAddress;
	
	public JPARegistrationBindingKey() {}
	
	public JPARegistrationBindingKey(Address contactAddress,String sipAddress) {
		this.contactAddress = contactAddress;
		this.sipAddress = sipAddress;
	}

	public Address getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(Address contactAddress) {
		this.contactAddress = contactAddress;
	}

	public String getSipAddress() {
		return sipAddress;
	}

	public void setSipAddress(String sipAddress) {
		this.sipAddress = sipAddress;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result +  contactAddress.hashCode();
		result = prime * result + sipAddress.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj != null && getClass() == obj.getClass()) {
			final JPARegistrationBindingKey other = (JPARegistrationBindingKey) obj;
			return this.contactAddress.equals(other.contactAddress) && this.sipAddress.equals(other.sipAddress);
		}
		else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "contactAddress="+getContactAddress()+",sipAddress="+getSipAddress();
	}
	
}
