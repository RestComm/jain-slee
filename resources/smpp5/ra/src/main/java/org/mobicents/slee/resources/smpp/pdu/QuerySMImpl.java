package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.QuerySM;
import net.java.slee.resources.smpp.pdu.SmppResponse;
import net.java.slee.resources.smpp.pdu.Tag;

import org.mobicents.protocols.smpp.message.SMPPPacket;
import org.mobicents.slee.resources.smpp.ExtSmppRequest;

/**
 * 
 * @author amit bhayani
 * 
 */
public class QuerySMImpl extends PDUImpl implements QuerySM, ExtSmppRequest {

	public QuerySMImpl(org.mobicents.protocols.smpp.message.QuerySM querySM) {
		this.smppPacket = querySM;
	}

	public QuerySMImpl(long sequenceNumber) {
		super();
		this.smppPacket = new org.mobicents.protocols.smpp.message.QuerySM();
		this.smppPacket.setSequenceNum(sequenceNumber);
	}

	public String getMessageID() {
		return ((org.mobicents.protocols.smpp.message.QuerySM) this.smppPacket).getMessageId();
	}

	public Address getSourceAddress() {
		return this.convertProtoAddress(((org.mobicents.protocols.smpp.message.QuerySM) this.smppPacket).getSource());
	}

	public void setMessageID(String messageID) {
		((org.mobicents.protocols.smpp.message.QuerySM) this.smppPacket).setMessageId(messageID);
	}

	public void setSourceAddress(Address address) {
		if (address != null) {
			((org.mobicents.protocols.smpp.message.QuerySM) this.smppPacket).setSource(((AddressImpl) address)
					.getProtoAddress());
		} else {
			((org.mobicents.protocols.smpp.message.QuerySM) this.smppPacket).setSource(null);
		}
	}

	public SmppResponse createSmppResponseEvent(int status) {
		QuerySMRespImpl querySMRespImpl = new QuerySMRespImpl(status);
		querySMRespImpl.setSequenceNum(this.getSequenceNum());
		return querySMRespImpl;
	}

	public boolean isTLVPermitted(Tag tag) {
		return false;
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

}
