package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.QuerySMResp;
import net.java.slee.resources.smpp.pdu.SmppRequest;

/**
 * 
 * @author amit bhayani
 *
 */
public class QuerySMRespImpl extends QuerySMResp {
	
	private long commandId = SmppRequest.QUERY_SM_RESP;
	private long commandStatus = 0;
	private long sequenceNumber = -1;

	
	private int errorCode = 0;
	private String finalDate;
	private String messageID;
	private int messageState;

	public QuerySMRespImpl(int status) {
		this.commandStatus = status;
	}

	@Override
	public int getErrorCode() {
		return errorCode;
	}

	@Override
	public String getFinalDate() {
		return finalDate;
	}

	@Override
	public String getMessageID() {
		return messageID;
	}

	@Override
	public int getMessageState() {
		return messageState;
	}

	@Override
	public void setErrorCode(int errorCode) {
this.errorCode = errorCode;
	}

	@Override
	public void setFinalDate(String date) {
		this.finalDate = date;
	}

	@Override
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	@Override
	public void setMessageState(int messageState) {
		this.messageState = messageState;
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
		result = prime * result + errorCode;
		result = prime * result + ((finalDate == null) ? 0 : finalDate.hashCode());
		result = prime * result + ((messageID == null) ? 0 : messageID.hashCode());
		result = prime * result + messageState;
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
		final QuerySMRespImpl other = (QuerySMRespImpl) obj;
		if (commandId != other.commandId)
			return false;
		if (errorCode != other.errorCode)
			return false;
		if (finalDate == null) {
			if (other.finalDate != null)
				return false;
		} else if (!finalDate.equals(other.finalDate))
			return false;
		if (messageID == null) {
			if (other.messageID != null)
				return false;
		} else if (!messageID.equals(other.messageID))
			return false;
		if (messageState != other.messageState)
			return false;
		if (sequenceNumber != other.sequenceNumber)
			return false;
		return true;
	}

}
