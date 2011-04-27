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

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.message.Request;
import javax.sip.message.Response;

public interface DialogActivity extends Dialog {

	/**
	 * The sendRequest method is used to send a request to the Dialog's remote
	 * party on a new Client Transaction. Any response to this request will be
	 * fired on the Activity associated with this Dialog. This method should be
	 * used by an application that is Dialog stateful, in preference to the
	 * javax.sip.Dialog.sendRequest(ClientTransaction) method. The reason for
	 * this preference is that the API is simpler and SLEE resources are used.
	 * The method returns the Client Transaction used to represent the SIP
	 * transaction. Note that the returned Client Transaction does not have a
	 * corresponding Activity in the SLEE. This method will set the From and the
	 * To tags for the outgoing request. This method increments the dialog
	 * sequence number and sets the correct sequence number to the outgoing
	 * Request and associates the client transaction with this dialog. Note that
	 * any tags assigned by the user will be over-written by this method. If the
	 * caller sets no RouteHeader in the Request to be sent out, the
	 * implementation of this method will add the RouteHeader from the route set
	 * that is maintained in the dialog. If the caller sets the route header,
	 * the implementation will leave the route headers unaltered. This allows
	 * the application to manage its own route set if so desired.
	 * 
	 * @param request
	 * @return
	 * @throws SipException
	 *             This exception is thrown if the Request is unable to be sent
	 *             for any reason
	 */
	public ClientTransaction sendRequest(Request request) throws SipException;

	/**
	 * The createRequest method is used to create a Request that will be sent on
	 * this Dialog, where the Request has been received on an incoming Dialog.
	 * This scenario is common for B2BUA applications. The method returns a new
	 * Request to be sent on the Dialog. The method copies the headers and body
	 * present in the argument to the return result, and sets the headers in the
	 * return result to reflect that the message is to be sent on this Dialog.
	 * 
	 * @param origRequest
	 * @return
	 * @throws SipException
	 *             if a new Request is unable to be created.
	 */
	public Request createRequest(Request origRequest) throws SipException;

	/**
	 * is typically performed when a B2BUA application receives a response from
	 * one Dialog and wishes to forward the response on another Dialog. The
	 * method returns a new Response to be sent on this Dialog. The method
	 * copies the headers and body present in the response argument to the
	 * return result, and copies the transactions� identifier to the return
	 * result. This method is intended to be used in conjunction with the
	 * associateServerTransaction and getAssociatedServerTransaction methods.
	 * 
	 * @param origServerTransaction
	 *            This represents the ServerTransaction on the Dialog which the
	 *            newly created response will be sent on.
	 * @param receivedResponse
	 *            This is the Response which will be copied as part of the
	 *            process of creating the new Response.
	 * @return
	 * @throws SipException
	 *             if it is unable to create a new Response.
	 */
	public Response createResponse(ServerTransaction origServerTransaction,
			Response receivedResponse) throws SipException;

	/**
	 * This method creates an association between a ServerTransaction from
	 * another Dialog with a ClientTransaction for use with this Dialog. This
	 * association can be accessed later via the getAssociatedServerTransaction
	 * method. The association is cleared when the ServerTransaction terminates.
	 * It is used primarily by B2BUA applications that wish to forward responses
	 * from a UAC Dialog to a UAS Dialog. It is intended to be used in
	 * conjunction with the getAssociatedServerTransaction and
	 * createResponse(ServerTransaction, Response) methods.
	 * 
	 * @param ct -
	 *            This argument represents a Client Transaction for this Dialog.
	 * @param st
	 *            � This argument represents a Server Transaction for another
	 *            Dialog.
	 */
	public void associateServerTransaction(ClientTransaction ct,
			ServerTransaction st);

	/**
	 * This method returns the ServerTransaction associated with the specified
	 * ClientTransaction. If no association exists for the specified
	 * ClientTransaction then this method returns null. This method is intended
	 * to be used in conjunction with the associateServerTransaction and
	 * createResponse( ServerTransaction, Response) methods.
	 * 
	 * @param ct
	 *            � This argument represents a Client Transaction for this
	 *            Dialog.
	 * @return
	 */
	public ServerTransaction getAssociatedServerTransaction(ClientTransaction ct);

	/**
	 * This method creates and sends a CANCEL request for the last INVITE
	 * request that was sent on this dialog. This method may be used for
	 * cancelling initial INVITEs or re-INVITEs. This method does not directly
	 * affect the dialog state. The CANCEL should cause the downstream server to
	 * send a 487 response to the INVITE, which will automatically end the
	 * dialog if the INVITE was the initial request.
	 * 
	 * @return This method returns a javax.sip.ClientTransaction activity
	 *         object, representing the CANCEL's client transaction. The SBB may
	 *         obtain the javax.slee.ActivityContextInterface for this activity
	 *         and attach to it, if it is interested in the response to the
	 *         CANCEL. Otherwise, this activity may be safely ignored.
	 * @throws SipException
	 */
	public ClientTransaction sendCancel() throws SipException;

}