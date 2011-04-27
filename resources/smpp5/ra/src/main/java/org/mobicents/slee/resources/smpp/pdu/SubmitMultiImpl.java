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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.SmppResponse;
import net.java.slee.resources.smpp.pdu.SmppTooManyValuesException;
import net.java.slee.resources.smpp.pdu.SubmitMulti;
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
public class SubmitMultiImpl extends PDUImpl implements SubmitMulti, ExtSmppRequest {

	private static final int MAX_DEST_ADDRESS_ALLOWED = 254;

	public SubmitMultiImpl(org.mobicents.protocols.smpp.message.SubmitMulti protoSubmitMulti) {
		this.smppPacket = protoSubmitMulti;
	}

	public SubmitMultiImpl(long sequenceNumber) {
		super();
		this.smppPacket = new org.mobicents.protocols.smpp.message.SubmitMulti();
		this.smppPacket.setSequenceNum(sequenceNumber);
	}

	public void addDestAddress(Address address) throws SmppTooManyValuesException {
		if (((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getDestinationTable().getLength() == MAX_DEST_ADDRESS_ALLOWED) {
			throw new SmppTooManyValuesException("Maximum allowed destinations are 254");
		} else if (address != null) {
			((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getDestinationTable().add(
					((AddressImpl) address).getProtoAddress());
		}
	}

	public void addDistrListName(String distributionListName) throws SmppTooManyValuesException {
		if (((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getDestinationTable().getLength() == MAX_DEST_ADDRESS_ALLOWED) {
			throw new SmppTooManyValuesException("Maximum allowed destinations are 254");
		} else if (distributionListName != null) {
			((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getDestinationTable().add(
					distributionListName);
		}
	}

	public int getDataCoding() {
		return ((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getDataCoding();
	}

	public Collection<Address> getDestAddresses() {
		List<Address> addresses = new ArrayList<Address>();
		for (org.mobicents.protocols.smpp.Address protoAdd : ((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket)
				.getDestinationTable().getAddresses()) {
			addresses.add(new AddressImpl(protoAdd));
		}

		return Collections.unmodifiableCollection(addresses);
	}

	public Collection<String> getDistrListNames() {
		return ((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getDestinationTable()
				.getDistributionLists();
	}

	public int getEsmClass() {
		return ((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getEsmClass();
	}

	public Address getSourceAddress() {
		return this.convertProtoAddress(((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket)
				.getSource());
	}

	public byte[] getMessage() {
		return ((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getMessage();
	}

	public int getPriority() {
		return ((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getPriority();
	}

	public int getProtocolID() {
		return ((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getProtocolID();
	}

	public int getRegisteredDelivery() {
		return ((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getRegistered();
	}

	public int getReplaceIfPresentFlag() {
		return ((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getReplaceIfPresent();
	}

	public SMPPDate getScheduleDeliveryTime() {
		return this.convertProtoDate(((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket)
				.getDeliveryTime());
	}

	public String getServiceType() {
		return ((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getServiceType();
	}

	public int getSmDefaultMsgID() {
		return ((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getDefaultMsg();
	}

	public SMPPDate getValidityPeriod() {
		return this.convertProtoDate(((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket)
				.getExpiryTime());
	}

	public boolean removeDestAddress(Address address) {

		if (address != null) {
			int origLength = ((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getDestinationTable()
					.getLength();
			((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getDestinationTable().remove(
					((AddressImpl) address).getProtoAddress());
			int newLength = ((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getDestinationTable()
					.getLength();
			if (origLength == newLength + 1) {
				return true;
			}
		}

		return false;
	}

	public boolean removeDistrListName(String distributionListName) {

		if (distributionListName != null) {
			int origLength = ((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getDestinationTable()
					.getLength();
			((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getDestinationTable().remove(
					distributionListName);
			int newLength = ((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).getDestinationTable()
					.getLength();
			if (origLength == newLength + 1) {
				return true;
			}
		}
		return false;
	}

	public void setDataCoding(int dataCoding) {
		((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).setDataCoding(dataCoding);
	}

	public void setEsmClass(int esmClass) {
		((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).setEsmClass(esmClass);

	}

	public void setSourceAddress(Address address) {
		((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).setSource(((AddressImpl) address)
				.getProtoAddress());
	}

	public void setMessage(byte[] message) {
		((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).setMessage(message);
	}

	public void setPriority(int priority) {
		((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).setPriority(priority);
	}

	public void setProtocolID(int protocolID) {
		((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).setProtocolID(protocolID);
	}

	public void setRegisteredDelivery(int registeredDelivery) {
		((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).setRegistered(registeredDelivery);
	}

	public void setReplaceIfPresentFlag(int replaceIfPresentFlag) {
		((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).setReplaceIfPresent(replaceIfPresentFlag);
	}

	public void setScheduleDeliveryTime(SMPPDate time) {
		((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket)
				.setDeliveryTime(time != null ? ((SMPPDateImpl) time).getSMPPDate() : null);
	}

	public void setServiceType(String serviceType) {
		((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).setServiceType(serviceType);
	}

	public void setSmDefaultMsgID(int smDefaultMsgID) {
		((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket).setDefaultMsg(smDefaultMsgID);
	}

	public void setValidityPeriod(SMPPDate period) {
		((org.mobicents.protocols.smpp.message.SubmitMulti) this.smppPacket)
				.setExpiryTime(period != null ? ((SMPPDateImpl) period).getSMPPDate() : null);
	}

	public SmppResponse createSmppResponseEvent(int status) {
		SubmitMultiRespImpl submitMultiRespImpl = new SubmitMultiRespImpl(status);
		submitMultiRespImpl.setSequenceNum(this.getSequenceNum());
		return submitMultiRespImpl;
	}

	public boolean isTLVPermitted(Tag tag) {
		return (tag.equals(Tag.ALERT_ON_MESSAGE_DELIVERY) || tag.equals(Tag.BILLING_IDENTIFICATION)
				|| tag.equals(Tag.CALLBACK_NUM) || tag.equals(Tag.CALLBACK_NUM_ATAG)
				|| tag.equals(Tag.CALLBACK_NUM_PRES_IND) || tag.equals(Tag.DEST_ADDR_NP_COUNTRY)
				|| tag.equals(Tag.DEST_ADDR_NP_INFORMATION) || tag.equals(Tag.DEST_ADDR_NP_RESOLUTION)
				|| tag.equals(Tag.DEST_ADDR_SUBUNIT) || tag.equals(Tag.DEST_BEARER_TYPE)
				|| tag.equals(Tag.DEST_NETWORK_ID) || tag.equals(Tag.DEST_NETWORK_TYPE) || tag.equals(Tag.DEST_NODE_ID)
				|| tag.equals(Tag.DEST_SUBADDRESS) || tag.equals(Tag.DEST_TELEMATICS_ID) || tag.equals(Tag.DEST_PORT)
				|| tag.equals(Tag.DISPLAY_TIME) || tag.equals(Tag.ITS_REPLY_TYPE) || tag.equals(Tag.ITS_SESSION_INFO)
				|| tag.equals(Tag.LANGUAGE_INDICATOR) || tag.equals(Tag.MESSAGE_PAYLOAD)
				|| tag.equals(Tag.MORE_MESSAGES_TO_SEND) || tag.equals(Tag.MS_MSG_WAIT_FACILITIES)
				|| tag.equals(Tag.MS_VALIDITY) || tag.equals(Tag.NUMBER_OF_MESSAGES) || tag.equals(Tag.PAYLOAD_TYPE)
				|| tag.equals(Tag.PRIVACY_INDICATOR) || tag.equals(Tag.QOS_TIME_TO_LIVE)
				|| tag.equals(Tag.SAR_MSG_REF_NUM) || tag.equals(Tag.SAR_SEGMENT_SEQNUM)
				|| tag.equals(Tag.SAR_TOTAL_SEGMENTS) || tag.equals(Tag.SET_DPF) || tag.equals(Tag.SMS_SIGNAL)
				|| tag.equals(Tag.SOURCE_ADDR_SUBUNIT) || tag.equals(Tag.SOURCE_BEARER_TYPE)
				|| tag.equals(Tag.SOURCE_NETWORK_ID) || tag.equals(Tag.SOURCE_NETWORK_TYPE)
				|| tag.equals(Tag.SOURCE_NODE_ID) || tag.equals(Tag.SOURCE_PORT) || tag.equals(Tag.SOURCE_SUBADDRESS)
				|| tag.equals(Tag.SOURCE_TELEMATICS_ID) || tag.equals(Tag.USER_MESSAGE_REFERENCE)
				|| tag.equals(Tag.USER_RESPONSE_CODE) || tag.equals(Tag.USSD_SERVICE_OP));
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

}
