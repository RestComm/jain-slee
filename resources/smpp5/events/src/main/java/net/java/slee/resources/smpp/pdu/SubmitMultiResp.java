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

/**
 * <p>
 * The MC response to a submit_multi PDU. This is similar to the submit_sm_resp
 * PDU. The main difference is that where some of the specified recipients were
 * either invalid or rejected by the Message Centre, the PDU can specify the
 * list of failed recipients, appending a specific error code for each one,
 * indicating the reason the recipient was invalid. Also included is a MC
 * message_id that can be used in subsequent operations to query, cancel or
 * replace the contents of an undelivered message.
 * 
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface SubmitMultiResp extends SmppResponse {

	public String getMessageID();

	public void setMessageID(String messageID);

	public int getNumUnsuccess();

	public java.util.List<ErrorAddress> getUnsuccessSME();

	public void addErrorAddress(ErrorAddress errAddress);

}
