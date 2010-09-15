package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.SubmitSMResp;
import net.java.slee.resources.smpp.pdu.Tag;

import org.mobicents.protocols.smpp.message.SMPPPacket;
import org.mobicents.slee.resources.smpp.ExtSmppResponse;

/**
 * 
 * @author amit bhayani
 * 
 */
public class SubmitSMRespImpl extends PDUImpl implements SubmitSMResp, ExtSmppResponse {

	public SubmitSMRespImpl(org.mobicents.protocols.smpp.message.SubmitSMResp submitSMResp) {
		this.smppPacket = submitSMResp;
	}

	public SubmitSMRespImpl(int commandStatus) {
		this.smppPacket = new org.mobicents.protocols.smpp.message.SubmitSMResp();
		this.smppPacket.setCommandStatus(commandStatus);
	}

	public String getMessageID() {
		return ((org.mobicents.protocols.smpp.message.SubmitSMResp) this.smppPacket).getMessageId();
	}

	public void setMessageID(String messageID) {
		((org.mobicents.protocols.smpp.message.SubmitSMResp) this.smppPacket).setMessageId(messageID);
	}

	public boolean isTLVPermitted(Tag tag) {
		return (tag.equals(Tag.ADDITIONAL_STATUS_INFO_TEXT) || tag.equals(Tag.DELIVERY_FAILURE_REASON)
				|| tag.equals(Tag.DPF_RESULT) || tag.equals(Tag.NETWORK_ERROR_CODE));
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

}
