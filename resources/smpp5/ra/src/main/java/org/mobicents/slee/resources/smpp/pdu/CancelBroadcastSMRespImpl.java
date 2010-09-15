package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.CancelBroadcastSMResp;
import net.java.slee.resources.smpp.pdu.Tag;

import org.mobicents.protocols.smpp.message.SMPPPacket;
import org.mobicents.slee.resources.smpp.ExtSmppResponse;

/**
 * 
 * @author amit bhayani
 *
 */
public class CancelBroadcastSMRespImpl extends PDUImpl implements CancelBroadcastSMResp, ExtSmppResponse {

	public CancelBroadcastSMRespImpl(org.mobicents.protocols.smpp.message.CancelBroadcastSMResp cancelSMResp) {
		this.smppPacket = cancelSMResp;
	}

	public CancelBroadcastSMRespImpl(int commandStatus) {
		this.smppPacket = new org.mobicents.protocols.smpp.message.CancelBroadcastSMResp();
		this.smppPacket.setCommandStatus(commandStatus);
	}

	public boolean isTLVPermitted(Tag tag) {
		return false;
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

}
