/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
