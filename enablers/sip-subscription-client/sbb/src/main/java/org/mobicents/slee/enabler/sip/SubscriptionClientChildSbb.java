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

package org.mobicents.slee.enabler.sip;

import java.text.ParseException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.EventHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.RouteHeader;
import javax.sip.header.SubscriptionStateHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerID;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ResourceAdaptorTypeID;

import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.DialogForkedEvent;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

import org.mobicents.slee.ActivityContextInterfaceExt;
import org.mobicents.slee.SbbContextExt;
import org.mobicents.slee.SbbLocalObjectExt;

/**
 * SIP Subscription Client SLEE Enabler.
 * 
 * @author baranowb
 * @author emartins
 */
public abstract class SubscriptionClientChildSbb implements Sbb, SubscriptionClientChild {
	private static final int DEFAULT_EXPIRES_DRIFT = 15;
	private static final ResourceAdaptorTypeID sipResourceAdaptorTypeID = new ResourceAdaptorTypeID("JAIN SIP","javax.sip","1.2");
	private static final TimerOptions TIMER_OPTIONS = new TimerOptions();
	private static Tracer tracer;

	protected SbbContextExt sbbContext;

	// sip ra
	protected SipActivityContextInterfaceFactory sipActivityContextInterfaceFactory = null;
	protected SleeSipProvider sleeSipProvider = null;
	// sip stuff
	protected MessageFactory messageFactory;
	protected AddressFactory addressFactory;
	protected HeaderFactory headerFactory;

	// something more
	protected int expiresDrift = DEFAULT_EXPIRES_DRIFT;
	protected Address ecsAddress;
	
	// //////////////////
	// SBB LO methods //
	// //////////////////

	@Override
	public SubscriptionData getSubscriptionData() {
		return getSubscriptionDataCMP();
	}
	
	@Override
	public void subscribe(SubscriptionData subscriptionData)
			throws SubscriptionException {
		subscribe(subscriptionData, null);
	}
	
	@Override
	public void subscribe(SubscriptionData subscriptionData,
		SubscriptionRequestContent initialSubscribeContent)
		throws SubscriptionException {
		
		if (getSubscriptionData() != null) {
			throw new IllegalStateException("Active subscription found for: " + getSubscriptionData());
		}

		if (subscriptionData == null) {
			throw new IllegalArgumentException("subscriptionData argument must not be null!");
		}
		
		if (subscriptionData.getEventPackage() == null || subscriptionData.getNotifierURI() == null || subscriptionData.getSubscriberURI() == null) {
			throw new IllegalArgumentException("subscriptionData argument must contain a non null subscriber, notifier and event package.");
		}
		this.setSubscriptionDataCMP(subscriptionData);
		
		// lets get started
		ActivityContextInterface aci = null;
		try {
			Address from = addressFactory.createAddress(subscriptionData.getSubscriberURI());
			if (subscriptionData.getSubscriberDisplayName() != null) {
				from.setDisplayName(subscriptionData.getSubscriberDisplayName());
			}
			Address to = addressFactory.createAddress(subscriptionData.getNotifierURI());
			DialogActivity dialogActivity = sleeSipProvider.getNewDialog(from,to);
			
			Request subscribeRequest = createInitialSubscribe(dialogActivity, subscriptionData, initialSubscribeContent);
			
			// DA
			aci = this.sipActivityContextInterfaceFactory.getActivityContextInterface(dialogActivity);
			setSubscribeRequestTypeCMP(SubscribeRequestType.NEW);
			// attach to DA
			aci.attach(this.sbbContext.getSbbLocalObject());
			// now lets send :)
			dialogActivity.sendRequest(subscribeRequest);

		} catch (TransactionUnavailableException e) {
			// failed to create tx
			if (tracer.isSevereEnabled()) {
				tracer.severe("Failed to create client transaction", e);
			}
			throw new SubscriptionException("Failed to create client transaction", e);
		} catch (Exception e) {
			// failed to create dialog or send msg.
			if (tracer.isSevereEnabled()) {
				tracer.severe("Failed to create dialog or send SUBSRIBE", e);
			}
			if (aci != null) {
				// its a failure on init, just terminate
				aci.detach(this.sbbContext.getSbbLocalObject());
				((DialogActivity) aci.getActivity()).delete();
			}
			throw new SubscriptionException("Failed to create dialog or send SUBSRIBE", e);
		}
		
	}

