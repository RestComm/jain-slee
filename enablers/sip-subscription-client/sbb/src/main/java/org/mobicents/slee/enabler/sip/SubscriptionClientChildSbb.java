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
import javax.sip.header.AcceptHeader;
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

/**
 * SIP Subscription Client SLEE Enabler.
 * 
 * @author baranowb
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
	public void setParentSbb(SubscriptionClientParentSbbLocalObject parent) {
		setParentSbbCMP(parent);
	}

	@Override
	public String getSubscriber() {
		return getSubscriberCMP();
	}

	@Override
	public String getEventPackage() {
		return getEventPackageCMP();
	}

	@Override
	public String getNotifier() {
		return getNotifierCMP();
	}

	@Override
	public void subscribe(String subscriber, String subscriberdisplayName, String notifier, String eventPackage, int expires,
			String contentType, String contentSubType) throws SubscriptionException {
		if (getSubscriber() != null) {
			throw new IllegalStateException("Active subscription found for: " + getSubscriber() + ", events: " + getEventPackage());
		}

		if (subscriber == null) {
			throw new IllegalArgumentException("subscriber argument must not be null!");
		}

		if (subscriberdisplayName == null) {
			throw new IllegalArgumentException("subscriberdisplayName argument must not be null!");
		}

		if (notifier == null) {
			throw new IllegalArgumentException("notifier argument must not be null!");
		}

		if (eventPackage == null) {
			throw new IllegalArgumentException("eventPackage argument must not be null!");
		}

		if (expires <= 0) {
			throw new IllegalArgumentException("expires argument must be greater than zero!");
		}

		// content MAY be null? contentType and contentSubType MUST not
		if (contentType == null) {
			throw new IllegalArgumentException("contentType argument must not be null!");
		}
		if (contentSubType == null) {
			throw new IllegalArgumentException("contentSubType argument must not be null!");
		}

		// store all data.
		this.setSubscriberCMP(subscriber);
		this.setNotifierCMP(notifier);

		this.setContentTypeCMP(contentType);
		this.setContentSubTypeCMP(contentSubType);
		this.setEventPackageCMP(eventPackage);

		// lets get started
		ActivityContextInterface aci = null;
		try {
			Address from = addressFactory.createAddress(subscriber);
			from.setDisplayName(subscriberdisplayName);
			Address to = addressFactory.createAddress(notifier);
			DialogActivity dialogActivity = sleeSipProvider.getNewDialog(from,to);
			
			Request subscribeRequest = createInitialSubscribe(dialogActivity, eventPackage, expires, contentType, contentSubType);
			// DA
			aci = this.sipActivityContextInterfaceFactory.getActivityContextInterface(dialogActivity);
			setSubscribeRequestTypeCMP(SubscribeRequestType.NEW);
			// attach after send, so we dont have to worry about detaching?
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
			ActivityContextInterface aci = getDialogAci();
			cancelExpiresTimer(aci);
			DialogActivity da = (DialogActivity) aci.getActivity();
			Request unsubscribeRequest = createUnSubscribe(da);
			da.sendRequest(unsubscribeRequest);

			this.setSubscribeRequestTypeCMP(SubscribeRequestType.REMOVE);
			// DO NOT da.delete(); - we will receive NOTIFY with "terminated" as
			// Subscription-State value
		} catch (Exception e) {
			if (tracer.isSevereEnabled()) {
				tracer.severe("Failed to  send unSUBSRIBE", e);
			}
			// just fail?
			// ActivityContextInterface daAci = getDialogAci();
			// if (daAci != null) {
			// // its a failure on init, just terminate
			// daAci.detach(this.sbbContext.getSbbLocalObject());
			// ((DialogActivity) daAci.getActivity()).delete();
			// }

			// this.clear();
			this.setSubscribeRequestTypeCMP(null);
			throw new SubscriptionException("Failed to send unSUBSRIBE", e);
		}
	}

	// ///////////////
	// CMP Methods //
	// ///////////////

	public abstract void setParentSbbCMP(SubscriptionClientParentSbbLocalObject parent);

	public abstract SubscriptionClientParentSbbLocalObject getParentSbbCMP();

	public abstract String getEventPackageCMP();

	public abstract void setEventPackageCMP(String s);

	public abstract String getSubscriberCMP();

	public abstract void setSubscriberCMP(String s);

	public abstract String getNotifierCMP();

	public abstract void setNotifierCMP(String s);

	public abstract int getExpiresCMP();

	public abstract void setExpiresCMP(int s);

	public abstract String getContentTypeCMP();

	public abstract void setContentTypeCMP(String s);

	public abstract String getContentSubTypeCMP();

	public abstract void setContentSubTypeCMP(String s);

	public abstract SubscribeRequestType getSubscribeRequestTypeCMP();

	public abstract void setSubscribeRequestTypeCMP(SubscribeRequestType s);

	// /////////////////////////////////
	// Event handlers, for SUBSCRIBE //
	// /////////////////////////////////

	public void onSuccessRespEvent(ResponseEvent event, ActivityContextInterface ac) {
		if (tracer.isFineEnabled())
			tracer.fine("Received 2xx (SUCCESS) response:\n" + event.getResponse());

		SubscribeRequestType type = getSubscribeRequestTypeCMP();
		setSubscribeRequestTypeCMP(null);
		Response response = event.getResponse();
		// we've got OK.
		switch (type) {
		case NEW:
			if (getExpiresCMP() == 0) {
				// check expire, otherwise, NOTIFY got here first and it carried
				// proper value
				ExpiresHeader expiresHeader = response.getExpires();
				setExpiresCMP(expiresHeader.getExpires());
				startExpiresTimer(getDialogAci());
			}

			try {
				getParentSbbCMP().subscribeSucceed(response.getStatusCode(), (SubscriptionClientChildSbbLocalObject) sbbContext.getSbbLocalObject());
			} catch (Exception e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Received exception from parent on subscribe callback", e);
				}
			}
			break;
		case REMOVE:
			try {
				getParentSbbCMP().unsubscribeSucceed(event.getResponse().getStatusCode(), (SubscriptionClientChildSbbLocalObject) sbbContext.getSbbLocalObject());
			} catch (Exception e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Received exception from parent on subscribe callback", e);
				}
			}
			break;
		}
	}

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
		// TODO add more here? 4xx,5xx,6xx responses ?
		Request request = event.getRequest();

		EventHeader eventHeader = (EventHeader) request.getHeader(EventHeader.NAME);
		if (eventHeader == null || !eventHeader.getEventType().equals(getEventPackageCMP())) {
			try {
				Response badEventResponse = this.messageFactory.createResponse(Response.BAD_EVENT, request);

				event.getServerTransaction().sendResponse(badEventResponse);

				// TODO: terminate dialog?
			} catch (ParseException e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Failed to create 489 answer to NOTIFY", e);
				}

			} catch (SipException e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Failed to send 489 answer to NOTIFY", e);
				}
			} catch (InvalidArgumentException e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Failed to send 489 answer to NOTIFY", e);
				}
			}
			return;
		} else {

			try {
				Response okResponse = this.messageFactory.createResponse(Response.OK, request);

				event.getServerTransaction().sendResponse(okResponse);

				// TODO: handle exceptions?, send REMOVE?
			} catch (ParseException e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Failed to create 200 answer to NOTIFY", e);
				}

			} catch (SipException e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Failed to send 200 answer to NOTIFY", e);
				}
			} catch (InvalidArgumentException e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Failed to send 200 answer to NOTIFY", e);
				}
			}
		}
		// lets create Notify
		Notify notify = new Notify();
		notify.setSubscriber(getSubscriberCMP());

		ContentTypeHeader contentType = (ContentTypeHeader) request.getHeader(ContentTypeHeader.NAME);
		// use CT Header?
		notify.setContentType(contentType.getContentType());
		notify.setContentSubType(contentType.getContentSubType());
		notify.setContent(new String(request.getRawContent()));
		notify.setNotifier(getNotifierCMP());
		notify.setSubscriber(getSubscriberCMP());
		// check, whats in header.

		SubscriptionStateHeader subscriptionStateHeader = (SubscriptionStateHeader) request.getHeader(SubscriptionStateHeader.NAME);

		SubscriptionStatus state = SubscriptionStatus.fromString(subscriptionStateHeader.getState());
		notify.setStatus(state);
		if (state == SubscriptionStatus.extension) {
			// whoops
			notify.setStatusExtension(subscriptionStateHeader.getState());
		}
		// do magic
		switch (state) {
		case active:
		case pending:
			// check for exp
			if (subscriptionStateHeader.getExpires() != Notify.NO_TIMEOUT) {
				notify.setExpires(subscriptionStateHeader.getExpires());
				setExpiresCMP(notify.getExpires());
				// reset timer if any
				cancelExpiresTimer(aci);
				startExpiresTimer(aci);
			}

			break;
		case waiting:
			// nothing...
			break;
		case terminated:
		case extension: // ext goes here, since we dont know what ext may
						// require
			// check reason
			String reasonString = subscriptionStateHeader.getReasonCode();
			TerminationReason reason = TerminationReason.fromString(reasonString);
			notify.setTerminationReason(reason);
			if (reason == TerminationReason.extension) {
				notify.setTerminationReasonExtension(reasonString);
			}

			// check reson.
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
				break;
			}
			break;

		}
		// now deliver
		try {
			if(state == SubscriptionStatus.terminated)
			{
				cancelExpiresTimer(aci);
				aci.detach(sbbContext.getSbbLocalObject());
				((DialogActivity)aci.getActivity()).delete();
			}
			getParentSbbCMP().onNotify(notify, (SubscriptionClientChildSbbLocalObject) this.sbbContext.getSbbLocalObject());
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
			Request refreshSubscribe = createRefresh(da, getExpiresCMP());
			da.sendRequest(refreshSubscribe);
			setSubscribeRequestTypeCMP(SubscribeRequestType.REFRESH);
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

	protected DialogActivity getDialogActivity() {
		ActivityContextInterface aci = this.getDialogAci();
		if (aci != null) {
			return (DialogActivity) aci.getActivity();
		} else {
			return null;
		}
	}

	/**
	 * @return
	 */
	protected ActivityContextInterface getDialogAci() {
		for (ActivityContextInterface aci : this.sbbContext.getActivities()) {
			if (aci.getActivity() instanceof DialogActivity) {
				return aci;
			}
		}

		return null;
	}

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
		switch (type) {
		case NEW:
			// if its NEW... ops, we can do clear.
			try {
				getParentSbbCMP().subscribeFailed(errorCode, (SubscriptionClientChildSbbLocalObject) sbbContext.getSbbLocalObject());
			} catch (Exception e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Received exception from parent on subscribe callback", e);
				}
			}
		
			break;
		case REFRESH:
			// failed to refresh, we should get notify when it times out.
			try {
				getParentSbbCMP().resubscribeFailed(errorCode, (SubscriptionClientChildSbbLocalObject) sbbContext.getSbbLocalObject());
			} catch (Exception e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Received exception from parent on subscribe callback", e);
				}
			}
			// if(response.getStatusCode() ==
			// Response.CALL_OR_TRANSACTION_DOES_NOT_EXIST)
			// {
			// //we did not get notify, and there is no subscription.
			// this.clear(); //nothing will ever happen.
			//
			// }
			break;
		case REMOVE:
			// failed to remove, we will receive notify.
			try {
				getParentSbbCMP().unsubscribeFailed(errorCode, (SubscriptionClientChildSbbLocalObject) sbbContext.getSbbLocalObject());
			} catch (Exception e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Received exception from parent on subscribe callback", e);
				}
			}
			// this.clear();
			break;
		}

	}

	/**
	 * @param subscriber
	 * @param subscriberdisplayName
	 * @param notifier
	 * @param content
	 * @return
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws SipException 
	 */
	protected Request createInitialSubscribe(DialogActivity da, String eventPackage, int expires,
			String contentType, String contentSubType) throws ParseException, InvalidArgumentException, SipException {
		
		final Request request = da.createRequest(Request.SUBSCRIBE);
		
		URI contactURI = sleeSipProvider.getLocalSipURI(sleeSipProvider.getListeningPoints()[0].getTransport());
		Address contactAddress = addressFactory.createAddress(contactURI);
		ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);
		request.setHeader(contactHeader);
		
		EventHeader eventHeader = this.headerFactory.createEventHeader(eventPackage);
		request.addHeader(eventHeader);

		AcceptHeader acceptHeader = this.headerFactory.createAcceptHeader(contentType, contentSubType);
		request.addHeader(acceptHeader);

		ExpiresHeader expiresHeader = this.headerFactory.createExpiresHeader(expires);
		request.addHeader(expiresHeader);
		
		// add route header?
		if (this.ecsAddress != null) {
			RouteHeader routeHeader = headerFactory.createRouteHeader(this.ecsAddress);
			request.addHeader(routeHeader);
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
		return this.createRefresh(da, 0);
	}

	/**
	 * @return
	 * @throws SipException
	 * @throws InvalidArgumentException
	 * @throws ParseException
	 */
	protected Request createRefresh(DialogActivity da, int expires) throws SipException, InvalidArgumentException, ParseException {
		Request request = da.createRequest(Request.SUBSCRIBE);
		ExpiresHeader expiresHeader = this.headerFactory.createExpiresHeader(expires);
		request.addHeader(expiresHeader);

		EventHeader eventHeader = (EventHeader) request.getHeader(EventHeader.NAME);
		if (eventHeader == null) {
			eventHeader = this.headerFactory.createEventHeader(getEventPackageCMP());
			request.addHeader(eventHeader);
		}
		AcceptHeader acceptHeader = this.headerFactory.createAcceptHeader(getContentTypeCMP(), getContentSubTypeCMP());
		request.addHeader(acceptHeader);

		return request;
	}

	/**
	 * 
	 */
	private void startExpiresTimer(ActivityContextInterface aci) {
		long expires = this.getExpiresCMP();
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
