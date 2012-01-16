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

package org.mobicents.slee.examples.callcontrol.forwarding;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
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
import javax.slee.EventContext;
import javax.slee.SLEEException;
import javax.slee.SbbContext;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.facilities.Tracer;

import org.mobicents.slee.example.sjr.data.DataSourceChildSbbLocalInterface;
import org.mobicents.slee.example.sjr.data.DataSourceChildSbbLocalObject;
import org.mobicents.slee.example.sjr.data.DataSourceParentSbbLocalInterface;
import org.mobicents.slee.example.sjr.data.RegistrationBinding;
import org.mobicents.slee.examples.callcontrol.common.SubscriptionProfileSbb;
import org.mobicents.slee.examples.callcontrol.profile.CallControlProfileCMP;


/**
 * 
 * If the user is subscribed to forwarding
 * 
 * @author torosvi
 * @author Ivelin Ivanov
 * @author baranowb
 */
public abstract class CallForwardingSbb extends SubscriptionProfileSbb implements javax.slee.Sbb,DataSourceParentSbbLocalInterface {

	public void onInvite(javax.sip.RequestEvent event, CallForwardingSbbActivityContextInterface localAci,EventContext eventContext) {
		Request request;

		try {
			

			if (localAci.getFilteredByAncestor()) {
				log.info("########## CALL FORWARDING SBB: FILTERED BY ANCESTOR ##########");
				// Next in chain has to know that someone is looking after
				// message
				localAci.setFilteredByMe(true);
				localAci.detach(this.getSbbLocalObject());
				// If it was not set, every change in the chain of services will
				// extort source change in service lower in chain...
				return;
			}
			//suspend, so others wont get it.
			
			//query location service
			eventContext.suspendDelivery();
			//store event context
			this.setEventContextCMP(eventContext);
			request = event.getRequest();
			// ToHeader toHeader = (ToHeader) request.getHeader(ToHeader.NAME);
			// URI toURI = toHeader.getAddress().getURI();
			URI toURI = event.getRequest().getRequestURI();
			getLocationSbb().getBindings(toURI.toString());
			
		} catch (CreateException e) {
			log.severe(e.getMessage(), e);
		}
		
		return;
	}

	public void setSbbContext(SbbContext context) {
		super.setSbbContext(context);
		this.log = getSbbContext().getTracer("CallForwardingSbb");
		// To create Header objects from a particular implementation of JAIN SIP
		headerFactory = getSipFactoryProvider().getHeaderFactory();
	}

	private Tracer log;
	
	protected final HeaderFactory getHeaderFactory() {
		return headerFactory;
	}

	private HeaderFactory headerFactory;

	public abstract CallForwardingSbbActivityContextInterface asSbbActivityContextInterface(ActivityContextInterface aci);

	
	// SBB LO methods
	
	
	@Override
	public void getBindingsResult(int arg0, List<RegistrationBinding> bindings) {
		//here we act.
		
		EventContext eventContext = this.getEventContextCMP();
		RequestEvent event = (RequestEvent) eventContext.getEvent();
		URI toURI = event.getRequest().getRequestURI();
		CallForwardingSbbActivityContextInterface localAci = this.asSbbActivityContextInterface(eventContext.getActivityContextInterface());
		
		//detach and resume delivery
		eventContext.resumeDelivery();
		localAci.detach(getSbbLocalObject());
		
		
		URI contactURI = isUserAvailable(bindings);
		if (contactURI != null) {
			// USER IS AVAILABLE
			localAci.setFilteredByMe(true);
			log.info("########## User " + toURI + " is available with contact " + contactURI);

			//Deffer to proxy ?
			
			return;
		} else {
			log.info("########## User " + toURI + " is not available, checking backup address");
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
	}

	@Override
	public void removeBindingsResult(int arg0, List<RegistrationBinding> arg1,
			List<RegistrationBinding> arg2) {
		//Definetly wont happen
		
	}

	@Override
	public void updateBindingsResult(int arg0, List<RegistrationBinding> arg1,
			List<RegistrationBinding> arg2, List<RegistrationBinding> arg3) {
		//Should not happen :)
	}

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
			log.severe(e.getMessage(), e);
		} catch (TransactionRequiredLocalException e) {
			log.severe(e.getMessage(), e);
		} catch (SLEEException e) {
			log.severe(e.getMessage(), e);
		} catch (SipException e) {
			log.severe(e.getMessage(), e);
		} catch (InvalidArgumentException e) {
			log.severe(e.getMessage(), e);
		}

		return toAddress;
	}

	/**
	 * Attempts to find a locally registered contact address for the given URI,
	 * using the location service interface.
	 */
	private URI isUserAvailable(List<RegistrationBinding> bindings)  {

		URI target = null;
		

		if (bindings != null & !bindings.isEmpty()) {
			Iterator<RegistrationBinding> it = bindings.iterator();

			while (it.hasNext()) {
				RegistrationBinding binding = it.next();
				log.info("########## BINDINGS: " + binding);
				ContactHeader header = null;
				try {
					header = getHeaderFactory().createContactHeader(getAddressFactory().createAddress(binding.getContactAddress()));
				} catch (ParseException e) {
					log.severe(e.getMessage(), e);
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
					log.severe(e.getMessage(), e);
				}
			}
		}

		return backupAddress;
	}

	/*
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
	 */
	public abstract ChildRelation getLocationChildRelation();

	public abstract DataSourceChildSbbLocalObject getLocationSbbCMP();

	public abstract void setLocationSbbCMP(DataSourceChildSbbLocalObject value);
	
	public abstract EventContext getEventContextCMP();

	public abstract void setEventContextCMP(EventContext value);
	
	
	

	public DataSourceChildSbbLocalInterface getLocationSbb() throws TransactionRequiredLocalException, SLEEException,
			CreateException {
		DataSourceChildSbbLocalObject sbbLocalObject = getLocationSbbCMP();
		if (sbbLocalObject == null) {
			sbbLocalObject = (DataSourceChildSbbLocalObject) getLocationChildRelation().create();
			setLocationSbbCMP(sbbLocalObject);
		}
		return sbbLocalObject;
	}
}