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

package org.mobicents.slee.resource.sip11;

import gov.nist.javax.sip.DialogExt;
import gov.nist.javax.sip.ListeningPointImpl;
import gov.nist.javax.sip.Utils;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.stack.SIPClientTransaction;
import gov.nist.javax.sip.stack.SIPServerTransaction;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.TooManyListenersException;
import java.util.concurrent.ConcurrentHashMap;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.Transaction;
import javax.sip.TransactionAlreadyExistsException;
import javax.sip.TransactionUnavailableException;
import javax.sip.TransportAlreadySupportedException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CallIdHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.facilities.Tracer;

import net.java.slee.resource.sip.CancelRequestEvent;
import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SleeSipProvider;

import org.mobicents.ha.javax.sip.ClusteredSipStack;
import org.mobicents.ha.javax.sip.LoadBalancerElector;
import org.mobicents.slee.resource.sip11.wrappers.ClientTransactionWrapper;
import org.mobicents.slee.resource.sip11.wrappers.DialogWrapper;
import org.mobicents.slee.resource.sip11.wrappers.ClientDialogWrapper;
import org.mobicents.slee.resource.sip11.wrappers.ServerTransactionWrapper;

/**
 * 
 * @author B.Baranowski
 * @author martins
 * 
 */
public class SleeSipProviderImpl implements SleeSipProvider {

	protected AddressFactory addressFactory = null;
	protected HeaderFactory headerFactory = null;
	protected MessageFactory messageFactory = null;
	protected ClusteredSipStack stack = null;
	protected SipResourceAdaptor ra = null;
	protected SipProvider provider = null;
	protected final Tracer tracer;
	private boolean active;
	
	public SleeSipProviderImpl(SipResourceAdaptor ra) {
		this.ra = ra;
		this.tracer = ra.getTracer(SleeSipProviderImpl.class.getSimpleName());
	}
	
	public void raActive(AddressFactory addressFactory,
			HeaderFactory headerFactory, MessageFactory messageFactory,
			ClusteredSipStack stack,SipProvider provider) {		
		this.addressFactory = addressFactory;
		this.headerFactory = headerFactory;
		this.messageFactory = messageFactory;
		this.stack = stack;
		this.provider = provider;	
		this.active = true;
	}
	
	public void raInactive() {
		this.addressFactory = null;
		this.headerFactory = null;
		this.messageFactory = null;
		this.stack = null;
		this.provider = null;
		this.active = false;
	}
	
