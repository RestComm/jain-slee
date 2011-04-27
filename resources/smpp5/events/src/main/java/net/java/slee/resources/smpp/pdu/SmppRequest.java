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
 * 
 * @author amit bhayani
 *
 */
public interface SmppRequest extends PDU {

	public static final int BIND_RECEIVER = 0x00000001;
	public static final int BIND_TRANSMITTER = 0x00000002;
	public static final int QUERY_SM = 0x00000003;
	public static final int SUBMIT_SM = 0x00000004;
	public static final int DELIVER_SM = 0x00000005;
	public static final int UNBIND = 0x00000006;
	public static final int REPLACE_SM = 0x00000007;
	public static final int CANCEL_SM = 0x00000008;
	public static final int BIND_TRANSCEIVER = 0x00000009;
	public static final int OUTBIND = 0x0000000B;
	public static final int ENQUIRE_LINK = 0x00000015;
	public static final int SUBMIT_MULTI = 0x00000021;
	public static final int ALERT_NOTIFICATION = 0x00000102;
	public static final int DATA_SM = 0x00000103;
	public static final int BROADCAST_SM = 0x00000111;
	public static final int QUERY_BROADCAST_SM = 0x00000112;
	public static final int CANCEL_BROADCAST_SM = 0x00000113;
	public static final int GENERIC_NACK = 0x80000000;
	public static final int BIND_RECEIVER_RESP = 0x80000001;
	public static final int BIND_TRANSMITTER_RESP = 0x80000002;
	public static final int QUERY_SM_RESP = 0x80000003;
	public static final int SUBMIT_SM_RESP = 0x80000004;
	public static final int DELIVER_SM_RESP = 0x80000005;
	public static final int UNBIND_RESP = 0x80000006;
	public static final int REPLACE_SM_RESP = 0x80000007;
	public static final int CANCEL_SM_RESP = 0x80000008;
	public static final int BIND_TRANSCEIVER_RESP = 0x80000009;
	public static final int ENQUIRY_LINK_RESP = 0x80000015;
	public static final int SUBMIT_MULTI_RESP = 0x80000021;
	public static final int DATA_SM_RESP = 0x80000103;
	public static final int BORADCAST_SM_RESP = 0x80000111;
	public static final int QUERY_BROADCAST_SM_RESP = 0x80000112;
	public static final int CANCEL_BROADCAST_SM_RESP = 0x80000113;

	SmppResponse createSmppResponseEvent(int status);
	
}
