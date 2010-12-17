package org.mobicents.slee.enabler.sip;

import java.text.ParseException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContentLengthHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.EventHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.MinExpiresHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.SIPETagHeader;
import javax.sip.header.SIPIfMatchHeader;
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

import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

/**
 * SIP Publication Client SLEE Enabler. It creates PUBLISH interaction and manages
 * it. It automatically refreshes publication based on content of ECS/PA response. It keeps map of ETag to publish interaction.
 * 
 * @author baranowb
 */
public abstract class PublicationClientChildSbb implements Sbb, PublicationClientChild {

	private static final int DEFAULT_EXPIRES_DRIFT = 5;
	private static Tracer tracer;

	protected SbbContext sbbContext;

	//sip ra
	protected SipActivityContextInterfaceFactory sipActivityContextInterfaceFactory = null;
	protected SleeSipProvider sleeSipProvider = null;
	//sip stuff
	protected MessageFactory messageFactory;
	protected AddressFactory addressFactory;
	protected HeaderFactory headerFactory;
	protected Address ecsAddress;
	protected int expiresDrift = DEFAULT_EXPIRES_DRIFT;
	//slee stuff
	protected TimerFacility timerFacility;
	protected NullActivityContextInterfaceFactory nullACIFactory;
	protected NullActivityFactory nullActivityFactory;
	
	// //////////////////
	// SBB LO methods //
	// //////////////////

	@Override
	public void setParentSbb(PublicationClientParentLocalObject parent) {
		setParentSbbCMP(parent);
	}

	@Override
	public String getEntity() {
		return this.getEntityCMP();
	}

	@Override
	public String getETag() {
		return this.getETagCMP();
	}

	@Override
	public boolean isRefreshing()
	{
		return this.getRefreshCMP();
	}
	//FIXME: should xxxPulication methods throw exception to indicate failure and not clean state? boolean return value is ambiguous.
	@Override
	public void newPublication(String entity, String eventPackage, String document, String contentType, String contentSubType, int expires) throws PublicationException{
		//initial, we should check if there is publication active 
		if(isPublicationActive())
		{
			throw new IllegalStateException("Enabler is handling publication for: "+this.getEntityCMP());
		}
		
		//check args
		if(entity == null)
		{
			throw new IllegalArgumentException("entity must not be null.");
		}
		if(eventPackage == null)
		{
			throw new IllegalArgumentException("eventPackage must not be null.");
		}
		
		if(contentType == null)
		{
			throw new IllegalArgumentException("contentType must not be null.");
		}
		
		if(contentSubType == null)
		{
			throw new IllegalArgumentException("contentSubType must not be null.");
		}
		if(document == null)
		{
			throw new IllegalArgumentException("document must not be null.");
		}
		if(expires < 0)
		{
			throw new IllegalArgumentException("expires must be greater or equal to 0.");
		}
		
		//test if we can create SipURI from entity!
		try {
			String[] ents = entity.split("@");
			SipURI test = addressFactory.createSipURI(ents[0], ents[1]);
		} catch (ParseException e1) {
			throw new IllegalArgumentException("Failed to parse entity!",e1);
		}
		//otherwise lets kick off.
		
		
		//PublicationClientChildActivityContextInterface pccAci = asSbbActivityContextInterface(naAci);
		//store some data.
		
		this.setEntityCMP(entity);
		this.setEventPackageCMP(eventPackage);
		this.setExpiresCMP(expires);
		
		
		ClientTransaction ctx = null;
		ActivityContextInterface ctxAci = null;
		boolean clear = true;
		try {

			Request r = createNewPublishRequest( contentType, contentSubType, document);
			
			ctx = this.sleeSipProvider.getNewClientTransaction(r);
			ctxAci = this.sipActivityContextInterfaceFactory.getActivityContextInterface(ctx);
			ctxAci.attach(this.sbbContext.getSbbLocalObject());
			

			//Dont start timer here, since we don't have ETag!
			//startExpiresTimer();
			
			asSbbActivityContextInterface(ctxAci).setPublishRequestType(PublishRequestType.NEW);
			ctx.sendRequest();
			clear = false;
		} catch (TransactionUnavailableException e) {
			if(tracer.isSevereEnabled())
			{
				tracer.severe("Failed to create SIP transaction!", e);
			}
			//no CTX
			throw new PublicationException(e);
		} catch (SipException e) {
			if(tracer.isSevereEnabled())
			{
				tracer.severe("Failed to send SIP request!", e);
			}
			
			//tx is here...
			ctxAci.detach(this.sbbContext.getSbbLocalObject());
			//it will timeout?
		} catch (Exception e) {
			//ParseException, InvalidArgumentException
			if(tracer.isSevereEnabled())
			{
				tracer.severe("Failed to create SIP message!", e);
			}
			//no CTX
			throw new PublicationException(e);
		}finally
		{
			if(clear)
			{
				this.clear();
			}
		}
	}

