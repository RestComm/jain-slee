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

import net.java.slee.resources.smpp.pdu.DeliverSMResp;
import net.java.slee.resources.smpp.pdu.Tag;

import org.mobicents.protocols.smpp.message.SMPPPacket;
import org.mobicents.slee.resources.smpp.ExtSmppResponse;

/**
 * 
 * @author amit bhayani
 * 
 */
public class DeliverSMRespImpl extends PDUImpl implements DeliverSMResp, ExtSmppResponse {

	public DeliverSMRespImpl(org.mobicents.protocols.smpp.message.DeliverSMResp deliverSMResp) {
		this.smppPacket = deliverSMResp;
	}

	public DeliverSMRespImpl(int commandStatus) {
		this.smppPacket = new org.mobicents.protocols.smpp.message.DeliverSMResp();
		this.smppPacket.setCommandStatus(commandStatus);
	}

	public String getMessageID() {
		return ((org.mobicents.protocols.smpp.message.DeliverSMResp) this.smppPacket).getMessageId();
	}

	public void setMessageID(String messageID) {
		((org.mobicents.protocols.smpp.message.DeliverSMResp) this.smppPacket).setMessageId(messageID);
	}

	public boolean isTLVPermitted(Tag tag) {
		return (tag.equals(Tag.ADDITIONAL_STATUS_INFO_TEXT) || tag.equals(Tag.DELIVERY_FAILURE_REASON) || tag
				.equals(Tag.NETWORK_ERROR_CODE));
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

}
