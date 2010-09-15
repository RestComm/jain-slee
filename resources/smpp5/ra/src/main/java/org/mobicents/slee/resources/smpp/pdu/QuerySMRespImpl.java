package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.QuerySMResp;
import net.java.slee.resources.smpp.pdu.Tag;
import net.java.slee.resources.smpp.util.SMPPDate;

import org.mobicents.protocols.smpp.message.SMPPPacket;
import org.mobicents.slee.resources.smpp.ExtSmppResponse;
import org.mobicents.slee.resources.smpp.util.SMPPDateImpl;

/**
 * 
 * @author amit bhayani
 * 
 */
public class QuerySMRespImpl extends PDUImpl implements QuerySMResp, ExtSmppResponse {

	public QuerySMRespImpl(org.mobicents.protocols.smpp.message.QuerySMResp querySMResp) {
		this.smppPacket = querySMResp;
	}

	public QuerySMRespImpl(long sequenceNumber) {
		super();
		this.smppPacket = new org.mobicents.protocols.smpp.message.QuerySMResp();
		this.smppPacket.setSequenceNum(sequenceNumber);
	}

	public int getErrorCode() {
		return ((org.mobicents.protocols.smpp.message.QuerySMResp) this.smppPacket).getErrorCode();
	}

	public SMPPDate getFinalDate() {
		return this.convertProtoDate(((org.mobicents.protocols.smpp.message.QuerySMResp) this.smppPacket)
				.getFinalDate());
	}

	public String getMessageID() {
		return ((org.mobicents.protocols.smpp.message.QuerySMResp) this.smppPacket).getMessageId();
	}

	public int getMessageState() {
		return ((org.mobicents.protocols.smpp.message.QuerySMResp) this.smppPacket).getMessageState().getValue();
	}

	public void setErrorCode(int errorCode) {
		((org.mobicents.protocols.smpp.message.QuerySMResp) this.smppPacket).setErrorCode(errorCode);
	}

	public void setFinalDate(SMPPDate date) {
		((org.mobicents.protocols.smpp.message.QuerySMResp) this.smppPacket)
				.setFinalDate(date != null ? ((SMPPDateImpl) date).getSMPPDate() : null);
	}

	public void setMessageID(String messageID) {
		((org.mobicents.protocols.smpp.message.QuerySMResp) this.smppPacket).setMessageId(messageID);
	}

	public void setMessageState(int messageState) {
		((org.mobicents.protocols.smpp.message.QuerySMResp) this.smppPacket)
				.setMessageState(org.mobicents.protocols.smpp.message.MessageState.getMessageState(messageState));
	}

	public boolean isTLVPermitted(Tag tag) {
		return false;
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

}
