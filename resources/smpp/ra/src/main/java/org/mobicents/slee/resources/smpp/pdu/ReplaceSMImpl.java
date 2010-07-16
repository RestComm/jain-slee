package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.ReplaceSM;
import net.java.slee.resources.smpp.pdu.SmppRequest;
import net.java.slee.resources.smpp.pdu.SmppResponse;

/**
 * 
 * @author amit bhayani
 *
 */
public class ReplaceSMImpl extends ReplaceSM {

	private long commandId = SmppRequest.REPLACE_SM;
	private long commandStatus = 0;
	private long sequenceNumber = -1;

	private byte[] message;
	private String messageID;
	private int registeredDelivery;
	private String scheduleDeliveryTime;
	private int smDefaultMsgID;
	private Address sourceAddress;
	private String validityPeriod;

	public ReplaceSMImpl(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public byte[] getMessage() {
		return message;
	}

	@Override
	public String getMessageID() {
		return messageID;
	}

	@Override
	public int getRegisteredDelivery() {
		return registeredDelivery;
	}

	@Override
	public String getScheduleDeliveryTime() {
		return scheduleDeliveryTime;
	}

	@Override
	public int getSmDefaultMsgID() {
		return smDefaultMsgID;
	}

	@Override
	public Address getSourceAddress() {
		return sourceAddress;
	}

	@Override
	public String getValidityPeriod() {
		return validityPeriod;
	}

	@Override
	public void setMessage(byte[] message) {
		this.message = message;
	}

	@Override
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	@Override
	public void setRegisteredDelivery(int registeredDelivery) {
		this.registeredDelivery = registeredDelivery;
	}

	@Override
	public void setScheduleDeliveryTime(String time) {
		this.scheduleDeliveryTime = time;
	}

	@Override
	public void setSmDefaultMsgID(int smDefaultMsgID) {
		this.smDefaultMsgID = smDefaultMsgID;
	}

	@Override
	public void setSourceAddress(Address address) {
		this.sourceAddress = address;
	}

	@Override
	public void setValidityPeriod(String period) {
		this.validityPeriod = period;
	}

	public SmppResponse createSmppResponseEvent(int status) {
		ReplaceSMRespImpl replaceSMRespImpl = new ReplaceSMRespImpl(status);
		replaceSMRespImpl.setSequenceNum(this.getSequenceNum());
		return replaceSMRespImpl;
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
		//result = prime * result + Arrays.hashCode(message);
		result = prime * result + ((messageID == null) ? 0 : messageID.hashCode());
		result = prime * result + registeredDelivery;
		result = prime * result + ((scheduleDeliveryTime == null) ? 0 : scheduleDeliveryTime.hashCode());
		result = prime * result + (int) (sequenceNumber ^ (sequenceNumber >>> 32));
		result = prime * result + smDefaultMsgID;
		result = prime * result + ((sourceAddress == null) ? 0 : sourceAddress.hashCode());
		result = prime * result + ((validityPeriod == null) ? 0 : validityPeriod.hashCode());
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
		final ReplaceSMImpl other = (ReplaceSMImpl) obj;
		if (commandId != other.commandId)
			return false;
//		if (!Arrays.equals(message, other.message))
//			return false;
		if (messageID == null) {
			if (other.messageID != null)
				return false;
		} else if (!messageID.equals(other.messageID))
			return false;
		if (registeredDelivery != other.registeredDelivery)
			return false;
		if (scheduleDeliveryTime == null) {
			if (other.scheduleDeliveryTime != null)
				return false;
		} else if (!scheduleDeliveryTime.equals(other.scheduleDeliveryTime))
			return false;
		if (sequenceNumber != other.sequenceNumber)
			return false;
		if (smDefaultMsgID != other.smDefaultMsgID)
			return false;
		if (sourceAddress == null) {
			if (other.sourceAddress != null)
				return false;
		} else if (!sourceAddress.equals(other.sourceAddress))
			return false;
		if (validityPeriod == null) {
			if (other.validityPeriod != null)
				return false;
		} else if (!validityPeriod.equals(other.validityPeriod))
			return false;
		return true;
	}

}
