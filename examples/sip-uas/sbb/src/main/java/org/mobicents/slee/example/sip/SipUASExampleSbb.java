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

package org.mobicents.slee.example.sip;

import java.text.ParseException;

import javax.sip.ListeningPoint;
import javax.sip.ServerTransaction;
import javax.sip.address.AddressFactory;
import javax.sip.header.ContactHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.serviceactivity.ServiceStartedEvent;

import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

import org.mobicents.slee.ActivityContextInterfaceExt;
import org.mobicents.slee.SbbContextExt;

public abstract class SipUASExampleSbb implements javax.slee.Sbb {

	private static final ResourceAdaptorTypeID sipRATypeID = new ResourceAdaptorTypeID(
			"JAIN SIP", "javax.sip", "1.2");
	private static final String sipRALink = "SipRA";

	private SipActivityContextInterfaceFactory sipActivityContextInterfaceFactory;
	private SleeSipProvider sleeSipProvider;
	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	private MessageFactory messageFactory;
	private TimerFacility timerFacility;

	private static ContactHeader contactHeader;
	private static TimerOptions timerOptions;
	private static Tracer tracer;

	private SbbContextExt sbbContext; // This SBB's SbbContext

	// SbbObject lifecycle methods

	public void setSbbContext(SbbContext context) {
		sbbContext = (SbbContextExt) context;
		sipActivityContextInterfaceFactory = (SipActivityContextInterfaceFactory) sbbContext
				.getActivityContextInterfaceFactory(sipRATypeID);
		sleeSipProvider = (SleeSipProvider) sbbContext
				.getResourceAdaptorInterface(sipRATypeID, sipRALink);
		addressFactory = sleeSipProvider.getAddressFactory();
		headerFactory = sleeSipProvider.getHeaderFactory();
		messageFactory = sleeSipProvider.getMessageFactory();
		timerFacility = sbbContext.getTimerFacility();
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	public void sbbCreate() throws javax.slee.CreateException {
	}

	public void sbbPostCreate() throws javax.slee.CreateException {
	}

	public void sbbActivate() {
	}

	public void sbbPassivate() {
	}

	public void sbbRemove() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
	}

	public void sbbRolledBack(RolledBackContext context) {
	}

	// some helper methods to deal with lazy init of static fields

	private ContactHeader getContactHeader() throws ParseException {
		if (contactHeader == null) {
			final ListeningPoint listeningPoint = sleeSipProvider
					.getListeningPoint("udp");
			final javax.sip.address.SipURI sipURI = addressFactory
					.createSipURI(null, listeningPoint.getIPAddress());
			sipURI.setPort(listeningPoint.getPort());
			sipURI.setTransportParam(listeningPoint.getTransport());
			contactHeader = headerFactory.createContactHeader(addressFactory
					.createAddress(sipURI));
		}
		return contactHeader;
	}

	private TimerOptions getTimerOptions() {
		if (timerOptions == null) {
			timerOptions = new TimerOptions();
			timerOptions.setPreserveMissed(TimerPreserveMissed.ALL);
		}
		return timerOptions;
	}

	private Tracer getTracer() {
		if (tracer == null) {
			tracer = sbbContext.getTracer(getClass().getSimpleName());
		}
		return tracer;
	}

	// event handlers, the service's logic

	/**
	 * Event handler method for the event signaling the service activation.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onServiceStartedEvent(ServiceStartedEvent event,
			ActivityContextInterface aci) {
		getTracer().warning("Service activated, now execute SIPP script.");
	}

	/**
	 * Event handler method for the invite SIP message.
	 * 
	 * @param requestEvent
	 * @param aci
	 */
	public void onInviteEvent(javax.sip.RequestEvent requestEvent,
			ActivityContextInterface aci) {

		final ServerTransaction serverTransaction = requestEvent
				.getServerTransaction();
		try {
			// send "trying" response
			Response response = messageFactory.createResponse(Response.TRYING,
					requestEvent.getRequest());
			serverTransaction.sendResponse(response);
			// get local object
			final SbbLocalObject sbbLocalObject = this.sbbContext
					.getSbbLocalObject();
			// detach from the server tx activity
			aci.detach(sbbLocalObject);
			// create dialog activity and attach to it
			final DialogActivity dialog = (DialogActivity) sleeSipProvider
					.getNewDialog(serverTransaction);
			final ActivityContextInterfaceExt dialogAci = (ActivityContextInterfaceExt) sipActivityContextInterfaceFactory
					.getActivityContextInterface(dialog);
			dialogAci.attach(sbbLocalObject);
			// set timer of 60 secs on the dialog aci
			timerFacility.setTimer(dialogAci, null,
					System.currentTimeMillis() + 60000L, getTimerOptions());
			// send 180
			response = messageFactory.createResponse(Response.RINGING,
					requestEvent.getRequest());
			serverTransaction.sendResponse(response);
			// send 200 ok
			response = messageFactory.createResponse(Response.OK,
					requestEvent.getRequest());
			response.addHeader(getContactHeader());
			serverTransaction.sendResponse(response);
		} catch (Exception e) {
			getTracer().severe("failure while processing initial invite", e);
		}
	}

	/**
	 * Event handler method for the timer event.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		aci.detach(sbbContext.getSbbLocalObject());
		final DialogActivity dialog = (DialogActivity) aci.getActivity();
		try {
			dialog.sendRequest(dialog.createRequest(Request.BYE));
		} catch (Exception e) {
			getTracer().severe("failure while processing timer event", e);
		}
	}

}
