package org.mobicents.slee.enabler.sip;

import java.text.ParseException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.ClientTransaction;
import javax.sip.DialogState;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.AcceptHeader;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentLengthHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.EventHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.SubscriptionStateHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.facilities.Tracer;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.DialogForkedEvent;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

/**
 * SIP Subscription Client SLEE Enabler.
 * 
 * @author baranowb
 */
public abstract class SubscriptionClientChildSbb implements Sbb, SubscriptionClientChild {
	private static final int DEFAULT_EXPIRES_DRIFT = 15;
	private static Tracer tracer;

	protected SbbContext sbbContext;

	// sip ra
	protected SipActivityContextInterfaceFactory sipActivityContextInterfaceFactory = null;
	protected SleeSipProvider sleeSipProvider = null;
	// sip stuff
	protected MessageFactory messageFactory;
	protected AddressFactory addressFactory;
	protected HeaderFactory headerFactory;

	// slee stuff
	protected TimerFacility timerFacility;
	protected NullActivityContextInterfaceFactory nullACIFactory;
	protected NullActivityFactory nullActivityFactory;

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
	public int getExpires() {
		return getExpiresCMP();
	}

	@Override
	public boolean isRefreshing() {
		return this.getSubscribeRequestTypeCMP() == SubscribeRequestType.REFRESH;
	}

	@Override
	public String getContentType() {
		return getContentTypeCMP();
	}

	@Override
	public String getContentSubType() {
		return getContentSubTypeCMP();
	}

