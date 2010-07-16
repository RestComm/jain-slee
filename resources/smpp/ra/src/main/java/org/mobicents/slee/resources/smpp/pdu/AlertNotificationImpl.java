package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.AlertNotification;
import net.java.slee.resources.smpp.pdu.SmppRequest;
import net.java.slee.resources.smpp.pdu.SmppResponse;

/**
 * 
 * @author amit bhayani
 *
 */
public class AlertNotificationImpl extends AlertNotification {

	private long commandId = SmppRequest.ALERT_NOTIFICATION;
	private long commandStatus = 0;
	private long sequenceNumber = -1;

	private Address esmeAdd = null;
	private Address srcAdd = null;

	public AlertNotificationImpl(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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
	public Address getEsmeAddress() {
		return this.esmeAdd;
	}

	@Override
	public Address getSourceAddress() {
		return this.srcAdd;
	}

	@Override
	public void setEsmeAddress(Address address) {
		this.esmeAdd = address;
	}

	@Override
	public void setSourceAddress(Address address) {
		this.srcAdd = address;
	}

	public SmppResponse createSmppResponseEvent(int status) {
		throw new UnsupportedOperationException("No response exist for Alert Notification");
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ALERT_NOTIFICATION").append("[commandId=").append(commandId).append(" ").append("commandStatus=")
				.append(commandStatus).append("sequenceNumber=").append(sequenceNumber).append("]");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (commandId ^ (commandId >>> 32));
		result = prime * result + ((esmeAdd == null) ? 0 : esmeAdd.hashCode());
		result = prime * result + (int) (sequenceNumber ^ (sequenceNumber >>> 32));
		result = prime * result + ((srcAdd == null) ? 0 : srcAdd.hashCode());
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
		final AlertNotificationImpl other = (AlertNotificationImpl) obj;
		if (commandId != other.commandId)
			return false;
		if (esmeAdd == null) {
			if (other.esmeAdd != null)
				return false;
		} else if (!esmeAdd.equals(other.esmeAdd))
			return false;
		if (sequenceNumber != other.sequenceNumber)
			return false;
		if (srcAdd == null) {
			if (other.srcAdd != null)
				return false;
		} else if (!srcAdd.equals(other.srcAdd))
			return false;
		return true;
	}
	
	
	
	

}
