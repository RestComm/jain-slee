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

package org.mobicents.slee.examples.wakeup;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.RequestEvent;
import javax.sip.SipException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.ChildRelation;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.Tracer;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import net.java.slee.resource.sip.SleeSipProvider;

import org.mobicents.slee.example.sjr.data.DataSourceChildSbbLocalInterface;
import org.mobicents.slee.example.sjr.data.DataSourceParentSbbLocalInterface;
import org.mobicents.slee.example.sjr.data.RegistrationBinding;

public abstract class WakeUpSbb implements javax.slee.Sbb, DataSourceParentSbbLocalInterface {

	private static final String FIRST_TOKEN = "WAKE UP IN ";
	private static final String MIDDLE_TOKEN = "s! MSG: ";
	private static final String LAST_TOKEN = "!";
	private static final int FIRST_TOKEN_LENGTH = FIRST_TOKEN.length();
	private static final int MIDDLE_TOKEN_LENGTH = MIDDLE_TOKEN.length();

	// the Sbb's context
	private SbbContext sbbContext;

	// the Sbb's single tracer
	private Tracer tracer = null;

	// cached objects in Sbb's environment, lookups are expensive
	private SleeSipProvider sipProvider;
	private TimerFacility timerFacility;
	private NullActivityContextInterfaceFactory nullACIFactory;
	private NullActivityFactory nullActivityFactory;

