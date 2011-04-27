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
 * The MC returns a query_sm_resp PDU as a means of indicating the result of a
 * message query attempt. The PDU will indicate the success or failure of the
 * attempt and for successful attempts will also include the current state of
 * the message.
 * 
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface QuerySMResp extends SmppResponse {

	public abstract String getMessageID();

	public abstract void setMessageID(String messageID);

	public abstract SMPPDate getFinalDate();

	public abstract void setFinalDate(SMPPDate date);

	/**
	 * @see MessageState
	 * @return
	 */
	public abstract int getMessageState();

	public abstract void setMessageState(int messageState);

	public abstract int getErrorCode();

	public abstract void setErrorCode(int errorCode);

}
