package org.mobicents.slee.services.sip.registrar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.DateHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.HeaderAddress;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.ChildRelation;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.SLEEException;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.serviceactivity.ServiceActivity;
import javax.slee.serviceactivity.ServiceActivityFactory;

import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

import org.apache.log4j.Logger;
import org.mobicents.slee.services.sip.location.LocationSbbLocalObject;
import org.mobicents.slee.services.sip.location.LocationServiceException;
import org.mobicents.slee.services.sip.location.RegistrationBinding;
import org.mobicents.slee.services.sip.registrar.mbean.RegistrarConfigurator;

/**
 * 
 * this is the abstract class that will be deployed
 * 
 * Sbb abstract class provided by the Sbb developer
 */
public abstract class RegistrarSbb implements Sbb {

	private static Logger logger = Logger.getLogger(RegistrarSbb.class);
	
	/**
	 * MBean Configurator
	 */
	private static final RegistrarConfigurator config = new RegistrarConfigurator();
	
	private SleeSipProvider provider=null;
	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	private MessageFactory messageFactory;
	private SipActivityContextInterfaceFactory acif;

	private SbbContext sbbContext;
	private Context sbbEnv;
	
	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;
		try {
			sbbEnv = (Context) new InitialContext().lookup("java:comp/env");		
			provider = (SleeSipProvider) sbbEnv.lookup("slee/resources/jainsip/1.2/provider");
			messageFactory = provider.getMessageFactory();
			headerFactory = provider.getHeaderFactory();
			addressFactory = provider.getAddressFactory();
			acif = (SipActivityContextInterfaceFactory) sbbEnv.lookup("slee/resources/jainsip/1.2/acifactory");
		} catch (NamingException ne) {
			logger.error("Could not set SBB context: ", ne);
		}
	}

	
	
	// **** SETUP CONFIGURATION MBEAN
	
	public void onServiceStarted(
			javax.slee.serviceactivity.ServiceStartedEvent serviceEvent,
			ActivityContextInterface aci) {		
		try {
			// check if it's my service that is starting
			ServiceActivity sa = ((ServiceActivityFactory) sbbEnv
					.lookup("slee/serviceactivity/factory")).getActivity();
			if (sa.equals(aci.getActivity())) {
				config.startService();	
				logger.info("Registrar Configuration MBean started");
				getLocationSbb().init();
			}
			else {
				aci.detach(this.sbbContext.getSbbLocalObject());
			}
		} catch (Exception e) {
			logger.error(e);
		}		
	}
	
	public void onActivityEndEvent(ActivityEndEvent event, ActivityContextInterface aci) {
		try {
			// check if it's my service that is starting
			ServiceActivity sa = ((ServiceActivityFactory) sbbEnv
					.lookup("slee/serviceactivity/factory")).getActivity();
			if (sa.equals(aci.getActivity())) {
				config.stopService();
				getLocationSbb().shutdown();
			}
		} catch (Exception e) {
			logger.error(e);
		}		
	}
	
	
	
	// **** REGISTER REQUEST PROCESSING
	
	public void onRegisterEvent(RequestEvent event, ActivityContextInterface ac) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("onRegisterEvent:\n request="+event.getRequest());
		}
		
		// detach from this server transaction activity, we don't want to handle activity end event
		ac.detach(this.sbbContext.getSbbLocalObject());
		
		try {

			// see if child sbb local object is already in CMP
			LocationSbbLocalObject locationService = getLocationSbb();
			
			// get configuration from MBean
			final long maxExpires=config.getSipRegistrationMaxExpires();
			final long minExpires=config.getSipRegistrationMinExpires();
			
			// Process require header

			// Authenticate
			// Authorize
			// OK we're authorized now ;-)

			// extract address-of-record
			String sipAddressOfRecord = getCanonicalAddress((HeaderAddress) event.getRequest()
					.getHeader(ToHeader.NAME));

			if (logger.isDebugEnabled()) {
				logger.debug("onRegisterEvent: address-of-record from request= " + sipAddressOfRecord);
			}

			// map will be empty if user not in LS...
			// Note we don't care if the user has a valid account in the LS, we
			// just add them anyway.
			String sipAddress = getCanonicalAddress((HeaderAddress) event.getRequest()
					.getHeader(ToHeader.NAME));
			Map<String, RegistrationBinding> bindings = locationService
					.getBindings(sipAddress);

			// Do we have any contact header(s)?
			if (event.getRequest().getHeader(ContactHeader.NAME) == null) {
				// Just send OK with current bindings - this request was a
				// query.
				logger.info("query for bindings: sipAddress="+sipAddress);
				sendRegistrationOKResponse(event.getServerTransaction(), event.getRequest(), bindings);
				return;
			}

			// Check contact, callid, cseq
			
			ArrayList newContacts = getContactHeaderList(event.getRequest()
					.getHeaders(ContactHeader.NAME));
			final String callId = ((CallIdHeader) event.getRequest()
			.getHeader(CallIdHeader.NAME)).getCallId();
			final long seq = ((CSeqHeader) event.getRequest()
					.getHeader(CSeqHeader.NAME)).getSeqNumber();
			ExpiresHeader expiresHeader = event.getRequest().getExpires();

			if (hasWildCard(newContacts)) { // This is a "Contact: *" "remove
				// all bindings" request
				if ((expiresHeader == null)
						|| (expiresHeader.getExpires() != 0)
						|| (newContacts.size() > 1)) {
					// malformed request in RFC3261 ch10.3 step 6
					sendErrorResponse(Response.BAD_REQUEST,event.getServerTransaction(),event.getRequest());		
					return;
				}

				if (logger.isDebugEnabled()) {
					logger.debug("Removing bindings");
				}
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
								locationService.removeBinding(sipAddressOfRecord,
										binding.getContactAddress());
							} else {
								sendErrorResponse(Response.BAD_REQUEST,event.getServerTransaction(),event.getRequest());
								return;
							}
						} else {
							it.remove();
							locationService.removeBinding(sipAddressOfRecord, binding
									.getContactAddress());
						}
					}

				} catch (LocationServiceException lse) {
					logger.error(lse);
					sendErrorResponse(Response.SERVER_INTERNAL_ERROR,event.getServerTransaction(),event.getRequest());
					return;
				}

				sendRegistrationOKResponse(event.getServerTransaction(), event.getRequest(), bindings);
			}
						
			else {
				// Update bindings
				if (logger.isDebugEnabled()) {
					logger.debug("Updating bindings");
				}
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
						sendIntervalTooBriefResponse(event.getServerTransaction(), event.getRequest(), minExpires);
						return;
					}

					// Get the q-value (preference) for this binding - default
					// to 0.0 (min)
					float q = 0;
					if (contact.getQValue() != -1)
						q = contact.getQValue();
					if ((q > 1) || (q < 0)) {
						sendErrorResponse(Response.BAD_REQUEST,event.getServerTransaction(),event.getRequest());
						return;
					}

					// Find existing binding
					String contactAddress = contact.getAddress().getURI().toString();

					RegistrationBinding binding = (RegistrationBinding) bindings
							.get(contactAddress);

					if (binding != null) { // Update this binding
						
						if (callId.equals(binding.getCallId())) {
							if (seq <= binding.getCSeq()) {
								sendErrorResponse(Response.BAD_REQUEST,event.getServerTransaction(),event.getRequest());
								return;
							}
						}

						if (requestedExpires == 0) {
							if (logger.isDebugEnabled()) {
								logger.debug("Removing binding: "
									+ sipAddressOfRecord + " -> "
									+ contactAddress);
							}
							bindings.remove(contactAddress);
							locationService.removeBinding(sipAddressOfRecord,
										binding.getContactAddress());
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug("Updating binding: "
									+ sipAddressOfRecord + " -> "
									+ contactAddress);
								logger.debug("contact: " + contact.toString());
							}
							// Lets push it into location service, this will
							// update version of binding
							binding.setCallId(callId);
							binding.setExpires(requestedExpires);
							binding.setRegistrationDate(System.currentTimeMillis());
							binding.setCSeq(seq);
							binding.setQValue(q);
							locationService.updateBinding(binding);
						}

					} else {
						// Create new binding
						if (requestedExpires != 0) {
							if (logger.isDebugEnabled()) {
								logger.debug("Adding new binding: "
									+ sipAddressOfRecord + " -> "
									+ contactAddress);
								logger.debug(contact.toString());
							}

							// removed comment parameter to registration binding
							// - Address and Contact headers don't have comments
							// in 1.1
							RegistrationBinding registrationBinding = locationService
									.addBinding(sipAddress,
											contactAddress, "",
											requestedExpires, System.currentTimeMillis(), q, callId,
											seq);
							bindings.put(registrationBinding.getContactAddress(), registrationBinding);

						}
					}
				}
				// Update bindings, return 200 if successful, 500 on error
				sendRegistrationOKResponse(event.getServerTransaction(), event.getRequest(), bindings);
			}
		} catch (Exception e) {
			// Send error response so client can deal with it
			logger.warn("Exception during request processing", e);
			try {
				sendErrorResponse(Response.SERVER_INTERNAL_ERROR,event.getServerTransaction(),event.getRequest());
			} catch (Exception ex) {
				logger.error(e);
			}
		}
		
	}

	// ------------------- helper methods
	
	private String getCanonicalAddress(HeaderAddress header) {	
		String addr = header.getAddress().getURI().toString();
		int index = addr.indexOf(':');
		index = addr.indexOf(':', index + 1);
		if (index != -1) {
			// Get rid of the port
			addr = addr.substring(0, index);
		}
		return addr;
	}

	private List<ContactHeader> getContactHeaders(
			Collection<RegistrationBinding> bindings) {
		if (bindings == null)
			return null;
		ArrayList<ContactHeader> contactHeaders = new ArrayList<ContactHeader>();
		Iterator<RegistrationBinding> it = bindings.iterator();

		while (it.hasNext()) {
			RegistrationBinding binding = it.next();
			try {
				Address contactAddress = addressFactory.createAddress(binding.getContactAddress());
				javax.sip.header.ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);
		    	// String comment = getComment();
		    	contactHeader.setExpires((int)binding.getExpiresDelta());
		    	contactHeader.setQValue(binding.getQValue());
				contactHeaders.add(contactHeader);
			} catch (Exception e) {
				logger.warn("Failed to create contact headers",e);
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

	private void sendRegistrationOKResponse(
			ServerTransaction serverTransaction, Request request,
			Map<String, RegistrationBinding> bindings) throws ParseException,
			SipException, InvalidArgumentException {
		List contactHeaders = getContactHeaders(bindings.values());

		Response res = (this.messageFactory
				.createResponse(Response.OK, request));

		if ((contactHeaders != null) && (!contactHeaders.isEmpty())) {
			if (logger.isDebugEnabled()) {
				logger.debug("Adding " + contactHeaders.size() + " headers");
			}
			for (int i = 0; i < contactHeaders.size(); i++) {
				ContactHeader hdr = (ContactHeader) contactHeaders.get(i);
				res.addHeader(hdr);
			}
		}
		DateHeader dateHeader = this.headerFactory
				.createDateHeader(new GregorianCalendar());
		res.setHeader(dateHeader);
		if (logger.isInfoEnabled()) {
			logger.info("sending response:\n" + res);
		}
		serverTransaction.sendResponse(res);
	}

	private void sendErrorResponse(int responseCode, ServerTransaction serverTransaction, Request request) throws ParseException, SipException, InvalidArgumentException {
		Response response = this.messageFactory.createResponse(responseCode, request); 
		serverTransaction.sendResponse(response);
		if (logger.isInfoEnabled()) {
			logger.info("sending response:\n" + response);
		}
	}
	
	private void sendIntervalTooBriefResponse(
			ServerTransaction serverTransaction, Request request,
			long minExpires) throws ParseException, SipException,
			InvalidArgumentException {

		Response res = this.messageFactory.createResponse(
				Response.INTERVAL_TOO_BRIEF, request);
		// set date header
		res.setHeader(this.headerFactory
				.createDateHeader(new GregorianCalendar()));
		// set min expires header
		res.addHeader(this.headerFactory.createHeader("Min-Expires", Long
				.toString(minExpires)));
		if (logger.isInfoEnabled()) {
			logger.info("sending response:\n" + res.toString());
		}
		serverTransaction.sendResponse(res);
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

	// location service child relation
	public abstract ChildRelation getLocationSbbChildRelation();

	public LocationSbbLocalObject getLocationSbb() throws TransactionRequiredLocalException, SLEEException, CreateException {
		return (LocationSbbLocalObject) getLocationSbbChildRelation().create();			
	}

	// usuall stuff
	
	public void unsetSbbContext() { sbbContext = null; }
	public void sbbCreate() throws CreateException {}
	public void sbbPostCreate() throws CreateException {}
	public void sbbRemove() {}
	public void sbbPassivate() {}
	public void sbbActivate() {}
	public void sbbLoad() {}
	public void sbbStore() {}
	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface aci) {}
	public void sbbRolledBack(RolledBackContext context) {}
	
}