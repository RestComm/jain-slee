package net.java.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.util.SMPPDate;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface BroadcastSM extends SmppRequest {

	public String getServiceType();

	public void setServiceType(String serviceType);

	public Address getEsmeAddress();

	public void setEsmeAddress(Address address);

	public String getMessageID();

	public void setMessageID(String messageID);

	public int getPriority();

	public void setPriority(int priority);

	public void setScheduleDeliveryTime(SMPPDate time);

	public SMPPDate getScheduleDeliveryTime();

	public SMPPDate getValidityPeriod();

	public void setValidityPeriod(SMPPDate period);

	public int getReplaceIfPresentFlag();

	public void setReplaceIfPresentFlag(int replaceIfPresentFlag);

	public int getDataCoding();

	public void setDataCoding(int dataCoding);

	public int getSmDefaultMsgID();

	public void setSmDefaultMsgID(int smDefaultMsgID);

}
