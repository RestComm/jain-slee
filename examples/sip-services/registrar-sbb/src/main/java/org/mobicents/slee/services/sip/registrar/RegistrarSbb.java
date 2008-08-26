package org.mobicents.slee.services.sip.registrar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipProvider;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.DateHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderAddress;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.ChildRelation;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.SbbID;
import javax.slee.SbbLocalObject;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.TimerFacility;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;
import javax.slee.serviceactivity.ServiceActivity;
import javax.slee.serviceactivity.ServiceActivityFactory;

import org.mobicents.slee.resource.sip.SipFactoryProvider;
import org.mobicents.slee.services.sip.common.ConfigurationProvider;
import org.mobicents.slee.services.sip.common.LocationSbbLocalObject;
import org.mobicents.slee.services.sip.common.LocationServiceException;
import org.mobicents.slee.services.sip.common.RegistrationBinding;
import org.mobicents.slee.services.sip.registrar.mbean.RegistrarConfigurator;
import org.mobicents.slee.services.sip.registrar.mbean.RegistrarConfiguratorMBean;

/**
 * 
 * this is the abstract class that will be deployed
 * 
 * Sbb abstract class provided by the Sbb developer
 */
public abstract class RegistrarSbb implements Sbb {
	// private static Logger log;

	// For test only -- to test post conditions.
	// private SleeContainer myServiceContainer;

	private static Logger logger = Logger.getLogger(RegistrarSbb.class
			.getName());


	
	// **** EVENT HANDLERS
	
