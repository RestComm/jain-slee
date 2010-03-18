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

import java.net.URISyntaxException;
import java.text.ParseException;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.ContactHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;

public interface SipUtils {

	/**
	 * Generates a tag to be used e.g. as a from header tag parameter making
	 * up a part of the dialog id.
	 * 
	 * @return Returns a generated tag as a String
	 */
	public String generateTag();

	public Address convertURIToAddress(String uri);

	public SipURI convertURIToSipURI(String uri);
	/**
	 * Returns the Dialog for a RequestEvent.
	 * Use this method e.g. if automatic dialog support is disabled in the sip 
	 * stack that is used.
	 * @param event
	 * @return
	 * @throws SipException
	 */
	public Dialog getDialog(RequestEvent event) throws SipException;
	/**
	 * Returns the Dialog for a ResponseEvent.
	 * Use this method e.g. if automatic dialog support is disabled in the sip 
	 * stack that is used.
	 * @param event
	 * @return
	 * @throws SipException
	 */
	public Dialog getDialog(ResponseEvent event) throws SipException;
	
	/**
	 * Parses a String in the format "sip:user@host" and returns a String array
	 * containing user in index 0 and host in index 1.
	 * 
	 * @param SipURI
	 *            The URI to parse
	 * @return A String array containing the user (index 0) and host (index 1)
	 * @throws URISyntaxException
	 *             If the URI is malformed
	 */
	public String[] parseSipUri(String sipURI) throws URISyntaxException;

	public SipURI convertAddressToSipURI(Address address) throws ParseException;

	/**
	 * Sends a sip CANCEL request in the context of the transaction that is to be cancelled.
	 * 
	 * @param ct The client transaction that was used to send e.g. the initial INVITE that is to be cancelled.
	 * @throws SipException
	 */
	public void sendCancel(ClientTransaction ct) throws SipException;

	/**
	 * Sends an SIP invite request from caller to callee
	 * 
	 * @param fromAddress The originator address 
	 * @param toAddress The destination address
	 * @param content The SDP content (use null if not present)
	 * @param cSeq The sequence number of the INVITE
	 * @return The INVITE request
	 * @throws ParseException If illegal address format
	 * @throws InvalidArgumentException If cSeq invalid number
	 */
	public Request buildInvite(Address fromAddress,
			Address toAddress, byte[] content, int cSeq)
			throws ParseException, InvalidArgumentException;
	
	/**
	 * Build the request associated with the event again but this time include
	 * an authorization header or proxy authorization header based on the Digest
	 * Scheme presented in RFC 2069.
	 * 
	 * @param event
	 *            The event containing the 401 Unauthorized or 407 Proxy
	 *            Authentication Required response and the request that
	 *            generated this response.
	 * @param password
	 *            The password used to authenticate the user
	 * @return The request including authentication.
	 * @throws TransactionUnavailableException 
	 */
	public Request buildRequestWithAuthorizationHeader(
			ResponseEvent event, String password) throws TransactionUnavailableException;
	
	/**
	 * Build an ACK request based on a dialog and an optional content. 
	 * 
	 * @param dialog The dialog to which this ACK should be connected 
	 * @param content The content to append to the ACK Request (use null if no content)
	 * @return The generated ACK Request
	 * @throws SipException
	 */
	public Request buildAck(Dialog dialog, Object content) throws SipException;
	
	
	/**
	 * Add a contact header (which is mandatory for INVITE's). The Contact header
	 * field value contains the URI at which the User Agent would like to
	 * receive requests, and this URI MUST be valid even if used in subsequent
	 * requests outside of any dialogs.
	 * 
	 * @return A Contact Header based on the local ip of the sip stack, the port
	 *         and transport based on the sip providers first listening point.
	 * @throws ParseException
	 */
	public ContactHeader createLocalContactHeader(String user) throws ParseException;
	
	/**
	 * Create a ViaHeader based on the local ip of the sip stack, the port and
	 * transport based on the sip providers first listening point. The Via
	 * header field indicates the transport used for the transaction and
	 * identifies the location where the response is to be sent.
	 * 
	 * @return A new via header
	 * @throws InvalidArgumentException
	 * @throws ParseException
	 */
	public ViaHeader createLocalViaHeader() throws ParseException, InvalidArgumentException;
	
	/**
	 * Sends a sip OK request
	 * 
	 * @param request The request to which the OK is sent
	 * @throws ParseException
	 * @throws SipException
	 */
	public void sendOk(Request request) throws ParseException, SipException;
	
	public void sendStatefulOk(RequestEvent event) throws ParseException, SipException, InvalidArgumentException;
	
}
