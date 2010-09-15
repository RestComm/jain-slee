package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.ReplaceSMResp;
import net.java.slee.resources.smpp.pdu.Tag;

import org.mobicents.protocols.smpp.message.SMPPPacket;
import org.mobicents.slee.resources.smpp.ExtSmppResponse;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ReplaceSMRespImpl extends PDUImpl implements ReplaceSMResp, ExtSmppResponse {

	public ReplaceSMRespImpl(org.mobicents.protocols.smpp.message.ReplaceSMResp replaceSMResp) {
		this.smppPacket = replaceSMResp;
	}

	public ReplaceSMRespImpl(int commandStatus) {
		this.smppPacket = new org.mobicents.protocols.smpp.message.ReplaceSMResp();
		this.smppPacket.setCommandStatus(commandStatus);
	}

	public boolean isTLVPermitted(Tag tag) {
		return false;
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

}