	@Override
	public void updatePublication(String document, String contentType, String contentSubType, int expires)  throws PublicationException{
		if(!isPublicationActive())
		{
			throw new IllegalStateException("Enabler does not handle any publication, cannot update!");
		}
		//check args
		if(contentType == null)
		{
			throw new IllegalArgumentException("contentType must not be null.");
		}
		
		if(contentSubType == null)
		{
			throw new IllegalArgumentException("contentSubType must not be null.");
		}
		if(document == null)
		{
			throw new IllegalArgumentException("document must not be null.");
		}
		if(expires <= 0)
		{
			throw new IllegalArgumentException("expires must be greater than 0.");
		}
		if(getRefreshCMP())
		{
			throw new PublicationException("Enabler is refreshing, cannot send another PUBLISH!"); 
		}
		cancelExpiresTimer();
		//update expire
		//TODO: ensure its not less than min expires?
		this.setExpiresCMP(expires);
		
		
		//issue request.
		ClientTransaction ctx = null;
		ActivityContextInterface ctxAci = null;
		try {

			Request r = createUpdatePublishRequest( contentType, contentSubType, document);
			
			ctx = this.sleeSipProvider.getNewClientTransaction(r);
			ctxAci = this.sipActivityContextInterfaceFactory.getActivityContextInterface(ctx);
			ctxAci.attach(this.sbbContext.getSbbLocalObject());
			

			//start timer here, if ECS/PA return something else, we will reschedule.
			startExpiresTimer();
			
			asSbbActivityContextInterface(ctxAci).setPublishRequestType(PublishRequestType.UPDATE);
			ctx.sendRequest();
		}  catch (TransactionUnavailableException e) {
			if(tracer.isSevereEnabled())
			{
				tracer.severe("Failed to create SIP transaction!", e);
			}
			//no CTX
			throw new PublicationException(e);
		} catch (SipException e) {
			if(tracer.isSevereEnabled())
			{
				tracer.severe("Failed to send SIP request!", e);
			}
			
			//tx is here...
			ctxAci.detach(this.sbbContext.getSbbLocalObject());
			//it will timeout?
		} catch (Exception e) {
			//ParseException, InvalidArgumentException
			if(tracer.isSevereEnabled())
			{
				tracer.severe("Failed to create SIP message!", e);
			}
			//no CTX
			throw new PublicationException(e);
		}finally
		{
			
		}

		return;
	}


	@Override
	public void removePublication()  throws PublicationException{
		if(!isPublicationActive())
		{
			throw new IllegalStateException("Enabler does not handle any publication, cannot remove!");
		}
		if(getRefreshCMP())
		{
			throw new PublicationException("Enabler is refreshing, cannot send another PUBLISH!"); 
		}else
		{
			cancelExpiresTimer(); // so no refresh is created.
			//set to 0, its done in REMOVE action, makes publication expire immediately.
			this.setExpiresCMP(0);
			
			
			//issue request.
			ClientTransaction ctx = null;
			ActivityContextInterface ctxAci = null;
			try {

				Request r = createRemovePublishRequest();
				
				ctx = this.sleeSipProvider.getNewClientTransaction(r);
				ctxAci = this.sipActivityContextInterfaceFactory.getActivityContextInterface(ctx);
				ctxAci.attach(this.sbbContext.getSbbLocalObject());
				

				//start timer here, if ECS/PA return something else, we will reschedule.
				startExpiresTimer();
				
				asSbbActivityContextInterface(ctxAci).setPublishRequestType(PublishRequestType.REMOVE);
				ctx.sendRequest();
			}  catch (TransactionUnavailableException e) {
				if(tracer.isSevereEnabled())
				{
					tracer.severe("Failed to create SIP transaction!", e);
				}
				//no CTX
				throw new PublicationException(e);
			} catch (SipException e) {
				if(tracer.isSevereEnabled())
				{
					tracer.severe("Failed to send SIP request!", e);
				}
				
				//tx is here...
				ctxAci.detach(this.sbbContext.getSbbLocalObject());
				//it will timeout?
			} catch (Exception e) {
				//ParseException, InvalidArgumentException
				if(tracer.isSevereEnabled())
				{
					tracer.severe("Failed to create SIP message!", e);
				}
				//no CTX
				throw new PublicationException(e);
			}finally
			{
				
			}
			return;
		}
	}
	
	
	

