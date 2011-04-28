/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
