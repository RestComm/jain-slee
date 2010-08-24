package net.java.slee.resources.smpp.pdu;

import java.util.Map;
/**
 * 
 * @author amit bhayani
 */
//TODO : Spec says about TLV in section 4.6.2.2 Cancel Broadcast Optional TLVs, but doesnot include TLV field in Table 4-40 cancel_broadcast_sm PDU
public abstract class CancelBroadcastSM implements SmppRequest {
	public abstract String getServiceType();
	public abstract void setServiceType(String serviceType);
	
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