	/////////////////
	// CMP Methods //
	/////////////////
	public abstract void setParentSbbCMP(PublicationClientParentLocalObject parent);
	public abstract PublicationClientParentLocalObject getParentSbbCMP();
	public abstract void setEntityCMP(String d);
	public abstract String getEntityCMP();
	public abstract void  setEventPackageCMP(String eventPackage);
	public abstract String getEventPackageCMP( );
	public abstract void setExpiresCMP( int expires);
	public abstract int getExpiresCMP();
	public abstract void setETagCMP(String d);
	public abstract String getETagCMP();
	//flags for simple concurrency control, since SBB is REENTRANT
	public abstract boolean getRefreshCMP();
	public abstract void setRefreshCMP(boolean b);
	
	 
	
	//public abstract void setTimerIDCMP( TimerID expires);
	//public abstract TimerID getTimerIDCMP();
	///////////////////////
	// As SBB ACI method //
    ///////////////////////
	public abstract PublicationClientChildActivityContextInterface asSbbActivityContextInterface(ActivityContextInterface aci);
	
	////////////////////
	// Event handlers //
	////////////////////
	
	public void onInfoRespEvent(ResponseEvent event, ActivityContextInterface ac) {
		if (tracer.isFineEnabled())
			tracer.fine("Received 1xx (SUCCESS) response:\n"+event.getResponse());
		//here , we do nothing.
	}

	public void onSuccessRespEvent(ResponseEvent event, ActivityContextInterface ac) {
		//if (tracer.isFineEnabled())
			tracer.info("Received 2xx (SUCCESS) response:\n"+event.getResponse());
		//2xx, great, lets see...
		Response response = event.getResponse();
		//get ETag
		
		SIPETagHeader SIP_ETag = (SIPETagHeader) response.getHeader(SIPETagHeader.NAME);
		ExpiresHeader expiresHeader = (ExpiresHeader) response.getHeader(ExpiresHeader.NAME);
		if(getExpiresCMP() == 0 || expiresHeader!=null)
		{
			//update expires time.
			setExpiresCMP(expiresHeader.getExpires());
			
		
		}
		Result result = new Result(response.getStatusCode(),SIP_ETag.getETag(),expiresHeader.getExpires(),getEntityCMP());
		
		
		
		PublicationClientChildActivityContextInterface pccAci = asSbbActivityContextInterface(ac);
		switch (pccAci.getPublishRequestType()) {
		case NEW:
			cancelExpiresTimer();
			startExpiresTimer();
			//update ETag
			this.setETagCMP(SIP_ETag.getETag());
			try{
				this.getParentSbbCMP().afterNewPublication(result, (PublicationClientChildLocalObject) this.sbbContext.getSbbLocalObject());
			}catch(Exception e)
			{
				if(tracer.isSevereEnabled())
				{
					tracer.severe("Exception in publication parent!", e);
				}
			}
			
			//
			break;
		case REFRESH:
			cancelExpiresTimer();
			startExpiresTimer();
			//update ETag
			this.setETagCMP(SIP_ETag.getETag());
			try{
				this.getParentSbbCMP().afterRefreshPublication(result, (PublicationClientChildLocalObject) this.sbbContext.getSbbLocalObject());
			}catch(Exception e)
			{
				if(tracer.isSevereEnabled())
				{
					tracer.severe("Exception in publication parent!", e);
				}
			}
			setRefreshCMP(false);
			break;
		case UPDATE:
			cancelExpiresTimer();
			startExpiresTimer();
			//update ETag
			this.setETagCMP(SIP_ETag.getETag());
			try{
				this.getParentSbbCMP().afterUpdatePublication(result, (PublicationClientChildLocalObject) this.sbbContext.getSbbLocalObject());
			}catch(Exception e)
			{
				if(tracer.isSevereEnabled())
				{
					tracer.severe("Exception in publication parent!", e);
				}
			}
			
			break;
		case REMOVE:
			cancelExpiresTimer();
			//update ETag
			this.setETagCMP(SIP_ETag.getETag());
			try{
				this.getParentSbbCMP().afterRemovePublication(result, (PublicationClientChildLocalObject) this.sbbContext.getSbbLocalObject());
			}catch(Exception e)
			{
				if(tracer.isSevereEnabled())
				{
					tracer.severe("Exception in publication parent!", e);
				}
			}
			
			this.clear();
			break;
		}
	}

