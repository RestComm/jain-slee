package org.mobicents.slee.services.sip.proxy;

import gov.nist.javax.sip.address.SipUri;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.ClientTransaction;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipProvider;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionState;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.RecordRouteHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.ChildRelation;
import javax.slee.CreateException;
import javax.slee.InitialEventSelector;
import javax.slee.RolledBackContext;
import javax.slee.SLEEException;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.serviceactivity.ServiceActivity;
import javax.slee.serviceactivity.ServiceActivityFactory;

import net.java.slee.resource.sip.CancelRequestEvent;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.services.sip.common.MessageHandlerInterface;
import org.mobicents.slee.services.sip.common.MessageUtils;
import org.mobicents.slee.services.sip.common.ProxyConfiguration;
import org.mobicents.slee.services.sip.common.SipLoopDetectedException;
import org.mobicents.slee.services.sip.common.SipSendErrorResponseException;
import org.mobicents.slee.services.sip.location.LocationSbbLocalObject;
import org.mobicents.slee.services.sip.location.LocationService;
import org.mobicents.slee.services.sip.location.LocationServiceException;
import org.mobicents.slee.services.sip.location.RegistrationBinding;
import org.mobicents.slee.services.sip.proxy.mbean.ProxyConfigurator;

public abstract class ProxySbb implements Sbb {

	private static Logger logger = Logger.getLogger(ProxySbb.class);
	
	/**
	 * Proxy Configuration MBean
	 */
	private final static ProxyConfigurator proxyConfigurator = new ProxyConfigurator();
	
	private SbbContext sbbContext;
	private Context myEnv;

	
	private SleeSipProvider provider=null;
	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	private MessageFactory messageFactory;
	private SipActivityContextInterfaceFactory acif;
	/**
	 * Generate a custom convergence name so that events with the same call
	 * identifier will go to the same root SBB entity.
	 */
	public InitialEventSelector callIDSelect(InitialEventSelector ies) {
		Object event = ies.getEvent();
		String callId = null;
		if (event instanceof ResponseEvent) {
			ies.setInitialEvent(false);
			return ies;
		} else if (event instanceof RequestEvent) {
			// If request event, the convergence name to callId
			Request request = ((RequestEvent) event).getRequest();
			if (!request.getMethod().equals(Request.ACK)) {
				callId = ((ViaHeader) request.getHeaders(ViaHeader.NAME).next())
						.getBranch();
			} else {
				callId = ((ViaHeader) request.getHeaders(ViaHeader.NAME).next())
						.getBranch()
						+ "_ACK";
			}
		}
		// Set the convergence name
		if (logger.isDebugEnabled()) {
			logger.debug( "Setting convergence name to: " + callId);
		}
		ies.setCustomName(callId);
		
		return ies;
	}

	// ****************************** ABSTRACT PARTS ********************
	// **** SLEE Children
	
	public abstract ChildRelation getRegistrarSbbChildRelation();
	
	public abstract ChildRelation getLocationSbbChildRelation();
	
	public LocationSbbLocalObject getLocationSbb() throws TransactionRequiredLocalException, SLEEException, CreateException {		
		final ChildRelation childRelation = getLocationSbbChildRelation();
		if (childRelation.isEmpty()) {
			return (LocationSbbLocalObject) childRelation.create();
		}
		else {
			return (LocationSbbLocalObject) childRelation.iterator().next();
		}
	}
	
	// ***** Custom ACI
	
	public abstract ProxySbbActivityContextInterface asSbbActivityContextInterface(
			ActivityContextInterface ac);

	// ***** CMPs

	public abstract void setConfiguration(ProxyConfigurator pc);
	public abstract ProxyConfigurator getConfiguration();

	/**
	 * This flag tells the SBB that the transaction has been terminated, and
	 * that any further messages on this transaction (eg. ACKs after the proxy
	 * returns 404 NOT_FOUND) should be ignored and not forwarded.
	 */
	public abstract boolean getServerTransactionTerminated();
	public abstract void setServerTransactionTerminated(
			boolean transactionTerminated);

	// This is required for cancels and acks, those need the same via as
	// invites?
	public abstract void setForwardedInviteViaHeader(ViaHeader via);
	public abstract ViaHeader getForwardedInviteViaHeader();

	// ***************************** SERVICE (DE)ACTIVATION

	public void onServiceStarted(
			javax.slee.serviceactivity.ServiceStartedEvent serviceEvent,
			ActivityContextInterface aci) {
		
		try {
			// check if it's my service that is starting
			ServiceActivity sa = ((ServiceActivityFactory) myEnv
					.lookup("java:comp/env/slee/serviceactivity/factory")).getActivity();
			if (sa.equals(aci.getActivity())) {
				startMBeanConfigurator();		
			}
			else {
				aci.detach(this.sbbContext.getSbbLocalObject());
			}
		} catch (Exception e) {
			logger.error(e);
		}	

	}

