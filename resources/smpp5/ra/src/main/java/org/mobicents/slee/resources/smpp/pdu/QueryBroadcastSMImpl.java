package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.QueryBroadcastSM;
import net.java.slee.resources.smpp.pdu.SmppResponse;
import net.java.slee.resources.smpp.pdu.Tag;

import org.mobicents.protocols.smpp.message.SMPPPacket;
import org.mobicents.slee.resources.smpp.ExtSmppRequest;

/**
 * 
 * @author amit bhayani
 *
 */
public class QueryBroadcastSMImpl extends PDUImpl implements QueryBroadcastSM, ExtSmppRequest {

	public QueryBroadcastSMImpl(org.mobicents.protocols.smpp.message.QueryBroadcastSM broadcastSM) {
		this.smppPacket = broadcastSM;
	}

	public QueryBroadcastSMImpl(long sequenceNumber) {
		super();
		this.smppPacket = new org.mobicents.protocols.smpp.message.QueryBroadcastSM();
		this.smppPacket.setSequenceNum(sequenceNumber);
	}

	public Address getSourceAddress() {
		return this.convertProtoAddress(((org.mobicents.protocols.smpp.message.QueryBroadcastSM) this.smppPacket)
				.getSource());
	}

	public String getMessageID() {
		return ((org.mobicents.protocols.smpp.message.QueryBroadcastSM) this.smppPacket).getMessageId();
	}

	public void setSourceAddress(Address address) {
		((org.mobicents.protocols.smpp.message.QueryBroadcastSM) this.smppPacket)
				.setSource(address != null ? ((AddressImpl) address).getProtoAddress() : null);
	}

	public void setMessageID(String messageID) {
		((org.mobicents.protocols.smpp.message.QueryBroadcastSM) this.smppPacket).setMessageId(messageID);
	}

	public SmppResponse createSmppResponseEvent(int status) {
		QueryBroadcastSMRespImpl queryBroadcastSMRespImpl = new QueryBroadcastSMRespImpl(status);
		queryBroadcastSMRespImpl.setSequenceNum(this.getSequenceNum());
		return queryBroadcastSMRespImpl;
	}

	public boolean isTLVPermitted(Tag tag) {
		return (tag.equals(Tag.USER_MESSAGE_REFERENCE));
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

}
