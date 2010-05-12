package org.mobicents.slee.resource.sip11;

import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.stack.SIPServerTransaction;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.TimeoutEvent;
import javax.sip.Transaction;
import javax.sip.TransactionState;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.Address;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.EventFlags;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptorContext;
import javax.slee.resource.SleeEndpoint;
import javax.slee.resource.ConfigProperties.Property;

import net.java.slee.resource.sip.CancelRequestEvent;

import org.mobicents.ha.javax.sip.ClusteredSipStack;
import org.mobicents.ha.javax.sip.LoadBalancerElector;
import org.mobicents.ha.javax.sip.cache.SipResourceAdaptorMobicentsSipCache;
import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor;
import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext;
import org.mobicents.slee.resource.sip11.wrappers.ACKDummyTransaction;
import org.mobicents.slee.resource.sip11.wrappers.ClientTransactionWrapper;
import org.mobicents.slee.resource.sip11.wrappers.DialogWrapper;
import org.mobicents.slee.resource.sip11.wrappers.RequestEventWrapper;
import org.mobicents.slee.resource.sip11.wrappers.ResponseEventWrapper;
import org.mobicents.slee.resource.sip11.wrappers.ServerTransactionWrapper;
import org.mobicents.slee.resource.sip11.wrappers.TimeoutEventWrapper;
import org.mobicents.slee.resource.sip11.wrappers.TransactionWrapper;
import org.mobicents.slee.resource.sip11.wrappers.Wrapper;

public class SipResourceAdaptor implements SipListener,FaultTolerantResourceAdaptor<SipActivityHandle, String> {

	// Config Properties Names -------------------------------------------

	private static final String SIP_BIND_ADDRESS = "javax.sip.IP_ADDRESS";

	private static final String SIP_PORT_BIND = "javax.sip.PORT";

	private static final String TRANSPORTS_BIND = "javax.sip.TRANSPORT";

	private static final String STACK_NAME_BIND = "javax.sip.STACK_NAME";

	private static final String LOAD_BALANCER_HEART_BEAT_SERVICE_CLASS = "org.mobicents.ha.javax.sip.LoadBalancerHeartBeatingServiceClassName";
	
	private static final String BALANCERS = "org.mobicents.ha.javax.sip.BALANCERS";
	
	private static final String LOOSE_DIALOG_VALIDATION = "org.mobicents.javax.sip.LOOSE_DIALOG_VALIDATION";
	// Config Properties Values -------------------------------------------
	
	private int port;
	private Set<String> transports = new HashSet<String>();
	private String transportsProperty;
	private String stackAddress;
	private String sipBalancerHeartBeatServiceClassName;
	private String balancers;
	private String loadBalancerElector;
	/**
	 * default is true;
	 */
	private boolean looseDialogSeqValidation = true;

	/**
	 * allowed transports
	 */
	private Set<String> allowedTransports = new HashSet<String>();
	
	/**
	 * the real sip stack provider
	 */
	private SipProvider provider;

	/**
	 * the ra sip provider, which wraps the real one
	 */
	private SleeSipProviderImpl providerWrapper;
	
	/**
	 * manages the ra activities
	 */
	private SipActivityManagement activityManagement;

	/**
	 * caches the eventIDs, avoiding lookup in container
	 */
	private final EventIDCache eventIdCache = new EventIDCache();

	/**
	 * tells the RA if an event with a specified ID should be filtered or not
	 */
	private final EventIDFilter eventIDFilter = new EventIDFilter();

	/**
	 * 
	 */
	private ResourceAdaptorContext raContext;
	private SleeEndpoint sleeEndpoint;
	private EventLookupFacility eventLookupFacility;
	
	/**
	 * 
	 */
	private Tracer tracer;
	
	/**
	 * 
	 */
	private ClusteredSipStack sipStack = null;

	/**
	 * 
	 */
	private SipFactory sipFactory = null;

	/**
	 * 
	 */
	private Marshaler marshaler = new SipMarshaler();

	/**
	 * for all events we are interested in knowing when the event failed to be processed
	 */
	public static final int DEFAULT_EVENT_FLAGS = EventFlags.REQUEST_PROCESSING_FAILED_CALLBACK;

	public static final int UNREFERENCED_EVENT_FLAGS = EventFlags.setRequestEventReferenceReleasedCallback(DEFAULT_EVENT_FLAGS);

	public static final int NON_MARSHABLE_ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;//.NO_FLAGS;
	
	public static final int MARSHABLE_ACTIVITY_FLAGS = ActivityFlags.setSleeMayMarshal(NON_MARSHABLE_ACTIVITY_FLAGS);
	
	public SipResourceAdaptor() {
		// Those values are defualt
		this.port = 5060;
		// this.transport = "udp";
		allowedTransports.add("udp");
		allowedTransports.add("tcp");
		transports.add("udp");
		// this.stackAddress = "127.0.0.1";

		// this.stackPrefix = "gov.nist";
	}

	// XXX -- SipListenerMethods - here we process incoming data

