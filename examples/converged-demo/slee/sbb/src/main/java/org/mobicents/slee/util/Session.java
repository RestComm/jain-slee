
 /*
  * Mobicents: The Open Source SLEE Platform      
  *
  * Copyright 2003-2005, CocoonHive, LLC., 
  * and individual contributors as indicated
  * by the @authors tag. See the copyright.txt 
  * in the distribution for a full listing of   
  * individual contributors.
  *
  * This is free software; you can redistribute it
  * and/or modify it under the terms of the 
  * GNU Lesser General Public License as
  * published by the Free Software Foundation; 
  * either version 2.1 of
  * the License, or (at your option) any later version.
  *
  * This software is distributed in the hope that 
  * it will be useful, but WITHOUT ANY WARRANTY; 
  * without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
  * PURPOSE. See the GNU Lesser General Public License
  * for more details.
  *
  * You should have received a copy of the 
  * GNU Lesser General Public
  * License along with this software; 
  * if not, write to the Free
  * Software Foundation, Inc., 51 Franklin St, 
  * Fifth Floor, Boston, MA
  * 02110-1301 USA, or see the FSF site:
  * http://www.fsf.org.
  */
package org.mobicents.slee.util;

import java.io.Serializable;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.address.Address;

public class Session implements Serializable {

	private static final long serialVersionUID = 3546082466839474743L;

	private String callId;
	/**
	 * This member is used to store the dialog corresponding to the session.
	 * 
	 */
	private Dialog dialog;
	/**
	 * This is the SDP that is obtained from the peer sip user agent. <br><br>
	 * Use this data e.g. to send an ACK with sdp. 
	 */
	//TODO Remove this member if not needed in the advanced call flow. It is probably 
	// needed in the simple call flow.
	private byte[] peerSDP;

	private Address sipAddress;
	
	private ClientTransaction toBeCancelledClientTransaction;
	

	public Session() {
	}

	public Session(String callId) {
		this.callId = callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public byte[] getPeerSDP() {
		return peerSDP;
	}

	public void setPeerSDP(byte[] peerSDP) {
		this.peerSDP = peerSDP;
	}

	public String getCallId() {
		return callId;
	}

	public Address getSipAddress() {
		return sipAddress;
	}

	public void setSipAddress(Address sipAddress) {
		this.sipAddress = sipAddress;
	}


	public Dialog getDialog() {
		return dialog;
	}

	public void setDialog(Dialog dialog) {
//		if(this.dialog == null)
			this.dialog = dialog;
	}

	public ClientTransaction getToBeCancelledClientTransaction() {
		return toBeCancelledClientTransaction;
	}

	public void setToBeCancelledClientTransaction(
			ClientTransaction toBeCancelledClientTransaction) {
		this.toBeCancelledClientTransaction = toBeCancelledClientTransaction;
	}
	
	

	/*public String toString() {
		return "Session==[callId:" + callId + ", peerSDP:" + peerSDP
				+ ", SipAddress:" + sipAddress + "]";
	}*/

}
