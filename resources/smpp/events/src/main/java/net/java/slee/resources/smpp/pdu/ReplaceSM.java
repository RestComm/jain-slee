package net.java.slee.resources.smpp.pdu;

import java.util.Map;

/**
 * 
 * @author amit bhayani
 *
 */
public abstract class ReplaceSM implements SmppRequest {


	public abstract String getMessageID();
	public abstract void setMessageID(String messageID);

	public abstract Address getSourceAddress();
	public abstract void setSourceAddress(Address address);

	// TODO The specs says about absolute date and time or relative. Should we add the corresponding classes here to set
	// respective dates rather than String?
	public abstract void setScheduleDeliveryTime(String time);
	public abstract String getScheduleDeliveryTime();

	// TODO The specs says about absolute date and time or relative. Should we add the corresponding classes here to set
	// respective dates rather than String?
	public abstract String getValidityPeriod();
	public abstract void setValidityPeriod(String period);

	public abstract int getRegisteredDelivery();
	public abstract void setRegisteredDelivery(int registeredDelivery);

	public abstract int getSmDefaultMsgID();
	public abstract void setSmDefaultMsgID(int smDefaultMsgID);

	public abstract byte[] getMessage();
	public abstract void setMessage(byte[] message);

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