	// SbbObject LIFECYCLE METHODS

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#setSbbContext(javax.slee.SbbContext)
	 */
	public void setSbbContext(SbbContext context) {
		// save the sbb context in a field
		this.sbbContext = context;
		// get the tracer if needed
		this.tracer = context.getTracer(WakeUpSbb.class.getSimpleName());
		// get jndi environment stuff
		try {
			final Context myEnv = (Context) new InitialContext();
			// slee facilities
			this.timerFacility = (TimerFacility) myEnv
					.lookup(TimerFacility.JNDI_NAME);
			this.nullACIFactory = (NullActivityContextInterfaceFactory) myEnv
					.lookup(NullActivityContextInterfaceFactory.JNDI_NAME);
			this.nullActivityFactory = (NullActivityFactory) myEnv
					.lookup(NullActivityFactory.JNDI_NAME);
			// the sbb interface to interact with SIP resource adaptor
			this.sipProvider = (SleeSipProvider) myEnv
					.lookup("java:comp/env/slee/resources/jainsip/1.2/provider");
		} catch (Exception e) {
			tracer.severe("Failed to set sbb context", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbCreate()
	 */
	public void sbbCreate() throws javax.slee.CreateException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbPostCreate()
	 */
	public void sbbPostCreate() throws javax.slee.CreateException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbActivate()
	 */
	public void sbbActivate() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbPassivate()
	 */
	public void sbbPassivate() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbRemove()
	 */
	public void sbbRemove() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbLoad()
	 */
	public void sbbLoad() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbStore()
	 */
	public void sbbStore() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbExceptionThrown(java.lang.Exception,
	 * java.lang.Object, javax.slee.ActivityContextInterface)
	 */
	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbRolledBack(javax.slee.RolledBackContext)
	 */
	public void sbbRolledBack(RolledBackContext context) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#unsetSbbContext()
	 */
	public void unsetSbbContext() {
		this.sbbContext = null;
		this.tracer = null;
		this.timerFacility = null;
		this.nullACIFactory = null;
		this.nullActivityFactory = null;
		this.sipProvider = null;
	}

	// CHILD RELATIONS

	/**
	 * Child relation to the location service
	 * 
	 * @return
	 */
	public abstract ChildRelation getLocationChildRelation();

	// CMP FIELDS

	public abstract void setSender(Address sender);

	public abstract Address getSender();

	public abstract void setCallId(CallIdHeader callId);

	public abstract CallIdHeader getCallId();

	public abstract void setBody(String body);

	public abstract String getBody();

	// EVENT HANDLERS

	/**
	 * Event handler for the SIP MESSAGE from the UA
	 * 
	 * @param event
	 * @param aci
	 */
	public void onMessageEvent(javax.sip.RequestEvent event,
			ActivityContextInterface aci) {

		final Request request = event.getRequest();
		try {
			// message body should be *FIRST_TOKEN<timer value in
			// seconds>MIDDLE_TOKEN<msg to send back to UA>LAST_TOKEN*
			final String body = new String(request.getRawContent());
			final int firstTokenStart = body.indexOf(FIRST_TOKEN);
			final int timerDurationStart = firstTokenStart + FIRST_TOKEN_LENGTH;
			final int middleTokenStart = body.indexOf(MIDDLE_TOKEN,
					timerDurationStart);
			final int bodyMessageStart = middleTokenStart + MIDDLE_TOKEN_LENGTH;
			final int lastTokenStart = body.indexOf(LAST_TOKEN,
					bodyMessageStart);
			if (firstTokenStart > -1 && middleTokenStart > -1
					&& lastTokenStart > -1) {
				// extract the timer duration
				final int timerDuration = Integer.parseInt(body.substring(
						timerDurationStart, middleTokenStart));
				// create a null AC and attach the sbb local object
				final ActivityContextInterface timerACI = this.nullACIFactory
						.getActivityContextInterface(this.nullActivityFactory
								.createNullActivity());
				timerACI.attach(sbbContext.getSbbLocalObject());
				// set the timer on the null AC, because the one from this event
				// will end as soon as we send back the 200 ok
				this.timerFacility.setTimer(timerACI, null, System
						.currentTimeMillis()
						+ (timerDuration * 1000), new TimerOptions());
				// extract the body message
				final String bodyMessage = body.substring(bodyMessageStart,
						lastTokenStart);
				// store it in a cmp field
				setBody(bodyMessage);
				// do the same for the call id
				setCallId((CallIdHeader) request.getHeader(CallIdHeader.NAME));
				// also store the sender's address, so we can send the wake up
				// message
				final FromHeader fromHeader = (FromHeader) request
						.getHeader(FromHeader.NAME);
				if (tracer.isInfoEnabled()) {
					tracer.info("Received a valid message from "
							+ fromHeader.getAddress()
							+ " requesting a reply containing '" + bodyMessage
							+ "' after " + timerDuration + "s");
				}
				setSender(fromHeader.getAddress());
				// finally reply to the SIP message request
				sendResponse(event, Response.OK);
			} else {
				// parsing failed
				tracer.warning("Invalid msg '" + body + "' received");
				sendResponse(event, Response.BAD_REQUEST);
			}
		} catch (Throwable e) {
			// oh oh something wrong happened
			tracer.severe("Exception while processing MESSAGE", e);
			try {
				sendResponse(event, Response.SERVER_INTERNAL_ERROR);
			} catch (Exception f) {
				tracer.severe("Exception while sending SERVER INTERNAL ERROR",
						f);
			}
		}
	}

	/**
	 * Event handler from the timer event, which signals that a message must be
	 * sent back to the UA
	 * 
	 * @param event
	 * @param aci
	 */
	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		// detaching so the null AC is claimed after the event handling
		aci.detach(sbbContext.getSbbLocalObject());
		try {
			DataSourceChildSbbLocalInterface child = (DataSourceChildSbbLocalInterface) getLocationChildRelation().create();
			child.getBindings(getSender().getURI().toString());
		} catch (Exception e) {
			tracer.severe("failed to create sip registrar child sbb, to lookup the sender's contacts",e);
			return;
		}
	}

	public void getBindingsResult(int resultCode, List<RegistrationBinding> bindings) {
		if (resultCode < 300) {
			// get data from cmp fields
			String body = getBody();
			CallIdHeader callId = getCallId();
			Address sender = getSender();
			try {
				// create headers needed to create a out-of-dialog request
				AddressFactory addressFactory = sipProvider.getAddressFactory();
				Address fromNameAddress = addressFactory
						.createAddress("sip:wakeup@mobicents.org");
				fromNameAddress.setDisplayName("Wake Up Service");
				HeaderFactory headerFactory = sipProvider.getHeaderFactory();
				FromHeader fromHeader = headerFactory.createFromHeader(
						fromNameAddress, null);
				List<ViaHeader> viaHeaders = new ArrayList<ViaHeader>(1);
				ListeningPoint listeningPoint = sipProvider.getListeningPoints()[0];
				ViaHeader viaHeader = sipProvider.getHeaderFactory()
						.createViaHeader(listeningPoint.getIPAddress(),
								listeningPoint.getPort(),
								listeningPoint.getTransport(), null);
				viaHeaders.add(viaHeader);
				ContentTypeHeader contentTypeHeader = headerFactory
						.createContentTypeHeader("text", "plain");
				CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(2L,
						Request.MESSAGE);
				MaxForwardsHeader maxForwardsHeader = headerFactory
						.createMaxForwardsHeader(70);
				
				// send a message to each contact of the target resource
				MessageFactory messageFactory = sipProvider.getMessageFactory();
				for (RegistrationBinding registration : bindings) {
					try {
						// create request uri
						URI requestURI = addressFactory.createURI(registration
								.getContactAddress());
						// create to header
						ToHeader toHeader = headerFactory.createToHeader(sender,
								null);
						// create request
						Request request = messageFactory.createRequest(requestURI,
								Request.MESSAGE, callId, cSeqHeader, fromHeader,
								toHeader, viaHeaders, maxForwardsHeader,
								contentTypeHeader, body);
						// create client transaction and send request
						ClientTransaction clientTransaction = sipProvider
								.getNewClientTransaction(request);
						clientTransaction.sendRequest();
					} catch (Throwable f) {
						tracer.severe("Failed to create and send message", f);
					}
				}
			} catch (Throwable e) {
				tracer.severe("Failed to create message headers", e);
			}
		}
		else {
			tracer.severe("Unable to send wake up message, the SIP Registrar did not retrieved the target bindings with sucess");
		}
		
	}
	
	public void removeBindingsResult(int arg0, List<RegistrationBinding> arg1,
			List<RegistrationBinding> arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public void updateBindingsResult(int arg0, List<RegistrationBinding> arg1,
			List<RegistrationBinding> arg2, List<RegistrationBinding> arg3) {
		// TODO Auto-generated method stub
		
	}
	
	// HELPERS

	private void sendResponse(RequestEvent event, int responseCode)
			throws SipException, InvalidArgumentException, ParseException {
		event.getServerTransaction().sendResponse(
				sipProvider.getMessageFactory().createResponse(responseCode,
						event.getRequest()));
	}

}