	@Override
	public boolean isSubscriptionActive() {
		// one dialog, one subscription, its active, when there is dialog in
		// case of this enabler.
		DialogActivity da = getDialogActivity();
		if (da != null) {
			DialogState dialogState = da.getState();
			// it will only go to TERM, since we do Dialog.delete();
			if (dialogState != DialogState.TERMINATED) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void subscribe(String subscriber, String subscriberdisplayName, String notifier, String eventPackage, int expires, String content,
			String contentType, String contentSubType) throws SubscriptionException {
		if (isSubscriptionActive()) {
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

		// test notifier
		try {
			String[] ents = notifier.split("@");
			SipURI test = addressFactory.createSipURI(ents[0], ents[1]);
		} catch (Exception e1) {
			throw new IllegalArgumentException("Failed to parse notifier!", e1);
		}

		// store all data.
		this.setSubscriberCMP(subscriber);
		this.setNotifierCMP(notifier);

		this.setContentTypeCMP(contentType);
		this.setContentSubTypeCMP(contentSubType);
		this.setEventPackageCMP(eventPackage);
		// this.setExpiresCMP(expires); //dont set expires, on 2xx we check if
		// its zero, if zero, than no NOTIFY race, and we can start it.

		// lets get started
		try {
			Request subscribeRequest = createInitialSubscribe(subscriber, subscriberdisplayName, notifier, eventPackage, expires, contentType, contentSubType,
					content);
			// this will be activity, but we dont attach
			// since there will be DialogActivity
			ClientTransaction ctx = this.sleeSipProvider.getNewClientTransaction(subscribeRequest);
			// DA
			DialogActivity da = (DialogActivity) this.sleeSipProvider.getNewDialog(ctx);
			ActivityContextInterface daAci = this.sipActivityContextInterfaceFactory.getActivityContextInterface(da);
			setSubscribeRequestTypeCMP(SubscribeRequestType.NEW);
			// attach after send, so we dont have to worry about detaching?
			daAci.attach(this.sbbContext.getSbbLocalObject());
			// now lets send :)
			da.sendRequest(ctx);

		} catch (TransactionUnavailableException e) {
			// failed to create tx
			if (tracer.isSevereEnabled()) {
				tracer.severe("Failed to create client transaction", e);
			}
			this.clear();
			throw new SubscriptionException("Failed to create client transaction", e);
		} catch (Exception e) {
			// failed to create dialog or send msg.
			if (tracer.isSevereEnabled()) {
				tracer.severe("Failed to create dialog or send SUBSRIBE", e);
			}
			ActivityContextInterface daAci = getDialogAci();
			if (daAci != null) {
				// its a failure on init, just terminate
				daAci.detach(this.sbbContext.getSbbLocalObject());
				((DialogActivity) daAci.getActivity()).delete();
			}
			this.clear();
			throw new SubscriptionException("Failed to create dialog or send SUBSRIBE", e);

		}

	}

	@Override
	public void unsubscribe() throws SubscriptionException {
		if (!isSubscriptionActive()) {
			throw new IllegalStateException("No active subscruption active to ubsubscribe from!");
		}
		if (getSubscribeRequestTypeCMP() != null) {
			throw new SubscriptionException("Enabler is " + getSubscribeRequestTypeCMP() + ", cannot unsubscribe.");
		}

		try {
			cancelExpiresTimer();
			DialogActivity da = getDialogActivity();
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

	// /////////////////////
	// As SBB ACI method //
	// /////////////////////
	
	public abstract SubscriptionClientChildActivityContextInterface asSbbActivityContextInterface(ActivityContextInterface aci);

	// /////////////////////////////////
	// Event handlers, for SUBSCRIBE //
	// /////////////////////////////////

	public void onInfoRespEvent(ResponseEvent event, ActivityContextInterface ac) {
		if (tracer.isFineEnabled())
			tracer.fine("Received 1xx (SUCCESS) response:\n" + event.getResponse());
		// here , we do nothing.
	}

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
				startExpiresTimer();
			}

			try {
				getParentSbbCMP().afterSubscribe(response.getStatusCode(), (SubscriptionClientChildSbbLocalObject) sbbContext.getSbbLocalObject());
			} catch (Exception e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Received exception from parent on subscribe callback", e);
				}
			}
			break;
		case REFRESH:
			try {
				getParentSbbCMP().afterRefresh(event.getResponse().getStatusCode(), (SubscriptionClientChildSbbLocalObject) sbbContext.getSbbLocalObject());
			} catch (Exception e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Received exception from parent on subscribe callback", e);
				}
			}
			break;
		case REMOVE:
			try {
				getParentSbbCMP().afterRemove(event.getResponse().getStatusCode(), (SubscriptionClientChildSbbLocalObject) sbbContext.getSbbLocalObject());
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
		handleFailure(event, ac);
	}

	public void onClientErrorRespEvent(ResponseEvent event, ActivityContextInterface ac) {
		if (tracer.isFineEnabled())
			tracer.fine("Received 4xx (CLIENT ERROR) response:\n" + event.getResponse());
		handleFailure(event, ac);
	}

	public void onServerErrorRespEvent(ResponseEvent event, ActivityContextInterface ac) {
		if (tracer.isFineEnabled())
			tracer.fine("Received 5xx (SERVER ERROR) response:\n" + event.getResponse());
		handleFailure(event, ac);
	}

	public void onGlobalFailureRespEvent(ResponseEvent event, ActivityContextInterface ac) {
		if (tracer.isFineEnabled())
			tracer.fine("Received 6xx (GLOBAL FAILURE) response:\n" + event.getResponse());
		handleFailure(event, ac);
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
				cancelExpiresTimer();
				startExpiresTimer();
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
				getDialogActivity().delete();
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
		if (!isSubscriptionActive()) {
			// weird
			if (tracer.isSevereEnabled()) {
				tracer.severe("Got timer event, but there is no active subscription!");
			}
			return;
		}
		cancelExpiresTimer(); // JIC, to remove TID from ACI.
		if (getSubscribeRequestTypeCMP() != null) {
			return;
		}

		try {

			Request refreshSubscribe = createRefresh(getDialogActivity(), getExpiresCMP());
			DialogActivity da = getDialogActivity();
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
		SubscribeRequestType type = getSubscribeRequestTypeCMP();
		setSubscribeRequestTypeCMP(null);
		switch (type) {
		case NEW:

			try {
				getParentSbbCMP().subscribeFailed((SubscriptionClientChildSbbLocalObject) sbbContext.getSbbLocalObject());
			} catch (Exception e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Received exception from parent on subscribe callback", e);
				}
			}
		
			break;
		case REFRESH:
			// failed to refresh, we should get notify when it times out.

			try {
				getParentSbbCMP().refreshFailed((SubscriptionClientChildSbbLocalObject) sbbContext.getSbbLocalObject());
			} catch (Exception e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Received exception from parent on subscribe callback", e);
				}
			}

			break;
		case REMOVE:
			// failed to remove, we will receive notify.
			try {
				getParentSbbCMP().removeFailed((SubscriptionClientChildSbbLocalObject) sbbContext.getSbbLocalObject());
			} catch (Exception e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Received exception from parent on subscribe callback", e);
				}
			}

			break;
		}

	}

