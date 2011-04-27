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
import net.java.slee.resources.smpp.pdu.BroadcastSM;
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
public class BroadcastSMImpl extends PDUImpl implements BroadcastSM, ExtSmppRequest {

	public BroadcastSMImpl(org.mobicents.protocols.smpp.message.BroadcastSM broadcastSM) {
		this.smppPacket = broadcastSM;
	}

	public BroadcastSMImpl(long sequenceNumber) {
		super();
		this.smppPacket = new org.mobicents.protocols.smpp.message.BroadcastSM();
		this.smppPacket.setSequenceNum(sequenceNumber);
	}

	public int getDataCoding() {
		return ((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket).getDataCoding();
	}

	public Address getEsmeAddress() {
		return this.convertProtoAddress(((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket)
				.getSource());
	}

	public String getMessageID() {
		return ((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket).getMessageId();
	}

	public int getPriority() {
		return ((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket).getPriority();
	}

	public int getReplaceIfPresentFlag() {
		return ((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket).getReplaceIfPresent();
	}

	public SMPPDate getScheduleDeliveryTime() {
		return this.convertProtoDate(((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket)
				.getDeliveryTime());
	}

	public String getServiceType() {
		return ((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket).getServiceType();
	}

	public int getSmDefaultMsgID() {
		return ((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket).getDefaultMsg();
	}

	public SMPPDate getValidityPeriod() {
		return this.convertProtoDate(((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket)
				.getExpiryTime());
	}

	public void setDataCoding(int dataCoding) {
		((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket).setDataCoding(dataCoding);
	}

	public void setEsmeAddress(Address address) {
		((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket)
				.setSource(address != null ? ((AddressImpl) address).protoAddress : null);
	}

	public void setMessageID(String messageID) {
		((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket).setMessageId(messageID);
	}

	public void setPriority(int priority) {
		((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket).setPriority(priority);
	}

	public void setReplaceIfPresentFlag(int replaceIfPresentFlag) {
		((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket).setReplaceIfPresent(replaceIfPresentFlag);
	}

	public void setScheduleDeliveryTime(SMPPDate time) {
		((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket)
				.setDeliveryTime(time != null ? ((SMPPDateImpl) time).getSMPPDate() : null);
	}

	public void setServiceType(String serviceType) {
		((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket).setServiceType(serviceType);
	}

	public void setSmDefaultMsgID(int smDefaultMsgID) {
		((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket).setDefaultMsg(smDefaultMsgID);
	}

	public void setValidityPeriod(SMPPDate period) {
		((org.mobicents.protocols.smpp.message.BroadcastSM) this.smppPacket)
				.setExpiryTime(period != null ? ((SMPPDateImpl) period).getSMPPDate() : null);
	}

	public SmppResponse createSmppResponseEvent(int status) {
		BroadcastSMRespImpl broadcastSMRespImpl = new BroadcastSMRespImpl(status);
		broadcastSMRespImpl.setSequenceNum(this.getSequenceNum());
		return broadcastSMRespImpl;
	}

	public boolean isTLVPermitted(Tag tag) {
		return (tag.equals(Tag.BROADCAST_AREA_IDENTIFIER) || tag.equals(Tag.BROADCAST_CONTENT_TYPE)
				|| tag.equals(Tag.BROADCAST_REP_NUM) || tag.equals(Tag.BROADCAST_FREQUENCY_INTERVAL)
				|| tag.equals(Tag.ALERT_ON_MESSAGE_DELIVERY) || tag.equals(Tag.BROADCAST_CHANNEL_INDICATOR)
				|| tag.equals(Tag.BROADCAST_CONTENT_TYPE_INFO) || tag.equals(Tag.BROADCAST_MESSAGE_CLASS)
				|| tag.equals(Tag.BROADCAST_SERVICE_GROUP) || tag.equals(Tag.CALLBACK_NUM)
				|| tag.equals(Tag.CALLBACK_NUM_ATAG) || tag.equals(Tag.CALLBACK_NUM_PRES_IND)
				|| tag.equals(Tag.DEST_ADDR_SUBUNIT) || tag.equals(Tag.DEST_SUBADDRESS) || tag.equals(Tag.DEST_PORT)
				|| tag.equals(Tag.DISPLAY_TIME) || tag.equals(Tag.LANGUAGE_INDICATOR)
				|| tag.equals(Tag.MESSAGE_PAYLOAD) || tag.equals(Tag.MS_VALIDITY) || tag.equals(Tag.PAYLOAD_TYPE)
				|| tag.equals(Tag.PRIVACY_INDICATOR) || tag.equals(Tag.SMS_SIGNAL)
				|| tag.equals(Tag.SOURCE_ADDR_SUBUNIT) || tag.equals(Tag.SOURCE_PORT)
				|| tag.equals(Tag.SOURCE_SUBADDRESS) || tag.equals(Tag.USER_MESSAGE_REFERENCE));
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

}
