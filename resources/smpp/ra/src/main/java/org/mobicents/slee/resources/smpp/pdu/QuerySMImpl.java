package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.QuerySM;
import net.java.slee.resources.smpp.pdu.SmppRequest;
import net.java.slee.resources.smpp.pdu.SmppResponse;

/**
 * 
 * @author amit bhayani
 *
 */
public class QuerySMImpl extends QuerySM {

	private long commandId = SmppRequest.QUERY_SM;
	private long commandStatus = 0;
	private long sequenceNumber = -1;

	private String messageID = null;
	private Address sourceAddress = null;

	public QuerySMImpl(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public String getMessageID() {
		return messageID;
	}

	@Override
	public Address getSourceAddress() {
		return sourceAddress;
	}

	@Override
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	@Override
	public void setSourceAddress(Address address) {
		this.sourceAddress = address;
	}

	public SmppResponse createSmppResponseEvent(int status) {
		QuerySMRespImpl querySMRespImpl = new QuerySMRespImpl(status);
		querySMRespImpl.setSequenceNum(this.getSequenceNum());
		return querySMRespImpl;
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
		result = prime * result + ((messageID == null) ? 0 : messageID.hashCode());
		result = prime * result + (int) (sequenceNumber ^ (sequenceNumber >>> 32));
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
		final QuerySMImpl other = (QuerySMImpl) obj;
		if (commandId != other.commandId)
			return false;
		if (messageID == null) {
			if (other.messageID != null)
				return false;
		} else if (!messageID.equals(other.messageID))
			return false;
		if (sequenceNumber != other.sequenceNumber)
			return false;
		if (sourceAddress == null) {
			if (other.sourceAddress != null)
				return false;
		} else if (!sourceAddress.equals(other.sourceAddress))
			return false;
		return true;
	}

}
