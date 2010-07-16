package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.CancelSMResp;
import net.java.slee.resources.smpp.pdu.SmppRequest;

/**
 * 
 * @author amit bhayani
 *
 */
public class CancelSMRespImpl extends CancelSMResp {

	private long commandId = SmppRequest.CANCEL_SM_RESP;
	private long commandStatus = 0;
	private long sequenceNumber = -1;

	public CancelSMRespImpl(int status) {
		this.commandStatus = status;
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
		final CancelSMRespImpl other = (CancelSMRespImpl) obj;
		if (commandId != other.commandId)
			return false;
		if (sequenceNumber != other.sequenceNumber)
			return false;
		return true;
	}
	

}
