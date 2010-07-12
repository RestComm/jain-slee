package net.java.slee.resources.smpp.pdu;

import java.util.Map;

public abstract class QuerySM implements SmppRequest {

	public abstract String getMessageID();
	public abstract void setMessageID(String messageID);	

	public abstract Address getEsmeAddress();
	public abstract void setEsmeAddress(Address address);

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