	private void startMBeanConfigurator() {

		proxyConfigurator.setSipHostName(provider.getListeningPoints()[0].getIPAddress());
		proxyConfigurator.setSipPort(provider.getListeningPoints()[0].getPort());
		proxyConfigurator.setSipTransports(new String[]{provider.getListeningPoints()[0].getTransport()});

		String confValue = null;
		Context myEnv = null;
		try {
			logger.info("Building Configuration from ENV Entries");
			myEnv = (Context) new InitialContext().lookup("java:comp/env");

		} catch (NamingException ne) {

			logger.warn("Could not set SBB context:" + ne.getMessage());
			return;
		}

		// env-entries
		try {
			confValue = (String) myEnv.lookup("configuration-URI-SCHEMES");
		} catch (NamingException e) {
			logger.error(e);
		}
		if (confValue == null) {
			proxyConfigurator.addSupportedURIScheme("sip");
			proxyConfigurator.addSupportedURIScheme("sips");
		} else {
			String[] tmp = confValue.split(";");
			for (int i = 0; i < tmp.length; i++)
				proxyConfigurator.addSupportedURIScheme(tmp[i]);
		}

		confValue = null;

		try {

			confValue = (String) myEnv.lookup("configuration-LOCAL-DOMAINS");
		} catch (NamingException e) {
			logger.error(e);
		}

		if (confValue == null) {
			// Domains should be present in conf file, it shouldnt do much harm
			// to add those
			proxyConfigurator.addLocalDomain("nist.gov");
			proxyConfigurator.addLocalDomain("mobicents.org");
		} else {
			String[] tmp = confValue.split(";");
			for (int i = 0; i < tmp.length; i++)
				proxyConfigurator.addLocalDomain(tmp[i]);
		}

		String configurationName=null;
		try {
			configurationName = (String) myEnv
			.lookup("configuration-MBEAN");
			
		} catch (NamingException e) {
			logger.error(e);
		}
		
		if(configurationName!=null)
			proxyConfigurator.setName(configurationName);
		// GO ;] start service
		proxyConfigurator.startService();

	}
	
