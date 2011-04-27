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

package net.java.slee.resources.smpp.pdu;

import java.util.Collection;

import net.java.slee.resources.smpp.util.SMPPDate;

/**
 * <p>
 * A variation of the submit_sm PDU that supports up to 255 recipients for the
 * given message.
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface SubmitMulti extends SmppRequest {

	public abstract String getServiceType();

	public abstract void setServiceType(String serviceType);

	public abstract Address getSourceAddress();

	public abstract void setSourceAddress(Address address);

	public abstract void addDestAddress(Address address)
			throws SmppTooManyValuesException;

	public abstract boolean removeDestAddress(Address address);

	public abstract Collection<Address> getDestAddresses();

	public abstract void addDistrListName(String distributionListName)
			throws SmppTooManyValuesException;

	public abstract boolean removeDistrListName(String distributionListName);

	public abstract Collection<String> getDistrListNames();

	public abstract int getEsmClass();

	public abstract void setEsmClass(int esmClass);

	public abstract int getProtocolID();

	public abstract void setProtocolID(int protocolID);

	public abstract void setPriority(int priority);

	public abstract int getPriority();

	public abstract void setScheduleDeliveryTime(SMPPDate time);

	public abstract SMPPDate getScheduleDeliveryTime();

	public abstract SMPPDate getValidityPeriod();

	public abstract void setValidityPeriod(SMPPDate period);

	public abstract int getRegisteredDelivery();

	public abstract void setRegisteredDelivery(int registeredDelivery);

	public abstract int getReplaceIfPresentFlag();

	public abstract void setReplaceIfPresentFlag(int replaceIfPresentFlag);

	public abstract int getDataCoding();

	public abstract void setDataCoding(int dataCoding);

	public abstract int getSmDefaultMsgID();

	public abstract void setSmDefaultMsgID(int smDefaultMsgID);

	public abstract byte[] getMessage();

	public abstract void setMessage(byte[] message);

}
