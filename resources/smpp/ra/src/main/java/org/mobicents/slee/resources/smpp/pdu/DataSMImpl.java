package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.DataSM;
import net.java.slee.resources.smpp.pdu.SmppRequest;
import net.java.slee.resources.smpp.pdu.SmppResponse;

/**
 * 
 * @author amit bhayani
 *
 */
public class DataSMImpl extends DataSM {
	
	private long commandId = SmppRequest.DATA_SM;
	private long commandStatus = 0;
	private long sequenceNumber = -1;

	private int dataCoding = 0;
	private Address desrAddress;
	private int esmClass = 0;
	private int registeredDelivery = 0;
	private String serviceType;
	private Address sourceAddress;

	public DataSMImpl(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public int getDataCoding() {
		return dataCoding;
	}

	@Override
	public Address getDestAddress() {
		return desrAddress;
	}

	@Override
	public int getEsmClass() {
		return esmClass;
	}

	@Override
	public int getRegisteredDelivery() {
		return registeredDelivery;
	}

	@Override
	public String getServiceType() {
		return serviceType;
	}

	@Override
	public Address getSourceAddress() {
		return sourceAddress;
	}

	@Override
	public void setDataCoding(int dataCoding) {
		this.dataCoding = dataCoding;
	}

	@Override
	public void setDestAddress(Address address) {
		this.desrAddress = address;
	}

	@Override
	public void setEsmClass(int esmClass) {
		this.esmClass = esmClass;
	}

	@Override
	public void setRegisteredDelivery(int registeredDelivery) {
		this.registeredDelivery = registeredDelivery;
	}

	@Override
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	@Override
	public void setSourceAddress(Address address) {
		this.sourceAddress = address;
	}

	public SmppResponse createSmppResponseEvent(int status) {
		DataSMRespImpl dataSMRespImpl = new DataSMRespImpl(status);
		dataSMRespImpl.setSequenceNum(this.getSequenceNum());
		return dataSMRespImpl;
	}

	public long getCommandId() {
		return this.commandId;
	}

	public long getCommandStatus() {
		return this.commandStatus;
	}

	public long getSequenceNum() {
		return this.sequenceNumber;
	}

	public void setSequenceNum(long sequenceNum) {
		this.sequenceNumber = sequenceNum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (commandId ^ (commandId >>> 32));
		result = prime * result + dataCoding;
		result = prime * result + ((desrAddress == null) ? 0 : desrAddress.hashCode());
		result = prime * result + esmClass;
		result = prime * result + registeredDelivery;
		result = prime * result + (int) (sequenceNumber ^ (sequenceNumber >>> 32));
		result = prime * result + ((serviceType == null) ? 0 : serviceType.hashCode());
		result = prime * result + ((sourceAddress == null) ? 0 : sourceAddress.hashCode());
		
		System.err.println("DELIVER_SM hash = "+ result);
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DataSMImpl other = (DataSMImpl) obj;
		if (commandId != other.commandId)
			return false;
		if (dataCoding != other.dataCoding)
			return false;
		if (desrAddress == null) {
			if (other.desrAddress != null)
				return false;
		} else if (!desrAddress.equals(other.desrAddress))
			return false;
		if (esmClass != other.esmClass)
			return false;
		if (registeredDelivery != other.registeredDelivery)
			return false;
		if (sequenceNumber != other.sequenceNumber)
			return false;
		if (serviceType == null) {
			if (other.serviceType != null)
				return false;
		} else if (!serviceType.equals(other.serviceType))
			return false;
		if (sourceAddress == null) {
			if (other.sourceAddress != null)
				return false;
		} else if (!sourceAddress.equals(other.sourceAddress))
			return false;
		return true;
	}

}
