/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.examples.callcontrol.forwarding;

import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;

import javax.sip.InvalidArgumentException;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.address.Address;
import javax.sip.address.URI;
import javax.sip.header.ContactHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.AddressPlan;
import javax.slee.ChildRelation;
import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRequiredLocalException;

import org.mobicents.slee.examples.callcontrol.common.SubscriptionProfileSbb;
import org.mobicents.slee.examples.callcontrol.profile.CallControlProfileCMP;
import org.mobicents.slee.services.sip.common.SipSendErrorResponseException;
import org.mobicents.slee.services.sip.location.LocationSbbLocalObject;
import org.mobicents.slee.services.sip.location.LocationServiceException;
import org.mobicents.slee.services.sip.location.RegistrationBinding;

/**
 * 
 * If the user is subscribed to forwarding
 * 
 * @author torosvi
 * @author Ivelin Ivanov
 * 
 */
public abstract class CallForwardingSbb extends SubscriptionProfileSbb implements javax.slee.Sbb {

	public void onInvite(javax.sip.RequestEvent event, CallForwardingSbbActivityContextInterface localAci) {
		Request request;

		try {
			localAci.detach(this.getSbbLocalObject());

			if (localAci.getFilteredByAncestor()) {
				log.info("########## CALL FORWARDING SBB: FILTERED BY ANCESTOR ##########");
				// Next in chain has to know that someone is looking after
				// message
				localAci.setFilteredByMe(true);
				// If it was not set, every change in the chain of services will
				// extort source change in service lower in chain...
				return;
			}

			request = event.getRequest();
			// ToHeader toHeader = (ToHeader) request.getHeader(ToHeader.NAME);
			// URI toURI = toHeader.getAddress().getURI();
			URI toURI = event.getRequest().getRequestURI();
			URI contactURI = isUserAvailable(toURI);
			if (contactURI != null) {
				// USER IS AVAILABLE
				localAci.setFilteredByMe(true);
				log.info("########## User " + toURI + " is available with contact " + contactURI);

				// Create proxy child SBB
				ChildRelation ProxyRelation = getJainSipProxySbb();
				SbbLocalObject ProxyChild = ProxyRelation.create();
				// Attach ProxyChild to the activity
				// Event router will pass this event to child SBB,
				// which in this case is the Proxy SBB. It will in turn proxy
				// the request to the callee.
				localAci.attach(ProxyChild);

				return;
			} else {
				log.info("########## User " + toURI + " is not available, not forwarding");
			}
		} catch (SipSendErrorResponseException e) {
			log.error(e.getMessage(), e);
		} catch (CreateException e) {
			log.error(e.getMessage(), e);
		}

		// IF WE GOT HERE IT MEANS THAT USER IS NOT AVAILABLE AND SBB HIGHER IN
		// CHAIN DID NOT FILTER INVITE.
		// WE HAVE TO FIND NEW ADDRESS... OR LEAVE INVITE TO BE PROCESSED BY
		// NEXT SBB IN CHAIN.
		Address add = forwardCall(event, localAci);

		if (add != null) {
			// INVITE WAS FORWARDED
			// let the next service in the chain know that the event was
			// processed here.
			localAci.setFilteredByMe(true);
		}

		// LET NEXT CHAINED SBB TAKE CARE OF INVITE.
		return;
	}

	public void setSbbContext(SbbContext context) {
		super.setSbbContext(context);
		// To create Header objects from a particular implementation of JAIN SIP
		headerFactory = getSipFactoryProvider().getHeaderFactory();
	}

	protected final HeaderFactory getHeaderFactory() {
		return headerFactory;
	}

	private HeaderFactory headerFactory;

	public abstract ChildRelation getJainSipProxySbb();

	public abstract org.mobicents.slee.examples.callcontrol.profile.CallControlProfileCMP getCallControlProfileCMP(
			javax.slee.profile.ProfileID profileID) throws javax.slee.profile.UnrecognizedProfileNameException,
			javax.slee.profile.UnrecognizedProfileTableNameException;

	public abstract CallForwardingSbbActivityContextInterface asSbbActivityContextInterface(ActivityContextInterface aci);

	/**
	 * Try to find a backup address for the user where the call can be forwarded
	 * to
	 * 
	 * @param event
	 * @param ac
	 * @return the address where the call is forwarded or null if there is none
	 */
	protected Address forwardCall(javax.sip.RequestEvent event, ActivityContextInterface ac) {
		Address toAddress = null;
		Request request = event.getRequest();

		try {
			// Checking if the called user has any backup address
			ToHeader toHeader = (ToHeader) request.getHeader(ToHeader.NAME);
			String toURI = toHeader.getAddress().getURI().toString();

			Address backupAddress = getBackupAddress(toURI);

			if (backupAddress != null) {
				// Checking whether the called user has any backup address.
				toAddress = getAddressFactory().createAddress(backupAddress.toString());
				// Notifying the caller that the call has to be redirected
				ServerTransaction st = (ServerTransaction) ac.getActivity();
				ContactHeader contactHeader = getHeaderFactory().createContactHeader(toAddress);
				Response response = getMessageFactory().createResponse(Response.MOVED_TEMPORARILY, request);
				response.setHeader(contactHeader);
				st.sendResponse(response);
				log.info("########## REQUEST FORWARDED: " + contactHeader.toString());
				// The Request-URI of the new request uses the value
				// of the Contact header field in the response
			}

		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		} catch (TransactionRequiredLocalException e) {
			log.error(e.getMessage(), e);
		} catch (SLEEException e) {
			log.error(e.getMessage(), e);
		} catch (SipException e) {
			log.error(e.getMessage(), e);
		} catch (InvalidArgumentException e) {
			log.error(e.getMessage(), e);
		}

		return toAddress;
	}