	public void onActivityEndEvent(javax.slee.ActivityEndEvent event, ActivityContextInterface aci) {
		if (tracer.isFineEnabled())
			tracer.fine("Received Activtiy End: " + aci.getActivity());
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
	 * 
	 */
	protected void removeDialog() {
		DialogActivity da = getDialogActivity();
		if (da != null && da.getState() != DialogState.TERMINATED) {
			try{
				//be nice, try to free server resources.
				unsubscribe();
			}catch (Exception e) {
				if(tracer.isSevereEnabled())
				{
					tracer.severe("Failed to send unsubscribe");
				}
			}
			
			//after that, we dont care.
			da.delete();
		}
	}

	/**
	 * @param event
	 * @param ac
	 */
	protected void handleFailure(ResponseEvent event, ActivityContextInterface ac) {
		SubscribeRequestType type = getSubscribeRequestTypeCMP();
		setSubscribeRequestTypeCMP(null);
		Response response = event.getResponse();
		switch (type) {
		case NEW:
			// if its NEW... ops, we can do clear.
			try {
				getParentSbbCMP().afterSubscribe(response.getStatusCode(), (SubscriptionClientChildSbbLocalObject) sbbContext.getSbbLocalObject());
			} catch (Exception e) {
				if (tracer.isSevereEnabled()) {
					tracer.severe("Received exception from parent on subscribe callback", e);
				}
			}
		
			break;
		case REFRESH:
			// failed to refresh, we should get notify when it times out.

			try {
				getParentSbbCMP().afterRefresh(event.getResponse().getStatusCode(), (SubscriptionClientChildSbbLocalObject) sbbContext.getSbbLocalObject());
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
				getParentSbbCMP().afterRemove(event.getResponse().getStatusCode(), (SubscriptionClientChildSbbLocalObject) sbbContext.getSbbLocalObject());
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
	 * @throws TransportNotSupportedException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 */
	protected Request createInitialSubscribe(String subscriber, String subscriberdisplayName, String notifier, String eventPackage, int expires,
			String contentType, String contentSubType, String content) throws TransportNotSupportedException, ParseException, InvalidArgumentException {
		// No dialog, we need to forge everything, lets go.
		// SUBSCRIBE sip:$notifier SIP/2.0
		// Via: SIP/2.0/[transport]
		// [local_ip]:[local_port];branch=z9hG4bK-[pid]-[call_number]-[$counter]
		// From:<$subscriberDisplay> $subscriber@localip;tag=[call_number]
		// To: $notifier
		// Call-ID: [call_id]
		// CSeq: [cseq] SUBSCRIBE
		// Contact: sip:$subscriber
		// Max-Forwards: 70
		// Expires: [$expires]
		// Event: $eventPackage
		// Accept: $contentType/$contetnSubType
		// Content-Length: $ln
		// YYY
		ViaHeader viaHeader = sleeSipProvider.getLocalVia(sleeSipProvider.getListeningPoints()[0].getTransport(), null/*
																													 * null
																													 * branch
																													 * ?
																													 */);

		SipURI fromAddress = addressFactory.createSipURI(subscriber, viaHeader.getHost());

		Address fromNameAddress = addressFactory.createAddress(fromAddress);
		fromNameAddress.setDisplayName(subscriberdisplayName);
		FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress, this.hashCode() + "_" + System.currentTimeMillis());
		// remove port if present;
		String[] ents = notifier.split("@");

		// create To Header
		SipURI toAddress = addressFactory.createSipURI(ents[0], ents[1]);
		// NOTE: dont remove port, if its removed, Dialog methods to create
		// referesh for instance wont work properly.
		// if(toAddress.getPort()!=-1)
		// {
		// //remove it from this hdr, it will be present in Request URI
		// toAddress.setPort(-1);
		// }
		Address toNameAddress = addressFactory.createAddress(toAddress);

		ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);
		// set To-Tag???
		toHeader.setTag(System.currentTimeMillis() + "_" + toHeader.hashCode());
		// create Request URI
		SipURI requestURI = addressFactory.createSipURI(ents[0], ents[1]);

		// Create ViaHeaders

		ArrayList viaHeaders = new ArrayList();
		// add via headers
		viaHeaders.add(viaHeader);

		// Create a new CallId header
		CallIdHeader callIdHeader = sleeSipProvider.getNewCallId();

		// Create a new Cseq header
		CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(1L, Request.SUBSCRIBE);

		// Create a new MaxForwardsHeader
		MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70); // leave
																					// it
																					// as
																					// 70?

		// Create the request.
		Request request = messageFactory.createRequest(requestURI, Request.SUBSCRIBE, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);

		SipURI contactUri = (SipURI) fromAddress.clone();
		contactUri.setPort(viaHeader.getPort());
		ContactHeader contactHeader = this.headerFactory.createContactHeader(this.addressFactory.createAddress(contactUri));
		request.addHeader(contactHeader);

		EventHeader eventHeader = this.headerFactory.createEventHeader(eventPackage);
		request.addHeader(eventHeader);

		AcceptHeader acceptHeader = this.headerFactory.createAcceptHeader(contentType, contentSubType);
		request.addHeader(acceptHeader);

		ExpiresHeader expiresHeader = this.headerFactory.createExpiresHeader(expires);
		request.addHeader(expiresHeader);
		if (content != null) {
			ContentTypeHeader contentTypeHeader = this.headerFactory.createContentTypeHeader(acceptHeader.getContentType(), acceptHeader.getContentSubType());
			// request.addHeader(contentTypeHeader);
			byte[] rawContent = content.getBytes(); // is this proper for this
													// casE?
			ContentLengthHeader contentLengthHeader = this.headerFactory.createContentLengthHeader(rawContent.length);
			request.addHeader(contentLengthHeader);
			request.setContent(rawContent, contentTypeHeader);
		}

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

	protected void clear() {
		cancelExpiresTimer();
		setContentSubTypeCMP(null);
		setContentTypeCMP(null);
		setSubscriberCMP(null);
		setSubscribeRequestTypeCMP(null);
		setEventPackageCMP(null);
		setExpiresCMP(0);
		setNotifierCMP(null);
	}

	/**
	 * 
	 */
	private void startExpiresTimer() {
		if (getExpiresCMP() == 0) {
			// nothing yet.
			return;
		}
		ActivityContextInterface naAci = null;
		ActivityContextInterface[] acis = this.sbbContext.getActivities();
		for (ActivityContextInterface aci : acis) {
			if (aci.getActivity() instanceof NullActivity) {
				NullActivity na = (NullActivity) aci.getActivity();
				naAci = this.nullACIFactory.getActivityContextInterface(na);
			}
		}

		if (naAci == null) {
			// create activity for this publication;
			NullActivity na = this.nullActivityFactory.createNullActivity();
			naAci = this.nullACIFactory.getActivityContextInterface(na);
			// attach.
			naAci.attach(this.sbbContext.getSbbLocalObject());
		}
		long expires = this.getExpiresCMP();

		// lets schedule a bit earlier. 5s?
		if (expires - expiresDrift > 0) {
			expires -= expiresDrift;
		}

		TimerOptions to = new TimerOptions(100, TimerPreserveMissed.LAST);
		// this.setTimerIDCMP(this.timerFacility.setTimer(naAci,null,System.currentTimeMillis()+expires,to));
		this.asSbbActivityContextInterface(naAci).setExpiresTimerID(this.timerFacility.setTimer(naAci, null, System.currentTimeMillis() + expires * 1000, to));
	}

	/**
	 * 
	 */
	private void cancelExpiresTimer() {
		ActivityContextInterface naAci = null;
		ActivityContextInterface[] acis = this.sbbContext.getActivities();
		for (ActivityContextInterface aci : acis) {
			if (aci.getActivity() instanceof NullActivity) {
				NullActivity na = (NullActivity) aci.getActivity();
				naAci = this.nullACIFactory.getActivityContextInterface(na);
			}
		}

		if (naAci != null) {
			SubscriptionClientChildActivityContextInterface aci = asSbbActivityContextInterface(naAci);
			if (aci.getExpiresTimerID() != null) {
				this.timerFacility.cancelTimer(aci.getExpiresTimerID());
			}
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
		// if this is called, its means there wont be any msgs, remove
		// everything.
		removeDialog();
		//help SLEE?
		this.clear();
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
		this.sbbContext = sbbContext;
		if (tracer == null) {
			tracer = sbbContext.getTracer(SubscriptionClientChildSbb.class.getSimpleName());
		}
		try {
			Context context = (Context) new InitialContext().lookup("java:comp/env");
			// sip ra
			this.sipActivityContextInterfaceFactory = (SipActivityContextInterfaceFactory) context.lookup("slee/resources/jainsip/1.2/acifactory");
			this.sleeSipProvider = (SleeSipProvider) context.lookup("slee/resources/jainsip/1.2/provider");
			// sip stuff
			this.messageFactory = this.sleeSipProvider.getMessageFactory();
			this.addressFactory = this.sleeSipProvider.getAddressFactory();
			this.headerFactory = this.sleeSipProvider.getHeaderFactory();

			// slee stuff
			context = new InitialContext(); // new, since facilities names are
											// absolute path.
			this.timerFacility = (TimerFacility) context.lookup(TimerFacility.JNDI_NAME);
			this.nullACIFactory = (NullActivityContextInterfaceFactory) context.lookup(NullActivityContextInterfaceFactory.JNDI_NAME);
			this.nullActivityFactory = (NullActivityFactory) context.lookup(NullActivityFactory.JNDI_NAME);
			// env, conf
			try {
				String serverAddress = (String) context.lookup("server.address");
				if (serverAddress != null) {
					// RFC show entity as SIP URI and ECS address?
					this.ecsAddress = this.sleeSipProvider.getAddressFactory().createAddress(serverAddress);
				}
			} catch (NamingException e) {
				if (tracer.isInfoEnabled()) {
					tracer.info("No ECS/PA address to use in Route header.");
				}
			}

			try {
				String expireTime = (String) context.lookup("expires.drift");
				if (expireTime != null) {
					int intExpireTime = Integer.parseInt(expireTime);
					if (intExpireTime < 0) {
						if (tracer.isInfoEnabled()) {
							tracer.info("Expire time drift less than zero, using default: " + this.expiresDrift + "s.");
						}
					} else {
						this.expiresDrift = intExpireTime;
						if (tracer.isInfoEnabled()) {
							tracer.info("Expire time drift set to: " + this.expiresDrift + "s.");
						}
					}

				}
			} catch (NamingException e) {
				if (tracer.isInfoEnabled()) {
					tracer.info("No Expire time drift, using default: " + this.expiresDrift + "s.");
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
