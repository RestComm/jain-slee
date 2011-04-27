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
 * A broadcast ESME, wishing to broadcast a short message, can use this PDU to
 * specify the alias, geographical areas, and text of the short message.
 * 
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface BroadcastSM extends SmppRequest {

	public String getServiceType();

	public void setServiceType(String serviceType);

	public Address getEsmeAddress();

	public void setEsmeAddress(Address address);

	public String getMessageID();

	public void setMessageID(String messageID);

	public int getPriority();

	public void setPriority(int priority);

	public void setScheduleDeliveryTime(SMPPDate time);

	public SMPPDate getScheduleDeliveryTime();

	public SMPPDate getValidityPeriod();

	public void setValidityPeriod(SMPPDate period);

	public int getReplaceIfPresentFlag();

	public void setReplaceIfPresentFlag(int replaceIfPresentFlag);

	public int getDataCoding();

	public void setDataCoding(int dataCoding);

	public int getSmDefaultMsgID();

	public void setSmDefaultMsgID(int smDefaultMsgID);

}
