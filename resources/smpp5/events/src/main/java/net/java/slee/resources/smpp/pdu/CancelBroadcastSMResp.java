package net.java.slee.resources.smpp.pdu;

import java.util.Map;

public abstract class CancelBroadcastSMResp implements SmppResponse {

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
