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

package net.java.slee.resource.sip;

import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipProvider;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.AddressFactory;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Response;

public interface SleeSipProvider extends SipProvider {

	/**
	 * This method returns an implementation of the AddressFactory interface.
	 * 
	 * @return
	 */
	public AddressFactory getAddressFactory();

	/**
	 * This method returns an implementation of the HeaderFactory interface
	 * 
	 * @return
	 */
	public HeaderFactory getHeaderFactory();

	/**
	 * This method returns an implementation of the MessageFactory interface.
	 * 
	 * @return
	 */
	public MessageFactory getMessageFactory();

	/**
	 * This method creates a new SIP Dialog, starts a new Activity in the SLEE,
	 * and returns the new DialogActivity. The new Dialog is a UAC Dialog. The
	 * parameters specify the local and remote addresses for the Dialog. <br>
	 * First request sent via this activity MUST be dialog creating request,
	 * To/From values are erased and set to those in dialog
	 * 
	 * @param from
	 * @param to
	 * @return
	 * @throws SipException
	 *             if the new Dialog is unable to be created.
	 */
	public DialogActivity getNewDialog(Address from, Address to)
			throws SipException;

	/**
	 * This method creates a new SIP Dialog, starts a new Activity in the SLEE,
	 * and returns the new DialogActivity. The new Dialog is a UAC Dialog. This
	 * method copies the local and remote addresses from incoming Dialog to the
	 * newly created Dialog. It generates a new local tag and the remote tag
	 * will be null. If the useSameCallId parameter is true then the newly
	 * created Dialog will use the Call ID from the incoming Dialog, otherwise a
	 * new Call ID is generated. This method takes the following arguments First
	 * request sent via this activity MUST be dialog creating request.
	 * 
	 * @param incomingDialog -
	 *            UAS dialog
	 * @param useSameCallId -
	 *            if call-id should be reused
	 * @return -new activity
	 * @throws SipException
	 *             if dialog cant be created
	 */
	public DialogActivity getNewDialog(DialogActivity incomingDialog,
			boolean useSameCallId) throws SipException;

	/**
	 * This method returns true if the URI is local to the SIP stack.
	 * 
	 * @param uri
	 * @return
	 */
	public boolean isLocalSipURI(SipURI uri);

	/**
	 * This method returns true if the hostname is local to the SIP stack.
	 * 
	 * @param host
	 * @return
	 */
	public boolean isLocalHostname(String host);

	/**
	 * This method returns a via header with the correct local address for the
	 * SIP stack.
	 * 
	 * @param transport
	 * @return
	 */
	public SipURI getLocalSipURI(String transport);

	/**
	 * This method returns a via header with the correct local address for the
	 * SIP stack.
	 * 
	 * @param transport -
	 *            a case in-sensitive transport name, such as �udp�, �tcp�, or
	 *            �tls�. This value is used as the result of the getTransport
	 *            method on javax.sip.header.ViaHeader.
	 * @param branch
	 *            -the branch for the via header, if null the RA will
	 *            automatically generate a valid branch parameter.
	 * @return
	 * @throws TransportNotSupportedException
	 *             if the given transport is not supported by the Resource
	 *             Adaptor.
	 */
	public ViaHeader getLocalVia(String transport, String branch)
			throws TransportNotSupportedException;

	/**
	 * This method is used when forwarding a forked response, that was received
	 * in a DialogForkedEvent. A transparent B2BUA application must use this
	 * method to forward forked responses upstream. This method sends the
	 * response on the supplied javax.sip.ServerTransaction, and creates a new
	 * Dialog Activity that the SBB should attach to, to receive mid-dialog
	 * requests on the new dialog. Sending a forked response upstream using the
	 * sendResponse(javax.sip.message.Response) method on
	 * javax.sip.ServerTransaction will not work correctly, because if the
	 * caller sends any mid-dialog requests on the new dialog, the RA Entity
	 * will not match them with a dialog activity, and will not fire these
	 * request events on the Dialog Activity. If the response is a final
	 * response, all other early dialogs will be terminated.
	 * 
	 * @param origServerTransaction
	 * @param response
	 * @return
	 * @throws SipException
	 */
	public DialogActivity forwardForkedResponse(
			ServerTransaction origServerTransaction, Response response)
			throws SipException;

	/**
	 * This method is a convenience method for handling CANCEL requests. When a
	 * CANCEL arrives, and application can ask the RA to handle the request by
	 * calling this method. The behavior of the RA is dependent on whether the
	 * CANCEL matched the INVITE, and also whether the application is acting as
	 * a proxy or not, as determined by the isProxy parameter. The method
	 * returns true if the CANCEL matched an INVITE. The behavior of the method
	 * is summarized as follows. <table border="1">
	 * <tr>
	 * <th></th>
	 * <th><b>isProxy==false</b></th>
	 * <th><b>isProxy==true</b></th>
	 * </tr>
	 * <tr>
	 * <td><b>CANCEL matched INVITE</b></td>
	 * <td>Send �200 OK� response to CANCEL. Send �487 Request Terminated�
	 * response to INVITE.</td>
	 * <td>Send �200 OK� response to CANCEL. The Proxy application must cancel
	 * outstanding branches and wait for responses (RFC3261 �16.10).</td>
	 * </tr>
	 * <tr>
	 * <td><b>CANCEL did not match INVITE</b></td>
	 * <td>Send �481 Call or Transaction Does Not Exist� response to CANCEL.</td>
	 * <td>Do nothing. Proxy application must statelessly forward the CANCEL
	 * downstream (RFC3261 �16.10).</td>
	 * </tr>
	 * </table>
	 * 
	 * The resource adaptor is expected to check if the CANCEL request was
	 * processed by any SBBs. If the CANCEL request was not processed by an SBB,
	 * the RA is expected to send a �481 Call or Transaction Does Not Exist�.
	 * 
	 * @param cancelEvent
	 * @param isProxy
	 * @return
	 */
	public boolean acceptCancel(CancelRequestEvent cancelEvent, boolean isProxy);
}