package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.DataSMResp;
import net.java.slee.resources.smpp.pdu.Tag;

import org.mobicents.protocols.smpp.message.SMPPPacket;
import org.mobicents.slee.resources.smpp.ExtSmppResponse;

/**
 * 
 * @author amit bhayani
 * 
 */
public class DataSMRespImpl extends PDUImpl implements DataSMResp, ExtSmppResponse {

	public DataSMRespImpl(org.mobicents.protocols.smpp.message.DataSMResp dataSMResp) {
		this.smppPacket = dataSMResp;
	}

	public DataSMRespImpl(int commandStatus) {
		this.smppPacket = new org.mobicents.protocols.smpp.message.DataSMResp();
		this.smppPacket.setCommandStatus(commandStatus);
	}

	public String getMessageID() {
		return ((org.mobicents.protocols.smpp.message.DataSMResp) this.smppPacket).getMessageId();
	}

	public void setMessageID(String messageID) {
		((org.mobicents.protocols.smpp.message.DataSMResp) this.smppPacket).setMessageId(messageID);
	}

	public boolean isTLVPermitted(Tag tag) {
		return (tag.equals(Tag.ADDITIONAL_STATUS_INFO_TEXT) || tag.equals(Tag.DELIVERY_FAILURE_REASON) || tag
				.equals(Tag.NETWORK_ERROR_CODE));
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

}
