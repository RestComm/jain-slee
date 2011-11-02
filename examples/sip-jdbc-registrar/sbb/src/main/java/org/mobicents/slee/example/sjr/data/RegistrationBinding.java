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

package org.mobicents.slee.example.sjr.data;


/**
 * TODO
 * 
 * @author martins
 * 
 */
public class RegistrationBinding {
	private String sipAddress;
	private String contactAddress;
	private long expires;
	private long registrationDate;
	private float qValue;
	private String callId;
	private long cSeq;


	public RegistrationBinding(String sipAddress, String contactAddress, long expires, long registrationDate, float value, String callId, long seq) {
		super();
		this.sipAddress = sipAddress;
		this.contactAddress = contactAddress;
		this.expires = expires;
		this.registrationDate = registrationDate;
		this.qValue = value;
		this.callId = callId;
		this.cSeq = seq;
		
	}

	public String getSipAddress() {
		return sipAddress;
	}

	public void setSipAddress(String sipAddress) {
		this.sipAddress = sipAddress;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

	public long getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(long registrationDate) {
		this.registrationDate = registrationDate;
	}

	public float getQValue() {
		return qValue;
	}

	public void setQValue(float value) {
		qValue = value;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public long getCSeq() {
		return cSeq;
	}

	public void setCSeq(long seq) {
		cSeq = seq;
	}

	// --- logic methods

	/**
	 * Returns number of mseconds till this entry expires May be 0 or -ve if
	 * already expired
	 */
	public long getExpiresDelta() {
		return ((getExpires() - (System.currentTimeMillis() - getRegistrationDate()) / 1000));
	}

	public String toString() {
		return "RegistrationBinding[sipAddress=" + getSipAddress() + ",contactAddress=" + getContactAddress() +  ",expires="
				+ getExpires() + ",qValue=" + getQValue() + ",callId=" + getCallId() + ",cSeq=" + getCSeq() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getContactAddress() == null) ? 0 : getContactAddress().hashCode());
		result = prime * result + ((getSipAddress() == null) ? 0 : getSipAddress().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			RegistrationBinding other = (RegistrationBinding) obj;
			return this.getSipAddress().equals(other.getSipAddress()) && this.getContactAddress().equals(other.getContactAddress());
		} else {
			return false;
		}
	}
}
