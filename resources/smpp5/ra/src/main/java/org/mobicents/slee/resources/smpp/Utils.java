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

package org.mobicents.slee.resources.smpp;

import net.java.slee.resources.smpp.SmppTransaction;
import net.java.slee.resources.smpp.pdu.SmppRequest;

import org.mobicents.slee.resources.smpp.pdu.AlertNotificationImpl;
import org.mobicents.slee.resources.smpp.pdu.CancelSMImpl;
import org.mobicents.slee.resources.smpp.pdu.DataSMImpl;
import org.mobicents.slee.resources.smpp.pdu.DeliverSMImpl;
import org.mobicents.slee.resources.smpp.pdu.QuerySMImpl;
import org.mobicents.slee.resources.smpp.pdu.ReplaceSMImpl;
import org.mobicents.slee.resources.smpp.pdu.SubmitMultiImpl;
import org.mobicents.slee.resources.smpp.pdu.SubmitSMImpl;

/**
 * 
 * @author amit bhayani
 * 
 */
public class Utils {

	/**
	 * Define Events
	 */
	protected static final String ALERT_NOTIFICATION = "net.java.slee.resources.smpp.ALERT_NOTIFICATION";
	protected static final String GENERIC_NACK = "net.java.slee.resources.smpp.GENERIC_NACK";
	protected static final String DELIVER_SM = "net.java.slee.resources.smpp.DELIVER_SM";
	protected static final String DELIVER_SM_RESP = "net.java.slee.resources.smpp.DELIVER_SM_RESP";
	protected static final String DELIVERY_REPORT = "net.java.slee.resources.smpp.DELIVERY_REPORT";
	protected static final String SUBMIT_SM = "net.java.slee.resources.smpp.SUBMIT_SM";
	protected static final String SUBMIT_SM_RESP = "net.java.slee.resources.smpp.SUBMIT_SM_RESP";
	protected static final String DATA_SM = "net.java.slee.resources.smpp.DATA_SM";
	protected static final String DATA_SM_RESP = "net.java.slee.resources.smpp.DATA_SM_RESP";
	protected static final String SUBMIT_MULTI = "net.java.slee.resources.smpp.SUBMIT_MULTI";
	protected static final String SUBMIT_MULTI_RESP = "net.java.slee.resources.smpp.SUBMIT_MULTI_RESP";
	protected static final String QUERY_SM = "net.java.slee.resources.smpp.QUERY_SM";
	protected static final String QUERY_SM_RESP = "net.java.slee.resources.smpp.QUERY_SM_RESP";
	protected static final String CANCEL_SM = "net.java.slee.resources.smpp.CANCEL_SM";
	protected static final String CANCEL_SM_RESP = "net.java.slee.resources.smpp.CANCEL_SM_RESP";
	protected static final String REPLACE_SM = "net.java.slee.resources.smpp.REPLACE_SM";
	protected static final String REPLACE_SM_RESP = "net.java.slee.resources.smpp.REPLACE_SM_RESP";
	protected static final String SMPP_TIMEOUT_RESPONSE_SENT = "net.java.slee.resources.smpp.SMPP_TIMEOUT_RESPONSE_SENT";
	protected static final String SMPP_TIMEOUT_RESPONSE_RECEIVED = "net.java.slee.resources.smpp.SMPP_TIMEOUT_RESPONSE_RECEIVED";

	protected static final String BROADCAST_SM = "net.java.slee.resources.smpp.BROADCAST_SM";
	protected static final String BROADCAST_SM_RESP = "net.java.slee.resources.smpp.BROADCAST_SM_RESP";
	protected static final String CANCEL_BROADCAST_SM = "net.java.slee.resources.smpp.CANCEL_BROADCAST_SM";
	protected static final String CANCEL_BROADCAST_SM_RESP = "net.java.slee.resources.smpp.CANCEL_BROADCAST_SM_RESP";
	protected static final String QUERY_BROADCAST_SM = "net.java.slee.resources.smpp.QUERY_BROADCAST_SM";
	protected static final String QUERY_BROADCAST_SM_RESP = "net.java.slee.resources.smpp.QUERY_BROADCAST_SM_RESP";

	private SmppResourceAdaptor smppResourceAdaptor;

	protected Utils(SmppResourceAdaptor smppResourceAdaptor) {
		this.smppResourceAdaptor = smppResourceAdaptor;
	}

	protected String getAddressForEventFire(SmppRequest smppRequest) {
		long commandId = smppRequest.getCommandId();
		if (commandId == SmppRequest.ALERT_NOTIFICATION) {
			return ((AlertNotificationImpl) smppRequest).getEsmeAddress().getAddress();
		} else if (commandId == SmppRequest.CANCEL_SM) {
			return ((CancelSMImpl) smppRequest).getSourceAddress().getAddress();
		} else if (commandId == SmppRequest.DATA_SM) {
			return ((DataSMImpl) smppRequest).getSourceAddress().getAddress();
		} else if (commandId == SmppRequest.DELIVER_SM) {
			// For Deliver_SM, the address would be destination Address
			return ((DeliverSMImpl) smppRequest).getDestAddress().getAddress();
		} else if (commandId == SmppRequest.QUERY_SM) {
			return ((QuerySMImpl) smppRequest).getSourceAddress().getAddress();
		} else if (commandId == SmppRequest.REPLACE_SM) {
			return ((ReplaceSMImpl) smppRequest).getSourceAddress().getAddress();
		} else if (commandId == SmppRequest.SUBMIT_MULTI) {
			return ((SubmitMultiImpl) smppRequest).getSourceAddress().getAddress();
		} else if (commandId == SmppRequest.SUBMIT_SM) {
			return ((SubmitSMImpl) smppRequest).getSourceAddress().getAddress();
		}
		return null;
	}

