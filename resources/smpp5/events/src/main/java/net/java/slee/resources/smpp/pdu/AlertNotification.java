package net.java.slee.resources.smpp.pdu;

import java.util.HashMap;
import java.util.Map;

public abstract class AlertNotification implements SmppRequest {

	private Map<Tag, Object> tlvs = new HashMap<Tag, Object>();

	public abstract Address getEsmeAddress();

	public abstract Address getSourceAddress();

	public abstract void setEsmeAddress(Address address);

	public abstract void setSourceAddress(Address address);

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
		return tag.equals(Tag.MS_AVAILABILITY_STATUS);
	}

	public Map<Tag, Object> getAllTLVs() {
		return this.tlvs;
	}

}
