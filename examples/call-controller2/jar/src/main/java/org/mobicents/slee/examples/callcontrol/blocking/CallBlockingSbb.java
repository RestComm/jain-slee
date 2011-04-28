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

package org.mobicents.slee.examples.callcontrol.blocking;

import java.text.ParseException;
import java.util.ArrayList;

import javax.sip.InvalidArgumentException;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.FromHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.SLEEException;
import javax.slee.SbbContext;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.facilities.Tracer;
import javax.slee.serviceactivity.ServiceActivity;

import org.mobicents.slee.examples.callcontrol.common.SubscriptionProfileSbb;
import org.mobicents.slee.examples.callcontrol.profile.CallControlProfileCMP;
import org.mobicents.slee.examples.callcontrol.profile.ProfileCreator;

public abstract class CallBlockingSbb extends SubscriptionProfileSbb implements
		javax.slee.Sbb {

	public void onInvite(javax.sip.RequestEvent event, 
			CallBlockingSbbActivityContextInterface localAci) {
		Request request = event.getRequest();

		try {
			localAci.detach(this.getSbbLocalObject());
			
			FromHeader fromHeader = (FromHeader) request.getHeader(FromHeader.NAME);
			ToHeader toHeader = (ToHeader) request.getHeader(ToHeader.NAME);
			// From URI
			URI fromURI = fromHeader.getAddress().getURI();
			// To URI
			URI toURI = toHeader.getAddress().getURI();
			// In the Profile Table the port is not used
			((SipURI)fromURI).removePort();
			((SipURI)toURI).removePort();
			
			ArrayList targets = getBlockedArrayList(toURI.toString());
						
			if (targets != null) {
				// Cheking whether the caller is blocked by the called user
				for (int i = 0; i < targets.size(); i++) {
					if ((targets.get(i).toString()).equalsIgnoreCase(fromURI.toString())) {
						log.info("########## BLOCKING ADDRESS: " + targets.get(i));
						log.info("########## BLOCKING FOR URI: " + toURI);
						localAci.setFilteredByMe(true);
						// Notifiying the client that the INVITE has been blocked
						ServerTransaction stBlocking = (ServerTransaction) localAci.getActivity();
						Response blockingResponse = getMessageFactory().createResponse(
								Response.FORBIDDEN, request);
						stBlocking.sendResponse(blockingResponse);
					}
				}
			}

		} catch (TransactionRequiredLocalException e) {
			log.severe(e.getMessage(), e);
		} catch (SLEEException e) {
			log.severe(e.getMessage(), e);
		} catch (ParseException e) {
			log.severe(e.getMessage(), e);
		} catch (SipException e) {
			log.severe(e.getMessage(), e);
		} catch (InvalidArgumentException e) {
			log.severe(e.getMessage(), e);
		}
	}
	public void onStartServiceEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		//create profiles, since its 1.1 service, we will get event only for this.
		//JSLEE 1.1: 8.8.3
		// Changed in 1.1: A new version of the Service Started Event type has
		// been added. This event is fired only to
		// the starting Service.
		log.info("Processing activation of: "+getSbbContext().getService()+", in "+getSbbContext().getSbb());
		try{
			ProfileCreator.createProfiles();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void onActivityEndEvent(
			javax.slee.ActivityEndEvent event,
			ActivityContextInterface aci) {
		if(aci.getActivity() instanceof ServiceActivity)
		{
			ServiceActivity sa = (ServiceActivity) aci.getActivity();
			//again, we will be attached only to our SA.
			//cleanup.
			log.info("Processing deactivation of: "+getSbbContext().getService()+", in "+getSbbContext().getSbb());
			ProfileCreator.removeProfiles();
		}
	}
	/**
	 * Attempt to find a list of Blocked Addresses (SIP URIs), but the method
	 * returns null if the called user (sipAddress) does not block to any user.
	 */
	private ArrayList getBlockedArrayList(String sipAddress) {
		//sipAddress is AOR: sip:newbie@mobicents.org
		ArrayList uris = null;
		CallControlProfileCMP profile = super.lookup(new Address(AddressPlan.SIP,
				sipAddress));
		if (profile != null) {
			Address[] addresses = profile.getBlockedAddresses();

			if (addresses != null) {
				uris = new ArrayList(addresses.length);

				for (int i = 0; i < addresses.length; i++) {
					String address = addresses[i].getAddressString();

					try {
						SipURI uri = (SipURI) getAddressFactory().createURI(address);
						uris.add(uri);

					} catch (ParseException e) {
						log.severe(e.getMessage(), e);
					}
				}
			}
		}

		return uris;
	}
	
	private Tracer log;
	
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.examples.callcontrol.common.SubscriptionProfileSbb#setSbbContext(javax.slee.SbbContext)
	 */
	@Override
	public void setSbbContext(SbbContext context) {
		// TODO Auto-generated method stub
		super.setSbbContext(context);
		this.log = getSbbContext().getTracer("CallBlockingSbb");
	}

	/*
	public abstract org.mobicents.slee.examples.callcontrol.profile.CallControlProfileCMP getCallControlProfileCMP(
			javax.slee.profile.ProfileID profileID)
			throws javax.slee.profile.UnrecognizedProfileNameException,
			javax.slee.profile.UnrecognizedProfileTableNameException;
	*/
	public abstract org.mobicents.slee.examples.callcontrol.blocking.CallBlockingSbbActivityContextInterface asSbbActivityContextInterface(
			ActivityContextInterface aci);
}