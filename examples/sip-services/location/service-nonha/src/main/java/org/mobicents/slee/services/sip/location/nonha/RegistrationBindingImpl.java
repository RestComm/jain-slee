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
