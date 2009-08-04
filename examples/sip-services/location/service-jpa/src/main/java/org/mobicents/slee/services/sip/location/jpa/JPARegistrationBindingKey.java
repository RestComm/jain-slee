package org.mobicents.slee.services.sip.location.jpa;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class JPARegistrationBindingKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1690457764225957427L;
	
	/**
	 * a real contact for the sipAddress 
	 */
	private String contactAddress;
	
	/**
	 * a sip public identity
	 */
	private String sipAddress;
	
	public JPARegistrationBindingKey() {}
	
	public JPARegistrationBindingKey(String contactAddress,String sipAddress) {
		this.contactAddress = contactAddress;
		this.sipAddress = sipAddress;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
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