	public void onActivityEndEvent(ActivityEndEvent event,
			ActivityContextInterface aci) {
		try {
			if (aci.getActivity() instanceof ServiceActivity) {
				if (logger.isDebugEnabled()) {
					logger.debug("Service aci ending, removing mbean");
				}
				// lets remove our mbean
				SleeContainer.lookupFromJndi().getMBeanServer()
						.unregisterMBean(
								new ObjectName(
										ProxyConfiguration.MBEAN_NAME_PREFIX
												+ proxyConfigurator.getName()));
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	// ****************** EVENT HANLDERS

	public void onRegisterEvent(RequestEvent event, ActivityContextInterface ac) {

		if (logger.isDebugEnabled())
			logger.debug("Received REGISTER request, class="+ event.getClass());

		// TODO: IF NOT LOCAL DOMAIN THEN PROXY REQUEST

		// ELSE this is local domain
		// attach child to this activity
		try {
			ac.attach(getRegistrarSbbChildRelation().create());
		} catch (Exception e) {
			// failed to attach the register, send error back
			logger.error(e);
			// TODO send 500 back
		}
		// detach myself
		ac.detach(sbbContext.getSbbLocalObject());

	}

	public void onInviteEvent(RequestEvent event, ActivityContextInterface ac) {
		
		if (logger.isDebugEnabled())
			logger.debug("Received INVITE request");
		
		processRequest(event.getServerTransaction(), event.getRequest(), ac);
	}

	public void onByeEvent(RequestEvent event, ActivityContextInterface ac) {

		// getDefaultSbbUsageParameterSet().incrementNumberOfBye(1);
		if (logger.isDebugEnabled())
			logger.debug("Received BYE request");
		
		processRequest(event.getServerTransaction(), event.getRequest(), ac);
	}

	public void onCancelEvent(CancelRequestEvent event, ActivityContextInterface ac) {
		
		if (logger.isDebugEnabled())
			logger.debug("Received CANCEL request");
		
		final ServerTransaction serverTransaction = event.getServerTransaction();
		
		try {
			// CANCELs are hop-by-hop, so here we respond immediately on the
			// server txn, if the RA didn't do it
			if ((serverTransaction.getState() != TransactionState.TERMINATED)
					&& (serverTransaction.getState() != TransactionState.COMPLETED)
					&& (serverTransaction.getState() != TransactionState.CONFIRMED)) {
				serverTransaction.sendResponse(messageFactory.createResponse(
						Response.OK, event.getRequest()));
			}
		} catch (Exception e) {
			logger.warn( "Failed to reply to CANCEL", e);
		}
			// This will generate a new CANCEL request that originates from the
			// proxy
			processRequest(serverTransaction, event.getRequest(), ac);

		

	}

	public void onAckEvent(RequestEvent event, ActivityContextInterface ac) {
		
		if (logger.isDebugEnabled())
			logger.debug("Received ACK request");
		
		processRequest(event.getServerTransaction(), event.getRequest(), ac);
	}

	public void onMessageEvent(RequestEvent event, ActivityContextInterface ac) {
		
		if (logger.isDebugEnabled())
			logger.debug("Received MESSAGE request");
		
		processRequest(event.getServerTransaction(), event.getRequest(), ac);		
	}

	public void onOptionsEvent(RequestEvent event, ActivityContextInterface ac) {
		if (logger.isDebugEnabled())
			logger.debug("Received OPTIONS request");
		try {

			Request request = event.getRequest();
			ServerTransaction serverTransaction = event.getServerTransaction();
			ProxyConfiguration proxyConfiguration = (ProxyConfiguration) getConfiguration();
			// check if it's for me, in that case reply 501
			SipUri localNodeURI = new SipUri();
			localNodeURI.setHost(proxyConfiguration.getSipHostname());
			localNodeURI.setPort(proxyConfiguration.getSipPort());
			if (request.getRequestURI().equals(localNodeURI)) {
				if (request.getHeader(MaxForwardsHeader.NAME) == null) {
					request
							.addHeader(headerFactory
									.createMaxForwardsHeader(69));
				}
				serverTransaction.sendResponse(messageFactory.createResponse(
						Response.NOT_IMPLEMENTED, request));
			} else {
				processRequest(serverTransaction, request, ac);
			}
		} catch (Exception e) {
			logger.warn( "Exception during onOptionsEvent", e);
		}
	}

	public void onInfoRespEvent(ResponseEvent event, ActivityContextInterface ac) {

		if (logger.isDebugEnabled())
			logger.debug("Received 1xx (FINER) response");
		
		processResponse(event.getClientTransaction(), event.getResponse(), ac);		
	}

	public void onSuccessRespEvent(ResponseEvent event,
			ActivityContextInterface ac) {
		if (logger.isDebugEnabled())
			logger.debug("Received 2xx (SUCCESS) response");

		processResponse(event.getClientTransaction(), event.getResponse(), ac);		
	}
	
	public void onRedirRespEvent(ResponseEvent event,
			ActivityContextInterface ac) {
		if (logger.isDebugEnabled())
			logger.debug("Received 3xx (REDIRECT) response");

		processResponse(event.getClientTransaction(), event.getResponse(), ac);		
	}

	public void onClientErrorRespEvent(ResponseEvent event,
			ActivityContextInterface ac) {
		if (logger.isDebugEnabled())
			logger.debug("Received 4xx (CLIENT ERROR) response");

		processResponse(event.getClientTransaction(), event.getResponse(), ac);		
	}

	public void onServerErrorRespEvent(ResponseEvent event,
			ActivityContextInterface ac) {
		if (logger.isDebugEnabled())
			logger.debug("Received 5xx (SERVER ERROR) response");

		processResponse(event.getClientTransaction(), event.getResponse(), ac);		
	}

	public void onGlobalFailureRespEvent(ResponseEvent event,
			ActivityContextInterface ac) {
		if (logger.isDebugEnabled())
			logger.debug("Received 6xx (GLOBAL FAILURE) response");

		processResponse(event.getClientTransaction(), event.getResponse(), ac);		
	}

	/*
	 * Timeouts
	 */

	public void onTransactionTimeoutEvent(TimeoutEvent event,
			ActivityContextInterface ac) {
		
		if (logger.isDebugEnabled())
			logger.debug("Received transaction timeout event, tid="
					+ event.getClientTransaction());
		
		ServerTransaction serverTransaction = event.getServerTransaction();
		if (serverTransaction != null) {
			try {
				serverTransaction.sendResponse(messageFactory.createResponse(
						Response.REQUEST_TIMEOUT, serverTransaction
								.getRequest()));
				setServerTransactionTerminated(true);
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	public ServerTransaction getServerTransaction(
			ClientTransaction clientTransaction) {
		ActivityContextInterface myacis[] = sbbContext.getActivities();
		for (int i = 0; i < myacis.length; i++) {
			Object activity = myacis[i].getActivity();
			if (activity instanceof ServerTransaction) {
				ServerTransaction stx = (ServerTransaction) activity;
				Request req = stx.getRequest();
				if (!req.getMethod().equals(Request.CANCEL)
						&& req.getMethod().equals(clientTransaction.getRequest().getMethod()))
					return stx;
			}
		}
		return null;
	}

	// ***** SENDER METHODS

	public ClientTransaction sendRequest(Request request, boolean attach)
			throws SipException {
		if (request.getHeader(MaxForwardsHeader.NAME) == null) {
			try {
				request.addHeader(headerFactory.createMaxForwardsHeader(69));
			} catch (Exception e) {
				logger.error(e);
			}
		}
		ClientTransaction ct = provider.getNewClientTransaction(request);
		if (attach) {
			try {
				ActivityContextInterface aci = acif.getActivityContextInterface(ct);
				aci.attach(sbbContext.getSbbLocalObject());
			} catch (UnrecognizedActivityException e) {
				logger.warn("unable to attach to client transaction", e);
			}
		}

		ct.sendRequest();
		return ct;
	}

	public void sendStatelessRequest(Request request) throws SipException {
		provider.sendRequest(request);
	}

	public void sendStatelessResponse(Response response) throws SipException {
		provider.sendResponse(response);
	}

	// ** PROXY MESSAGE

	private void processRequest(ServerTransaction serverTransaction,
			Request request, ActivityContextInterface ac) {
		
		ac.detach(sbbContext.getSbbLocalObject());
		
		if (logger.isInfoEnabled())
			logger.info("processing request: method = \n"
					+ request.getMethod().toString());
		
		// log.error("===> REQUEST METHOD["+request.getMethod()+"]
		// CALLID["+((CallID)request.getHeader(CallID.NAME)).getCallId()+"]
		// TO["+((ToHeader)request.getHeader(ToHeader.NAME)).getAddress()+"]
		// BRANCH["+serverTransaction.getBranchId()+"]");
		try {

			if (getServerTransactionTerminated()) {
				if (logger.isDebugEnabled())
					logger.debug("[PROXY MACHINE] txTERM \n" + request);
				return;
			}

			// if (getServerTX() == null)
			// setServerTX(serverTransaction);
			// Go - if it is invite here, serverTransaction can be CANCEL
			// transaction!!!! so we dont want to overwrite it above
			new ProxyMachine(getProxyConfigurator(), getLocationSbb(),
					this.addressFactory, this.headerFactory,
					this.messageFactory, this.provider)
				.processRequest(serverTransaction, request);

		} catch (Exception e) {
			// Send error response so client can deal with it
			logger.warn( "Exception during processRequest", e);
			try {
				serverTransaction.sendResponse(messageFactory.createResponse(
						Response.SERVER_INTERNAL_ERROR, request));
			} catch (Exception ex) {
				logger.warn( "Exception during processRequest", e);
			}
		}

	}

	private void processResponse(ClientTransaction clientTransaction,
			Response response, ActivityContextInterface ac) {
				
		ac.detach(sbbContext.getSbbLocalObject());
		
		if (logger.isInfoEnabled())
			logger.info("processing response: status = \n"
					+ response.getStatusCode());
		// log.error("===> RESPONSE CODE["+response.getStatusCode()+"]
		// METHOD["+((CSeq)response.getHeader(CSeq.NAME)).getMethod()+"]
		// CALLID["+((CallID)response.getHeader(CallID.NAME)).getCallId()+"]
		// TO["+((ToHeader)response.getHeader(ToHeader.NAME)).getAddress()+"]
		// BRANCH["+clientTransaction.getBranchId()+"]");
		try {

			if (getServerTransactionTerminated()) {
				return;
			}

			// Go
			ServerTransaction serverTransaction = getServerTransaction(clientTransaction);
			if (serverTransaction != null) {
				new ProxyMachine(getProxyConfigurator(), getLocationSbb(), this.addressFactory,
						this.headerFactory, this.messageFactory, this.provider)
				.processResponse(serverTransaction,
						clientTransaction, response);
			} else {
				logger.warn("Weird got null tx for[" + response + "]");
			}

		} catch (Exception e) {
			// Send error response so client can deal with it
			logger.warn( "Exception during processResponse", e);
		}

	}

	private ProxyConfigurator getProxyConfigurator() {
		ProxyConfigurator configurator = getConfiguration();
		if (configurator == null) {
			configurator = (ProxyConfigurator)proxyConfigurator.clone();
			setConfiguration(configurator);
		}
		return configurator;
	}

	// ** STRICT RFC 3261 Proxy part

	// *********** SBB SLEE METHODS

	public void sbbActivate() {}
	public void sbbCreate() throws CreateException {}
	public void sbbExceptionThrown(Exception arg0, Object arg1,
			ActivityContextInterface arg2) {}
	public void sbbLoad() {}
	public void sbbPassivate() {}
	public void sbbPostCreate() throws CreateException {}
	public void sbbRemove() {}
	public void sbbRolledBack(RolledBackContext arg0) {}
	public void sbbStore() {}

	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;
		try {
			myEnv = new InitialContext();
		
			provider = (SleeSipProvider) myEnv.lookup("java:comp/env/slee/resources/jainsip/1.2/provider");
			messageFactory = provider.getMessageFactory();
			headerFactory = provider.getHeaderFactory();
			addressFactory = provider.getAddressFactory();
			acif = (SipActivityContextInterfaceFactory) myEnv.lookup("java:comp/env/slee/resources/jainsip/1.2/acifactory");
		} catch (NamingException ne) {
			logger.error("Could not set SBB context: ", ne);
		}
	}

	public void unsetSbbContext() {	this.sbbContext = null; }

	// Inner class - this is pojo, but it needs access to some SLEE stuff, its
	// more conveniant to do this like this, since otherwise we would have
	// to either pass whole sbb or interface
	
	class ProxyMachine extends MessageUtils implements MessageHandlerInterface {
		protected final Logger log = Logger.getLogger("ProxyMachine.class");

		// We can use those variables from top level class, but let us have our
		// own.

		protected LocationService reg = null;

		protected AddressFactory af = null;

		protected HeaderFactory hf = null;

		protected MessageFactory mf = null;

		protected SipProvider provider = null;

		protected HashSet<URI> localMachineInterfaces = new HashSet<URI>();

		protected ProxyConfiguration pc = null;

		protected ProxyConfiguration config = null;

		public ProxyMachine(ProxyConfiguration config,
				LocationService registrarAccess, AddressFactory af,
				HeaderFactory hf, MessageFactory mf, SipProvider prov)
				throws ParseException {
			super(config);
			reg = registrarAccess;
			this.mf = mf;
			this.af = af;
			this.hf = hf;
			this.provider = prov;
			this.config = config;
			SipUri localMachineURI = new SipUri();
			localMachineURI.setHost(this.config.getSipHostname());
			localMachineURI.setPort(this.config.getSipPort());
			this.localMachineInterfaces.add(localMachineURI);
		}

		public void processRequest(ServerTransaction stx, Request req) {
			if (log.isDebugEnabled()) {
				log.debug("processRequest");
			}
			try {
				Request tmpNewRequest = (Request) req.clone();

				// 16.3 Request Validation
				validateRequest(stx, tmpNewRequest);

				// 16.4 Route Information Preprocessing
				routePreProcess(tmpNewRequest);

				// logger.debug("Server transaction " + stx);
				// 16.5 Determining Request Targets
				List targets = determineRequestTargets(tmpNewRequest);

				Iterator it = targets.iterator();
				while (it.hasNext()) {
					Request newRequest = (Request) tmpNewRequest.clone();
					URI target = (URI) it.next();

					// Part of loop detection, here we will stop initial reqeust
					// that makes loop in local stack
					if (isLocalMachine(target)) {

						continue;
					}
					// logger.fine("SIP Proxy Forwarding: "
					// + req.getMethod() + " to URI target: " + target);
					// 16.6 Request Forwarding
					// 1. Copy request

					// 2. Request-URI
					if (target.isSipURI() && !((SipUri) target).hasLrParam())
						newRequest.setRequestURI(target);

					// *NEW* CANCEL processing
					// CANCELs are hop-by-hop, so here must remove any existing
					// Via
					// headers,
					// Record-Route headers. We insert Via header below so we
					// will
					// get response.
					if (newRequest.getMethod().equals(Request.CANCEL)) {
						newRequest.removeHeader(ViaHeader.NAME);
						newRequest.removeHeader(RecordRouteHeader.NAME);
					} else {
						// 3. Max-Forwards
						decrementMaxForwards(newRequest);
						// 4. Record-Route
						addRecordRouteHeader(newRequest);
					}

					// 5. Add Additional Header Fields
					// TBD
					// 6. Postprocess routing information
					// TBD
					// 7. Determine Next-Hop Address, Port and Transport
					// TBD

					// 8. Add a Via header field value
					addViaHeader(newRequest);

					// 9. Add a Content-Leangth header field if necessary
					// TBD

					// 10. Forward Request

					ClientTransaction ctx = forwardRequest(stx, newRequest);

					// 11. Set timer C

				}

			} catch (SipSendErrorResponseException se) {
				se.printStackTrace();
				int statusCode = se.getStatusCode();
				sendErrorResponse(stx, req, statusCode);
			} catch (SipLoopDetectedException slde) {
				log.warn("Loop detected, droping message.");
				slde.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public void processResponse(ServerTransaction stx,
				ClientTransaction ctx, Response resp) {

			// Now check if we really want to send it right away

			// log.info(this.getClass().getName(), "processResponse");

			try {

				Response newResponse = (Response) resp.clone();

				// 16.7 Response Processing
				// 1. Find appropriate response context

				// 2. Update timer C for provisional responses

				// 3. Remove topmost via
				Iterator viaHeaderIt = newResponse.getHeaders(ViaHeader.NAME);
				viaHeaderIt.next();
				viaHeaderIt.remove();
				if (!viaHeaderIt.hasNext())
					return; // response was meant for this proxy

				// 4. Add the response to the response context

				// 5. Check to see if this response should be forwarded
				// immediately
				if (newResponse.getStatusCode() == Response.TRYING) {
					return;
				}

				// 6. When necessary, choose the best final response from the

				// 7. Aggregate authorization header fields if necessary

				// 8. Optionally rewrite Record-Route header field values

				// 9. Forward the response
				forwardResponse(stx, newResponse);

				// 10. Generate any necessary CANCEL requests

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public ClientTransaction forwardRequest(
				ServerTransaction serverTransaction, Request request) {
			ClientTransaction toReturn = null;
			if (log.isDebugEnabled())
				log.debug("Forwarding request " + request.getMethod()
						+ " of server tx " + serverTransaction.getBranchId());

			// ProxySbb.log.error("===> REQUEST FWD
			// METHOD["+request.getMethod()+"]
			// CALLID["+((CallID)request.getHeader(CallID.NAME)).getCallId()+"]
			// BRANCH["+serverTransaction.getBranchId()+"]");
			// log.info("PRXY forwardReqeust\n"+request);
			try {

				if (request.getMethod().equals(Request.ACK)) {

					sendStatelessRequest(request);

				} else if (request.getMethod().equals(Request.CANCEL)) {

					sendRequest(request, false);
				} else {

					ClientTransaction clientTransaction = sendRequest(request,
							true);

					return clientTransaction;
				}

			} catch (Exception e) {
				// log.info( "Exception during forwardRequest-->"+ e);
				e.printStackTrace();
				if (!serverTransaction.getRequest().getMethod().endsWith(
						Request.CANCEL)) {
					// send back error if it's nt a cancel, because that one
					// already got a 200 ok
					sendErrorResponse(serverTransaction, serverTransaction
							.getRequest(), Response.SERVER_INTERNAL_ERROR);
				}
			}
			return toReturn;
		}

		public void sendErrorResponse(ServerTransaction txn, Request request,
				int statusCode) {
			try {

				// sipaci.setTransactionTerminated(true);
				setServerTransactionTerminated(true);
				Response response = mf.createResponse(statusCode, request);
				if (response.getHeader(MaxForwardsHeader.NAME) == null) {
					response.addHeader(hf.createMaxForwardsHeader(69));
				}
				txn.sendResponse(response);
			} catch (Exception e) {
				// trace(Level.WARNING, "Exception during sendErrorResponse",
				// e);
				e.printStackTrace();
			}
		}

		public void forwardResponse(ServerTransaction txn, Response response) {
			if (log.isDebugEnabled())
				log.debug("Forwarding response " + response.getStatusCode()
						+ " of server tx " + txn.getBranchId());

			// log.info("PRXY forwardResponse\n"+response);
			// try{
			// ProxySbb.log.error("===> RESPONSE FWD
			// CODE["+response.getStatusCode()+"]
			// METHOD["+((CSeq)response.getHeader(CSeq.NAME)).getMethod()+"]
			// CALLID["+((CallID)response.getHeader(CallID.NAME)).getCallId()+"]
			// BRANCH["+txn==null?"GO TNULL":txn.getBranchId()+"]");
			// }catch(Exception e)
			// {
			// e.printStackTrace();
			// }
			try {
				// trace(Level.FINEST, "Forwarding response:\n" + response);
				if (txn != null) {
					txn.sendResponse(response);
				} else {
					// forward statelessly anyway
					sendStatelessResponse(response);
				}
			} catch (Exception e) {
				log.error("Exception during forwardResponse[\n" + response
						+ "\n] TXBRANCH[" + txn.getBranchId() + "] TXR[\n"
						+ txn.getRequest() + "\n]:" + e);
			}
		}

		/**
		 * Performs request validation as per RFC 3261 section 16.3. If a
		 * request fails validation, throw exception to cause appropriate error
		 * response to client.
		 * 
		 * @param txn
		 *            the server transaction of the request.
		 * @param request
		 *            the SIP request to be validated.
		 * @throws SipLoopDetectedException -
		 *             error that is beeing thrown when local stack goes into
		 *             loop
		 * @throws SipSendErrorResponseException -
		 *             thrown uri of passed message is not supported
		 */
		public void validateRequest(ServerTransaction txn, Request request)
				throws SipSendErrorResponseException, SipLoopDetectedException {
			// 1. Reasonable syntax

			// 2. URI scheme
			URI requestURI = null;
			requestURI = request.getRequestURI();

			boolean supportedURIScheme = false;
			supportedURIScheme = isSupportedURIScheme(requestURI);

			if (!supportedURIScheme) {
				throw new SipSendErrorResponseException(
						"Unsupported URI scheme",
						Response.UNSUPPORTED_URI_SCHEME);
			}

			// 3. Max-Forwards
			checkMaxForwards(txn, request);

			// 4. Loop Detection - TBD
			detectLoop(request);
			// 5. Proxy-Require - TBD
			// 6. Proxy-Authorization - TBD
		}

		public void detectLoop(Request request) throws SipLoopDetectedException {

			URI requestURI = null;
			requestURI = request.getRequestURI();
			// , now its simple, so we wont spin requests
			// to local node
			SipUri localNodeURI = new SipUri();
			try {
				localNodeURI.setHost(this.config.getSipHostname());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			localNodeURI.setPort(this.config.getSipPort());

			// This only works if UAC conforms to rfc 3261
			if (requestURI.equals(localNodeURI)) {
				// throw new SipSendErrorResponseException("Possible local
				// looping on node",Response.LOOP_DETECTED);
				// throw new SipLoopDetectedException(
				// "Possible loop detected on LOCAL["+localNodeURI+"]
				// MSG["+requestURI+"] message:n" + request
				// + "\n====================================");
				// this can be a loop, we will only warn as this is uncertain at
				// this point
				// if You know more, please patch :]

				if (log.isDebugEnabled()) {
					log.debug("Possible loop detected on LOCAL[" + localNodeURI
							+ "] MSG[" + requestURI + "] message:n" + request
							+ "\n====================================");
				}

			}

			// do more:

			ListIterator lit = request.getHeaders(ViaHeader.NAME);

			if (lit != null && lit.hasNext()) {
				int found = 0;

				do {
					ViaHeader vh = (ViaHeader) lit.next();
					if (vh.getHost().equals(localNodeURI.getHost())
							&& vh.getPort() == localNodeURI.getPort()) {
						found++;
					}

					if (found >= 2) {
						throw new SipLoopDetectedException(
								"Possible loop detected[mutliple via headers] on message:n"
										+ request
										+ "\n====================================");
					}
				} while (lit.hasNext());

			}

		}

		/**
		 * Validate the max-forwards header throw a user error exception (too
		 * many hops) if max-forwards reaches 0.
		 * 
		 * @param txn
		 * @param request
		 * @throws SipSendErrorResponseException
		 */
		public void checkMaxForwards(ServerTransaction txn, Request request)
				throws SipSendErrorResponseException {
			MaxForwardsHeader mfh = (MaxForwardsHeader) request
					.getHeader(MaxForwardsHeader.NAME);
			if (mfh == null)
				return;

			int maxForwards = 0;
			maxForwards = ((MaxForwardsHeader) request
					.getHeader(MaxForwardsHeader.NAME)).getMaxForwards();

			if (maxForwards > 0) {
				return;
			} else {
				// MAY respond to OPTIONS, otherwise return 483 Too Many Hops
				throw new SipSendErrorResponseException("Too many hops",
						Response.TOO_MANY_HOPS);
			}

		}

		/**
		 * Attempts to find a locally registered contact address for the given
		 * URI, using the location service interface.
		 */
		public LinkedList<URI> findLocalTarget(URI uri)
				throws SipSendErrorResponseException {
			String addressOfRecord = uri.toString();

			Map<String, RegistrationBinding> bindings = null;
			LinkedList<URI> listOfTargets = new LinkedList<URI>();
			try {
				bindings = reg.getBindings(addressOfRecord);
			} catch (LocationServiceException e) {

				e.printStackTrace();
				return listOfTargets;
			}

			if (bindings == null) {
				throw new SipSendErrorResponseException("User not found",
						Response.NOT_FOUND);
			}
			if (bindings.isEmpty()) {
				throw new SipSendErrorResponseException(
						"User temporarily unavailable",
						Response.TEMPORARILY_UNAVAILABLE);
			}

			Iterator it = bindings.values().iterator();
			URI target = null;
			while (it.hasNext()) {
				String contactAddress = ((RegistrationBinding)it.next()).getContactAddress();
				try {
					listOfTargets.add(af.createURI(contactAddress));
				} catch (ParseException e) {
					log.warn("Ignoring contact address "+contactAddress+" due to parse error",e);
				}
			}
			if (listOfTargets.size() == 0) {
				// logger.fine("findLocalTarget: No contacts for "
				// + addressOfRecord + " found.");
				throw new SipSendErrorResponseException(
						"User temporarily unavailable",
						Response.TEMPORARILY_UNAVAILABLE);
			}
			return listOfTargets;
		}

		/**
		 * Adds a default Via header to the request. Override to provide a
		 * different Via header.
		 */
		public void addViaHeader(Request request)
				throws SipSendErrorResponseException {
			ViaHeader via = null;
			try {
				// ViaHeader via = hf.createViaHeader(config.getSipHostname(),
				// config.getSipPort(), config.getSipTransport(), null);

				// if (request.getMethod().equals(Request.CANCEL)
				// || request.getMethod().equals(Request.ACK)) {
				// For now we cant do rfc 3261 ch 17.1.1.3
				if (request.getMethod().equals(Request.CANCEL)) {
					via = getForwardedInviteViaHeader();

					if (via == null) {
						throw new SipSendErrorResponseException(
								"Couldnt add via [" + via + "] to msg[\n"
										+ request + "\n], didnt find forwarded via!!!", Response.BAD_REQUEST);
					}

				} else {
					via = hf.createViaHeader(config.getSipHostname(), config
							.getSipPort(), config.getSipTransports()[0],
							"z9hG4bK" + System.currentTimeMillis() + "_"
									+ Math.random() + "_"
									+ System.currentTimeMillis());
					if (request.getMethod().equals(Request.INVITE)) {
						setForwardedInviteViaHeader(via);
					}
				}

				// THIS: config.getSipTransports()[0] // has to be changed!!!
				log.debug("[&&&] addViaHeader\n" + via + "");
				// via.setParameter("ID",
				// ""+System.currentTimeMillis()+"_"+Math.random()+"_"+config.getSipHostname()+":"+config.getSipPort());
				request.addHeader(via);

			} catch (Exception e) {
				
				throw new SipSendErrorResponseException("Couldnt add via ["
						+ via + "] to msg[\n" + request + "\n]",
						Response.SERVER_INTERNAL_ERROR,e);
			}
		}

		/**
		 * Adds a default Record-Route header to the request. Override to
		 * provide a different Record-Route header.
		 */
		public void addRecordRouteHeader(Request request) {
			try {
				// Add our record-route header to list...
				SipURI myURI = af.createSipURI(null, config.getSipHostname());
				myURI.setPort(config.getSipPort());
				myURI.setMAddrParam(config.getSipHostname());
				myURI.setTransportParam(config.getSipTransports()[0]);
				myURI.setParameter("cluster", "mobi-cents");
				myURI.setParameter("lr", "");
				Address myName = af.createAddress(myURI);

				RecordRouteHeader myHeader = hf.createRecordRouteHeader(myName);
				request.addFirst(myHeader);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		/**
		 * Decrement the value of max-forwards. If no max-forwards header
		 * present, create a max-forwards header with the RFC3261 recommended
		 * default value
		 * 
		 * @param request
		 * @throws SipSendErrorResponseException
		 */
		public void decrementMaxForwards(Request request)
				throws SipSendErrorResponseException {
			MaxForwardsHeader max = (MaxForwardsHeader) request
					.getHeader(MaxForwardsHeader.NAME);
			try {
				if (max == null) {
					// add max-forwards with default 70 hops
					max = hf.createMaxForwardsHeader(70);
					request.setHeader(max);
				} else {
					// decrement max-forwards
					int maxForwards = max.getMaxForwards();
					maxForwards--;
					max.setMaxForwards(maxForwards);
					request.setHeader(max);
				}
			} catch (Exception e) {
				throw new SipSendErrorResponseException(
						"Error updating max-forwards",
						Response.SERVER_INTERNAL_ERROR);
			}
		}

		/**
		 * Check for strict-routing style route headers and swap with
		 * Request-URI if applicable.
		 */
		public void routePreProcess(Request request)
				throws SipSendErrorResponseException {
			URI requestURI = null;
			requestURI = request.getRequestURI();

			// TODO: add check on multiple names!!!!!
			if ((requestURI.isSipURI())
					&& (((SipURI) requestURI).getUser() == null)
					&& (((SipURI) requestURI).getHost().equalsIgnoreCase(config
							.getSipHostname()))) {
				// client is a strict router, replace request-URI with last
				// value in Route header field...
				try {
					ListIterator it = request.getHeaders(RouteHeader.NAME);
					LinkedList l = new LinkedList();
					// need last value in list
					while (it.hasNext()) {
						RouteHeader r = (RouteHeader) it.next();
						l.add(r);
					}
					if (l.size() == 0)
						return;

					RouteHeader route = (RouteHeader) l.getLast();

					l.removeLast(); // Remove the last route header from the
					// list,
					// possibly leaving an empty list

					request.removeHeader(RouteHeader.NAME); // Remove all route
					// headers from the
					// message

					// Re-add the remaining headers to the message
					for (int i = 0; i < l.size(); i++) {
						RouteHeader routeHeader = (RouteHeader) l.get(i);
						request.addHeader(routeHeader);
					}

					URI newURI = route.getAddress().getURI();
					request.setRequestURI(newURI);

				} catch (Exception e) {
					e.printStackTrace();
					throw new SipSendErrorResponseException(
							"Error updating route headers",
							Response.SERVER_INTERNAL_ERROR);

				}
			}
			// From RFC3261 16.4:
			// If the first value in the Route header field indicates this
			// proxy,
			// the proxy MUST remove that value from the request.
			Iterator routeHeaders = request.getHeaders(RouteHeader.NAME);
			if (routeHeaders.hasNext()) {
				RouteHeader r = (RouteHeader) routeHeaders.next();
				// is this route header for our hostname & port?
				URI uri = r.getAddress().getURI();
				if (uri.isSipURI()) {
					SipURI sipURI = (SipURI) uri;
					int uriPort = sipURI.getPort();
					if (uriPort <= 0)
						uriPort = 5060; // WARNING: Assumes stack impl returns
					// <= 0
					// if port not specified in URI
					String cluster = sipURI.getParameter("cluster");

					boolean isMobicents = (cluster != null && cluster
							.equals("mobi-cents"));

					// JAIN SIP does not specify but NIST SIP returns -1
					if (((sipURI.getHost().equalsIgnoreCase(config
							.getSipHostname())) && (uriPort == config
							.getSipPort()))
							|| isMobicents) {
						// logger.fine("Cluster = " + cluster);
						// remove this route header
						routeHeaders.remove();
						// if this was the last one, remove the header entirely
						// (why
						// isn't this automatic?)
						if (!routeHeaders.hasNext())
							request.removeHeader(RouteHeader.NAME);
					}
				}
			}

		}

		/**
		 * Determines target SIP URI(s) for request, using location service or
		 * other criteria.
		 * 
		 * TODO: Forking (return more than one target)
		 * 
		 * @param request
		 *            the SIP request being forwarded
		 * @return a list of URIs
		 */
		public List determineRequestTargets(Request request)
				throws SipSendErrorResponseException {
			LinkedList targets = null;

			URI requestURI = null;
			URI target = null;
			boolean localDomain = false;
			requestURI = request.getRequestURI();
			localDomain = isLocalDomain(requestURI);

			if (request.getMethod().equals(Request.ACK)
					|| request.getMethod().equals(Request.BYE)) {
				RouteHeader rh = (RouteHeader) request
						.getHeader(RouteHeader.NAME);
				if (rh != null) {
					target = rh.getAddress().getURI();
				} else
					target = request.getRequestURI();
				targets = new LinkedList();
				targets.add(target);
			} else if (localDomain) {
				// determine local SIP target(s) using location service etc
				targets = findLocalTarget(requestURI);
				// This is done in findLocalTarget
				// if (target == null) { // not found (or not currently
				// registered)
				// throw new SipSendErrorResponseException("User not
				// registered",
				// Response.TEMPORARILY_UNAVAILABLE);
				// }
			} else {
				// destination addr is outside our domain
				target = requestURI;
				targets = new LinkedList();
				targets.add(target);
			}

			return targets;
		}

		public boolean isLocalMachine(URI hostURI) {

			return this.localMachineInterfaces.contains(hostURI);

		}

	}

}
