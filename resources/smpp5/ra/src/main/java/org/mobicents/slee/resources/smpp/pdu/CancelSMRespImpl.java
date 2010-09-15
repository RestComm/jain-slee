package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.CancelSMResp;
import net.java.slee.resources.smpp.pdu.Tag;

import org.mobicents.protocols.smpp.message.SMPPPacket;
import org.mobicents.slee.resources.smpp.ExtSmppResponse;

/**
 * 
 * @author amit bhayani
 * 
 */
public class CancelSMRespImpl extends PDUImpl implements CancelSMResp, ExtSmppResponse {

	public CancelSMRespImpl(org.mobicents.protocols.smpp.message.CancelSMResp cancelSMResp) {
		this.smppPacket = cancelSMResp;
	}

	public CancelSMRespImpl(int commandStatus) {
		this.smppPacket = new org.mobicents.protocols.smpp.message.CancelSMResp();
		this.smppPacket.setCommandStatus(commandStatus);
	}

	public boolean isTLVPermitted(Tag tag) {
		return false;
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

}
