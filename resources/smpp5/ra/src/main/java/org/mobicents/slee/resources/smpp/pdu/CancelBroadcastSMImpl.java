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

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.CancelBroadcastSM;
import net.java.slee.resources.smpp.pdu.SmppResponse;
import net.java.slee.resources.smpp.pdu.Tag;

import org.mobicents.protocols.smpp.message.SMPPPacket;
import org.mobicents.slee.resources.smpp.ExtSmppRequest;

/**
 * 
 * @author amit bhayani
 * 
 */
public class CancelBroadcastSMImpl extends PDUImpl implements ExtSmppRequest, CancelBroadcastSM {

	public CancelBroadcastSMImpl(org.mobicents.protocols.smpp.message.CancelBroadcastSM cancelSM) {
		this.smppPacket = cancelSM;
	}

	public CancelBroadcastSMImpl(long sequenceNumber) {
		super();
		this.smppPacket = new org.mobicents.protocols.smpp.message.CancelBroadcastSM();
		this.smppPacket.setSequenceNum(sequenceNumber);
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

	public SmppResponse createSmppResponseEvent(int status) {
		CancelBroadcastSMRespImpl cancelBroadcastSMRespImpl = new CancelBroadcastSMRespImpl(status);
		cancelBroadcastSMRespImpl.setSequenceNum(this.getSequenceNum());
		return cancelBroadcastSMRespImpl;
	}

	public boolean isTLVPermitted(Tag tag) {
		return false;
	}

	public Address getEsmeAddress() {
		return this.convertProtoAddress(((org.mobicents.protocols.smpp.message.CancelBroadcastSM) this.smppPacket)
				.getSource());
	}

	public String getMessageID() {
		return ((org.mobicents.protocols.smpp.message.CancelBroadcastSM) this.smppPacket).getMessageId();
	}

	public String getServiceType() {
		return ((org.mobicents.protocols.smpp.message.CancelBroadcastSM) this.smppPacket).getServiceType();
	}

	public void setEsmeAddress(Address address) {
		((org.mobicents.protocols.smpp.message.CancelBroadcastSM) this.smppPacket)
				.setSource(address != null ? ((AddressImpl) address).getProtoAddress() : null);
	}

	public void setMessageID(String messageID) {
		((org.mobicents.protocols.smpp.message.CancelBroadcastSM) this.smppPacket).setMessageId(messageID);
	}

	public void setServiceType(String serviceType) {
		((org.mobicents.protocols.smpp.message.CancelBroadcastSM) this.smppPacket).setServiceType(serviceType);
	}

}