	/*
	 * (non-Javadoc)
	 * @see javax.sip.SipListener#processIOException(javax.sip.IOExceptionEvent)
	 */
	public void processIOException(IOExceptionEvent arg0) {
		tracer.severe("processIOException event = "+arg0.toString());
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.SipListener#processRequest(javax.sip.RequestEvent)
	 */
	public void processRequest(RequestEvent req) {

		final boolean infoTrace = tracer.isInfoEnabled();
		
		if (infoTrace) {
			tracer.info("Received Request:\n"+req.getRequest());
		}
		
		if (req.getRequest().getMethod().equals(Request.CANCEL)) {
			processCancelRequest(req);
		} else {
			processNotCancelRequest(req,infoTrace);
		}
	}

	/**
	 * 
	 * @param req
	 */
	private void processCancelRequest(RequestEvent req) {
		
		final boolean fineTrace = tracer.isFineEnabled();

		// get server tx wrapper		
		final ServerTransactionWrapper cancelSTW = getServerTransactionWrapper(req,fineTrace);
		if (cancelSTW == null) {
			return;
		}
		// get canceled invite stw
		final ServerTransaction inviteST = ((SIPServerTransaction)cancelSTW.getWrappedServerTransaction()).getCanceledInviteTransaction();
		final ServerTransactionWrapper inviteSTW = inviteST != null ? (ServerTransactionWrapper) inviteST.getApplicationData() : null;
		// get dialog
		final Dialog d = cancelSTW.getWrappedServerTransaction().getDialog();
		final DialogWrapper dw = (d == null ? null : (DialogWrapper) d.getApplicationData());
		Wrapper activity = dw;
		if (activity != null) {
			if (!inLocalMode()) {
				// ensure the dw is linked to the ra and wrapped dialog
				dw.setResourceAdaptor(this);
				dw.setWrappedDialog(d);
			}
			// add tx
			dw.addOngoingTransaction(cancelSTW);
		}
		else {
			if (inviteSTW != null) {
				activity = inviteSTW;
				// ensure the stw is linked to the ra
				inviteSTW.setResourceAdaptor(this);
			}
			else {
				activity = cancelSTW;
				cancelSTW.setActivity(true);
				if (!addActivity(activity, false, fineTrace)) {
					final String errorMsg = "Failed to add cancel transaction activity, can't proceed.";
					tracer.severe(errorMsg);
					sendErrorResponse(req.getServerTransaction(), req.getRequest(),
							Response.SERVER_INTERNAL_ERROR,errorMsg);				
					return;
				}
			}
		}
		if (fineTrace) {
			tracer.fine("Activity selected to fire CANCEL event: " + activity);
		}
		
		final CancelRequestEvent REW = new CancelRequestEvent(this.providerWrapper, cancelSTW,
				inviteSTW, dw, req.getRequest());
		final int eventsFlags = EventFlags.setRequestEventReferenceReleasedCallback(DEFAULT_EVENT_FLAGS);
		final FireableEventType eventType = eventIdCache.getEventId(eventLookupFacility, REW.getRequest(), activity.isDialog());
		if (eventIDFilter.filterEvent(eventType)) {
			if (fineTrace) {
				tracer.fine("Event " + (eventType == null?"null":eventType.getEventType()) + " filtered");
			}
			// event filtered
			processCancelNotHandled(cancelSTW,req.getRequest());
		} else {
			try {
				sleeEndpoint.fireEvent(activity.getActivityHandle(), eventType, REW, activity.getEventFiringAddress(), null,eventsFlags);
			} catch (Throwable e) {
				tracer.severe("Failed to fire event",e);
				// event not fired due to error
				processCancelNotHandled(cancelSTW,req.getRequest());
			}
		}
	}

	private void processCancelNotHandled(ServerTransactionWrapper cancelSTW, Request r) {
		try {
			cancelSTW.getWrappedServerTransaction().sendResponse(providerWrapper.getMessageFactory().createResponse(Response.CALL_OR_TRANSACTION_DOES_NOT_EXIST,r));
		} catch (Throwable e) {
			tracer.severe(e.getMessage(),e);
		}
		// we may need to delete it manually, since the stack does not do it for early server dialogs
		// TODO confirm the above statement
		final Dialog d = cancelSTW.getDialog();
		if (d != null) {
			d.delete();
		}
	}
	
	/**
	 * 
	 * @param requestEvent
	 * @param fineTrace
	 * @return
	 */
	private ServerTransactionWrapper getServerTransactionWrapper(RequestEvent requestEvent, boolean fineTrace) {
		ServerTransactionWrapper stw = null;
		final ServerTransaction st = requestEvent.getServerTransaction();
		if (st != null) {
			stw = (ServerTransactionWrapper) st.getApplicationData();
			if (stw == null) {
				// new server tx which needs to be wrapped
				stw = new ServerTransactionWrapper(st,this);
				if (fineTrace) {
					tracer.fine("New server transaction "+stw);
				}
			}
			else {
				// ensure the stw is linked to the ra
				stw.setResourceAdaptor(this);
			}
		}
		else {
			if (requestEvent.getDialog() != null && inLocalMode()) {
				if (fineTrace) {
					tracer.fine("Got in-dialog request with null server transaction, in local mode must be a retransmission, thus dropping. Request: "+requestEvent.getRequest());
				}
				return null;
			}
			if (!requestEvent.getRequest().getMethod().equals(Request.ACK)) {
				try {
					stw = new ServerTransactionWrapper(provider.getNewServerTransaction(requestEvent.getRequest()),this);
				} catch (Throwable e) {
					if (fineTrace) {
						tracer.fine(e.getMessage(),e);
					}
				}
			}
			else {
				// create fake ack server transaction
				stw = new ServerTransactionWrapper(new ACKDummyTransaction(requestEvent.getRequest()),this);
				if (fineTrace) {
						tracer.fine("New ACK server transaction "+stw);
				}
			}
		}
		return stw;
	}
	
	/**
	 * 
	 * @param req
	 * @param infoTrace
	 */
	private void processNotCancelRequest(RequestEvent req, boolean infoTrace) {
		
		final boolean fineTrace = tracer.isFineEnabled();
		
		// get dialog wrapper
		final Dialog d = req.getDialog();
		final DialogWrapper dw = (d == null ? null : (DialogWrapper) d.getApplicationData());
				
		// get server tx wrapper
		final ServerTransactionWrapper stw = getServerTransactionWrapper(req,fineTrace);
		if (stw == null) {
			return;
		}
		
		Wrapper activity = dw;
		if (activity == null) {
			activity = stw;
			stw.setActivity(true);
			if (!addActivity(activity, false,fineTrace)) {
				sendErrorResponse(req.getServerTransaction(), req.getRequest(),
						Response.SERVER_INTERNAL_ERROR,
						"Failed to deliver request event to JAIN SLEE container");
				return;
			}
		}
		else {
			if (!inLocalMode()) {
				// ensure the dw is linked to the ra and wrapped dialog
				dw.setResourceAdaptor(this);
				dw.setWrappedDialog(d);
			}
			// add tx to dialog
			dw.addOngoingTransaction(stw);
		}
		
		int eventFlags = DEFAULT_EVENT_FLAGS;
		if (stw.isAckTransaction()) {
			// ack txs are not terminated by stack, so we need to rely on the event release callback
			eventFlags = UNREFERENCED_EVENT_FLAGS;
		}				
		
		final FireableEventType eventType = eventIdCache.getEventId(eventLookupFacility, req.getRequest(), dw != null);
		final RequestEventWrapper rew = new RequestEventWrapper(this.providerWrapper,stw,dw,req.getRequest());
		
		if (eventIDFilter.filterEvent(eventType)) {
			if (fineTrace) {
				tracer.fine("Event " + (eventType==null?"null":eventType.getEventType()) + " filtered");
			}
			// event was filtered
			if (!stw.isAckTransaction()) {
				sendErrorResponse(req.getServerTransaction(), req.getRequest(),
						Response.SERVER_INTERNAL_ERROR,
						"Failed to deliver request event to JAIN SLEE container");
			}
			else {
				// stack won't do it for us
				processTransactionTerminated(stw);
			}
		} else {
			try {
				sleeEndpoint.fireEvent(activity.getActivityHandle(), eventType, rew, activity.getEventFiringAddress(), null,eventFlags);
			} catch (Throwable e) {
				tracer.severe("Failed to fire event",e);
				// event not fired due to error
				if (!stw.isAckTransaction()) {
					sendErrorResponse(req.getServerTransaction(), req.getRequest(),
							Response.SERVER_INTERNAL_ERROR,
							"Failed to deliver request event to JAIN SLEE container");
				}
				else {
					// stack won't do it for us
					processTransactionTerminated(stw);
				}
			}
		}
			
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sip.SipListener#processResponse(javax.sip.ResponseEvent)
	 */
	public void processResponse(ResponseEvent responseEvent) {
		
		final boolean infoTrace = tracer.isInfoEnabled();
		final boolean fineTrace = tracer.isFineEnabled();
		
		if (infoTrace) {
			tracer.info("Received Response:\n"+responseEvent.getResponse());
		}

		final Response response = responseEvent.getResponse();

		// get the activity handle and see if we need event unreferenced callback
		SipActivityHandle handle = null;
		Address address = null;
		ClientTransactionWrapper ctw = null;
		final Dialog d = responseEvent.getDialog();
		final DialogWrapper dw = (d == null ? null : (DialogWrapper) d.getApplicationData());
		if (dw != null) {
			if (!inLocalMode()) {
				// ensure the dw is linked to the ra and wrapped dialog
				dw.setResourceAdaptor(this);
				dw.setWrappedDialog(d);
			}
		}
				
		boolean requestEventUnreferenced = false;
		final ClientTransaction ct = responseEvent.getClientTransaction();
		if (ct == null) {
			// now it gets serious
			final FromHeader fromHeader = (FromHeader) response.getHeader(FromHeader.NAME);
			address = ClientTransactionWrapper.getEventFiringAddress(fromHeader.getAddress());
			if (dw == null) {
				// tricky, it may be a late response for a dialog fork, an early dialog that died in another node
				// or it may be a client tx retransmission or lost in a cluster member which went down, lets find out
				final String localTag = fromHeader.getTag();
				if (localTag != null && isDialogConfirmation(response)) {
					// dialog was not found, but we have a tag and it's a dialog confirmation
					final DialogWithoutIdActivityHandle masterHandle = new DialogWithoutIdActivityHandle(((CallIdHeader) response.getHeader(CallIdHeader.NAME)).getCallId(), localTag, null);
					if (activityManagement.get(masterHandle) != null) {
						// and we found the master, it's a late response, ack and bye
						if (fineTrace) {
							tracer.fine("Received a 2xx response without a client transaction, but found a master dialog so it's a late fork response, sending ack and bye.");
						}
						processLateDialogFork2xxResponse(response);
					}
				}
			}
			else {
				// in dialog retransmissions should be filtered 
				if (fineTrace) {
					tracer.fine("Received "+response.getStatusCode()+" in-dialog response without a client transaction, dropping, should be a retransmission.");
				}
			}
			return;				
		}
		else {
			ctw = (ClientTransactionWrapper) ct.getApplicationData();
			if (ctw == null) {
				tracer.severe("Dropping response without app data in client tx, can't proceed");
				return;
			}
			if (fineTrace) {
				tracer.fine("Received "+response.getStatusCode()+" response on existent client transaction "+ctw.getActivityHandle());
			}
			// ensure the ctw is bound to this ra object
			ctw.setResourceAdaptor(this);
			// get address where to fire event
			address = ctw.getEventFiringAddress();
			//FIXME: determine if we should check here if dw is also an activity and fire on it.
			//specs do not cover this. See SleeSipProviderImpl.getNewDialog(Transaction);
			if (dw != null) {
				handle = dw.getActivityHandle();
				// (client tx that has a final response and is not an activity must be
				// cleared on this callback, not on transaction terminated event)
				requestEventUnreferenced = response.getStatusCode() > 199;
			}
			else {
				handle = ctw.getActivityHandle();
			}
		}
						
		int eventFlags = DEFAULT_EVENT_FLAGS;
		if (requestEventUnreferenced) {
			eventFlags = UNREFERENCED_EVENT_FLAGS;
		}
		
		if (dw == null || !dw.processIncomingResponse(responseEvent)) {
			final ResponseEventWrapper rew = new ResponseEventWrapper(this.providerWrapper, ctw, dw, response);
			final FireableEventType eventType = eventIdCache.getEventId(eventLookupFacility, response);
			if (eventIDFilter.filterEvent(eventType)) {
				if (fineTrace) {
					tracer.fine("Event " + (eventType == null?"null":eventType.getEventType()) + " filtered");
				}
				// event filtered
				if (requestEventUnreferenced) {
					// event was filtered, consider it is unreferenced now
					processResponseEventUnreferenced(rew);
				}
			} else {
				try {
					sleeEndpoint.fireEvent(handle, eventType, rew, address, null,eventFlags);
				} catch (Throwable e) {
					tracer.severe("Failed to fire event",e);
					// event not fired due to error
					if (requestEventUnreferenced) {
						// consider event is unreferenced now
						processResponseEventUnreferenced(rew);
					}
				}
			}
		}
		
		if ((response.getStatusCode() == 481 || response.getStatusCode() == 408) && dw != null) {
			final String method = ((CSeqHeader) response.getHeader(CSeqHeader.NAME)).getMethod();
			if (!method.equals(Request.INVITE) || !method.equals(Request.SUBSCRIBE)) {
				try {
					this.provider.sendRequest(dw.createRequest(Request.BYE));
				} catch (Throwable e) {
					tracer.severe(e.getMessage(),e);
				}
			}
		}
		
	}
	
	/**
	 * @param response
	 * @return
	 */
	private boolean isDialogConfirmation(Response response) {
		final CSeqHeader cSeqHeader = (CSeqHeader) response.getHeader(CSeqHeader.NAME);
		if (!cSeqHeader.getMethod().equals(Request.INVITE) && !cSeqHeader.getMethod().equals(Request.SUBSCRIBE)) {
			// not a dialog creating method
			return false;
		}
		if (cSeqHeader.getSeqNumber() != 1) {
			return false;
		}
		if (response.getStatusCode() < 300 && response.getStatusCode() > 199) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * @param response
	 */
	private void processLateDialogFork2xxResponse(Response response) {


		final SleeSipProviderImpl provider = getProviderWrapper();

		try {
			final CSeqHeader cseq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);
			List<RouteHeader> routeSet = org.mobicents.slee.resource.sip11.Utils.getRouteList(response,provider.getHeaderFactory());
			//RFC3261 8.1.1.1
			URI requestURI = org.mobicents.slee.resource.sip11.Utils.getRequestUri(response, provider.getAddressFactory());
			String branch = ((ViaHeader)response.getHeaders(ViaHeader.NAME).next()).getBranch();

			long cseqNumber = cseq.getSeqNumber();

			if (requestURI == null) {
				tracer.severe("Cannot ack on request that has empty contact!!!!");
				return;
			}

			MaxForwardsHeader mf = provider.getHeaderFactory().createMaxForwardsHeader(70);
			List<ViaHeader> lst = new ArrayList<ViaHeader>(1);
			final ViaHeader localViaHeader = provider.getLocalVia();
			localViaHeader.setBranch(branch);
			lst.add(localViaHeader);
			Request forgedRequest = provider.getMessageFactory().createRequest(requestURI,Request.ACK,(CallIdHeader)response.getHeader(CallIdHeader.NAME),provider.getHeaderFactory().createCSeqHeader(cseqNumber, Request.ACK),
					(FromHeader)response.getHeader(FromHeader.NAME)	,(ToHeader)response.getHeader(ToHeader.NAME),lst,mf	);
			for (Header h : routeSet) {
				forgedRequest.addLast(h);
			}

			//forgedRequest.addHeader(this.provider.getLocalVia(this.provider.getListeningPoints()[0].getTransport(), branch));

			if (tracer.isInfoEnabled()) {
				tracer.info("Sending request:\n"+forgedRequest);
			}
			provider.sendRequest(forgedRequest);

			//forgedRequest = this.provider.getMessageFactory().createRequest(null);
			lst = new ArrayList<ViaHeader>();
			lst.add(provider.getLocalVia());
			requestURI = org.mobicents.slee.resource.sip11.Utils.getRequestUri(response,provider.getAddressFactory());
			forgedRequest = provider.getMessageFactory().createRequest(requestURI,Request.BYE,(CallIdHeader)response.getHeader(CallIdHeader.NAME),provider.getHeaderFactory().createCSeqHeader(cseqNumber+1, Request.BYE),
					(FromHeader)response.getHeader(FromHeader.NAME)	,(ToHeader)response.getHeader(ToHeader.NAME),lst,mf	);

			for (Header h : routeSet) {
				forgedRequest.addLast(h);
			}

			//forgedRequest.addHeader(this.provider.getLocalVia(this.provider.getListeningPoints()[0].getTransport(), null));
			// ITS BUG....
			((SIPRequest) forgedRequest).setMethod(Request.BYE);
			if (tracer.isInfoEnabled()) {
				tracer.info("Sending request:\n"+forgedRequest);
			}
			provider.sendRequest(forgedRequest);

			// response.get
		} catch (Exception e) {
			tracer.severe(e.getMessage(),e);
		}


		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.SipListener#processTimeout(javax.sip.TimeoutEvent)
	 */
	public void processTimeout(TimeoutEvent arg0) {

		if (tracer.isInfoEnabled()) {
			if (arg0.isServerTransaction()) {
				tracer.info("Server transaction "
						+ arg0.getServerTransaction().getBranchId()
						+ " timer expired");
			} else {
				tracer.info("Client transaction "
						+ arg0.getClientTransaction().getBranchId()
						+ " timer expired");
			}
		}

		final Transaction t = arg0.isServerTransaction() ? arg0.getServerTransaction() : arg0.getClientTransaction();
		final TransactionWrapper tw = (TransactionWrapper) t.getApplicationData();
		if (tw == null) {
			tracer.severe("FAILURE on processTimeout. Unexpected app data["
						+ t.getApplicationData() + "] branch["+t.getBranchId()+"]");			
			return;
		} 
		// ensure tw is linked to this ra object
		tw.setResourceAdaptor(this);
		
		TimeoutEventWrapper tew = null;
		if (arg0.isServerTransaction()) {
			tew = new TimeoutEventWrapper(providerWrapper,(ServerTransaction)tw, arg0.getTimeout());
		}
		else {
			tew = new TimeoutEventWrapper(providerWrapper,(ClientTransaction) tw, arg0.getTimeout());
		}
		
		final Dialog d = tw.getWrappedTransaction().getDialog();
		final DialogWrapper dw = (d != null ? (DialogWrapper) d.getApplicationData() : null);
		final FireableEventType eventType = eventIdCache.getTransactionTimeoutEventId(
				eventLookupFacility, dw != null);
		if (!eventIDFilter.filterEvent(eventType)) {
			Wrapper activity = tw.isActivity() ? tw : dw;
			if (dw != null) {
				if (!inLocalMode()) {
					// ensure the dw is linked to the ra and wrapped dialog
					dw.setResourceAdaptor(this);
					dw.setWrappedDialog(d);
				}
			}
			try {
				sleeEndpoint.fireEvent(activity.getActivityHandle(), eventType, tew, activity.getEventFiringAddress(), null,DEFAULT_EVENT_FLAGS);
			} catch (Throwable e) {
				tracer.severe("Failed to fire event",e);
			}
		}
		else {
			if (tracer.isFineEnabled()) {
				tracer.fine("Event "+eventType+" filtered.");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.SipListener#processTransactionTerminated(javax.sip.TransactionTerminatedEvent)
	 */
	public void processTransactionTerminated(
			TransactionTerminatedEvent txTerminatedEvent) {

		Transaction t = null;
		if (txTerminatedEvent.isServerTransaction()) {
			t = txTerminatedEvent.getServerTransaction();
		} else {
			t = txTerminatedEvent.getClientTransaction();			
		}
		
		final TransactionWrapper tw = (TransactionWrapper) t.getApplicationData();		
		if (tw != null) {
			if (tracer.isInfoEnabled()) {
				tracer.info("SIP Transaction "+tw.getActivityHandle()+" terminated");
			}
			processTransactionTerminated(tw);
		}
		else {
			if (tracer.isInfoEnabled()) {
				tracer.info("SIP Transaction "+t.getBranchId()+':'+t.getRequest().getMethod()+" terminated");
			}
		}	
	}

	private void processTransactionTerminated(TransactionWrapper tw) {
		tw.terminated();
		if (tw.isActivity()) {
			tw.setResourceAdaptor(this);
			sendActivityEndEvent(tw);
		}
		else {
			if (!tw.isClientTransaction()) {
				// client txs which are not activity are cleared on event unreferenced callbacks
				tw.clear();
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sip.SipListener#processDialogTerminated(javax.sip.DialogTerminatedEvent)
	 */
	public void processDialogTerminated(DialogTerminatedEvent dte) {
		final Dialog d = dte.getDialog();
		if (d!= null) {
			DialogWrapper dw = (DialogWrapper) d.getApplicationData();
			if (dw != null) {
				if (!inLocalMode()) {
					// ensure the dw is linked to the ra and wrapped dialog
					dw.setResourceAdaptor(this);
					dw.setWrappedDialog(d);
				}
				processDialogTerminated(dw);
			} else {
				if (tracer.isFineEnabled()) {
					tracer.fine("DialogTerminatedEvent dropped due to null app data.");
				}
			}
		}
	}

	/**
	 * 
	 * @param dw
	 */
	public void processDialogTerminated(DialogWrapper dw) {
		if (!dw.isEnding()) {
			if (tracer.isInfoEnabled()) {
				tracer.info("SIP Dialog " + dw.getActivityHandle() + " terminated");
			}
			if (!sendActivityEndEvent(dw)) {
				tracer.warning("Dialog activity that ended not found.");
			}
			dw.ending();
		}
	}

	// *************** Event Life cycle

	/**
	 * 
	 */
	public boolean sendActivityEndEvent(Wrapper activity) {
		try {
			activity.ending();
			sleeEndpoint.endActivity(activity.getActivityHandle());
			return true;
		} catch (Exception e) {
			tracer.severe(e.getMessage(),e);
		}
		return false;
	}
		
	/**
	 * 
	 * @param wrapperActivity
	 * @param transacted
	 * @param fineTrace
	 * @return
	 */
	public boolean addActivity(Wrapper wrapperActivity, boolean transacted, boolean fineTrace) {

		if (fineTrace) {
			tracer.fine("Adding sip activity handle " + wrapperActivity.getActivityHandle());
		}

		final int activityFlags = wrapperActivity.isDialog() ? MARSHABLE_ACTIVITY_FLAGS : NON_MARSHABLE_ACTIVITY_FLAGS;
		try {
			if (transacted) {
				sleeEndpoint.startActivityTransacted(wrapperActivity.getActivityHandle(),wrapperActivity,activityFlags);
			}
			else {
				sleeEndpoint.startActivity(wrapperActivity.getActivityHandle(),wrapperActivity,activityFlags);
			}
		}
		catch (Throwable e) {
			tracer.severe(e.getMessage(),e);
			return false;
		}
		activityManagement.put(wrapperActivity.getActivityHandle(), wrapperActivity);
		return true;
	}
	
	/**
	 * 
	 * @param wrapperActivity
	 * @param fineTrace
	 * @return
	 */
	public boolean addSuspendedActivity(Wrapper wrapperActivity, boolean fineTrace) {

		if (fineTrace) {
			tracer.fine("Adding suspended sip activity handle " + wrapperActivity.getActivityHandle());
		}

		final int activityFlags = wrapperActivity.isDialog() ? MARSHABLE_ACTIVITY_FLAGS : NON_MARSHABLE_ACTIVITY_FLAGS;

		try {
			sleeEndpoint.startActivitySuspended(wrapperActivity.getActivityHandle(),wrapperActivity,activityFlags);
		}
		catch (Throwable e) {
			tracer.severe(e.getMessage(),e);
			return false;
		}
		activityManagement.put(wrapperActivity.getActivityHandle(), wrapperActivity);
		return true;
	}

	// ------- END OF PROVISIONING

	// --- XXX - error responses to be a good citizen
	private void sendErrorResponse(ServerTransaction serverTransaction,
			Request request, int code, String msg) {
		if (!request.getMethod().equals(Request.ACK)) {
			try {
				ContentTypeHeader contentType = this.providerWrapper
						.getHeaderFactory().createContentTypeHeader("text",
								"plain");
				Response response = providerWrapper.getMessageFactory()
						.createResponse(code, request, contentType,
								msg.getBytes());
				if (serverTransaction != null) {
					serverTransaction.sendResponse(response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// LIFECYLE
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raActive()
	 */
	public void raActive() {
		
		try {
			final Properties properties = new Properties();
			// load properties for the stack
			properties.load(getClass().getResourceAsStream("sipra.properties"));
			// now load config properties
			properties.setProperty(SIP_BIND_ADDRESS, this.stackAddress);
			// setting the ra entity name as the stack name
			properties.setProperty(STACK_NAME_BIND, raContext.getEntityName());
			properties.setProperty(TRANSPORTS_BIND, transportsProperty);
			properties.setProperty(SIP_PORT_BIND, Integer.toString(this.port));
			if (sipBalancerHeartBeatServiceClassName != null) {
				properties.setProperty(LOAD_BALANCER_HEART_BEAT_SERVICE_CLASS, sipBalancerHeartBeatServiceClassName);
			}
			if (balancers != null) {
				properties.setProperty(BALANCERS, balancers);
			}
			if (loadBalancerElector != null) {
				properties.setProperty(LoadBalancerElector.IMPLEMENTATION_CLASS_NAME_PROPERTY, loadBalancerElector);
			}
			// define impl of the cache  of the HA stack
			properties.setProperty(ClusteredSipStack.CACHE_CLASS_NAME_PROPERTY,SipResourceAdaptorMobicentsSipCache.class.getName());
			this.sipFactory = SipFactory.getInstance();
			this.sipFactory.setPathName("org.mobicents.ha");
			this.sipStack = (ClusteredSipStack) this.sipFactory.createSipStack(properties);
			this.sipStack.start();
			if (inLocalMode()) {
				activityManagement = new LocalSipActivityManagement();
			}
			else {
				activityManagement = new ClusteredSipActivityManagement(sipStack,ftRaContext.getReplicateData(true),raContext.getSleeTransactionManager()); 
			}
			
			if (tracer.isFineEnabled()) {
				tracer
						.fine("---> START "
								+ Arrays.toString(transports.toArray()));
			}
			boolean created = false;
			for (String trans : transports) {
				ListeningPoint lp = this.sipStack.createListeningPoint(
						this.stackAddress, this.port, trans);
				if (!created) {
					this.provider = this.sipStack.createSipProvider(lp);
					this.provider.addSipListener(this);
					created = true;
				} else
					this.provider.addListeningPoint(lp);
			}

			// LETS CREATE FP
			// SipFactory sipFactory = SipFactory.getInstance();
			AddressFactory addressFactory = sipFactory.createAddressFactory();
			HeaderFactory headerFactory = sipFactory.createHeaderFactory();
			MessageFactory messageFactory = sipFactory.createMessageFactory();

			this.providerWrapper = new SleeSipProviderImpl(addressFactory,
					headerFactory, messageFactory, sipStack, this, provider);

		} catch (Throwable ex) {
			String msg = "error in initializing resource adaptor";
			tracer.severe(msg, ex);	
			throw new RuntimeException(msg,ex);
		}		
		
		if (tracer.isFineEnabled()) {
			tracer.fine("Sip Resource Adaptor entity active.");
		}	
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raInactive()
	 */
	public void raInactive() {
		
		this.provider.removeSipListener(this);
		
		ListeningPoint[] listeningPoints = this.provider.getListeningPoints();
		
		for (int i = 0; i < listeningPoints.length; i++) {
			ListeningPoint lp = listeningPoints[i];
			for (int k = 0; k < 10; k++) {
				try {
					this.sipStack.deleteListeningPoint(lp);
					this.sipStack.deleteSipProvider(this.provider);
					break;
				} catch (ObjectInUseException ex) {
					tracer
							.severe(
									"Object in use -- retrying to delete listening point",
									ex);
					try {
						Thread.sleep(100);
					} catch (Exception e) {

					}
				}
			}
		}

		if (tracer.isFineEnabled()) {
			tracer.fine("Sip Resource Adaptor entity inactive.");
		}		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raStopping()
	 */
	public void raStopping() {
		if (tracer.isFineEnabled()) {
			tracer.fine("Object for entity named "+raContext.getEntityName()+" is stopping. "+activityManagement);
		}
		
	}
	
	//	EVENT PROCESSING CALLBACKS
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#eventProcessingFailed(javax.slee.resource.ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object, javax.slee.Address, javax.slee.resource.ReceivableService, int, javax.slee.resource.FailureReason)
	 */
	public void eventProcessingFailed(ActivityHandle ah,
			FireableEventType arg1, Object event, Address arg3,
			ReceivableService arg4, int arg5, FailureReason arg6) {
		
		if (event.getClass() == CancelRequestEvent.class) {
			
			// PROCESSING FAILED, WE HAVE TO SEND 481 response to CANCEL
			try {
				Response txDoesNotExistsResponse = this.providerWrapper
				.getMessageFactory().createResponse(
						Response.CALL_OR_TRANSACTION_DOES_NOT_EXIST,
						((CancelRequestEvent) event).getRequest());
				ServerTransactionWrapper stw = (ServerTransactionWrapper) getActivity(ah);
				// provider.sendResponse(txDoesNotExistsResponse);
				stw.sendResponse(txDoesNotExistsResponse);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#eventProcessingSuccessful(javax.slee.resource.ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object, javax.slee.Address, javax.slee.resource.ReceivableService, int)
	 */
	public void eventProcessingSuccessful(ActivityHandle arg0,
			FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5) {
		// not used		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#eventUnreferenced(javax.slee.resource.ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object, javax.slee.Address, javax.slee.resource.ReceivableService, int)
	 */
	public void eventUnreferenced(ActivityHandle handle, FireableEventType eventType,
			Object event, Address arg3, ReceivableService arg4, int arg5) {

		if(tracer.isFineEnabled())	{
			tracer.fine("Event Unreferenced. Handle = "+handle+", type = "+eventType.getEventType()+", event = "+event);
		}

		if(event instanceof ResponseEventWrapper) {
			final ResponseEventWrapper rew = (ResponseEventWrapper) event;
			processResponseEventUnreferenced(rew);	
		}
		else if(event instanceof RequestEventWrapper) {
			final RequestEventWrapper rew = (RequestEventWrapper) event;
			final ServerTransactionWrapper stw = (ServerTransactionWrapper) rew.getServerTransaction();
			if (stw.isAckTransaction()) {
				processTransactionTerminated(stw);
			}
			else {
				final Request r = rew.getRequest();
				if (r.getMethod().equals(Request.CANCEL)) {
					// lets be a good citizen and reply if no sbb has done
					if (stw.getState() != TransactionState.TERMINATED) {
						processCancelNotHandled(stw,r);
					}
				}
			}
		}
	}
	
	/**
	 * @param rew
	 */
	private void processResponseEventUnreferenced(ResponseEventWrapper rew) {
		final ClientTransactionWrapper ctw  = (ClientTransactionWrapper) rew.getClientTransaction();		
		if (!ctw.isActivity()) {
			// a client tx that is not activity must be removed from dialog here
			ctw.getDialogWrapper().removeOngoingTransaction(ctw);
			ctw.clear();
		}
		else {
			// end the activity
			sendActivityEndEvent(activityManagement.get(ctw.getActivityHandle()));
		}
	}

	//	RA CONFIG
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raConfigurationUpdate(javax.slee.resource.ConfigProperties)
	 */
	public void raConfigurationUpdate(ConfigProperties properties) {
		raConfigure(properties);		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raConfigure(javax.slee.resource.ConfigProperties)
	 */
	public void raConfigure(ConfigProperties properties) {
		
		if (tracer.isFineEnabled()) {
			tracer.fine("Configuring RA.");
		}
		
		this.port = (Integer) properties.getProperty(SIP_PORT_BIND).getValue();

		this.stackAddress = (String) properties.getProperty(SIP_BIND_ADDRESS).getValue();
		if (this.stackAddress.equals("")) {
			this.stackAddress = System.getProperty("jboss.bind.address");				
		}

		this.balancers = (String) properties.getProperty(BALANCERS).getValue();
		if (this.balancers.equals("")) {
			this.balancers = null;
		}
		else {
			this.sipBalancerHeartBeatServiceClassName = (String) properties.getProperty(LOAD_BALANCER_HEART_BEAT_SERVICE_CLASS).getValue();
			if (this.sipBalancerHeartBeatServiceClassName.equals("")) {
				throw new IllegalArgumentException("invalid "+LOAD_BALANCER_HEART_BEAT_SERVICE_CLASS+" property value");				
			}
		}
				
		this.loadBalancerElector = (String) properties.getProperty(LoadBalancerElector.IMPLEMENTATION_CLASS_NAME_PROPERTY).getValue();
		if (this.loadBalancerElector.equals("")) {
			this.loadBalancerElector = null;				
		}
		
		this.transportsProperty = (String) properties.getProperty(TRANSPORTS_BIND).getValue();
		for (String transport : this.transportsProperty.split(",")) {
			this.transports.add(transport);
		}

		Property p = properties.getProperty(LOOSE_DIALOG_VALIDATION);
		if(p!=null && p.getValue()!=null)
		{
			this.looseDialogSeqValidation = (Boolean) p.getValue();
		}
		
		tracer.info("RA entity named "+raContext.getEntityName()+" bound to port " + this.port);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raUnconfigure()
	 */
	public void raUnconfigure() {		
		this.port = -1;
		this.stackAddress = null;
		this.transports.clear();
		this.balancers = null;
		this.loadBalancerElector = null;
		this.sipBalancerHeartBeatServiceClassName= null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raVerifyConfiguration(javax.slee.resource.ConfigProperties)
	 */
	public void raVerifyConfiguration(ConfigProperties properties)
			throws InvalidConfigurationException {
		
		try {
			// get port
			Integer port = (Integer) properties.getProperty(SIP_PORT_BIND).getValue();
			// get host
			String stackAddress = (String) properties.getProperty(SIP_BIND_ADDRESS).getValue();
			if (stackAddress.equals("")) {
				stackAddress = System.getProperty("jboss.bind.address");				
			}
			// try to open socket
			InetSocketAddress sockAddress = new InetSocketAddress(stackAddress,
					port);
			new DatagramSocket(sockAddress).close();
			// check transports			
			String transports = (String) properties.getProperty(TRANSPORTS_BIND).getValue();
			String[] transportsArray = transports.split(",");
			boolean validTransports = true;
			if (transportsArray.length > 0) {
				for (String transport : transportsArray) {
					if (!allowedTransports.contains(transport.toLowerCase()))
						validTransports = false;
					break;
				}
			}
			else {
				validTransports = false;
			}
			if (!validTransports) {
				throw new IllegalArgumentException(TRANSPORTS_BIND+" config property with invalid value: "+transports);
			}
			// get balancer heart beat class name
			String sipBalancerHeartBeatServiceClassName = (String) properties.getProperty(LOAD_BALANCER_HEART_BEAT_SERVICE_CLASS).getValue();
			if (!sipBalancerHeartBeatServiceClassName.equals("")) {
				// check class is available
				Class.forName(sipBalancerHeartBeatServiceClassName);				
			}
			// get balancer elector class name
			String sipBalancerElectorClassName = (String) properties.getProperty(LoadBalancerElector.IMPLEMENTATION_CLASS_NAME_PROPERTY).getValue();
			if (!sipBalancerElectorClassName.equals("")) {
				// check class is available
				Class.forName(sipBalancerElectorClassName);				
			}
		}
		catch (Throwable e) {
			throw new InvalidConfigurationException(e.getMessage(),e);
		}
	}
	
	//	EVENT FILTERING
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#serviceActive(javax.slee.resource.ReceivableService)
	 */
	public void serviceActive(ReceivableService receivableService) {
		eventIDFilter.serviceActive(receivableService);		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#serviceInactive(javax.slee.resource.ReceivableService)
	 */
	public void serviceInactive(ReceivableService receivableService) {
		eventIDFilter.serviceInactive(receivableService);	
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#serviceStopping(javax.slee.resource.ReceivableService)
	 */
	public void serviceStopping(ReceivableService receivableService) {
		eventIDFilter.serviceStopping(receivableService);
		
	}
	
	// RA CONTEXT
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#setResourceAdaptorContext(javax.slee.resource.ResourceAdaptorContext)
	 */
	public void setResourceAdaptorContext(ResourceAdaptorContext raContext) {
		this.raContext = raContext;		
		this.tracer = raContext.getTracer("SipResourceAdaptor");
		this.sleeEndpoint = raContext.getSleeEndpoint();
		this.eventLookupFacility = raContext.getEventLookupFacility();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#unsetResourceAdaptorContext()
	 */
	public void unsetResourceAdaptorContext() {
		this.raContext = null;
		this.sleeEndpoint = null;
		this.eventLookupFacility = null;
	}

	/**
	 * 
	 * @return
	 */
	public EventLookupFacility getEventLookupFacility() {
		return eventLookupFacility;
	}
	
	/**
	 * 
	 * @return
	 */
	public SleeEndpoint getSleeEndpoint() {
		return sleeEndpoint;
	}
	
	/**
	 * 
	 * @param tracerName
	 * @return
	 */
	public Tracer getTracer(String tracerName) {
		return raContext.getTracer(tracerName);
	}
	
	/**
	 * @return the activityManagement
	 */
	public SipActivityManagement getActivityManagement() {
		return activityManagement;
	}
	
	// ACTIVITY MANAGEMENT
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#activityEnded(javax.slee.resource.ActivityHandle)
	 */
	public void activityEnded(ActivityHandle activityHandle) {
		final Wrapper activity = activityManagement.remove((SipActivityHandle) activityHandle);
		if (activity != null) {
			activity.clear();			
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#administrativeRemove(javax.slee.resource.ActivityHandle)
	 */
	public void administrativeRemove(ActivityHandle activityHandle) {
		// TODO
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#activityUnreferenced(javax.slee.resource.ActivityHandle)
	 */
	public void activityUnreferenced(ActivityHandle arg0) {
		// not used		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getActivity(javax.slee.resource.ActivityHandle)
	 */
	public Object getActivity(ActivityHandle arg0) {
		final Wrapper w = activityManagement.get((SipActivityHandle)arg0);
		if (w != null) {
			w.setResourceAdaptor(this);
		}
		return w;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getActivityHandle(java.lang.Object)
	 */
	public ActivityHandle getActivityHandle(Object activity) {
		if (activity instanceof Wrapper) {
			final Wrapper w = (Wrapper) activity;
			return w != null ? w.getActivityHandle() : null;
		}
		else {
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#queryLiveness(javax.slee.resource.ActivityHandle)
	 */
	public void queryLiveness(ActivityHandle arg0) {
		final SipActivityHandle handle = (SipActivityHandle) arg0;
		final Wrapper activity = activityManagement.get(handle);
		if (activity == null || activity.isEnding()) {
			this.sleeEndpoint.endActivity(arg0);
		}		
	}
	
	// OTHER GETTERS
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getResourceAdaptorInterface(java.lang.String)
	 */
	public Object getResourceAdaptorInterface(String raTypeSbbInterfaceclassName) {
		// this ra implements a single ra type
		return providerWrapper;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getMarshaler()
	 */
	public Marshaler getMarshaler() {
		return marshaler;
	}	
	
	public SleeSipProviderImpl getProviderWrapper() {
		return providerWrapper;
	}

	/**
	 * @return the eventIdCache
	 */
	public EventIDCache getEventIdCache() {
		return eventIdCache;
	}
	
	/**
	 * @return the eventIDFilter
	 */
	public EventIDFilter getEventIDFilter() {
		return eventIDFilter;
	}
	/**
	 * 
	 * @return true if jsip dialog should validate cseq.
	 */
	public boolean isValidateDialogCSeq() {
		return looseDialogSeqValidation;
	}


	// CLUSTERING
	
	/**
	 * Indicates if the RA is running in local mode or in a clustered environment
	 * @return true if the RA is running in local mode, false if is running in a clustered environment
	 */
	public boolean inLocalMode() {
		return sipStack.getSipCache().inLocalMode();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor#dataRemoved(java.io.Serializable)
	 */
	public void dataRemoved(SipActivityHandle handle) {
		// if we get this callback ensure the handle is removed from activity management, or a leak could occurr
		// if the handle was used locally but removed remotely
		activityManagement.remove(handle);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor#failOver(java.io.Serializable)
	 */
	public void failOver(SipActivityHandle activityHandle) {
		// not used
	}
	
	private FaultTolerantResourceAdaptorContext<SipActivityHandle, String> ftRaContext;
	
	public void setFaultTolerantResourceAdaptorContext(
			FaultTolerantResourceAdaptorContext<SipActivityHandle, String> context) {
		this.ftRaContext = context;
	}
	
	public void unsetFaultTolerantResourceAdaptorContext() {
		this.ftRaContext = null;
	}
}