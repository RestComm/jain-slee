package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.CancelBroadcastSM;
import net.java.slee.resources.smpp.pdu.SmppResponse;
import net.java.slee.resources.smpp.pdu.Tag;

import org.mobicents.protocols.smpp.message.SMPPPacket;
import org.mobicents.slee.resources.smpp.ExtSmppRequest;

/**
 * 
 * @author amit bhayani
 * 
 */
public class CancelBroadcastSMImpl extends PDUImpl implements ExtSmppRequest, CancelBroadcastSM {

	public CancelBroadcastSMImpl(org.mobicents.protocols.smpp.message.CancelBroadcastSM cancelSM) {
		this.smppPacket = cancelSM;
	}

	public CancelBroadcastSMImpl(long sequenceNumber) {
		super();
		this.smppPacket = new org.mobicents.protocols.smpp.message.CancelBroadcastSM();
		this.smppPacket.setSequenceNum(sequenceNumber);
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

	public SmppResponse createSmppResponseEvent(int status) {
		CancelBroadcastSMRespImpl cancelBroadcastSMRespImpl = new CancelBroadcastSMRespImpl(status);
		cancelBroadcastSMRespImpl.setSequenceNum(this.getSequenceNum());
		return cancelBroadcastSMRespImpl;
	}

	public boolean isTLVPermitted(Tag tag) {
		return false;
	}

	public Address getEsmeAddress() {
		return this.convertProtoAddress(((org.mobicents.protocols.smpp.message.CancelBroadcastSM) this.smppPacket)
				.getSource());
	}

	public String getMessageID() {
		return ((org.mobicents.protocols.smpp.message.CancelBroadcastSM) this.smppPacket).getMessageId();
	}

	public String getServiceType() {
		return ((org.mobicents.protocols.smpp.message.CancelBroadcastSM) this.smppPacket).getServiceType();
	}

	public void setEsmeAddress(Address address) {
		((org.mobicents.protocols.smpp.message.CancelBroadcastSM) this.smppPacket)
				.setSource(address != null ? ((AddressImpl) address).getProtoAddress() : null);
	}

	public void setMessageID(String messageID) {
		((org.mobicents.protocols.smpp.message.CancelBroadcastSM) this.smppPacket).setMessageId(messageID);
	}

	public void setServiceType(String serviceType) {
		((org.mobicents.protocols.smpp.message.CancelBroadcastSM) this.smppPacket).setServiceType(serviceType);
	}

}
