package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.CancelSM;
import net.java.slee.resources.smpp.pdu.SmppRequest;
import net.java.slee.resources.smpp.pdu.SmppResponse;

/**
 * 
 * @author amit bhayani
 *
 */
public class CancelSMImpl extends CancelSM {

	private long commandId = SmppRequest.CANCEL_SM;
	private long commandStatus = 0;
	private long sequenceNumber = -1;

	private Address destAddress;
	private String messageID;
	private String serviceType;
	private Address sourceAddress;

	public CancelSMImpl(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public Address getDestAddress() {
		return destAddress;
	}

	@Override
	public String getMessageID() {
		return messageID;
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
	public void setDestAddress(Address address) {
		this.destAddress = address;
	}

	@Override
	public void setMessageID(String messageID) {
		this.messageID = messageID;
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
		CancelSMRespImpl cancelSMRespImpl = new CancelSMRespImpl(status);
		cancelSMRespImpl.setSequenceNum(this.getSequenceNum());
		return cancelSMRespImpl;
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
		result = prime * result + ((destAddress == null) ? 0 : destAddress.hashCode());
		result = prime * result + ((messageID == null) ? 0 : messageID.hashCode());
		result = prime * result + (int) (sequenceNumber ^ (sequenceNumber >>> 32));
		result = prime * result + ((serviceType == null) ? 0 : serviceType.hashCode());
		result = prime * result + ((sourceAddress == null) ? 0 : sourceAddress.hashCode());
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
		final CancelSMImpl other = (CancelSMImpl) obj;
		if (commandId != other.commandId)
			return false;
		if (destAddress == null) {
			if (other.destAddress != null)
				return false;
		} else if (!destAddress.equals(other.destAddress))
			return false;
		if (messageID == null) {
			if (other.messageID != null)
				return false;
		} else if (!messageID.equals(other.messageID))
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
