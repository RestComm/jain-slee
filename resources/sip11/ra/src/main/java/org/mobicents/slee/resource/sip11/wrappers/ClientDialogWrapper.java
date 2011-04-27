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

package org.mobicents.slee.resource.sip11.wrappers;

import gov.nist.javax.sip.DialogExt;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.message.SIPRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogDoesNotExistException;
import javax.sip.DialogState;
import javax.sip.InvalidArgumentException;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.Transaction;
import javax.sip.TransactionDoesNotExistException;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.Address;
import javax.sip.address.URI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.facilities.Tracer;

import org.mobicents.slee.resource.sip11.DialogWithoutIdActivityHandle;
import org.mobicents.slee.resource.sip11.SipResourceAdaptor;
import org.mobicents.slee.resource.sip11.SleeSipProviderImpl;
import org.mobicents.slee.resource.sip11.Utils;

public class ClientDialogWrapper extends DialogWrapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Tracer tracer;

	private Address fromAddress, toAddress;
	private CallIdHeader customCallId;
	private AtomicLong localSequenceNumber = new AtomicLong(0L);
	
	private AtomicBoolean forkingPossible = new AtomicBoolean(true);
	private boolean forkingWinner = false; 
	
	/**
	 * 
	 * @param handle
	 * @param wrappedDialog
	 * @param ra
	 */
	public ClientDialogWrapper(DialogWithoutIdActivityHandle handle, SipResourceAdaptor ra) {
		super(handle,ra);
		if (tracer == null) {
			tracer = ra.getTracer(ClientDialogWrapper.class.getSimpleName());
		}
	}

	public void setCustomCallId(CallIdHeader customCallId) {
		this.customCallId = customCallId;
	}
	
	public void setFromAddress(Address fromAddress) {
		this.fromAddress = fromAddress;
	}
	
	public void setToAddress(Address toAddress) {
		this.toAddress = toAddress;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isForkingPossible() {
		return forkingPossible.get();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean stopForking(boolean iAmTheMasterDialog) {
		boolean stoppedForking = forkingPossible.compareAndSet(true, false);
		if (stoppedForking) {
			forkingWinner = iAmTheMasterDialog;			
		}
		return stoppedForking;
	}
	
	public boolean isForkingWinner() {
		return forkingWinner;
	}
	
	@Override
	public javax.slee.Address getEventFiringAddress() {
		if (eventFiringAddress == null) {
			if (wrappedDialog != null) {
				eventFiringAddress = super.getEventFiringAddress();
			} else {
				// outgoing dialog where the wrapped dialog does not exists yet
				eventFiringAddress = ClientTransactionWrapper.getEventFiringAddress(fromAddress);
			}
		}
		return eventFiringAddress;
	}
	
	@Override
	public Request createAck(long arg0) throws InvalidArgumentException,
			SipException {
		verifyDialogExistency();
		return super.createAck(arg0);
	}

	@Override
	public Request createPrack(Response arg0)
			throws DialogDoesNotExistException, SipException {
		verifyDialogExistency();
		return super.createPrack(arg0);
	}

	@Override
	public Response createReliableProvisionalResponse(int arg0)
			throws InvalidArgumentException, SipException {
		verifyDialogExistency();
		return super.createReliableProvisionalResponse(arg0);
	}

	@Override
	public void delete() {
		
		if (wrappedDialog == null) {
			if (tracer.isFineEnabled()) {
				tracer.fine("Deleting "+getActivityHandle()+" dialog activity, there is no wrapped dialog.");
			}
			// no real dialog
			ra.processDialogTerminated(this);
		} else {
			super.delete();			
		}
	}

	@Override
	public CallIdHeader getCallId() {
		if (this.wrappedDialog == null) {
			return customCallId;
		}
		return super.getCallId();
	}

	@Override
	public String getDialogId() {
		if (this.wrappedDialog == null) {
			return null;
		}
		return super.getDialogId();
	}

	@SuppressWarnings("deprecation")
	@Override
	public Transaction getFirstTransaction() {
		verifyDialogExistency();
		return super.getFirstTransaction();
	}

	@Override
	public Address getLocalParty() {
		if (wrappedDialog == null) {
			return fromAddress;
		}
		else {
			return super.getLocalParty();
		}
	}

	@Override
	public Address getRemoteParty() {
		if (wrappedDialog == null) {
			return toAddress;
		}
		else {
			return super.getRemoteParty();
		}
	}

	@Override
	public Address getRemoteTarget() {
		if (wrappedDialog == null) {
			return toAddress;
		}
		else {
			return super.getRemoteTarget();
		}
	}

	@Override
	public long getLocalSeqNumber() {
		if (wrappedDialog == null) {
			return localSequenceNumber.get();
		}
		else {
			return super.getLocalSeqNumber();
		}
	}

	@Override
	public int getLocalSequenceNumber() {
		return (int) getLocalSeqNumber();
	}

	@Override
	public long getRemoteSeqNumber() {
		if (wrappedDialog != null)
			return super.getRemoteSeqNumber();
		else
			return 1L;
	}

	@Override
	public int getRemoteSequenceNumber() {
		return (int) getRemoteSeqNumber();
	}

	@Override
	public String getRemoteTag() {
		if (wrappedDialog != null) {
			return super.getRemoteTag();
		} else {
			return null;
		}
	}

	@Override
	public DialogState getState() {
		if (wrappedDialog == null) {
			return null;
		}
		else {
			return super.getState();
		}
	}

	@Override
	public void incrementLocalSequenceNumber() {
		if (wrappedDialog != null) {
			super.incrementLocalSequenceNumber();
		}
		else {
			localSequenceNumber.incrementAndGet();
		}
	}

	@Override
	public boolean isSecure() {
		if (wrappedDialog == null)
			return false;
		else
			return super.isSecure();
	}

	@Override
	public boolean isServer() {
		if (wrappedDialog == null)
			return false;
		else
			return super.isServer();
	}

	@Override
	public void sendAck(Request arg0) throws SipException {
		verifyDialogExistency();
		super.sendAck(arg0);
	}

	@Override
	public void sendReliableProvisionalResponse(Response arg0)
			throws SipException {
		verifyDialogExistency();
		super.sendReliableProvisionalResponse(arg0);
	}

	@Override
	public void terminateOnBye(boolean arg0) throws SipException {
		verifyDialogExistency();
		super.terminateOnBye(arg0);
	}

	// =========================== XXX: Helper methods =====================

	@Override
	public String toString() {
		return new StringBuilder("ClientDialogWrapper[ handle = ").append(getActivityHandle())
			.append(", state = ").append(getState())
			.append(", clientTXs = ").append(ongoingClientTransactions == null ? "" :  ongoingClientTransactions.keySet())
			.append(" ]").toString();
	}

	// ###########################################
	// # Strictly DialogActivity defined methods #
	// ###########################################

	@Override
	public ClientTransaction sendCancel() throws SipException {
		verifyDialogExistency();
		return super.sendCancel();
	}

	@Override
	public void associateServerTransaction(ClientTransaction ct,
			ServerTransaction st) {
		// ct MUST be in ongoing transaction, its local, st - comes from another
		// dialog
		verifyDialogExistency();
		super.associateServerTransaction(ct, st);
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.wrappers.DialogWrapper#createRequest(javax.sip.message.Request)
	 */
	@Override
	public Request createRequest(Request origRequest) throws SipException {
		Request request = super.createRequest(origRequest);
		if (wrappedDialog == null) {
			// hack uri and address headers
			final SIPRequest sipRequest = (SIPRequest) request;
			final SleeSipProviderImpl provider = ra.getProviderWrapper();
			final HeaderFactory headerFactory = provider.getHeaderFactory();
			try {
				// hack request with local addresses
				final URI requestURI = (URI) toAddress.getURI().clone();
				if(requestURI.isSipURI()) {
					((SipUri)requestURI).clearUriParms();					
				}
				sipRequest.setRequestURI(requestURI);
				final FromHeader fromHeader = headerFactory.createFromHeader(
						fromAddress, getLocalTag());
				sipRequest.setFrom(fromHeader);
				final ToHeader toHeader = headerFactory.createToHeader(
						toAddress, null);
				sipRequest.setTo(toHeader);
			} catch (Exception e) {
				throw new SipException(e.getMessage(), e);
			}
		}
		return request;
	}
	
	@Override
	public Request createRequest(String methodName) throws SipException {
		
		if (methodName.equals(Request.ACK) || methodName.equals(Request.PRACK)) {
            throw new SipException("Invalid method specified for createRequest:" + methodName);
        }
		
		final SleeSipProviderImpl provider = ra.getProviderWrapper();
		final HeaderFactory headerFactory = provider.getHeaderFactory();
		Request request = null;
		if (this.wrappedDialog == null) {
			// the real dialog doesn't exist yet so we act like we will build
			// such a dialog when sending this request
			
			try {
				// create headers
				URI requestURI = (URI) toAddress.getURI().clone();
				if(requestURI.isSipURI()) {
					((SipUri)requestURI).clearUriParms();
				}
				final FromHeader fromHeader = headerFactory.createFromHeader(
						fromAddress, getLocalTag());
				final ToHeader toHeader = headerFactory.createToHeader(
						toAddress, null);
				final List<Object> viaHeadersList = new ArrayList<Object>(1);
				viaHeadersList.add(provider.getLocalVia());
				final MaxForwardsHeader maxForwardsHeader = headerFactory
				.createMaxForwardsHeader(70);
				final CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(
						localSequenceNumber.get() + 1, methodName);
				request = provider.getMessageFactory()
				.createRequest(requestURI, methodName, customCallId,
						cSeqHeader, fromHeader, toHeader,
						viaHeadersList, maxForwardsHeader);								
			} catch (Exception e) {
				throw new SipException(e.getMessage(), e);
			}
		} 
		else {
			request = super.createRequest(methodName);
		}
		if (getState() == null) {
			// adds load balancer to route if exists
			ra.getProviderWrapper().addLoadBalancerToRoute(request);
		}
		return request;
	}
	
	@Override
	public ClientTransaction sendRequest(Request request) throws SipException,
			TransactionUnavailableException {

		if (wrappedDialog == null
				&& !Utils.getDialogCreatingMethods().contains(
						request.getMethod())) {
			throw new IllegalStateException(
					"Dialog activity present, but no dialog creating reqeust has been sent yet! This method: "
							+ request.getMethod()
							+ " is not dialog creating one");
		}

		ensureCorrectDialogLocalTag(request);

		final SleeSipProviderImpl provider = ra.getProviderWrapper();
		final ClientTransactionWrapper ctw = provider
				.getNewDialogActivityClientTransaction(this, request);

		final String method = request.getMethod();
		if (method.equals(Request.INVITE))
			lastCancelableTransactionId = ctw.activityHandle;

		if (tracer.isInfoEnabled()) {
			tracer.info(String.valueOf(ctw) + " sending request:\n"
					+ request);
		}
		
		final boolean createDialog = wrappedDialog == null;
		if (createDialog) {
			setWrappedDialog(provider.getRealProvider().getNewDialog(
					ctw.getWrappedTransaction()));
			if(ra.disableSequenceNumberValidation())
			{
				((DialogExt)this.wrappedDialog).disableSequenceNumberValidation();
			}
			// dialog in null state does not allows to send request
			ctw.getWrappedClientTransaction().sendRequest();
		} else {
			this.wrappedDialog.sendRequest(ctw
					.getWrappedClientTransaction());			
		}
		
		return ctw;
	}

	@Override
	public void sendRequest(ClientTransaction ct)
			throws TransactionDoesNotExistException, SipException {

		final SleeSipProviderImpl provider = ra.getProviderWrapper();
		final Request request = ct.getRequest();
		final ClientTransactionWrapper ctw = (ClientTransactionWrapper) ct;

		ensureCorrectDialogLocalTag(request);

		if (tracer.isInfoEnabled()) {
			tracer.info(String.valueOf(ctw) + " sending request:\n"
					+ request);
		}
		
		final boolean createDialog = wrappedDialog == null;

		if (createDialog) {
			if (!Utils.getDialogCreatingMethods().contains(request.getMethod())) {
				throw new IllegalStateException(
						"Dialog activity present, but no dialog creating reqeust has been sent yet! This method: "
								+ request.getMethod()
								+ " is not dialog creating one");
			}
			if (request.getMethod().equals(Request.INVITE))
				lastCancelableTransactionId = ctw.getActivityHandle();
			setWrappedDialog(provider.getRealProvider().getNewDialog(
					ctw.getWrappedTransaction()));
			this.addOngoingTransaction(ctw);
			// dialog in null state does not allows to send request
			ctw.getWrappedClientTransaction().sendRequest();			
		} else {
			if (wrappedDialog.getState() == null) {
				// first request,  a bug in jsip first sets local seq number to the one in request, then increases before sending
				// needs to use ct to send instead
				ctw.getWrappedClientTransaction().sendRequest();				
			}
			else {
				wrappedDialog.sendRequest(ctw.getWrappedClientTransaction());
			}
		}
	}

	/**
	 * 
	 */
	private void verifyDialogExistency() {
		if (wrappedDialog == null) {
			throw new IllegalStateException(
					"Dialog activity present, but no dialog creating request has been sent yet!");
		}
	}
	
	@Override
	public boolean isClientDialog() {
		return true;
	}
	
	@Override
	public void setWrappedDialog(Dialog wrappedDialog) {
		this.wrappedDialog = wrappedDialog;
		if (wrappedDialog != null) {
			wrappedDialog.setApplicationData(new DialogWithoutIdWrapperData(this));
		}
	}
	
	// serialization
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		throw new IOException("serialization forbidden");
	}

	private void readObject(ObjectInputStream stream) throws IOException,
			ClassNotFoundException {
		throw new IOException("serialization forbidden");
	}
}