	@Override
	public void unsubscribe() throws SubscriptionException {
		
		if (getSubscribeRequestTypeCMP() != null) {
			throw new SubscriptionException("Enabler is " + getSubscribeRequestTypeCMP() + ", cannot unsubscribe.");
		}

		try {
			ActivityContextInterface aci = sbbContext.getActivities()[0];
			cancelExpiresTimer(aci);
			DialogActivity da = (DialogActivity) aci.getActivity();
			Request unsubscribeRequest = createUnSubscribe(da);
			setSubscribeRequestTypeCMP(SubscribeRequestType.REMOVE);
			da.sendRequest(unsubscribeRequest);
		} catch (Exception e) {
			if (tracer.isSevereEnabled()) {
				tracer.severe("Failed to send unsubscribe", e);
			}
			this.setSubscribeRequestTypeCMP(null);
			throw new SubscriptionException("Failed to send unSUBSRIBE", e);
		}
	}

	// ///////////////
	// CMP Methods //
	// ///////////////

	public abstract SubscriptionData getSubscriptionDataCMP();

	public abstract void setSubscriptionDataCMP(SubscriptionData sd);

	public abstract SubscribeRequestType getSubscribeRequestTypeCMP();

	public abstract void setSubscribeRequestTypeCMP(SubscribeRequestType s);

	// /////////////////////////////////
	// Event handlers, for SUBSCRIBE //
	// /////////////////////////////////

	// all answers except 2xx indicate something bad ... ech
	public void onRedirRespEvent(ResponseEvent event, ActivityContextInterface ac) {
		if (tracer.isFineEnabled())
			tracer.fine("Received 3xx (REDIRECT) response:\n" + event.getResponse());
		// 3xx should not happen? since ECS/PA acts like proxy?
		handleFailure(event.getResponse().getStatusCode(), ac);
	}

	public void onClientErrorRespEvent(ResponseEvent event, ActivityContextInterface ac) {
		if (tracer.isFineEnabled())
			tracer.fine("Received 4xx (CLIENT ERROR) response:\n" + event.getResponse());
		handleFailure(event.getResponse().getStatusCode(), ac);
	}

	public void onServerErrorRespEvent(ResponseEvent event, ActivityContextInterface ac) {
		if (tracer.isFineEnabled())
			tracer.fine("Received 5xx (SERVER ERROR) response:\n" + event.getResponse());
		handleFailure(event.getResponse().getStatusCode(), ac);
	}

	public void onGlobalFailureRespEvent(ResponseEvent event, ActivityContextInterface ac) {
		if (tracer.isFineEnabled())
			tracer.fine("Received 6xx (GLOBAL FAILURE) response:\n" + event.getResponse());
		handleFailure(event.getResponse().getStatusCode(), ac);
	}

	// //////////////////////////////
	// Event handlers, for NOTIFY //
	// //////////////////////////////

