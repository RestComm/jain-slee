package net.java.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.util.SMPPDate;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface ReplaceSM extends SmppRequest {

	public abstract String getMessageID();

	public abstract void setMessageID(String messageID);

	public abstract Address getSourceAddress();

	public abstract void setSourceAddress(Address address);

	public abstract void setScheduleDeliveryTime(SMPPDate time);

	public abstract SMPPDate getScheduleDeliveryTime();

	public abstract SMPPDate getValidityPeriod();

	public abstract void setValidityPeriod(SMPPDate period);

	public abstract int getRegisteredDelivery();

	public abstract void setRegisteredDelivery(int registeredDelivery);

	public abstract int getSmDefaultMsgID();

	public abstract void setSmDefaultMsgID(int smDefaultMsgID);

	public abstract byte[] getMessage();

	public abstract void setMessage(byte[] message);

}
