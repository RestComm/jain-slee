package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.SmppRequest;
import net.java.slee.resources.smpp.pdu.SubmitSMResp;

/**
 * 
 * @author amit bhayani
 *
 */
public class SubmitSMRespImpl extends SubmitSMResp {

	private long commandId = SmppRequest.SUBMIT_SM_RESP;
	private long commandStatus = 0;
	private long sequenceNumber = -1;

	private String messageId;

	public SubmitSMRespImpl(long commandStatus) {
		this.commandStatus = commandStatus;
	}

	@Override
	public String getMessageID() {
		return this.messageId;
	}

	@Override
	public void setMessageID(String messageID) {
		this.messageId = messageID;
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
		result = prime * result + ((messageId == null) ? 0 : messageId.hashCode());
		result = prime * result + (int) (sequenceNumber ^ (sequenceNumber >>> 32));
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
		final SubmitSMRespImpl other = (SubmitSMRespImpl) obj;
		if (commandId != other.commandId)
			return false;
		if (messageId == null) {
			if (other.messageId != null)
				return false;
		} else if (!messageId.equals(other.messageId))
			return false;
		if (sequenceNumber != other.sequenceNumber)
			return false;
		return true;
	}

}
