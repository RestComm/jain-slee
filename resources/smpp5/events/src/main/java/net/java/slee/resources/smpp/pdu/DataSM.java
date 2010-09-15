package net.java.slee.resources.smpp.pdu;


/**
 * 
 * @author amit bhayani
 * 
 */
public interface DataSM extends SmppRequest {

	public abstract String getServiceType();

	public abstract void setServiceType(String serviceType);

	public abstract Address getDestAddress();

	public abstract void setDestAddress(Address address);

	public abstract Address getSourceAddress();

	public abstract void setSourceAddress(Address address);

	public abstract int getEsmClass();

	public abstract void setEsmClass(int esmClass);

	public abstract int getRegisteredDelivery();

	public abstract void setRegisteredDelivery(int registeredDelivery);

	public abstract int getDataCoding();

	public abstract void setDataCoding(int dataCoding);

}