	public void onRedirRespEvent(ResponseEvent event, ActivityContextInterface ac) {
		if (tracer.isFineEnabled())
			tracer.fine("Received 3xx (REDIRECT) response:\n"+event.getResponse());
		//3xx should not happen? since ECS/PA acts like proxy?
	}

	public void onClientErrorRespEvent(ResponseEvent event, ActivityContextInterface ac) {
		if (tracer.isFineEnabled())
			tracer.fine("Received 4xx (CLIENT ERROR) response:\n"+event.getResponse());
		
		Response response = event.getResponse();
		int statusCode = response.getStatusCode();
		
		if(statusCode == 423)
		{
			
			MinExpiresHeader minExpires = (MinExpiresHeader) response.getHeader(MinExpiresHeader.NAME);
			Result result = new Result(423, getETagCMP(),minExpires.getExpires(),getEntityCMP());
//			   If an EPA receives a 423 (Interval Too Brief) response to a PUBLISH
//			   request, it MAY retry the publication after changing the expiration
//			   interval in the Expires header field to be equal to or greater than
//			   the expiration interval within the Min-Expires header field of the
//			   423 (Interval Too Brief) response.

			//in this case, we may issue another command, let
			PublicationClientChildActivityContextInterface pccAci = asSbbActivityContextInterface(ac);
			switch (pccAci.getPublishRequestType()) {
			case NEW:
				this.clear();
				try{
					this.getParentSbbCMP().afterNewPublication(result, (PublicationClientChildLocalObject) this.sbbContext.getSbbLocalObject());
				}catch(Exception e)
				{
					if(tracer.isSevereEnabled())
					{
						tracer.severe("Exception in publication parent!", e);
					}
				}
				break;
			
			case UPDATE:
				//just deliver
				try{
					this.getParentSbbCMP().afterUpdatePublication(result, (PublicationClientChildLocalObject) this.sbbContext.getSbbLocalObject());
				}catch(Exception e)
				{
					if(tracer.isSevereEnabled())
					{
						tracer.severe("Exception in publication parent!", e);
					}
				}
				break;
				
			case REFRESH:
				//we handle refresh, but it IS weird to receive this :)
				setExpiresCMP(minExpires.getExpires()*2); //??
				cancelExpiresTimer();
				startExpiresTimer();
				doRefresh();
				break;
			case REMOVE:
				//should not happen?
				if(tracer.isSevereEnabled())
				{
					tracer.severe("Received 423 on REMOVE request!");
				}
				this.clear();
				try{
					this.getParentSbbCMP().afterRemovePublication(result, (PublicationClientChildLocalObject) this.sbbContext.getSbbLocalObject());
				}catch(Exception e)
				{
					if(tracer.isSevereEnabled())
					{
						tracer.severe("Exception in publication parent!", e);
					}
				}
				break;
			}
		}else
		{
			
			//ALL OTHER cases == very bad, can't be repaired?
//			   If an EPA receives a 412 (Conditional Request Failed) response, it
//			   MUST NOT reattempt the PUBLISH request.  Instead, to publish event
//			   state, the EPA SHOULD perform an initial publication, i.e., a PUBLISH
//			   request without a SIP-If-Match header field, as described in Section
//			   4.2.  The EPA MUST also discard the entity-tag that produced this
//			   error response.
//			   1. The ESC inspects the Request-URI to determine whether this request
//			      is targeted to a resource for which the ESC is responsible for
//			      maintaining event state.  If not, the ESC MUST return a 404 (Not
//			      Found) response and skip the remaining steps.
//			The ESC examines the Event header field of the PUBLISH request.
//		      If the Event header field is missing or contains an event package
//		      which the ESC does not support, the ESC MUST respond to the
//		      PUBLISH request with a 489 (Bad Event) response, and skip the
//		      remaining steps.
//		      *  Else, if the request has a SIP-If-Match header field, the ESC
//		         checks whether the header field contains a single entity-tag.
//		         If not, the request is invalid, and the ESC MUST return with a
//		         400 (Invalid Request) response and skip the remaining steps.
//			5. The ESC processes the published event state contained in the body
//		      of the PUBLISH request.  If the content type of the request does
//		      not match the event package, or is not understood by the ESC, the
//		      ESC MUST reject the request with an appropriate response, such as
//		      415 (Unsupported Media Type), and skip the remainder of the steps.
			
			handleFailure(statusCode,ac);
		}

	}



