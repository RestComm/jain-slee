package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.QueryBroadcastSMResp;
import net.java.slee.resources.smpp.pdu.Tag;

import org.mobicents.protocols.smpp.message.SMPPPacket;
import org.mobicents.slee.resources.smpp.ExtSmppResponse;

/**
 * 
 * @author amit bhayani
 * 
 */
public class QueryBroadcastSMRespImpl extends PDUImpl implements QueryBroadcastSMResp, ExtSmppResponse {

	public QueryBroadcastSMRespImpl(org.mobicents.protocols.smpp.message.QueryBroadcastSMResp queryBroadcastSMResp) {
		this.smppPacket = queryBroadcastSMResp;
	}

	public QueryBroadcastSMRespImpl(long sequenceNumber) {
		super();
		this.smppPacket = new org.mobicents.protocols.smpp.message.QueryBroadcastSMResp();
		this.smppPacket.setSequenceNum(sequenceNumber);
	}

	public String getMessageID() {
		return ((org.mobicents.protocols.smpp.message.QueryBroadcastSMResp) this.smppPacket).getMessageId();
	}

	public void setMessageID(String messageID) {
		((org.mobicents.protocols.smpp.message.QueryBroadcastSMResp) this.smppPacket).setMessageId(messageID);
	}

	public boolean isTLVPermitted(Tag tag) {
		return (tag.equals(Tag.BROADCAST_END_TIME) || tag.equals(Tag.USER_MESSAGE_REFERENCE));
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

}