	/**
	 * Attempts to find a locally registered contact address for the given URI,
	 * using the location service interface.
	 */
	private URI isUserAvailable(URI uri) throws SipSendErrorResponseException {
		String addressOfRecord = uri.toString();
		URI target = null;
		Map bindings = null;

		try {
			bindings = getLocationSbb().getBindings(addressOfRecord);

		} catch (LocationServiceException e) {
			log.error(e.getMessage(), e);
		} catch (TransactionRequiredLocalException e) {
			log.error(e.getMessage(), e);
		} catch (SLEEException e) {
			log.error(e.getMessage(), e);
		} catch (CreateException e) {
			log.error(e.getMessage(), e);
		}

		if (bindings != null & !bindings.isEmpty()) {
			Iterator it = bindings.values().iterator();

			while (it.hasNext()) {
				RegistrationBinding binding = (RegistrationBinding) it.next();
				log.info("########## BINDINGS: " + binding);
				ContactHeader header = null;
				try {
					header = getHeaderFactory().createContactHeader(getAddressFactory().createAddress(binding.getContactAddress()));
				} catch (ParseException e) {
					log.error(e.getMessage(), e);
				}
				log.info("########## CONTACT HEADER: " + header);

				if (header == null) { // entry expired
					continue; // see if there are any more contacts...
				}

				Address na = header.getAddress();
				log.info("isUserAvailable Address: " + na);
				target = na.getURI();
				break;
			}

			if (target == null) {
				log.error("findLocalTarget: No contacts for " + addressOfRecord + " found.");
				throw new SipSendErrorResponseException("User temporarily unavailable",
						Response.TEMPORARILY_UNAVAILABLE);
			}
		}

		return target;
	}

	/**
	 * Attempt to find a Backup Address, but the method returns null if there
	 * isn't any address to forward the INVITE.
	 */
	private Address getBackupAddress(String sipAddress) {
		Address backupAddress = null;
		CallControlProfileCMP profile = this.lookup(new javax.slee.Address(AddressPlan.SIP, sipAddress));

		if (profile != null) {
			javax.slee.Address address = profile.getBackupAddress();

			if (address != null) {

				try {
					backupAddress = getAddressFactory().createAddress(address.getAddressString());

				} catch (ParseException e) {
					log.error(e.getMessage(), e);
				}
			}
		}

		return backupAddress;
	}

	public void onAck(javax.sip.RequestEvent event, CallForwardingSbbActivityContextInterface localAci) {

		onNonInviteEvent(event, localAci);
	}

	public void onBye(javax.sip.RequestEvent event, CallForwardingSbbActivityContextInterface localAci) {

		onNonInviteEvent(event, localAci);
	}

	public void onCancel(net.java.slee.resource.sip.CancelRequestEvent event, CallForwardingSbbActivityContextInterface localAci) {

		onNonInviteEvent(event, localAci);
	}

	private void onNonInviteEvent(javax.sip.RequestEvent event, CallForwardingSbbActivityContextInterface localAci) {

		localAci.detach(this.getSbbLocalObject());
		// get proxy child SBB
		ChildRelation proxyRelation = getJainSipProxySbb();
		if (!proxyRelation.isEmpty()) {
			// we have a child so we are processing this call,
			// attach the proxy so it can have a chance to cancel the invite
			localAci.attach((SbbLocalObject) proxyRelation.iterator().next());
			log.info("########## Processing request " + event.getRequest().getMethod() + " for user "
					+ event.getRequest().getRequestURI());
		}
	}

	public abstract ChildRelation getLocationSbbChildRelation();

	public abstract LocationSbbLocalObject getLocationSbbCMP();

	public abstract void setLocationSbbCMP(LocationSbbLocalObject value);

	public LocationSbbLocalObject getLocationSbb() throws TransactionRequiredLocalException, SLEEException,
			CreateException {
		LocationSbbLocalObject sbbLocalObject = getLocationSbbCMP();
		if (sbbLocalObject == null) {
			sbbLocalObject = (LocationSbbLocalObject) getLocationSbbChildRelation().create();
			setLocationSbbCMP(sbbLocalObject);
		}
		return sbbLocalObject;
	}
}