	public void onServerErrorRespEvent(ResponseEvent event, ActivityContextInterface ac) {
		if (tracer.isFineEnabled())
			tracer.fine("Received 5xx (SERVER ERROR) response:\n"+event.getResponse());
		handleFailure(event.getResponse().getStatusCode(),ac);
	}

	public void onGlobalFailureRespEvent(ResponseEvent event, ActivityContextInterface ac) {
		if (tracer.isFineEnabled())
			tracer.fine("Received 6xx (GLOBAL FAILURE) response:\n"+event.getResponse());
		handleFailure(event.getResponse().getStatusCode(),ac);
	}

	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		// refresh time?
		setRefreshCMP(true);
		PublicationClientChildActivityContextInterface naAci = asSbbActivityContextInterface(aci);
		naAci.setExpiresTimerID(null);
		
		doRefresh();

	}

	public void onTransactionTimeoutEvent(TimeoutEvent event,ActivityContextInterface ac) {
		//TODO: add 5xx/4xx ?
	}
	
	public void onActivityEndEvent(javax.slee.ActivityEndEvent event, ActivityContextInterface aci) {
		if (tracer.isFineEnabled())
			tracer.fine("Received Activtiy End: "+aci.getActivity());
	}
	
	////////////////////////////
	// Private helper methods //
	////////////////////////////
	
	private boolean isPublicationActive()
	{
		return this.getEntityCMP()!=null;
	}
	/**
	 * 
	 */
	private void clear() {
		setEntityCMP(null);
		setETagCMP(null);
		setEventPackageCMP(null);
		setExpiresCMP(0);//?
		setRefreshCMP(false);
		//kill null ac
		for(ActivityContextInterface aci: this.sbbContext.getActivities())
		{
			if(aci instanceof NullActivity)
			{
				((NullActivity)aci.getActivity()).endActivity();
			}
			//kill detach from ctx?
		}
	}
	/**
	 * 
	 */
	private void startExpiresTimer() {
		if(getExpiresCMP()==0)
		{
			//nothing yet.
			return;
		}
		ActivityContextInterface naAci = null;
		ActivityContextInterface[] acis = this.sbbContext.getActivities();
		for(ActivityContextInterface aci:acis)
		{
			if(aci.getActivity() instanceof NullActivity)
			{
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
		
		//lets schedule a bit earlier. 5s?
		if(expires-expiresDrift >0)
		{
			expires-=expiresDrift;
		}
		
		TimerOptions to = new TimerOptions(100, TimerPreserveMissed.LAST);
		//this.setTimerIDCMP(this.timerFacility.setTimer(naAci,null,System.currentTimeMillis()+expires,to));
		this.asSbbActivityContextInterface(naAci).setExpiresTimerID(this.timerFacility.setTimer(naAci,null,System.currentTimeMillis()+expires*1000,to));
	}


	/**
	 * 
	 */
	private void cancelExpiresTimer() {
		ActivityContextInterface naAci = null;
		ActivityContextInterface[] acis = this.sbbContext.getActivities();
		for(ActivityContextInterface aci:acis)
		{
			if(aci.getActivity() instanceof NullActivity)
			{
				NullActivity na = (NullActivity) aci.getActivity();
				naAci = this.nullACIFactory.getActivityContextInterface(na);
			}
		}
		
		if (naAci != null) {
			PublicationClientChildActivityContextInterface aci = asSbbActivityContextInterface(naAci);
			if(aci.getExpiresTimerID()!=null)
			{
				this.timerFacility.cancelTimer(aci.getExpiresTimerID());
			}
		}
		
	}
	/**
	 * @param expires
	 * @param eventPackage
	 * @param entity
	 * @param contentType
	 * @param contentSubType
	 * @param document
	 * @return
	 * @throws ParseException 
	 * @throws TransportNotSupportedException 
	 * @throws InvalidArgumentException 
	 */
	protected Request createPublishRequest() throws ParseException, TransportNotSupportedException, InvalidArgumentException{
		
		String[] ents = getEntityCMP().split("@");
		SipURI fromAddress = addressFactory.createSipURI(ents[0], ents[1]);

		Address fromNameAddress = addressFactory.createAddress(fromAddress);
		FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress, this.hashCode() + "_" + System.currentTimeMillis());

		// create To Header
		SipURI toAddress = addressFactory.createSipURI(ents[0], ents[1]);
		Address toNameAddress = addressFactory.createAddress(toAddress);

		ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);

		// create Request URI
		SipURI requestURI = addressFactory.createSipURI(ents[0], ents[1]);

		// Create ViaHeaders

		ArrayList viaHeaders = new ArrayList();
		
		ViaHeader viaHeader = sleeSipProvider.getLocalVia(sleeSipProvider.getListeningPoints()[0].getTransport(), null/*null branch? */);

		// add via headers
		viaHeaders.add(viaHeader);

		// Create a new CallId header
		CallIdHeader callIdHeader = sleeSipProvider.getNewCallId();

		// Create a new Cseq header
		CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(1L, Request.PUBLISH);

		// Create a new MaxForwardsHeader
		MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70); //leave it as 70?

		// Create the request.
		Request request = messageFactory.createRequest(requestURI, Request.PUBLISH, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);

		//add route header?
		if(this.ecsAddress!=null)
		{
			RouteHeader routeHeader = headerFactory.createRouteHeader(this.ecsAddress);
			request.addHeader(routeHeader);
		}
		
		return request;
	}
	/**
	 * @param contentType
	 * @param contentSubType
	 * @param document
	 * @return
	 */
	protected Request createNewPublishRequest(String contentType, String contentSubType, String document) throws ParseException, TransportNotSupportedException, InvalidArgumentException {

		Request request = this.createPublishRequest();
		//if expires is >0 add it, if not, skip;
		if(getExpiresCMP()>0)
		{
			ExpiresHeader expiresHeader = this.headerFactory.createExpiresHeader(getExpiresCMP());
			request.addHeader(expiresHeader);
		}
		
		//add custom header
		EventHeader eventHeader = this.headerFactory.createEventHeader(getEventPackageCMP());
		request.addHeader(eventHeader);
		
		//add content
		
		ContentTypeHeader contentTypeHeader = this.headerFactory.createContentTypeHeader(contentType, contentSubType);
		//request.addHeader(contentTypeHeader);
		byte[] rawContent = document.getBytes(); //is this proper for this casE?
		ContentLengthHeader contentLengthHeader = this.headerFactory.createContentLengthHeader(rawContent.length);
		request.addHeader(contentLengthHeader);
		request.setContent(rawContent, contentTypeHeader);
		if(tracer.isInfoEnabled())
		{
			tracer.info("Created NPR:\n"+request+"\n----------------------------------------------------------");
		}
		return request;
	}
	/**
	 * @param contentType
	 * @param contentSubType
	 * @param document
	 * @return
	 */
	protected Request createUpdatePublishRequest(String contentType, String contentSubType, String document)throws ParseException, TransportNotSupportedException, InvalidArgumentException {
		Request request = this.createPublishRequest();
		
		//expires always here
		ExpiresHeader expiresHeader = this.headerFactory.createExpiresHeader(getExpiresCMP());
		request.addHeader(expiresHeader);
		
		//add SIP-If-Match
		SIPIfMatchHeader sipIfMatch = this.headerFactory.createSIPIfMatchHeader(getETagCMP());
		request.addHeader(sipIfMatch);
		//add custom header
		EventHeader eventHeader = this.headerFactory.createEventHeader(getEventPackageCMP());
		request.addHeader(eventHeader);
		
		//add content
		
		ContentTypeHeader contentTypeHeader = this.headerFactory.createContentTypeHeader(contentType, contentSubType);
		//request.addHeader(contentTypeHeader);
		byte[] rawContent = document.getBytes(); //is this proper for this casE?
		ContentLengthHeader contentLengthHeader = this.headerFactory.createContentLengthHeader(rawContent.length);
		request.addHeader(contentLengthHeader);
		request.setContent(rawContent, contentTypeHeader);
		if(tracer.isInfoEnabled())
		{
			tracer.info("Created UPR:\n"+request+"\n----------------------------------------------------------");
		}
		return request;
	}
	/**
	 * @return
	 */
	protected Request createRemovePublishRequest() throws ParseException, TransportNotSupportedException, InvalidArgumentException{
		Request request = this.createPublishRequest();
		
		//expires always here
		ExpiresHeader expiresHeader = this.headerFactory.createExpiresHeader(0);
		request.addHeader(expiresHeader);
		
		//add SIP-If-Match
		SIPIfMatchHeader sipIfMatch = this.headerFactory.createSIPIfMatchHeader(getETagCMP());
		request.addHeader(sipIfMatch);
		//add custom header
		EventHeader eventHeader = this.headerFactory.createEventHeader(getEventPackageCMP());
		request.addHeader(eventHeader);
		ContentLengthHeader contentLengthHeader = this.headerFactory.createContentLengthHeader(0);
		request.addHeader(contentLengthHeader);
		if(tracer.isInfoEnabled())
		{
			tracer.info("Created RPR:\n"+request+"\n----------------------------------------------------------");
		}
		return request;
	}
	
	protected Request createRefreshPublishRequest() throws ParseException, TransportNotSupportedException, InvalidArgumentException{
		Request request = this.createPublishRequest();
		
		//expires always here
		ExpiresHeader expiresHeader = this.headerFactory.createExpiresHeader(getExpiresCMP());
		request.addHeader(expiresHeader);
		
		//add SIP-If-Match
		SIPIfMatchHeader sipIfMatch = this.headerFactory.createSIPIfMatchHeader(getETagCMP());
		request.addHeader(sipIfMatch);
		//add custom header
		EventHeader eventHeader = this.headerFactory.createEventHeader(getEventPackageCMP());
		request.addHeader(eventHeader);
		ContentLengthHeader contentLengthHeader = this.headerFactory.createContentLengthHeader(0);
		request.addHeader(contentLengthHeader);
		if(tracer.isInfoEnabled())
		{
			tracer.info("Created RefPR:\n"+request+"\n----------------------------------------------------------");
		}
		return request;
	}
	/**
	 * @param statusCode
	 */
	protected void handleFailure(int statusCode,ActivityContextInterface ac) {
		//fill data we have.
		Result result = new Result(statusCode,getETagCMP(),getExpiresCMP(),getEntityCMP());
		PublicationClientChildActivityContextInterface pccAci = asSbbActivityContextInterface(ac);
		this.clear();
		switch (pccAci.getPublishRequestType()) {
		case NEW:

			try{
				this.getParentSbbCMP().afterNewPublication(result, (PublicationClientChildLocalObject) this.sbbContext.getSbbLocalObject());
			}catch(Exception e)
			{
				if(tracer.isSevereEnabled())
				{
					tracer.severe("Exception in publication parent!", e);
				}
			}
			
			
			break;
		case REFRESH:
			try{
				this.getParentSbbCMP().afterRefreshPublication(result, (PublicationClientChildLocalObject) this.sbbContext.getSbbLocalObject());
			}catch(Exception e)
			{
				if(tracer.isSevereEnabled())
				{
					tracer.severe("Exception in publication parent!", e);
				}
			}

			break;
		case UPDATE:
			
			try{
				this.getParentSbbCMP().afterUpdatePublication(result, (PublicationClientChildLocalObject) this.sbbContext.getSbbLocalObject());
			}catch(Exception e)
			{
				if(tracer.isSevereEnabled())
				{
					tracer.severe("Exception in publication parent!", e);
				}
			}

			break;
		case REMOVE:
			try{
				this.getParentSbbCMP().afterRemovePublication(result, (PublicationClientChildLocalObject) this.sbbContext.getSbbLocalObject());
			}catch(Exception e)
			{
				if(tracer.isSevereEnabled())
				{
					tracer.severe("Exception in publication parent!", e);
				}
			}

			break;
		}
		
	}
	

	
	/**
	 * 
	 */
	protected void doRefresh() {
		//issue request.
		ClientTransaction ctx = null;
		ActivityContextInterface ctxAci = null;
		boolean clear = true;
		try {

			Request r = createRefreshPublishRequest();
			
			ctx = this.sleeSipProvider.getNewClientTransaction(r);
			ctxAci = this.sipActivityContextInterfaceFactory.getActivityContextInterface(ctx);
			ctxAci.attach(this.sbbContext.getSbbLocalObject());
			

			//start timer here, if ECS/PA return something else, we will reschedule.
			startExpiresTimer();
			
			asSbbActivityContextInterface(ctxAci).setPublishRequestType(PublishRequestType.REFRESH);
			ctx.sendRequest();
			clear = false;
			
		} catch (TransactionUnavailableException e) {
			if(tracer.isSevereEnabled())
			{
				tracer.severe("Failed to create SIP transaction!", e);
			}
			//no CTX
		
		} catch (ParseException e) {
			if(tracer.isSevereEnabled())
			{
				tracer.severe("Failed to create SIP message!", e);
			}
			//no CTX
		} catch (InvalidArgumentException e) {
			if(tracer.isSevereEnabled())
			{
				tracer.severe("Failed to SIP message!", e);
			}
			//no CTX
		} catch (SipException e) {
			if(tracer.isSevereEnabled())
			{
				tracer.severe("Failed to send SIP request!", e);
			}
			
			//tx is here...
			ctxAci.detach(this.sbbContext.getSbbLocalObject());
			//it will timeout?
		}finally
		{
			if(clear)
			{
				//something went wrong, clear publication state.
				this.clear();
			}
		}
	}

	
	//////////////////////////////////
	// SBB OBJECT LIFECYCLE METHODS //
    //////////////////////////////////
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
		this.sbbContext = sbbContext;
		if (tracer == null) {
			tracer = sbbContext.getTracer(PublicationClientChildSbb.class.getSimpleName());
		}
		try {
			Context context = (Context) new InitialContext().lookup("java:comp/env");
			//sip ra
			this.sipActivityContextInterfaceFactory = (SipActivityContextInterfaceFactory) context.lookup("slee/resources/jainsip/1.2/acifactory");
			this.sleeSipProvider = (SleeSipProvider) context.lookup("slee/resources/jainsip/1.2/provider");
			//sip stuff
			this.messageFactory = this.sleeSipProvider.getMessageFactory();
			this.addressFactory = this.sleeSipProvider.getAddressFactory();
			this.headerFactory = this.sleeSipProvider.getHeaderFactory();
			
			//slee stuff
			context = new InitialContext(); //new, since facilities names are absolute path.
			this.timerFacility = (TimerFacility) context.lookup(TimerFacility.JNDI_NAME);
			this.nullACIFactory = (NullActivityContextInterfaceFactory) context.lookup(NullActivityContextInterfaceFactory.JNDI_NAME);
			this.nullActivityFactory = (NullActivityFactory) context.lookup(NullActivityFactory.JNDI_NAME);
			
			//env, conf
			try{
				String serverAddress = (String) context.lookup("server.address");
				if(serverAddress!=null)
				{	
					//RFC show entity as SIP URI and ECS address?
					this.ecsAddress = this.sleeSipProvider.getAddressFactory().createAddress(serverAddress);
				}
			}catch(NamingException e)
			{
				if(tracer.isInfoEnabled())
				{
					tracer.info("No ECS/PA address to use in Route header.");
				}
			}
			
			try{
				String expireTime = (String) context.lookup("expires.drift");
				if(expireTime!=null)
				{	
					int intExpireTime = Integer.parseInt(expireTime);
					if(intExpireTime<0)
					{
						if(tracer.isInfoEnabled())
						{
							tracer.info("Expire time drift less than zero, using default: "+this.expiresDrift+"s.");
						}
					}else
					{
						this.expiresDrift = intExpireTime;
						if(tracer.isInfoEnabled())
						{
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
