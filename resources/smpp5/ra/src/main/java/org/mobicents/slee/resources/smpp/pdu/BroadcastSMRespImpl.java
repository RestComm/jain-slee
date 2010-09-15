package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.BroadcastSMResp;
import net.java.slee.resources.smpp.pdu.Tag;

import org.mobicents.protocols.smpp.message.SMPPPacket;
import org.mobicents.slee.resources.smpp.ExtSmppResponse;

/**
 * 
 * @author amit bhayani
 *
 */
public class BroadcastSMRespImpl extends PDUImpl implements BroadcastSMResp, ExtSmppResponse {

	public BroadcastSMRespImpl(org.mobicents.protocols.smpp.message.BroadcastSMResp broadcastSMResp) {
		this.smppPacket = broadcastSMResp;
	}

	public BroadcastSMRespImpl(int commandStatus) {
		super();
		this.smppPacket = new org.mobicents.protocols.smpp.message.BroadcastSMResp();
		this.smppPacket.setCommandStatus(commandStatus);
	}

	public String getMessageID() {
		return ((org.mobicents.protocols.smpp.message.BroadcastSMResp) this.smppPacket).getMessageId();
	}

	public void setMessageID(String messageID) {
		((org.mobicents.protocols.smpp.message.BroadcastSMResp) this.smppPacket).setMessageId(messageID);
	}

	public boolean isTLVPermitted(Tag tag) {
		return (tag.equals(Tag.BROADCAST_ERROR_STATUS));
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

}
