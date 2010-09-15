package net.java.slee.resources.smpp.pdu;

import java.util.Collection;

import net.java.slee.resources.smpp.util.SMPPDate;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface SubmitMulti extends SmppRequest {

	public abstract String getServiceType();

	public abstract void setServiceType(String serviceType);

	public abstract Address getSourceAddress();

	public abstract void setSourceAddress(Address address);

	public abstract void addDestAddress(Address address) throws SmppTooManyValuesException;

	public abstract boolean removeDestAddress(Address address);

	public abstract Collection<Address> getDestAddresses();

	public abstract void addDistrListName(String distributionListName) throws SmppTooManyValuesException;

	public abstract boolean removeDistrListName(String distributionListName);

	public abstract Collection<String> getDistrListNames();

	public abstract int getEsmClass();

	public abstract void setEsmClass(int esmClass);

	public abstract int getProtocolID();

	public abstract void setProtocolID(int protocolID);

	public abstract void setPriority(int priority);

	public abstract int getPriority();

	public abstract void setScheduleDeliveryTime(SMPPDate time);

	public abstract SMPPDate getScheduleDeliveryTime();

	public abstract SMPPDate getValidityPeriod();

	public abstract void setValidityPeriod(SMPPDate period);

	public abstract int getRegisteredDelivery();

	public abstract void setRegisteredDelivery(int registeredDelivery);

	public abstract int getReplaceIfPresentFlag();

	public abstract void setReplaceIfPresentFlag(int replaceIfPresentFlag);

	public abstract int getDataCoding();

	public abstract void setDataCoding(int dataCoding);

	public abstract int getSmDefaultMsgID();

	public abstract void setSmDefaultMsgID(int smDefaultMsgID);

	public abstract byte[] getMessage();

	public abstract void setMessage(byte[] message);

}
