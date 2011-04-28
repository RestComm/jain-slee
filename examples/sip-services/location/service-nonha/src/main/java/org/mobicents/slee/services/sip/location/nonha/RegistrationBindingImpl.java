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

package org.mobicents.slee.services.sip.location.nonha;

import org.mobicents.slee.services.sip.location.RegistrationBinding;

public class RegistrationBindingImpl extends RegistrationBinding {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1996317730252868340L;
	
	private String sipAddress;
	private String contactAddress;
	private long expires;
	private long registrationDate;
	private float qValue;
	private String callId;
	private long cSeq;
	private String comment;
	
	public RegistrationBindingImpl(String sipAddress, String contactAddress,
			long expires, long registrationDate, float value, String callId,
			long seq, String comment) {
		super();
		this.sipAddress = sipAddress;
		this.contactAddress = contactAddress;
		this.expires = expires;
		this.registrationDate = registrationDate;
		qValue = value;
		this.callId = callId;
		cSeq = seq;
		this.comment = comment;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
