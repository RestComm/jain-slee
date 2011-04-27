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

import net.java.slee.resources.smpp.util.SMPPDate;

/**
 * <p>
 * The replace_sm PDU is used by an ESME to pass a message_id of a previously
 * submitted message along with several other fields used to update the text,
 * validity period and other attributes of the message.
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface ReplaceSM extends SmppRequest {

	public abstract String getMessageID();

	public abstract void setMessageID(String messageID);

	public abstract Address getSourceAddress();

	public abstract void setSourceAddress(Address address);

	public abstract void setScheduleDeliveryTime(SMPPDate time);

	public abstract SMPPDate getScheduleDeliveryTime();

	public abstract SMPPDate getValidityPeriod();

	public abstract void setValidityPeriod(SMPPDate period);

	public abstract int getRegisteredDelivery();

	public abstract void setRegisteredDelivery(int registeredDelivery);

	public abstract int getSmDefaultMsgID();

	public abstract void setSmDefaultMsgID(int smDefaultMsgID);

	public abstract byte[] getMessage();

	public abstract void setMessage(byte[] message);

}
