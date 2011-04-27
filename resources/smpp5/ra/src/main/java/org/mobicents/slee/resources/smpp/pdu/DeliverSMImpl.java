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
import net.java.slee.resources.smpp.pdu.DeliverSM;
import net.java.slee.resources.smpp.pdu.SmppResponse;
import net.java.slee.resources.smpp.pdu.Tag;
import net.java.slee.resources.smpp.util.SMPPDate;

import org.mobicents.protocols.smpp.message.SMPPPacket;
import org.mobicents.slee.resources.smpp.ExtSmppRequest;
import org.mobicents.slee.resources.smpp.util.SMPPDateImpl;

/**
 * 
 * @author amit bhayani
 * 
 */
public class DeliverSMImpl extends PDUImpl implements DeliverSM, ExtSmppRequest {

	public DeliverSMImpl(org.mobicents.protocols.smpp.message.DeliverSM submitSM) {
		this.smppPacket = submitSM;
	}

	public DeliverSMImpl(long sequenceNumber) {
		super();
		this.smppPacket = new org.mobicents.protocols.smpp.message.DeliverSM();
		this.smppPacket.setSequenceNum(sequenceNumber);
	}

	public int getDataCoding() {
		return ((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).getDataCoding();
	}

	public int getEsmClass() {
		return ((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).getEsmClass();
	}

	public byte[] getMessage() {
		return ((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).getMessage();
	}

	public int getPriority() {
		return ((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).getPriority();
	}

	public int getProtocolID() {
		return ((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).getProtocolID();
	}

	public int getRegisteredDelivery() {
		return ((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).getRegistered();
	}

	public int getReplaceIfPresentFlag() {
		return ((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).getReplaceIfPresent();
	}

	public SMPPDate getScheduleDeliveryTime() {
		return this.convertProtoDate(((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket)
				.getDeliveryTime());
	}

	public String getServiceType() {
		return ((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).getServiceType();
	}

	public int getSmDefaultMsgID() {
		return ((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).getDefaultMsg();
	}

	public Address getSourceAddress() {
		return this.convertProtoAddress(((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).getSource());
	}

	public SMPPDate getValidityPeriod() {
		return this
				.convertProtoDate(((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).getExpiryTime());
	}

	public void setDataCoding(int dataCoding) {
		((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).setDataCoding(dataCoding);
	}

	public void setEsmClass(int esmClass) {
		((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).setEsmClass(esmClass);
	}

	public void setDestAddress(Address address) {
		if (address != null) {
			((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).setDestination(((AddressImpl) address)
					.getProtoAddress());
		} else {
			((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).setDestination(null);
		}
	}

	public Address getDestAddress() {
		return this.convertProtoAddress(((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket)
				.getDestination());
	}

	public void setMessage(byte[] message) {
		((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).setMessage(message);
	}

	public void setPriority(int priority) {
		((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).setPriority(priority);
	}

	public void setProtocolID(int protocolID) {
		((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).setProtocolID(protocolID);
	}

	public void setRegisteredDelivery(int registeredDelivery) {
		((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).setRegistered(registeredDelivery);
	}

	public void setReplaceIfPresentFlag(int replaceIfPresentFlag) {
		((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).setReplaceIfPresent(replaceIfPresentFlag);
	}

	public void setScheduleDeliveryTime(SMPPDate time) {
		if (time != null) {
			((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).setDeliveryTime(((SMPPDateImpl) time)
					.getSMPPDate());
		} else {
			((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).setDeliveryTime(null);
		}
	}

	public void setServiceType(String serviceType) {
		((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).setServiceType(serviceType);
	}

	public void setSmDefaultMsgID(int smDefaultMsgID) {
		((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).setDefaultMsg(smDefaultMsgID);
	}

	public void setSourceAddress(Address address) {
		if (address != null) {
			((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).setSource(((AddressImpl) address)
					.getProtoAddress());
		} else {
			((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).setSource(null);
		}
	}

	public void setValidityPeriod(SMPPDate period) {
		if (period != null) {
			((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).setExpiryTime(((SMPPDateImpl) period)
					.getSMPPDate());
		} else {
			((org.mobicents.protocols.smpp.message.DeliverSM) this.smppPacket).setExpiryTime(null);
		}
	}

	public SmppResponse createSmppResponseEvent(int status) {
		DeliverSMRespImpl deliverSMRespImpl = new DeliverSMRespImpl(status);
		deliverSMRespImpl.setSequenceNum(this.getSequenceNum());
		return deliverSMRespImpl;
	}

	public boolean isTLVPermitted(Tag tag) {
		return (tag.equals(Tag.CALLBACK_NUM) || tag.equals(Tag.CALLBACK_NUM_ATAG)
				|| tag.equals(Tag.CALLBACK_NUM_PRES_IND) || tag.equals(Tag.DEST_ADDR_NP_COUNTRY)
				|| tag.equals(Tag.DEST_ADDR_NP_INFORMATION) || tag.equals(Tag.DEST_ADDR_NP_RESOLUTION)
				|| tag.equals(Tag.DEST_ADDR_SUBUNIT) || tag.equals(Tag.DEST_NETWORK_ID) || tag.equals(Tag.DEST_NODE_ID)
				|| tag.equals(Tag.DEST_SUBADDRESS) || tag.equals(Tag.DEST_PORT)

				|| tag.equals(Tag.DPF_RESULT) || tag.equals(Tag.ITS_REPLY_TYPE) || tag.equals(Tag.ITS_SESSION_INFO)
				|| tag.equals(Tag.LANGUAGE_INDICATOR) || tag.equals(Tag.MESSAGE_PAYLOAD)
				|| tag.equals(Tag.MESSAGE_STATE) || tag.equals(Tag.NETWORK_ERROR_CODE) || tag.equals(Tag.PAYLOAD_TYPE)
				|| tag.equals(Tag.PRIVACY_INDICATOR) || tag.equals(Tag.RECEIPTED_MESSAGE_ID)
				|| tag.equals(Tag.SAR_MSG_REF_NUM) || tag.equals(Tag.SAR_SEGMENT_SEQNUM)
				|| tag.equals(Tag.SAR_TOTAL_SEGMENTS) || tag.equals(Tag.SOURCE_ADDR_SUBUNIT)
				|| tag.equals(Tag.SOURCE_NETWORK_ID) || tag.equals(Tag.SOURCE_NODE_ID) || tag.equals(Tag.SOURCE_PORT)
				|| tag.equals(Tag.SOURCE_SUBADDRESS) || tag.equals(Tag.USER_MESSAGE_REFERENCE)
				|| tag.equals(Tag.USER_RESPONSE_CODE) || tag.equals(Tag.USSD_SERVICE_OP));
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

}