	protected String statusMessage(int status) {
		switch (status) {
		case SmppTransaction.ESME_ROK:
			return "No Error";
		case SmppTransaction.ESME_RINVMSGLEN:
			return "Message Length is invalid";
		case SmppTransaction.ESME_RINVCMDLEN:
			return "Command Length is invalid";
		case SmppTransaction.ESME_RINVCMDID:
			return "Invalid Command ID";
		case SmppTransaction.ESME_RINVBNDSTS:
			return "Incorrect BIND Status for given command";
		case SmppTransaction.ESME_RALYBND:
			return "ESME Already in Bound State";
		case SmppTransaction.ESME_RINVPRTFLG:
			return "Invalid Priority Flag";
		case SmppTransaction.ESME_RINVREGDLVFLG:
			return "Invalid Registered Delivery Flag";
		case SmppTransaction.ESME_RSYSERR:
			return "System Error";
		case SmppTransaction.ESME_RINVSRCADR:
			return "Invalid Source Address";
		case SmppTransaction.ESME_RINVDSTADR:
			return "Invalid Dest Addr";
		case SmppTransaction.ESME_RINVMSGID:
			return "Message ID is invalid";
		case SmppTransaction.ESME_RBINDFAIL:
			return "Bind Failed";
		case SmppTransaction.ESME_RINVPASWD:
			return "Invalid Password";
		case SmppTransaction.ESME_RINVSYSID:
			return "Invalid System ID";
		case SmppTransaction.ESME_RCANCELFAIL:
			return "Cancel SM Failed";
		case SmppTransaction.ESME_RREPLACEFAIL:
			return "Replace SM Failed";
		case SmppTransaction.ESME_RMSGQFUL:
			return "Message Queue Full";
		case SmppTransaction.ESME_RINVSERTYP:
			return "Invalid Service Type";
		case SmppTransaction.ESME_RINVNUMDESTS:
			return "Invalid number of destinations";
		case SmppTransaction.ESME_RINVDLNAME:
			return "Invalid Distribution List name";
		case SmppTransaction.ESME_RINVDESTFLAG:
			return "Destination flag is invalid (submit_multi)";
		case SmppTransaction.ESME_RINVSUBREP:
			return "Invalid ‘submit with replace’ request (i.e. submit_sm with replace_if_present_flag set)";
		case SmppTransaction.ESME_RINVESMCLASS:
			return "Invalid esm_class field data";
		case SmppTransaction.ESME_RCNTSUBDL:
			return "Cannot Submit to Distribution List";
		case SmppTransaction.ESME_RSUBMITFAIL:
			return "submit_sm or submit_multi failed";
		case SmppTransaction.ESME_RINVSRCTON:
			return "Invalid Source address TON";
		case SmppTransaction.ESME_RINVSRCNPI:
			return "Invalid Source address NPI";
		case SmppTransaction.ESME_RINVDSTTON:
			return "Invalid Destination address TON";
		case SmppTransaction.ESME_RINVDSTNPI:
			return "Invalid Destination address NPI";
		case SmppTransaction.ESME_RINVSYSTYP:
			return "Invalid system_type field";
		case SmppTransaction.ESME_RINVREPFLAG:
			return "Invalid replace_if_present flag";
		case SmppTransaction.ESME_RINVNUMMSGS:
			return "Invalid number of messages";
		case SmppTransaction.ESME_RTHROTTLED:
			return "Throttling error (ESME has exceeded allowed message limits)";
		case SmppTransaction.ESME_RINVSCHED:
			return "Invalid Scheduled Delivery Time";
		case SmppTransaction.ESME_RINVEXPIRY:
			return "Invalid message validity period (Expiry time)";
		case SmppTransaction.ESME_RINVDFTMSGID:
			return "Predefined Message Invalid or Not Found";
		case SmppTransaction.ESME_RX_T_APPN:
			return "ESME Receiver Temporary App Error Code";
		case SmppTransaction.ESME_RX_P_APPN:
			return "ESME Receiver Permanent App Error Code";
		case SmppTransaction.ESME_RX_R_APPN:
			return "ESME Receiver Reject Message Error Code";
		case SmppTransaction.ESME_RQUERYFAIL:
			return "query_sm request failed";
		case SmppTransaction.ESME_RDELIVERYFAILURE:
			return "Delivery   Failure (used for data_sm_resp)";
		case SmppTransaction.ESME_RUNKNOWNERR:
			return "Unknown Error";
		case -1:
			return "Some exception occured. Look at log files";
		}
		return "Unknonw ststus code " + status;
	}

}
