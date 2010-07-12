package net.java.slee.resources.smpp.pdu;

import java.util.HashMap;
import java.util.Map;

public abstract class QueryBroadcastSMResp implements SmppResponse {
	private Map<Tag, Object> tlvs = new HashMap<Tag, Object>();

	public abstract String getMessageID();

	public abstract void setMessageID(String messageID);

	public void addTLV(Tag tag, Object value) throws TLVNotPermittedException {
		if (isTLVPermitted(tag)) {
			this.tlvs.put(tag, value);
		} else {
			throw new TLVNotPermittedException(tag);
		}
	}

	public Object getValue(Tag tag) {
		return this.tlvs.get(tag);
	}

	public Object removeTLV(Tag tag) {
		return this.tlvs.remove(tag);
	}

	public boolean hasTLV(Tag tag) {
		return this.tlvs.containsKey(tag);
	}

	public boolean isTLVPermitted(Tag tag) {
		return (tag.equals(Tag.MESSAGE_STATE) || tag.equals(Tag.BROADCAST_AREA_IDENTIFIER)
				|| tag.equals(Tag.BROADCAST_AREA_SUCCESS) || tag.equals(Tag.BROADCAST_END_TIME) || tag
				.equals(Tag.USER_MESSAGE_REFERENCE));
	}

	public Map<Tag, Object> getAllTLVs() {
		return this.tlvs;
	}
}
