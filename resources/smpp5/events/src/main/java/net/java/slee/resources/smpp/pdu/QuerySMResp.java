package net.java.slee.resources.smpp.pdu;

import java.util.Map;

public abstract class QuerySMResp implements SmppResponse {

	public abstract String getMessageID();
	public abstract void setMessageID(String messageID);
	
	public abstract String getFinalDate();
	public abstract void setFinalDate(String date);
	
	/**
	 * @see MessageState
	 * @return
	 */
	public abstract int getMessageState();
	public abstract void setMessageState(int messageState);
	
	public abstract int getErrorCode();
	public abstract void setErrorCode(int errorCode);
	
	public void addTLV(Tag tag, Object value) throws TLVNotPermittedException {
		throw new TLVNotPermittedException(tag);
	}

	public Object getValue(Tag tag) {
		return null;
	}

	public Object removeTLV(Tag tag) {
		return null;
	}

	public boolean hasTLV(Tag tag) {
		return false;
	}

	public boolean isTLVPermitted(Tag tag) {
		return false;
	}

	public Map<Tag, Object> getAllTLVs() {
		return null;
	}	
}
