package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.AlertNotification;
import net.java.slee.resources.smpp.pdu.SmppResponse;
import net.java.slee.resources.smpp.pdu.Tag;

/**
 * 
 * @author amit bhayani
 * 
 */
public class AlertNotificationImpl extends PDUImpl implements AlertNotification {

	public AlertNotificationImpl(org.mobicents.protocols.smpp.message.AlertNotification alertNtfn) {
		this.smppPacket = alertNtfn;
	}

	public AlertNotificationImpl(long sequenceNumber) {
		super();
		this.smppPacket = new org.mobicents.protocols.smpp.message.AlertNotification();
		this.smppPacket.setSequenceNum(sequenceNumber);
	}

	public Address getEsmeAddress() {
		return this.convertProtoAddress(((org.mobicents.protocols.smpp.message.AlertNotification) this.smppPacket)
				.getDestination());
	}

	public Address getSourceAddress() {
		return this.convertProtoAddress(((org.mobicents.protocols.smpp.message.AlertNotification) this.smppPacket)
				.getSource());
	}

	public void setEsmeAddress(Address address) {
		if (address != null) {
			((org.mobicents.protocols.smpp.message.AlertNotification) this.smppPacket)
					.setDestination(((AddressImpl) address).getProtoAddress());
		} else {
			((org.mobicents.protocols.smpp.message.AlertNotification) this.smppPacket).setDestination(null);
		}
	}

	public void setSourceAddress(Address address) {
		if (address != null) {
			((org.mobicents.protocols.smpp.message.AlertNotification) this.smppPacket)
					.setDestination(((AddressImpl) address).getProtoAddress());
		} else {
			((org.mobicents.protocols.smpp.message.AlertNotification) this.smppPacket).setDestination(null);
		}
	}

	public SmppResponse createSmppResponseEvent(int status) {
		throw new UnsupportedOperationException("No response exist for Alert Notification");
	}

	public boolean isTLVPermitted(Tag tag) {
		return tag.equals(Tag.MS_AVAILABILITY_STATUS);
	}

}
