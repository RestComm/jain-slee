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

package org.mobicents.slee.example.sjr.sip;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.ActivityContextNamingFacility;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerID;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.facilities.Tracer;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.serviceactivity.ServiceActivity;
import javax.slee.serviceactivity.ServiceStartedEvent;

import net.java.slee.resource.sip.SleeSipProvider;

import org.mobicents.slee.ActivityContextInterfaceExt;
import org.mobicents.slee.ChildRelationExt;
import org.mobicents.slee.SbbContextExt;
import org.mobicents.slee.example.sjr.data.DataSourceChildSbbLocalInterface;
import org.mobicents.slee.example.sjr.data.DataSourceParentSbbLocalInterface;
import org.mobicents.slee.example.sjr.data.RegistrationBinding;
import org.mobicents.slee.example.sjr.sip.jmx.RegistrarConfigurator;

/**
 * A SBB that examples usage of the JDBC RA.
 * 
 * @author baranowb
 * @author martins
 */
public abstract class SIPRegistrarSbb implements Sbb,
		DataSourceParentSbbLocalInterface {

	// not a best thing :)
	private static final RegistrarConfigurator config = new RegistrarConfigurator();
	private static final TimerOptions defaultTimerOptions = createDefaultTimerOptions();

	private static TimerOptions createDefaultTimerOptions() {
		TimerOptions timerOptions = new TimerOptions();
		timerOptions.setPreserveMissed(TimerPreserveMissed.ALL);
		return timerOptions;
	}

	/**
	 * the SBB object context
	 */
	private SbbContextExt sbbContextExt;

	/**
	 * the SBB logger
	 */
	private Tracer tracer;

	// SIP RA
	private static final ResourceAdaptorTypeID sipRATypeID = new ResourceAdaptorTypeID(
			"JAIN SIP", "javax.sip", "1.2");
	private static final String sipRALink = "SipRA";
	private SleeSipProvider sipRA;

	private MessageFactory messageFactory;
	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;

	// SLEE stuff again, those are used in JDBC tasks to do the magic :)
	private TimerFacility timerFacility;
	private NullActivityFactory nullActivityFactory;
	private NullActivityContextInterfaceFactory nullActivityContextInterfaceFactory;
	private ActivityContextNamingFacility activityContextNamingFacility;

	// SLEE abstract

	public abstract SbbActivityContextInterface asSbbActivityContextInterface(
			ActivityContextInterface aci);

	public abstract ChildRelationExt getChildRelation();

	// SLEE Events
	/**
	 * Event handler for {@link ServiceStartedEvent}.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onServiceStartedEvent(ServiceStartedEvent event,
			ActivityContextInterface aci) {
		tracer.info("SIP JDBC Registrar starting...");
		// start config mbean
		SIPRegistrarSbb.config.startService();
		try {
			DataSourceChildSbbLocalInterface child = (DataSourceChildSbbLocalInterface) getChildRelation()
					.create(ChildRelationExt.DEFAULT_CHILD_NAME);
			child.init();
		} catch (Exception e) {
			tracer.severe(
					"Exception initiating data source, SIP JDBC Registrar startup failed.",
					e);
			return;
		}
		tracer.warning("SIP JDBC Registrar started.");
	}

	public void onTimerEvent(TimerEvent timer, SbbActivityContextInterface aci) {
		// detach from this activity, we don't want to handle any other event on
		// it
		aci.detach(this.sbbContextExt.getSbbLocalObject());
		// get data needed to remove binding from aci
		RegistrationBindingData data = aci.getData();
		if (data == null) {
			// another service's timer event, ignore
			return;
		}
		try {
			DataSourceChildSbbLocalInterface child = (DataSourceChildSbbLocalInterface) getChildRelation()
					.create(ChildRelationExt.DEFAULT_CHILD_NAME);
			child.removeBinding(data.getContact(), data.getAddress());
		} catch (Exception e) {
			tracer.severe("Exception invoking data source child sbb.", e);
			return;
		}
		// end the activity
		try {
			((NullActivity) aci.getActivity()).endActivity();
		} catch (Exception e) {
			tracer.warning("failed to end binding aci", e);
		}
	}

	public void onActivityEndEvent(ActivityEndEvent activityEndEvent,
			ActivityContextInterface aci) {
		if (aci.getActivity() instanceof ServiceActivity) {
			SIPRegistrarSbb.config.stopService();
		} else {
			tracer.warning("got an unexpected aee on activity "
					+ aci.getActivity());
		}
	}

	// SIP Events
	@SuppressWarnings("unchecked")
	public void onRegisterEvent(RequestEvent event, ActivityContextInterface aci) {
		if (this.tracer.isFineEnabled()) {
			this.tracer
					.fine("onRegisterEvent:\n request=" + event.getRequest());
		}
		try {
			// get configuration from MBean
			final long maxExpires = config.getSipRegistrationMaxExpires();
			final long minExpires = config.getSipRegistrationMinExpires();

			// Process require header

			// Authenticate
			// Authorize
			// OK we're authorized now ;-)

			// extract address-of-record
			String sipAddressOfRecord = getCanonicalAddress((HeaderAddress) event
					.getRequest().getHeader(ToHeader.NAME));

			if (this.tracer.isFineEnabled()) {
				this.tracer
						.fine("onRegisterEvent: address-of-record from request= "
								+ sipAddressOfRecord);
			}

			final String sipAddress = getCanonicalAddress((HeaderAddress) event
					.getRequest().getHeader(ToHeader.NAME));
			final String callId = ((CallIdHeader) event.getRequest().getHeader(
					CallIdHeader.NAME)).getCallId();
			final long cSeq = ((CSeqHeader) event.getRequest().getHeader(
					CSeqHeader.NAME)).getSeqNumber();
			if (event.getRequest().getHeader(ContactHeader.NAME) == null) {
				// Just send OK with current bindings - this request was a
				// query.
				if (this.tracer.isFineEnabled()) {
					this.tracer.fine("query for bindings: sipAddress="
							+ sipAddress);
				}
				try {
					DataSourceChildSbbLocalInterface child = (DataSourceChildSbbLocalInterface) getChildRelation()
							.create(ChildRelationExt.DEFAULT_CHILD_NAME);
					child.getBindings(sipAddress);
				} catch (Exception e) {
					tracer.severe("Exception invoking data source child sbb.",
							e);
					aci.detach(sbbContextExt.getSbbLocalObject());
					sendErrorResponse(Response.SERVER_INTERNAL_ERROR,
							event.getServerTransaction());
				}
				return;
			}

			// else its update/add/remove op
			// do some prechecks before sending task to JDBC RA. JDBC exeutes
			// batch in another set of threads. To avoid
			// latency, lets check if we can deny REGISTER without pushing job
			// to JDBC RA threads.
			List<ContactHeader> newContacts = getContactHeaderList(event
					.getRequest().getHeaders(ContactHeader.NAME));

			final ExpiresHeader expiresHeader = event.getRequest().getExpires();

			if (hasWildCard(newContacts)) {
				if (this.tracer.isFineEnabled()) {
					this.tracer.fine("Wildcard remove");
				}
				// This is a "Contact: *" "remove all bindings" request
				if ((expiresHeader == null)
						|| (expiresHeader.getExpires() != 0)
						|| (newContacts.size() > 1)) {
					// malformed request in RFC3261 ch10.3 step 6
					aci.detach(sbbContextExt.getSbbLocalObject());
					sendErrorResponse(Response.BAD_REQUEST,
							event.getServerTransaction());
					return;
				}

				// now do the work. in jdbc task
				try {
					DataSourceChildSbbLocalInterface child = (DataSourceChildSbbLocalInterface) getChildRelation()
							.create(ChildRelationExt.DEFAULT_CHILD_NAME);
					child.removeBindings(sipAddress, callId, cSeq);
				} catch (Exception e) {
					tracer.severe("Exception invoking data source child sbb.",
							e);
					aci.detach(sbbContextExt.getSbbLocalObject());
					sendErrorResponse(Response.SERVER_INTERNAL_ERROR,
							event.getServerTransaction());
					return;
				}

			} else {
				// Update bindings
				if (this.tracer.isFineEnabled()) {
					this.tracer.fine("Updating bindings");
				}
				ListIterator<ContactHeader> li = newContacts.listIterator();

				while (li.hasNext()) {
					final ContactHeader contact = (ContactHeader) li.next();

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
						aci.detach(sbbContextExt.getSbbLocalObject());
						sendErrorResponse(Response.INTERVAL_TOO_BRIEF,
								event.getServerTransaction());
						return;
					}

					try {
						contact.setExpires((int) requestedExpires);
					} catch (InvalidArgumentException e) {
						tracer.severe("failed to set expires?!?!", e);
						aci.detach(sbbContextExt.getSbbLocalObject());
						sendErrorResponse(Response.SERVER_INTERNAL_ERROR,
								event.getServerTransaction());
						return;
					}

					// Get the q-value (preference) for this binding - default
					// to 0.0 (min)
					float q = 0;
					if (contact.getQValue() != -1)
						q = contact.getQValue();
					if ((q > 1) || (q < 0)) {
						aci.detach(sbbContextExt.getSbbLocalObject());
						sendErrorResponse(Response.BAD_REQUEST,
								event.getServerTransaction());
						return;
					}

				}

				// play with DB :)
				try {
					DataSourceChildSbbLocalInterface child = (DataSourceChildSbbLocalInterface) getChildRelation()
							.create(ChildRelationExt.DEFAULT_CHILD_NAME);
					child.updateBindings(sipAddress, callId, cSeq, newContacts);
				} catch (Exception e) {
					tracer.severe("Exception invoking data source child sbb.",
							e);
					aci.detach(sbbContextExt.getSbbLocalObject());
					sendErrorResponse(Response.SERVER_INTERNAL_ERROR,
							event.getServerTransaction());
					return;
				}
			}

		} catch (Exception e) {
			this.tracer.severe("Failed to process REGISTER request", e);
		}
	}

	// call backs from data source child sbb

	@Override
	public void getBindingsResult(int resultCode,
			List<RegistrationBinding> bindings) {
		ServerTransaction serverTransaction = getRegisterTransactionToReply();
		if (serverTransaction == null) {
			tracer.warning("failed to find SIP server tx to send response");
			return;
		}
		try {
			if (resultCode < 300) {
				sendRegisterSuccessResponse(resultCode, bindings,
						serverTransaction);
			} else {
				sendErrorResponse(resultCode, serverTransaction);
			}
		} catch (Exception e) {
			tracer.severe("failed to send SIP response", e);
		}
	}

	private static final List<RegistrationBinding> EMPTY_LIST = Collections
			.emptyList();

	@Override
	public void removeBindingsResult(int resultCode,
			List<RegistrationBinding> currentBindings,
			List<RegistrationBinding> removedBindings) {
		// same processing as update result
		updateBindingsResult(resultCode, currentBindings, EMPTY_LIST,
				removedBindings);
	}

	@Override
	public void updateBindingsResult(int resultCode,
			List<RegistrationBinding> currentBindings,
			List<RegistrationBinding> updatedBindings,
			List<RegistrationBinding> removedBindings) {
		ServerTransaction serverTransaction = getRegisterTransactionToReply();
		if (serverTransaction == null) {
			tracer.warning("failed to find SIP server tx to send response");
			return;
		}
		try {
			if (resultCode < 300) {
				// we have to post process, reset/add/cancel timers...
				// first update, since those are more important. This has to be
				// done, since we want timers updated first
				// so events are not fired and dont corrupt DB entries(remove
				// them,
				// after they are updated)
				updateTimers(updatedBindings);
				cancelTimers(removedBindings);
				sendRegisterSuccessResponse(resultCode, currentBindings,
						serverTransaction);
			} else {
				// we just fail :)
				sendErrorResponse(resultCode, serverTransaction);
			}
		} catch (Exception e) {
			tracer.severe("failed to send SIP response", e);
		}
	}

	// --- SLEE 1.1 SBB contract

	@Override
	public void setSbbContext(SbbContext context) {
		// SLEE
		this.tracer = context.getTracer("SipRegistrar");
		this.sbbContextExt = (SbbContextExt) context;
		this.nullActivityContextInterfaceFactory = this.sbbContextExt
				.getNullActivityContextInterfaceFactory();
		this.activityContextNamingFacility = this.sbbContextExt
				.getActivityContextNamingFacility();
		this.nullActivityFactory = this.sbbContextExt.getNullActivityFactory();
		this.timerFacility = this.sbbContextExt.getTimerFacility();

		// get SIP stuff
		this.sipRA = (SleeSipProvider) this.sbbContextExt
				.getResourceAdaptorInterface(sipRATypeID, sipRALink);

		this.messageFactory = this.sipRA.getMessageFactory();
		this.headerFactory = this.sipRA.getHeaderFactory();
		this.addressFactory = this.sipRA.getAddressFactory();

	}

	@Override
	public void unsetSbbContext() {

		this.sbbContextExt = null;
		this.tracer = null;

		this.sipRA = null;

		this.headerFactory = null;
		this.messageFactory = null;
		this.addressFactory = null;

	}

	@Override
	public void sbbActivate() {

	}

	@Override
	public void sbbCreate() throws CreateException {

	}

	@Override
	public void sbbExceptionThrown(Exception arg0, Object arg1,
			ActivityContextInterface arg2) {

	}

	@Override
	public void sbbLoad() {

	}

	@Override
	public void sbbPassivate() {

	}

	@Override
	public void sbbPostCreate() throws CreateException {

	}

	@Override
	public void sbbRemove() {

	}

	@Override
	public void sbbRolledBack(RolledBackContext arg0) {

	}

	@Override
	public void sbbStore() {

	}

	// private stuff

	/**
	 * @return
	 */
	private ServerTransaction getRegisterTransactionToReply() {
		ActivityContextInterface[] acis = this.sbbContextExt.getActivities();
		Object activity = null;
		for (ActivityContextInterface aci : acis) {
			activity = aci.getActivity();
			if (activity instanceof ServerTransaction) {
				// detach to not handle the activity end
				aci.detach(sbbContextExt.getSbbLocalObject());
				return (ServerTransaction) activity;
			}
		}
		return null;
	}

	/**
	 * Simple method to cancel pending timers for contacts that have been
	 * removed
	 * 
	 * @param removedContacts
	 */
	private void cancelTimers(
			List<org.mobicents.slee.example.sjr.data.RegistrationBinding> removedContacts) {
		for (RegistrationBinding binding : removedContacts) {
			ActivityContextInterfaceExt aci = (ActivityContextInterfaceExt) this.activityContextNamingFacility
					.lookup(getACIName(binding.getContactAddress(),
							binding.getSipAddress()));
			// IF exists end the activity, SLEE will cancel timers, remove aci
			// names, etc.
			if (aci != null) {
				((NullActivity) aci.getActivity()).endActivity();
			}
		}
	}

	/**
	 * Simple method to update timers for passed contacts. If timer exists, it
	 * will be reset to new timeout. If it does not exist, it will be create
	 * along with NullActivity.
	 * 
	 * @param contacts
	 */
	private void updateTimers(
			List<org.mobicents.slee.example.sjr.data.RegistrationBinding> contacts) {
		String bindingAciName = null;
		ActivityContextInterface aci = null;
		for (RegistrationBinding binding : contacts) {
			bindingAciName = getACIName(binding.getContactAddress(),
					binding.getSipAddress());
			aci = this.activityContextNamingFacility.lookup(bindingAciName);
			if (aci != null) {
				// binding update, cancel old timer
				for (TimerID timerID : ((ActivityContextInterfaceExt) aci)
						.getTimers()) {
					this.timerFacility.cancelTimer(timerID);
				}
				// NOTE: DO NOT END activity, we will reuse it.
			} else {
				// binding creation, create null aci, bind a name and store the
				// jdbc task for removal (to run if timer expires)
				NullActivity nullActivity = this.nullActivityFactory
						.createNullActivity();
				aci = this.nullActivityContextInterfaceFactory
						.getActivityContextInterface(nullActivity);
				// NOTE: DO NOT ATTACH to activity, so the sbb entity is
				// claimed. The timer event is initial!
				// set name
				try {
					this.activityContextNamingFacility
							.bind(aci, bindingAciName);
				} catch (Exception e) {
					this.tracer.severe("Failed to bind aci name "
							+ bindingAciName, e);
				}
				SbbActivityContextInterface rgAci = asSbbActivityContextInterface(aci);
				rgAci.setData(new RegistrationBindingData().setAddress(
						binding.getSipAddress()).setContact(
						binding.getContactAddress()));
			}

			// set new timer
			timerFacility.setTimer(aci, null, System.currentTimeMillis()
					+ ((binding.getExpires() + 1) * 1000), defaultTimerOptions);

		}
	}

	// send methods

	private void sendRegisterSuccessResponse(int resultCode,
			List<RegistrationBinding> bindings,
			ServerTransaction serverTransaction) throws ParseException,
			SipException, InvalidArgumentException {

		List<?> contactHeaders = getContactHeaders(bindings);

		Response res = (this.messageFactory.createResponse(resultCode,
				serverTransaction.getRequest()));

		if ((contactHeaders != null) && (!contactHeaders.isEmpty())) {
			if (this.tracer.isFineEnabled()) {
				this.tracer
						.fine("Adding " + contactHeaders.size() + " headers");
			}
			for (int i = 0; i < contactHeaders.size(); i++) {
				ContactHeader hdr = (ContactHeader) contactHeaders.get(i);
				res.addHeader(hdr);
			}
		}
		DateHeader dateHeader = this.headerFactory
				.createDateHeader(new GregorianCalendar());
		res.setHeader(dateHeader);
		serverTransaction.sendResponse(res);
	}

	private void sendErrorResponse(int resultCode,
			ServerTransaction serverTransaction) throws ParseException,
			SipException, InvalidArgumentException {

		Response response = this.messageFactory.createResponse(resultCode,
				serverTransaction.getRequest());
		if (resultCode == Response.INTERVAL_TOO_BRIEF) {
			response.setHeader(this.headerFactory
					.createDateHeader(new GregorianCalendar()));
			// set min expires header
			response.addHeader(this.headerFactory.createHeader("Min-Expires",
					Long.toString(config.getSipRegistrationMinExpires())));
		}
		serverTransaction.sendResponse(response);
	}

	// util methods
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

	private boolean hasWildCard(List<?> contactHeaders) {
		Iterator<?> it = contactHeaders.iterator();
		while (it.hasNext()) {
			ContactHeader header = (ContactHeader) it.next();
			if (header.toString().indexOf('*') > 0)
				return true;
		}
		return false;
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
				Address contactAddress = addressFactory.createAddress(binding
						.getContactAddress());
				javax.sip.header.ContactHeader contactHeader = headerFactory
						.createContactHeader(contactAddress);
				// String comment = getComment();
				contactHeader.setExpires((int) binding.getExpiresDelta());
				contactHeader.setQValue(binding.getQValue());
				contactHeaders.add(contactHeader);
			} catch (Exception e) {
				this.tracer.warning("Failed to create contact headers", e);
			}
		}
		return contactHeaders;
	}

	private ArrayList<ContactHeader> getContactHeaderList(
			ListIterator<ContactHeader> it) {
		ArrayList<ContactHeader> l = new ArrayList<ContactHeader>();
		while (it.hasNext()) {
			l.add((ContactHeader) it.next());
		}
		return l;
	}

	private String getACIName(String contactAddress, String sipAddress) {
		return new StringBuilder("sjr:c=").append(contactAddress).append(",a=")
				.append(sipAddress).toString();
	}

}