	private void checkState() throws IllegalStateException {
		if (!active) {
			throw new IllegalStateException("ra not active");
		}
	}
	/**
	 * 
	 * @return
	 */
	public SipProvider getRealProvider() {
		return provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.java.slee.resource.sip.SleeSipProvider#getAddressFactory()
	 */
	public AddressFactory getAddressFactory() {
		checkState();
		return this.addressFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.java.slee.resource.sip.SleeSipProvider#getHeaderFactory()
	 */
	public HeaderFactory getHeaderFactory() {
		checkState();
		return this.headerFactory;
	}
	
	private ConcurrentHashMap<String, SipUri> localSipURIs = new ConcurrentHashMap<String, SipUri>();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.slee.resource.sip.SleeSipProvider#getLocalSipURI(java.lang.String
	 * )
	 */
	public SipURI getLocalSipURI(String transport) {
		checkState();
		SipUri sipURI = localSipURIs.get(localSipURIs);
		if (sipURI == null) {
			ListeningPoint lp = getListeningPoint(transport);
			if (lp != null) {
				sipURI = new SipUri();
				try {
					sipURI.setHost(lp.getIPAddress());
					sipURI.setTransportParam(transport);
				} catch (ParseException e) {
					tracer.severe("Failed to create local sip uri for transport "+transport,e);
				}
				sipURI.setPort(lp.getPort());
				localSipURIs.put(transport, sipURI);
			}
		}
		return sipURI;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.slee.resource.sip.SleeSipProvider#getLocalVia(java.lang.String,
	 * java.lang.String)
	 */
	public ViaHeader getLocalVia(String transport, String branch) {
		checkState();
		final ListeningPoint lp = provider.getListeningPoint(transport);
		if (lp != null) {
			try {
				return headerFactory.createViaHeader(lp.getIPAddress(), lp
						.getPort(), lp.getTransport(), branch);
			} catch (ParseException e) {
				tracer.severe(e.getMessage(), e);
			} catch (InvalidArgumentException e) {
				tracer.severe(e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * 
	 * @return
	 * @throws InvalidArgumentException
	 * @throws ParseException
	 */
	public ViaHeader getLocalVia() throws ParseException,
			InvalidArgumentException {
		checkState();
		final ListeningPointImpl lp = (ListeningPointImpl) provider.getListeningPoints()[0];
		return lp != null ? lp.createViaHeader() : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.java.slee.resource.sip.SleeSipProvider#getMessageFactory()
	 */
	public MessageFactory getMessageFactory() {
		checkState();
		return this.messageFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.slee.resource.sip.SleeSipProvider#isLocalHostname(java.lang.
	 * String)
	 */
	public boolean isLocalHostname(String host) {
		checkState();
		try {
			InetAddress[] addresses = InetAddress.getAllByName(host);

			Set<InetAddress> stackAddresses = new HashSet<InetAddress>();

			for (ListeningPoint lp : this.provider.getListeningPoints()) {
				InetAddress tmp = InetAddress.getByName(lp.getIPAddress());
				if (tmp != null)
					stackAddresses.add(tmp);
			}

			for (InetAddress ia : addresses) {
				if (stackAddresses.contains(ia))
					return true;
			}

		} catch (UnknownHostException e) {
			if (tracer.isFineEnabled()) {
				tracer.fine("Unknown host",e);
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.slee.resource.sip.SleeSipProvider#isLocalSipURI(javax.sip.address
	 * .SipURI)
	 */
	public boolean isLocalSipURI(SipURI uri) {
		checkState();
		// XXX: InetAddress api is
		// crude.....!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		ListeningPoint lp = this.provider.getListeningPoint(uri
				.getTransportParam());
		if (lp != null && lp.getIPAddress().equals(uri.getHost())
				&& lp.getPort() == uri.getPort()) {
			return true;
		} else {
			if (tracer.isFineEnabled()) {
				tracer.fine("Passed uri not local? Passed URI[" + uri
						+ "] doesnt match lp[" + lp + "]");
			}

			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#addListeningPoint(javax.sip.ListeningPoint)
	 */
	public void addListeningPoint(ListeningPoint arg0)
			throws ObjectInUseException, TransportAlreadySupportedException {
		throw new UnsupportedOperationException("No dynamic change to LP");
	}

	public void addSipListener(SipListener arg0)
			throws TooManyListenersException {
		throw new TooManyListenersException(
				"RA can be the only Listener for this stack!!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#getListeningPoint()
	 */
	@SuppressWarnings("deprecation")
	public ListeningPoint getListeningPoint() {
		checkState();
		return this.provider.getListeningPoint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#getListeningPoint(java.lang.String)
	 */
	public ListeningPoint getListeningPoint(String arg0) {
		checkState();
		return this.provider.getListeningPoint(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#getListeningPoints()
	 */
	public ListeningPoint[] getListeningPoints() {
		checkState();
		return this.provider.getListeningPoints();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#getNewCallId()
	 */
	public CallIdHeader getNewCallId() {
		checkState();
		return this.provider.getNewCallId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#getSipStack()
	 */
	public SipStack getSipStack() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#removeListeningPoint(javax.sip.ListeningPoint)
	 */
	public void removeListeningPoint(ListeningPoint arg0)
			throws ObjectInUseException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#removeSipListener(javax.sip.SipListener)
	 */
	public void removeSipListener(SipListener arg0) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#sendRequest(javax.sip.message.Request)
	 */
	public void sendRequest(Request arg0) throws SipException {
		checkState();
		this.provider.sendRequest(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#sendResponse(javax.sip.message.Response)
	 */
	public void sendResponse(Response arg0) throws SipException {
		checkState();
		this.provider.sendResponse(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#setAutomaticDialogSupportEnabled(boolean)
	 */
	public void setAutomaticDialogSupportEnabled(boolean arg0) {
		checkState();
		this.provider.setAutomaticDialogSupportEnabled(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#setListeningPoint(javax.sip.ListeningPoint)
	 */
	public void setListeningPoint(ListeningPoint arg0)
			throws ObjectInUseException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.sip.SipProvider#getNewClientTransaction(javax.sip.message.Request)
	 */
	public ClientTransaction getNewClientTransaction(Request request)
			throws TransactionUnavailableException {
		
		checkState();
		// add load balancer to route if it is configured
		try {
			addLoadBalancerToRoute(request);
		} catch (SipException e) {
			throw new TransactionUnavailableException("Failed to add load balancer to route",e);
		}
		
		final SIPClientTransaction ct = (SIPClientTransaction) provider.getNewClientTransaction(request);
		final ClientTransactionWrapper ctw = new ClientTransactionWrapper(ct,
				ra);
		ctw.setActivity(true);

		final DialogWrapper dw = ctw.getDialogWrapper();
		if (dw != null) {
			dw.addOngoingTransaction(ctw);
		}

		if (!ra.addSuspendedActivity(ctw)) {
			throw new TransactionUnavailableException(
					"Failed to create activity");
		}

		return ctw;
	}

	/**
	 * Creates a new {@link ClientTransactionWrapper} bound to a
	 * {@link DialogWrapper}, which is not an activity in SLEE.
	 * 
	 * @param dialogWrapper
	 * @param request
	 * @return
	 * @throws TransactionUnavailableException
	 */
	public ClientTransactionWrapper getNewDialogActivityClientTransaction(
			DialogWrapper dialogWrapper, Request request)
			throws TransactionUnavailableException {
		final SIPClientTransaction ct = (SIPClientTransaction) provider.getNewClientTransaction(request);
		final ClientTransactionWrapper ctw = new ClientTransactionWrapper(ct,
				ra);
		dialogWrapper.addOngoingTransaction(ctw);
		return ctw;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.sip.SipProvider#getNewServerTransaction(javax.sip.message.Request)
	 */
	public ServerTransaction getNewServerTransaction(Request request)
			throws TransactionAlreadyExistsException,
			TransactionUnavailableException {

		checkState();
		// TODO: add checks for wrapper

		final SIPServerTransaction st = (SIPServerTransaction) provider.getNewServerTransaction(request);
		ServerTransactionWrapper stw = new ServerTransactionWrapper(st, ra);

		if (stw.getDialogWrapper() == null) {
			// SLEE 1.1 specs: D.4
			// ServerTransaction Activity objects are created automatically when
			// the resource adaptor receives an
			// incoming SIP request. The activity ends when a final response is
			// sent on the ServerTransaction.
			stw.setActivity(true);
			if (!ra.addSuspendedActivity(stw)) {
				throw new TransactionUnavailableException(
						"Failed to create activity.");
			}
		}

		return stw;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#getNewDialog(javax.sip.Transaction)
	 */
	public Dialog getNewDialog(Transaction transaction) throws SipException {
		checkState();
		if (transaction.getClass() == ServerTransactionWrapper.class) {
			return getNewDialog((ServerTransactionWrapper) transaction);
			
		} else if (transaction.getClass() == ClientTransactionWrapper.class) {
			return getNewDialog((ClientTransactionWrapper) transaction);
			
		} else {
			throw new IllegalArgumentException("unknown transaction class");
		}
	}

	private Dialog getNewDialog(ServerTransactionWrapper stw) throws SipException {
		final ServerTransaction st = stw.getWrappedServerTransaction();
		final Dialog d = provider.getNewDialog(st);
		if(ra.disableSequenceNumberValidation())
		{
			((DialogExt)d).disableSequenceNumberValidation();
		}
		String localTag = d.getLocalTag();
		String dialogId = d.getDialogId();
		if (localTag == null) {
			// some hacking in jsip, we need a dialog id now and the real dialog
			// does not have a local tag
			localTag = Utils.getInstance().generateTag();
			dialogId = ((SIPRequest) st.getRequest()).getDialogId(
				true, localTag);
		}
		final DialogWithIdActivityHandle h = new DialogWithIdActivityHandle(dialogId);
		final DialogWrapper dw = new DialogWrapper(h,ra);
		dw.setLocalTag(localTag);
		dw.setWrappedDialog(d);
		ra.addSuspendedActivity(dw);
		return dw;
	}
	
	private Dialog getNewDialog(ClientTransactionWrapper ctw) throws SipException {
		
		final Request r = ctw.getWrappedTransaction().getRequest();
		
		final FromHeader fh = (FromHeader)r.getHeader(FromHeader.NAME);
		String localTag = fh.getTag();
		if(localTag == null) {
			localTag = Utils.getInstance().generateTag();
			try {				
				fh.setTag(localTag);
			} catch (ParseException e) {
				throw new SipException("Failed to set local tag.", e);
			}
		}		
		final Dialog d = provider.getNewDialog(ctw.getWrappedTransaction());
		if(ra.disableSequenceNumberValidation())
		{
			((DialogExt)d).disableSequenceNumberValidation();
		}
		final DialogWrapper dw = _getNewDialog(
				fh.getAddress(),
				localTag,
				((ToHeader) r.getHeader(ToHeader.NAME)).getAddress(), d.getCallId());
		dw.setWrappedDialog(d);
		dw.addOngoingTransaction(ctw);
		return dw;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.slee.resource.sip.SleeSipProvider#getNewDialog(javax.sip.address
	 * .Address, javax.sip.address.Address)
	 */
	public DialogActivity getNewDialog(Address from, Address to)
			throws SipException {
		checkState();
		if (from == null) {
			throw new IllegalArgumentException("From address cant be null");
		}
		if (to == null) {
			throw new IllegalArgumentException("To address cant be null");
		}
		return _getNewDialog(from, gov.nist.javax.sip.Utils.getInstance()
				.generateTag(), to, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.slee.resource.sip.SleeSipProvider#getNewDialog(net.java.slee
	 * .resource.sip.DialogActivity, boolean)
	 */
	public DialogActivity getNewDialog(DialogActivity incomingDialog,
			boolean useSameCallId) throws SipException {
		checkState();
		if (incomingDialog == null || !incomingDialog.isServer()) {
			throw new IllegalArgumentException(
					"Incoming dialog is either null or is UAC dialog!!");
		}
		CallIdHeader callIdHeader = null;
		if (useSameCallId) {
			callIdHeader = incomingDialog.getCallId();
		}

		return _getNewDialog(incomingDialog.getRemoteParty(), Utils.getInstance().generateTag(), incomingDialog.getLocalParty(), callIdHeader);
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @param callIdHeader
	 * @return
	 * @throws SipException
	 */
	private DialogWrapper _getNewDialog(Address from, String localTag,
			Address to, CallIdHeader callIdHeader) throws SipException {
		if (callIdHeader == null) {
			callIdHeader = provider.getNewCallId();
		}
		final DialogWithoutIdActivityHandle h = new DialogWithoutIdActivityHandle(callIdHeader.getCallId(),
				localTag);
		final ClientDialogWrapper dw = new ClientDialogWrapper(h,ra);
		dw.setLocalTag(localTag);
		dw.setFromAddress(from);
		dw.setToAddress(to);
		dw.setCustomCallId(callIdHeader == null ? provider.getNewCallId()
						: callIdHeader);
		if (!ra.addSuspendedActivity(dw)) {
			throw new SipException("Failed to create activity.");
		}

		return dw;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.slee.resource.sip.SleeSipProvider#acceptCancel(net.java.slee
	 * .resource.sip.CancelRequestEvent, boolean)
	 */
	public boolean acceptCancel(CancelRequestEvent cancelEvent, boolean isProxy) {
		checkState();
		if (cancelEvent.getMatchingTransaction() != null) {
			try {

				Response response = this.getMessageFactory().createResponse(
						Response.OK, cancelEvent.getRequest());
				cancelEvent.getServerTransaction().sendResponse(response);				
			} catch (Exception e) {
				// specs doesn't provide any throws clause
				throw new RuntimeException(e.getMessage(), e);
			}

			if (!isProxy)
				try {
					Response response = this.getMessageFactory()
							.createResponse(
									Response.REQUEST_TERMINATED,
									cancelEvent.getMatchingTransaction()
											.getRequest());
					cancelEvent.getMatchingTransaction().sendResponse(response);

				} catch (Exception e) {
					// specs doesn't provide any throws clause
					throw new RuntimeException(e.getMessage(), e);
				}
			return true;
		} else {
			if (!isProxy) {
				try {
					Response txDoesNotExistsResponse = this
							.getMessageFactory()
							.createResponse(
									Response.CALL_OR_TRANSACTION_DOES_NOT_EXIST,
									cancelEvent.getRequest());
					cancelEvent.getServerTransaction().sendResponse(
							txDoesNotExistsResponse);
				} catch (Exception e) {
					// specs doesn't provide any throws clause
					throw new RuntimeException(e.getMessage(), e);
				}
			}
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.slee.resource.sip.SleeSipProvider#forwardForkedResponse(javax
	 * .sip.ServerTransaction, javax.sip.message.Response)
	 */
	public DialogActivity forwardForkedResponse(
			ServerTransaction origServerTransaction, Response response)
			throws SipException {
		// TODO
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @return the stack
	 */
	public ClusteredSipStack getClusteredSipStack() {
		return stack;
	}

	/**
	 * 
	 * @param r
	 * @throws SipException
	 */
	@SuppressWarnings("deprecation")
	public void addLoadBalancerToRoute(Request r) throws SipException {
		LoadBalancerElector loadBalancerElector = stack.getLoadBalancerElector();
		if (loadBalancerElector != null) {
			Address lbAddress = loadBalancerElector.getLoadBalancer();
			if (lbAddress == null) {
				return;
			}
			lbAddress = (Address) loadBalancerElector.getLoadBalancer().clone();
			((SipURI)lbAddress.getURI()).setLrParam();
			r.addFirst(headerFactory.createRouteHeader(lbAddress));
		}
	}
}