	public void onNotify(RequestEvent event, ActivityContextInterface aci) {
		if (tracer.isFineEnabled())
			tracer.fine("Received Notify, on activity: " + aci.getActivity() + "\nRequest:\n" + event.getRequest());
		
		Request request = event.getRequest();
		SubscriptionData subscriptionData = getSubscriptionDataCMP();
		EventHeader eventHeader = (EventHeader) request.getHeader(EventHeader.NAME);
		if (eventHeader == null || !eventHeader.getEventType().equals(subscriptionData.getEventPackage())) {
			try {
				Response badEventResponse = this.messageFactory.createResponse(Response.BAD_EVENT, request);
				event.getServerTransaction().sendResponse(badEventResponse);
				// TODO: terminate dialog?
			} catch (Exception e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Failed to create 489 answer to NOTIFY", e);
				}

			}
			return;
			
		} else {

			try {
				Response okResponse = this.messageFactory.createResponse(Response.OK, request);
				event.getServerTransaction().sendResponse(okResponse);				
			} catch (Exception e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Failed to create 200 answer to NOTIFY", e);
				}
				return;
			}
		}
		// lets create Notify
		Notify notify = new Notify();
		notify.setSubscriber(subscriptionData.getSubscriberURI());

		ContentTypeHeader contentType = (ContentTypeHeader) request.getHeader(ContentTypeHeader.NAME);
		if (contentType != null) {
			// use CT Header?
			notify.setContentType(contentType.getContentType());
			notify.setContentSubType(contentType.getContentSubType());
			notify.setContent(new String(request.getRawContent()));
		}
		notify.setNotifier(subscriptionData.getNotifierURI());
		// check, whats in header.

		SubscriptionStateHeader subscriptionStateHeader = (SubscriptionStateHeader) request.getHeader(SubscriptionStateHeader.NAME);

		SubscriptionStatus state = SubscriptionStatus.fromString(subscriptionStateHeader.getState());
		notify.setStatus(state);
		
		// do magic
		switch (state) {
		case active:
		case pending:
			// check for exp
			if (subscriptionStateHeader.getExpires() != Notify.NO_TIMEOUT) {
				notify.setExpires(subscriptionStateHeader.getExpires());
				// set timer if needed
				SubscribeRequestType subscribeRequestType = getSubscribeRequestTypeCMP();
				if (subscribeRequestType == SubscribeRequestType.NEW || subscribeRequestType == SubscribeRequestType.REFRESH) {
					startExpiresTimer(aci,subscriptionStateHeader.getExpires());
					setSubscribeRequestTypeCMP(null);
				}								
			}

			break;
		case waiting:
			// nothing...
			break;
		case extension:
			notify.setStatusExtension(subscriptionStateHeader.getState());
		case terminated:
		 	// check reason
			String reasonString = subscriptionStateHeader.getReasonCode();
			TerminationReason reason = TerminationReason.fromString(reasonString);
			notify.setTerminationReason(reason);
			if (reason != null) {
				switch (reason) {
				case rejected:
				case noresource:
				case deactivated:
				case timeout:
					break;
				case probation:
				case giveup:
					if (subscriptionStateHeader.getRetryAfter() != Notify.NO_TIMEOUT) {
						notify.setRetryAfter(subscriptionStateHeader.getRetryAfter());
					}
					break;

				case extension:
					notify.setTerminationReasonExtension(reasonString);
					break;
				}
			}
			break;

		}
		// deliver to parent
		try {
			SbbLocalObjectExt sbbLocalObjectExt = sbbContext.getSbbLocalObject();
			if(state == SubscriptionStatus.terminated)
			{
				cancelExpiresTimer(aci);
				aci.detach(sbbLocalObjectExt);
				event.getDialog().delete();
			}
			((SubscriptionClientParentSbbLocalObject)sbbLocalObjectExt.getParent()).onNotify(notify, (SubscriptionClientChildSbbLocalObject) sbbLocalObjectExt);
		} catch (Exception e) {
			if (tracer.isSevereEnabled()) {
				tracer.severe("Received exception from parent on notify callback", e);
			}
		}

	}

	// //////////////////////////////
	// Event handlers, for FORKs //
	// //////////////////////////////

	public void onDialogForked(DialogForkedEvent event, ActivityContextInterface aci) {

		if (tracer.isFineEnabled())
			tracer.fine("Received fork, on activity: " + aci.getActivity() + "\nResponse:\n" + event.getResponse());
		// kill dialog & subscription, its killed with Expires: 0, not BYE

		Response response = event.getResponse();
		int responseCode = response.getStatusCode();

		// TODO: add headers?
		DialogActivity forkedDialog = (DialogActivity) event.getForkedDialog();
		if (responseCode < 200) {
			// Well?
		} else if (responseCode < 300) {
			try {
				Request unSubscribe = createUnSubscribe(forkedDialog);
				forkedDialog.sendRequest(unSubscribe);
				// we are not attached to forkedDialog, we dont care about resp.
			} catch (Exception e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Failed to send unSUBSCRIBE for forked dialog.", e);
				}
			} finally {
				forkedDialog.delete();
			}
		} else {
			// Well?
		}

	}

	// /////////////////////////////
	// Event handlers, for TIMER //
	// /////////////////////////////
	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		// time to refresh :)
		
		if (getSubscribeRequestTypeCMP() != null) {
			return;
		}

		try {
			DialogActivity da = (DialogActivity) aci.getActivity();
			Request refreshSubscribe = createRefresh(da, getSubscriptionData());
			setSubscribeRequestTypeCMP(SubscribeRequestType.REFRESH);
			da.sendRequest(refreshSubscribe);
		} catch (Exception e) {
			if (tracer.isSevereEnabled()) {
				tracer.severe("Failed to send unSUBSCRIBE for forked dialog.", e);
			}
		}
	}

	public void onTransactionTimeoutEvent(TimeoutEvent event, ActivityContextInterface ac) {
		// bad, failed to communicate
		handleFailure(Response.SERVER_TIMEOUT, ac);
	}

	// //////////////////////////
	// Private helper methods //
	// //////////////////////////

	/**
	 * @param event
	 * @param ac
	 */
	protected void handleFailure(int errorCode, ActivityContextInterface ac) {
		SubscribeRequestType type = getSubscribeRequestTypeCMP();
		setSubscribeRequestTypeCMP(null);
		DialogActivity dialogActivity = (DialogActivity) ac.getActivity();
		if (dialogActivity != null) {
			ac.detach(sbbContext.getSbbLocalObject());
			cancelExpiresTimer(ac);
			dialogActivity.delete();			
		}
		final SubscriptionClientChildSbbLocalObject sbbLocalObject = (SubscriptionClientChildSbbLocalObject) sbbContext.getSbbLocalObject();
		switch (type) {
		case NEW:

			try {
				((SubscriptionClientParentSbbLocalObject)sbbLocalObject.getParent()).subscribeFailed(errorCode, sbbLocalObject);
			} catch (Exception e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Received exception from parent on subscribe callback", e);
				}
			}
		
			break;
		case REFRESH:
			// failed to refresh, we should get notify when it times out.
			try {
				((SubscriptionClientParentSbbLocalObject)sbbLocalObject.getParent()).resubscribeFailed(errorCode, sbbLocalObject);
			} catch (Exception e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Received exception from parent on subscribe callback", e);
				}
			}

			break;
		case REMOVE:
			// failed to remove, we will receive notify.
			try {
				((SubscriptionClientParentSbbLocalObject)sbbLocalObject.getParent()).unsubscribeFailed(errorCode, sbbLocalObject);
			} catch (Exception e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Received exception from parent on subscribe callback", e);
				}
			}

			break;
		}

	}

	/**
	 * @param content 
	 * @param contentSubType 
	 * @param contentType 
	 * @param subscriber
	 * @param subscriberdisplayName
	 * @param notifier
	 * @param content
	 * @return
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws SipException 
	 */
	protected Request createInitialSubscribe(DialogActivity da, SubscriptionData subscriptionData, SubscriptionRequestContent subscriptionContent) throws ParseException, InvalidArgumentException, SipException {
		
		final Request request = da.createRequest(Request.SUBSCRIBE);
		
		URI contactURI = sleeSipProvider.getLocalSipURI(sleeSipProvider.getListeningPoints()[0].getTransport());
		Address contactAddress = addressFactory.createAddress(contactURI);
		ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);
		request.setHeader(contactHeader);
		
		fillSubscribeRequest(request, subscriptionData);
		
		ExpiresHeader expiresHeader = this.headerFactory.createExpiresHeader(subscriptionData.getExpires());
		request.addHeader(expiresHeader);
		// add route header?
		if (this.ecsAddress != null) {
			RouteHeader routeHeader = headerFactory.createRouteHeader(this.ecsAddress);
			request.addHeader(routeHeader);
		}
		//now add content if present
		if(subscriptionContent!=null) {
			ContentTypeHeader cth = this.headerFactory.createContentTypeHeader(subscriptionContent.getContentType().getType(), subscriptionContent.getContentType().getSubType());
			request.setContent(subscriptionContent.getContent().getBytes(), cth);
		}
		
		return request;
	}

	/**
	 * @param da
	 * @param eventId
	 * @return
	 * @throws SipException
	 * @throws InvalidArgumentException
	 * @throws ParseException
	 */
	protected Request createUnSubscribe(DialogActivity da) throws SipException, InvalidArgumentException, ParseException {
		Request request = da.createRequest(Request.SUBSCRIBE);
		fillSubscribeRequest(request,getSubscriptionData());
		request.setExpires(headerFactory.createExpiresHeader(0));
		return request;
	}

	/**
	 * @return
	 * @throws SipException
	 * @throws InvalidArgumentException
	 * @throws ParseException
	 */
	protected Request createRefresh(DialogActivity da, SubscriptionData subscriptionData) throws SipException, InvalidArgumentException, ParseException {
		Request request = da.createRequest(Request.SUBSCRIBE);
		fillSubscribeRequest(request,subscriptionData);
		request.setExpires(headerFactory.createExpiresHeader(subscriptionData.getExpires()));
		return request;
	}

	private void fillSubscribeRequest(Request request, SubscriptionData subscriptionData) throws ParseException {
		EventHeader eventHeader = this.headerFactory.createEventHeader(subscriptionData.getEventPackage());
		if(subscriptionData.getEventParameters()!=null) {
			for(EventPackageParameter parameter : subscriptionData.getEventParameters()) {
				eventHeader.setParameter(parameter.getName(), parameter.getValue());
			}
		}
		request.addHeader(eventHeader);
		if(subscriptionData.getAcceptedContentTypes()!=null) {
			for(ContentType contentType : subscriptionData.getAcceptedContentTypes()) {
				request.addHeader(headerFactory.createAcceptHeader(contentType.getType(), contentType.getSubType()));
			}
		}
		if (subscriptionData.isSupportResourceLists()) {
			// add necessary headers
			request.addHeader(headerFactory.createAcceptHeader("application", "rlmi+xml"));
			request.addHeader(headerFactory.createAcceptHeader("multipart", "related"));
			request.addHeader(headerFactory.createSupportedHeader("eventlist"));
		}		
	}

	/**
	 * 
	 */
	private void startExpiresTimer(ActivityContextInterface aci, int expires) {
		//lets schedule a bit earlier. 5s?
		if(expires-expiresDrift >0) {
			expires-=expiresDrift;
		}				
		sbbContext.getTimerFacility().setTimer(aci,null,System.currentTimeMillis()+expires*1000,TIMER_OPTIONS);
	}

	/**
	 * 
	 */
	private void cancelExpiresTimer(ActivityContextInterface aci) {
		ActivityContextInterfaceExt aciExt = (ActivityContextInterfaceExt) aci;
		TimerID[] timerIDs = aciExt.getTimers();
		if (timerIDs.length > 0) {
			sbbContext.getTimerFacility().cancelTimer(timerIDs[0]);
		}
	}

	// ////////////////////////////////
	// SBB OBJECT LIFECYCLE METHODS //
	// ////////////////////////////////
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbActivate()
	 */
	@Override
	public void sbbActivate() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbCreate()
	 */
	@Override
	public void sbbCreate() throws CreateException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbExceptionThrown(java.lang.Exception,
	 * java.lang.Object, javax.slee.ActivityContextInterface)
	 */
	@Override
	public void sbbExceptionThrown(Exception arg0, Object arg1, ActivityContextInterface arg2) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbLoad()
	 */
	@Override
	public void sbbLoad() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbPassivate()
	 */
	@Override
	public void sbbPassivate() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbPostCreate()
	 */
	@Override
	public void sbbPostCreate() throws CreateException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbRemove()
	 */
	@Override
	public void sbbRemove() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbRolledBack(javax.slee.RolledBackContext)
	 */
	@Override
	public void sbbRolledBack(RolledBackContext arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbStore()
	 */
	@Override
	public void sbbStore() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#setSbbContext(javax.slee.SbbContext)
	 */
	@Override
	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = (SbbContextExt) sbbContext;
		if (tracer == null) {
			tracer = sbbContext.getTracer(SubscriptionClientChildSbb.class.getSimpleName());
		}
		try {
			//sip ra
			this.sipActivityContextInterfaceFactory = (SipActivityContextInterfaceFactory) this.sbbContext.getActivityContextInterfaceFactory(sipResourceAdaptorTypeID);
			this.sleeSipProvider = (SleeSipProvider) this.sbbContext.getResourceAdaptorInterface(sipResourceAdaptorTypeID, "SipRA");
			//sip stuff
			this.messageFactory = this.sleeSipProvider.getMessageFactory();
			this.addressFactory = this.sleeSipProvider.getAddressFactory();
			this.headerFactory = this.sleeSipProvider.getHeaderFactory();
			//env, conf
			Context context = (Context) new InitialContext();
			try{
				String serverAddress = (String) context.lookup("server.address");
				if(serverAddress!=null) {	
					//RFC show entity as SIP URI and ECS address?
					this.ecsAddress = this.sleeSipProvider.getAddressFactory().createAddress(serverAddress);
				}
			}
			catch(NamingException e) {
				if(tracer.isInfoEnabled()) {
					tracer.info("No ECS/PA address to use in Route header.");
				}
			}
			
			try{
				String expireTime = (String) context.lookup("expires.drift");
				if(expireTime!=null) {	
					int intExpireTime = Integer.parseInt(expireTime);
					if(intExpireTime<0) {
						if(tracer.isInfoEnabled()) {
							tracer.info("Expire time drift less than zero, using default: "+this.expiresDrift+"s.");
						}
					}
					else {
						this.expiresDrift = intExpireTime;
						if(tracer.isInfoEnabled()) {
							tracer.info("Expire time drift set to: "+this.expiresDrift+"s.");
						}
					}					
				}
			}catch(NamingException e)
			{
				if(tracer.isInfoEnabled())
				{
					tracer.info("No Expire time drift, using default: "+this.expiresDrift+"s.");
				}
			}
		} catch (NamingException e) {
			tracer.severe("Can't set sbb context.", e);
		} catch (ParseException e) {
			tracer.severe("Can't set sbb context.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#unsetSbbContext()
	 */
	@Override
	public void unsetSbbContext() {
		this.sbbContext = null;
	}

}