	public void onServiceStarted(
			javax.slee.serviceactivity.ServiceStartedEvent serviceEvent,
			ActivityContextInterface aci) {
		aci.detach(this.context.getSbbLocalObject());
		try {
			// check if it's my service that is starting
			ServiceActivity sa = ((ServiceActivityFactory) sbbEnv
					.lookup("slee/serviceactivity/factory")).getActivity();
			if (sa.equals(aci.getActivity())) {
				startMBeanConfigurator();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void onRegisterEvent(RequestEvent event, ActivityContextInterface ac) {
		logger.log(Level.FINER, "SipRegistrarSBB: " + this
				+ ": got register event");
		try {
			ServerTransaction serverTransactionId = (ServerTransaction) ac
					.getActivity();
			Request request = event.getRequest();
			processRequest(serverTransactionId, request);

			//Detach, so everything can be reclaimed in super fast mode
			
			ac.detach(this.getSbbLocalObject());
		} catch (Exception e) {
			// Send error response so client can deal with it
			// e.printStackTrace();
			logger.log(Level.WARNING, "Exception during onRegisterEvent", e);
		}
	}

	
	// *** CONF METHODS
	protected void startMBeanConfigurator() {
		String confValue = null;
		Context myEnv = null;
		
		RegistrarConfigurator config=new RegistrarConfigurator();
		
		try {
			logger.info("Building Configuration from ENV Entries");
			myEnv = (Context) new InitialContext().lookup("java:comp/env");

		} catch (NamingException ne) {
			logger.warning("Could not set SBB context:" + ne.getMessage());
			return;
		}

		
		confValue = null;

		try {

			confValue = (String) myEnv.lookup("configuration-MAX-EXPIRES");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		if (confValue == null) {
			// WE DONT KNOW WHAT TO DO.... kernel panic
			// LETS ALLOW DEFUALT VALUE
			config.setSipRegistrationMaxExpires(3600);
		} else {
			long i = Long.parseLong(confValue);
			config.setSipRegistrationMaxExpires(i);
		}

		confValue = null;

		try {

			confValue = (String) myEnv.lookup("configuration-MIN-EXPIRES");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		if (confValue == null) {
			// WE DONT KNOW WHAT TO DO.... kernel panic
			// LETS ALLOW DEFUALT VALUE
			config.setSipRegistrationMinExpires(100);
		} else {
			long i = Long.parseLong(confValue);
			config.setSipRegistrationMinExpires(i);
		}
		
		try {
			
			String configurationName = (String) myEnv.lookup("configuration-MBEAN");
			if(configurationName!=null)
				config.setName(configurationName);
		} catch (NamingException ne) {

			
			
		}
		
		config.startService();
		
	}
	
	// *** END OCNF METHODS
	
	
	private void processRequest(ServerTransaction serverTransaction,
			Request request) {
		logger.log(Level.FINEST, "processRequest: request = >>>>>>>>>\n"
				+ request.toString() + "\n<<<<<<<<");
		try {

			ChildRelation children = getLocationSbbChild();
			LocationSbbLocalObject handler = null;
			SbbLocalObject slo=null;
			if (children.size() < 1)
			{
				slo=children.create();
				handler = (LocationSbbLocalObject) slo;
			}
			else
			{

				slo=(SbbLocalObject) children.iterator().next();
				handler = (LocationSbbLocalObject) slo;
			}
			// Create a worker to process this event

			// Go
			doRegistration(handler, request, serverTransaction);

			
		} catch (Exception e) {
			// Send error response so client can deal with it
			// e.printStackTrace();
			logger.log(Level.WARNING, "Exception during processRequest", e);
			try {
				serverTransaction
						.sendResponse(getMessageFactory().createResponse(
								Response.SERVER_INTERNAL_ERROR, request));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	// ------------------- helper methods

	private void doRegistration(LocationSbbLocalObject handler,
			Request registerReqeust, ServerTransaction txn) {

		
		
		
		try {
			
			
			
			RegistrarConfigurator config=(RegistrarConfigurator) ConfigurationProvider.getCopy(RegistrarConfiguratorMBean.MBEAN_NAME_PREFIX, "v1RegistrarConf");
			long maxExpires=config.getSipRegistrationMaxExpires();
			long minExpires=config.getSipRegistrationMinExpires();
			// Is this request for this domain?
			// This will always be false since proxy does that work....
			if (!isLocalDomain(registerReqeust.getRequestURI())) {
				// If we are a proxy then forward to correct domain
				// For now just return error code
				Response response = (this.messageFactory.createResponse(
						Response.FORBIDDEN, registerReqeust));
				txn.sendResponse(response);
				return;

			}

			// Process require header

			// Authenticate
			// Authorize
			// OK we're authorized now ;-)

			// extract address-of-record
			String sipAddressOfRecord = getCanonicalAddress((HeaderAddress) registerReqeust
					.getHeader(ToHeader.NAME));

			logger.finer("address-of-record = " + sipAddressOfRecord);

			// map will be empty if user not in LS...
			// Note we don't care if the user has a valid account in the LS, we
			// just
			// add them anyway.
			String sipAddress = getCanonicalAddress((HeaderAddress) registerReqeust
					.getHeader(ToHeader.NAME));
			Map<String, RegistrationBinding> bindings = handler
					.getUserBindings(sipAddress);

			// Do we have any contact header(s)?
			if (registerReqeust.getHeader(ContactHeader.NAME) == null) {
				// Just send OK with current bindings - this request was a
				// query.
				sendRegistrationOKResponse(txn, registerReqeust, bindings);
				return;
			}

			// Check contact, callid, cseq

			ArrayList newContacts = getContactHeaderList(registerReqeust
					.getHeaders(ContactHeader.NAME));
			ExpiresHeader expiresHeader = null;
			boolean removeAll = false;
			CallIdHeader cidh = (CallIdHeader) registerReqeust
					.getHeader(CallIdHeader.NAME);
			String callId = cidh.getCallId();

			CSeqHeader cseqh = (CSeqHeader) registerReqeust
					.getHeader(CSeqHeader.NAME);
			long seq = cseqh.getSequenceNumber();

			expiresHeader = registerReqeust.getExpires();

			if (hasWildCard(newContacts)) { // This is a "Contact: *" "remove
				// all bindings" request
				if ((expiresHeader == null)
						|| (expiresHeader.getExpires() != 0)
						|| (newContacts.size() > 1)) {
					// malformed request in RFC3261 ch10.3 step 6
					txn.sendResponse(this.messageFactory.createResponse(
							Response.BAD_REQUEST, registerReqeust));
					return;
				}

				removeAll = true;
			}

			if (removeAll) {
				logger.fine("Removing bindings");
				// Go through list of current bindings
				// if callid doesn't match - remove binding
				// if callid matches and seq greater, remove binding.
				Iterator<RegistrationBinding> it = bindings.values().iterator();

				try {
					while (it.hasNext()) {
						RegistrationBinding binding = (RegistrationBinding) it
								.next();
						if (callId.equals(binding.getCallId())) {

							if (seq > binding.getCSeq()) {
								it.remove();
								handler.removeBinding(sipAddressOfRecord,
										binding.getContactAddress());
							} else {
								txn.sendResponse(this.messageFactory
										.createResponse(Response.BAD_REQUEST,
												registerReqeust));
								return;
							}
						} else {
							it.remove();
							handler.removeBinding(sipAddressOfRecord, binding
									.getContactAddress());
						}
					}

				} catch (LocationServiceException lse) {
					lse.printStackTrace();
					txn.sendResponse(this.messageFactory.createResponse(
							Response.SERVER_INTERNAL_ERROR, registerReqeust));
					return;
				}

				sendRegistrationOKResponse(txn, registerReqeust, bindings);
			} else {
				// Update bindings
				logger.fine("Updating bindings");
				ListIterator li = newContacts.listIterator();

				while (li.hasNext()) {
					ContactHeader contact = (ContactHeader) li.next();

					// get expires value, either in header or default
					// do min-expires etc
					long requestedExpires = 0;

					if (contact.getExpires() >= 0) {
						requestedExpires = contact.getExpires();
					} else if ((expiresHeader != null)
							&& (expiresHeader.getExpires() >= 0)) {
						requestedExpires = expiresHeader.getExpires();
					} else {
						requestedExpires = 3600; // default
					}

					// If expires too large, reset to our local max
					if (requestedExpires > maxExpires) {
						requestedExpires = maxExpires;
					} else if ((requestedExpires > 0)
							&& (requestedExpires < minExpires)) {
						// requested expiry too short, send response with
						// min-expires
						// 
						sendIntervalTooBriefResponse(txn, registerReqeust, minExpires);
						return;
					}

					// Get the q-value (preference) for this binding - default
					// to 0.0 (min)
					float q = 0;
					if (contact.getQValue() != -1)
						q = contact.getQValue();
					if ((q > 1) || (q < 0)) {
						txn.sendResponse(this.messageFactory.createResponse(
								Response.BAD_REQUEST, registerReqeust));
						return;
					}

					// Find existing binding
					URI uri = contact.getAddress().getURI();
					String contactAddress = uri.toString();

					RegistrationBinding binding = (RegistrationBinding) bindings
							.get(contactAddress);

					if (binding != null) { // Update this binding
						if (callId.equals(binding.getCallId())) {
							if (seq <= binding.getCSeq()) {
								// Invalid request
								txn.sendResponse(this.messageFactory
										.createResponse(Response.BAD_REQUEST,
												registerReqeust));
								return;
							}
						}

						if (requestedExpires == 0) {
							logger.fine("Removing binding: "
									+ sipAddressOfRecord + " -> "
									+ contactAddress);
							bindings.remove(contactAddress);

							try {
								handler.removeBinding(sipAddressOfRecord,
										binding.getContactAddress());
							} catch (LocationServiceException lse) {
								lse.printStackTrace();
								txn.sendResponse(this.messageFactory
										.createResponse(
												Response.SERVER_INTERNAL_ERROR,
												registerReqeust));
								return;
							}

						} else {

							logger.fine("Updating binding: "
									+ sipAddressOfRecord + " -> "
									+ contactAddress);
							logger.fine("contact: " + contact.toString());
							// binding.setCallId(callId);
							// binding.setCSeq(seq);
							// binding.setExpiryDelta(requestedExpires);
							// binding.setQValue(q);
							// set timer for registration expiry
							// setRegistrationTimer(sipAddressOfRecord,
							// contactAddress, requestedExpires, callId,
							// seq);
							// Lets push it into location service, this will
							// update version of binding
							try {
								bindings.put(contactAddress, handler
										.addUserLocation(sipAddress,
												contactAddress, "",
												requestedExpires, q, callId,
												seq));
							} catch (LocationServiceException lse) {
								lse.printStackTrace();
								txn.sendResponse(this.messageFactory
										.createResponse(
												Response.SERVER_INTERNAL_ERROR,
												registerReqeust));
								return;
							}
						}

					} else {
						// Create new binding, add to list...
						if (requestedExpires != 0) {
							logger.fine("Adding new binding: "
									+ sipAddressOfRecord + " -> "
									+ contactAddress);
							logger.fine(contact.toString());

							// removed comment parameter to registration binding
							// - Address and Contact headers don't have comments
							// in 1.1

							try {
								bindings.put(contactAddress, handler
										.addUserLocation(sipAddress,
												contactAddress, "",
												requestedExpires, q, callId,
												seq));
							} catch (LocationServiceException lse) {
								lse.printStackTrace();
								txn.sendResponse(this.messageFactory
										.createResponse(
												Response.SERVER_INTERNAL_ERROR,
												registerReqeust));
								return;
							}

						}
					}
				}
				// Update bindings, return 200 if successful, 500 on error

				sendRegistrationOKResponse(txn, registerReqeust, bindings);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getDomain(URI uri) {
		String address = uri.toString();

		// get rid of user part
		int index = address.indexOf('@');
		if (index != -1)
			address = address.substring(index + 1);

		// get rid of protocol part
		index = address.indexOf(':');
		if (index != -1)
			address = address.substring(index + 1);

		// get rid of port and all that comes after
		index = address.indexOf(':');
		if (index != -1)
			address = address.substring(0, index);

		return address;
	}

	private boolean isLocalDomain(URI uri) {
		// Proxy acts only for local domain, so we should not worry??
		//TODO:Fix this
		return true;
	}

	public static String getCanonicalAddress(HeaderAddress header) {
		Address na = header.getAddress();

		URI uri = na.getURI();

		String addr = uri.toString();

		int index = addr.indexOf(':');
		index = addr.indexOf(':', index + 1);
		if (index != -1) {
			// Get rid of the port
			addr = addr.substring(0, index);
		}

		return addr;
	}

	// ****************** SIP METHODS

	private void sendRegistrationOKResponse(ServerTransaction txn,
			Request request, Map<String, RegistrationBinding> bindings) {
		List contactHeaders = getContactHeaders(bindings.values());
		try {
			Response res = (this.messageFactory.createResponse(Response.OK,
					request));

			if ((contactHeaders != null) && (!contactHeaders.isEmpty())) {
				logger.fine("Adding " + contactHeaders.size() + " headers");
				for (int i = 0; i < contactHeaders.size(); i++) {
					ContactHeader hdr = (ContactHeader) contactHeaders.get(i);
					res.addHeader(hdr);
				}
				// ((SIPResponse) res).setHeaders(contactHeaders);
			}
			DateHeader dateHeader = this.headerFactory
					.createDateHeader(new GregorianCalendar());
			res.setHeader(dateHeader);
			logger.fine("Sending Response:\n" + res.toString());
			txn.sendResponse(res);
		} catch (Exception e) {
			logger
					.log(Level.WARNING, "Failed to sendRegistrationOKResponse",
							e);
		}
	}

	private void sendIntervalTooBriefResponse(ServerTransaction txn,
			Request request, long minExpires) {
		try {
			Response res = this.messageFactory.createResponse(423, request); // In
			// RFC3261
			// but
			// not
			// JAIN
			// SIP -
			// coming
			// in
			// JAIN
			// SIP
			// 1.1??
			res.setReasonPhrase("Interval Too Brief");
			DateHeader dateHeader = this.headerFactory
					.createDateHeader(new GregorianCalendar());
			res.setHeader(dateHeader);
			Header minExpiresHeader = this.headerFactory.createHeader(
					"Min-Expires", String.valueOf(minExpires));
			res.addHeader(minExpiresHeader);
			logger.fine("Sending Response:\n" + res.toString());
			txn.sendResponse(res);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to sendInternalTooBriefResponse",
					e);
		}

	}

	private List<ContactHeader> getContactHeaders(
			Collection<RegistrationBinding> bindings) {
		if (bindings == null)
			return null;
		ArrayList<ContactHeader> contactHeaders = new ArrayList<ContactHeader>();
		Iterator<RegistrationBinding> it = bindings.iterator();

		while (it.hasNext()) {
			RegistrationBinding binding = it.next();
			ContactHeader header = binding.getContactHeader(
					this.addressFactory, this.headerFactory);
			if (header != null) {
				contactHeaders.add(header);
			}
		}
		return contactHeaders;

	}

	private ArrayList getContactHeaderList(ListIterator it) {
		ArrayList l = new ArrayList();
		while (it.hasNext()) {
			l.add(it.next());
		}
		return l;
	}

	private boolean hasWildCard(ArrayList contactHeaders) {
		Iterator it = contactHeaders.iterator();
		while (it.hasNext()) {
			ContactHeader header = (ContactHeader) it.next();
			if (header.toString().indexOf('*') > 0)
				return true;
		}
		return false;
	}

	public abstract ChildRelation getLocationSbbChild();

	public void setSbbContext(SbbContext context) {
		this.context = context;
		try {
			sbbEnv = (Context) new InitialContext().lookup("java:comp/env");
			id = context.getSbb();

			timerFacility = (TimerFacility) sbbEnv
					.lookup("slee/facilities/timer");
			alarmFacility = (AlarmFacility) sbbEnv
					.lookup("slee/facilities/alarm");
			// profileFacility = (ProfileFacility)
			// sbbEnv.lookup("slee/facilities/profile");
			nullACIFactory = (NullActivityContextInterfaceFactory) sbbEnv
					.lookup("slee/nullactivity/activitycontextinterfacefactory");
			nullActivityFactory = (NullActivityFactory) sbbEnv
					.lookup("slee/nullactivity/factory");
			fp = (SipFactoryProvider) sbbEnv
					.lookup("slee/resources/jainsip/1.2/provider");
			provider = fp.getSipProvider();
			addressFactory = fp.getAddressFactory();
			headerFactory = fp.getHeaderFactory();
			messageFactory = fp.getMessageFactory();
		} catch (NamingException ne) {
			logger.log(java.util.logging.Level.WARNING,
					"Could not set SBB context: ", ne);
		}

	}

	public void unsetSbbContext() {
		context = null;
	}

	public void sbbCreate() throws CreateException {

	}

	public void sbbPostCreate() throws CreateException {

	}

	public void sbbRemove() {

	}

	public void sbbPassivate() {

	}

	public void sbbActivate() {

	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface aci) {
	}

	public void sbbRolledBack(RolledBackContext context) {
	}

	private SipFactoryProvider factoryProvider;

	public SipProvider getSipProvider() {
		return provider;
	}

	public AddressFactory getAddressFactory() {
		return addressFactory;
	}

	public HeaderFactory getHeaderFactory() {
		return headerFactory;
	}

	public MessageFactory getMessageFactory() {
		return messageFactory;
	}

	public NullActivityFactory getNullActivityFactory() {
		return nullActivityFactory;
	}

	public final TimerFacility getTimerFacility() {
		return timerFacility;
	}

	public NullActivityContextInterfaceFactory getNullACIFactory() {
		return nullACIFactory;
	}

	public final SbbLocalObject getSbbLocalObject() {
		return context.getSbbLocalObject();
	}

	private SipFactoryProvider fp;
	private SipProvider provider;
	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	private MessageFactory messageFactory;

	private SbbContext context;
	private TimerFacility timerFacility;
	private AlarmFacility alarmFacility;
	// Not implelemented yet
	// private ProfileFacility profileFacility;
	private SbbID id;
	private NullActivityFactory nullActivityFactory;
	private NullActivityContextInterfaceFactory nullACIFactory;
	private Context sbbEnv;


}
