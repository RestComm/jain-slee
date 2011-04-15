package org.mobicents.slee.enabler.sip;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.ResponseEvent;
import javax.sip.TimeoutEvent;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
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
import javax.slee.SbbLocalObject;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerID;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.Tracer;
import javax.slee.nullactivity.NullActivity;
import javax.slee.resource.ResourceAdaptorTypeID;

import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

import org.mobicents.slee.ActivityContextInterfaceExt;
import org.mobicents.slee.SbbContextExt;

/**
 * SIP Publication Client SLEE Enabler. It creates PUBLISH interaction and manages
 * it. It automatically refreshes publication based on content of ECS/PA response. It keeps map of ETag to publish interaction.
 * 
 * @author baranowb
 * @author martins
 */
public abstract class PublicationClientChildSbb implements Sbb, PublicationClientChild {

	private static final int DEFAULT_EXPIRES_DRIFT = 15;
	private static final ResourceAdaptorTypeID sipResourceAdaptorTypeID = new ResourceAdaptorTypeID("JAIN SIP","javax.sip","1.2");
	private static final TimerOptions TIMER_OPTIONS = new TimerOptions();

	private static Tracer tracer;

	protected SbbContextExt sbbContext;

	//sip ra
	protected SipActivityContextInterfaceFactory sipActivityContextInterfaceFactory = null;
	protected SleeSipProvider sleeSipProvider = null;
	//sip stuff
	protected MessageFactory messageFactory;
	protected AddressFactory addressFactory;
	protected HeaderFactory headerFactory;
	protected Address ecsAddress;
	protected int expiresDrift = DEFAULT_EXPIRES_DRIFT;
	
	protected PublicationClientParentSbbLocalObject getParent() {
		return (PublicationClientParentSbbLocalObject) sbbContext.getSbbLocalObject().getParent();
	}
	
	// //////////////////
	// SBB LO methods //
	// //////////////////

	@Override
	public String getEntity() {
		return this.getEntityCMP();
	}

	@Override
	public String getETag() {
		return this.getETagCMP();
	}

	private boolean isBusy() {
		return this.getPublishRequestTypeCMP() != null;
	}
	
	@Override
	public void newPublication(String entity, String eventPackage, String document, String contentType, String contentSubType, int expires) {
		
		SbbLocalObject sbbLocalObject = sbbContext.getSbbLocalObject();
		try {
			Request r = createNewPublishRequest(entity,eventPackage,expires,contentType,contentSubType,document);
			ClientTransaction ctx = this.sleeSipProvider.getNewClientTransaction(r);
			ActivityContextInterface aci = this.sipActivityContextInterfaceFactory.getActivityContextInterface(ctx);
			aci.attach(sbbLocalObject);			
			ctx.sendRequest();
			// set cmps
			this.setPublishRequestTypeCMP(PublishRequestType.NEW);
			this.setEntityCMP(entity);
			this.setEventPackageCMP(eventPackage);
		} catch (Throwable e) {
			if(tracer.isSevereEnabled()) {
				tracer.severe("Failed to create publication", e);
			}
			
			getParent().newPublicationFailed(Response.SERVER_INTERNAL_ERROR, (PublicationClientChildSbbLocalObject) sbbLocalObject);
		}
	}

	@Override
	public void modifyPublication(String document, String contentType, String contentSubType, int expires) {
		
		// delay the request in case there is another ongoing request
		if (isBusy()) {
			setPostponedRequestCMP(new PostponedModifyPublicationRequest(document, contentType, contentSubType, expires));
			return;
		}
		
		cancelExpiresTimer(false);
		
		//issue request.
		SbbLocalObject sbbLocalObject = sbbContext.getSbbLocalObject();
		try {
			Request r = createUpdatePublishRequest( contentType, contentSubType, document);			
			ClientTransaction ctx = this.sleeSipProvider.getNewClientTransaction(r);
			ActivityContextInterface aci = this.sipActivityContextInterfaceFactory.getActivityContextInterface(ctx);
			aci.attach(sbbLocalObject);
			ctx.sendRequest();
			setPublishRequestTypeCMP(PublishRequestType.UPDATE);
		} catch (Throwable e) {
			if(tracer.isSevereEnabled()) {
				tracer.severe("Failed to modify publication", e);
			}
			getParent().modifyPublicationFailed(Response.SERVER_INTERNAL_ERROR, (PublicationClientChildSbbLocalObject) sbbLocalObject);
		}
	}


	@Override
	public void removePublication() {
		
		if(isBusy()) {
			setPostponedRequestCMP(new PostponedRemovePublicationRequest());
			return;
		}
		
		cancelExpiresTimer(true); 

		//issue request.
		SbbLocalObject sbbLocalObject = sbbContext.getSbbLocalObject();
		try {
			Request r = createRemovePublishRequest();
			ClientTransaction ctx = this.sleeSipProvider.getNewClientTransaction(r);
			ActivityContextInterface aci = this.sipActivityContextInterfaceFactory.getActivityContextInterface(ctx);
			aci.attach(sbbLocalObject);
			ctx.sendRequest();
			setPublishRequestTypeCMP(PublishRequestType.REMOVE);
		} catch (Throwable e) {
			if(tracer.isSevereEnabled()) {
				tracer.severe("Failed to remove publication", e);
			}
			getParent().removePublicationFailed(Response.SERVER_INTERNAL_ERROR, (PublicationClientChildSbbLocalObject) sbbLocalObject);
		}
	}
	
	/////////////////
	// CMP Methods //
	/////////////////
	public abstract void setEntityCMP(String d);
	public abstract String getEntityCMP();
	public abstract void  setEventPackageCMP(String eventPackage);
	public abstract String getEventPackageCMP( );
	public abstract void setExpiresCMP( int expires);
	public abstract int getExpiresCMP();
	public abstract void setETagCMP(String d);
	public abstract String getETagCMP();
	public abstract void setPostponedRequestCMP(PostponedRequest pr);
	public abstract PostponedRequest getPostponedRequestCMP();
	
	//type of ongoing request.
	public abstract void setPublishRequestTypeCMP(PublishRequestType t);
	public abstract PublishRequestType getPublishRequestTypeCMP();
		
	////////////////////
	// Event handlers //
	////////////////////
	
	public void onSuccessRespEvent(ResponseEvent event, ActivityContextInterface aci) {
		
		if (tracer.isFineEnabled())
			tracer.fine("Received 2xx (SUCCESS) response:\n"+event.getResponse());
		
		final SbbLocalObject sbbLocalObject = sbbContext.getSbbLocalObject();
		aci.detach(sbbLocalObject);
		
		final Response response = event.getResponse();
		final SIPETagHeader sipeTagHeader = (SIPETagHeader) response.getHeader(SIPETagHeader.NAME);
		if(sipeTagHeader != null) {
			this.setETagCMP(sipeTagHeader.getETag());
		}
				
		final PublishRequestType type = getPublishRequestTypeCMP();
		
		if (type != PublishRequestType.REMOVE) {
			final ExpiresHeader expiresHeader = (ExpiresHeader) response.getHeader(ExpiresHeader.NAME);
			if(expiresHeader!=null && expiresHeader.getExpires() != 0) {
				//update expires time.
				setExpiresCMP(expiresHeader.getExpires());
				startExpiresTimer();
			}
		}
		
		PostponedRequest postponedRequest = null;
		setPublishRequestTypeCMP(null);

		try{
			switch (type) {
			case NEW:
				postponedRequest = getPostponedRequestCMP();
				getParent().newPublicationSucceed((PublicationClientChildSbbLocalObject) sbbLocalObject);
				break;
			case REFRESH:
				postponedRequest = getPostponedRequestCMP();
				break;
			case UPDATE:
				postponedRequest = getPostponedRequestCMP();
				getParent().modifyPublicationSucceed((PublicationClientChildSbbLocalObject) sbbLocalObject);
				break;
			case REMOVE:
				getParent().removePublicationSucceed((PublicationClientChildSbbLocalObject) sbbLocalObject);
				break;
			}
		}
		catch(Exception e) {
			if(tracer.isSevereEnabled()) {
				tracer.severe("Exception in publication parent!", e);
			}
		}

		if (postponedRequest != null) {
			// refresh may be concurrent with another request, if there is a
			// postponed request resume it now
			postponedRequest.resume(this);
		}
		
	}

	public void onClientErrorRespEvent(ResponseEvent event, ActivityContextInterface aci) {
		
		if (tracer.isFineEnabled())
			tracer.fine("Received 4xx (CLIENT ERROR) response:\n"+event.getResponse());
		
		aci.detach(sbbContext.getSbbLocalObject());
		
		final Response response = event.getResponse();
		int statusCode = response.getStatusCode();
		
		if(statusCode == 423) {
			
			final MinExpiresHeader minExpires = (MinExpiresHeader) response.getHeader(MinExpiresHeader.NAME);
//			   If an EPA receives a 423 (Interval Too Brief) response to a PUBLISH
//			   request, it MAY retry the publication after changing the expiration
//			   interval in the Expires header field to be equal to or greater than
//			   the expiration interval within the Min-Expires header field of the
//			   423 (Interval Too Brief) response.

			//in this case, we may issue another command, let
			PublishRequestType type = getPublishRequestTypeCMP();
			setPublishRequestTypeCMP(null);			
			Request request = null;
			ContentTypeHeader cTypeHeader = null;
			switch (type) {
			case NEW:
				request = event.getClientTransaction().getRequest();
				cTypeHeader = (ContentTypeHeader) request.getHeader(ContentTypeHeader.NAME);
				newPublication(getEntityCMP(), getEventPackageCMP(), (String) request.getContent(), cTypeHeader.getContentType(), cTypeHeader.getContentSubType(), minExpires.getExpires());
				break;			
			case UPDATE:
				request = event.getClientTransaction().getRequest();
				cTypeHeader = (ContentTypeHeader) request.getHeader(ContentTypeHeader.NAME);
				modifyPublication((String) request.getContent(), cTypeHeader.getContentType(), cTypeHeader.getContentSubType(), minExpires.getExpires());
				break;				
			case REFRESH:
				setExpiresCMP(minExpires.getExpires()); //??
				doRefresh();
				break;
			case REMOVE:
				//should not happen?
				if(tracer.isSevereEnabled()) {
					tracer.severe("Received 423 on REMOVE request!");
				}
				try{
					getParent().removePublicationFailed(statusCode, (PublicationClientChildSbbLocalObject) this.sbbContext.getSbbLocalObject());
				}
				catch(Exception e) {
					if(tracer.isSevereEnabled()) {
						tracer.severe("Exception in publication parent!", e);
					}
				}
				break;
			}
			
		}
		else {
			
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
			
			handleFailure(statusCode,aci);
		}

	}



	public void onServerErrorRespEvent(ResponseEvent event, ActivityContextInterface aci) {
		
		if (tracer.isFineEnabled())
			tracer.fine("Received 5xx (SERVER ERROR) response:\n"+event.getResponse());
		
		aci.detach(sbbContext.getSbbLocalObject());
		handleFailure(event.getResponse().getStatusCode(),aci);
	}

	public void onGlobalFailureRespEvent(ResponseEvent event, ActivityContextInterface aci) {
		
		if (tracer.isFineEnabled())
			tracer.fine("Received 6xx (GLOBAL FAILURE) response:\n"+event.getResponse());
		
		aci.detach(sbbContext.getSbbLocalObject());
		handleFailure(event.getResponse().getStatusCode(),aci);
	}

	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		// refresh time?
		if(isBusy()) {
			if(tracer.isFineEnabled()) {
				tracer.fine("Performing "+getPublishRequestTypeCMP()+", skipping refresh.");
			}
			return;
		}
		
		if (tracer.isFineEnabled())
			tracer.fine("Refreshing publication.");
		
		doRefresh();
	}

	public void onTransactionTimeoutEvent(TimeoutEvent event,ActivityContextInterface aci) {
		
		if (tracer.isFineEnabled())
			tracer.fine("Received Tx Timeout");
		
		aci.detach(sbbContext.getSbbLocalObject());		
		handleFailure(Response.SERVER_TIMEOUT,aci);
	}
	
	////////////////////////////
	// Private helper methods //
	////////////////////////////
	
	private ActivityContextInterface getTimerACI() {
		for(ActivityContextInterface aci : this.sbbContext.getActivities()) {
			if(aci.getActivity() instanceof NullActivity) {
				return aci;
			}
		}
		return null;
	}
	
	/**
	 * 
	 */
	private void startExpiresTimer() {
		ActivityContextInterface naAci = getTimerACI();
		if (naAci == null) {
			// create activity for this publication;
			NullActivity na = sbbContext.getNullActivityFactory().createNullActivity();
			naAci = sbbContext.getNullActivityContextInterfaceFactory().getActivityContextInterface(na);
			// attach.
			naAci.attach(this.sbbContext.getSbbLocalObject());
		}
		long expires = this.getExpiresCMP();
		
		//lets schedule a bit earlier. 5s?
		if(expires-expiresDrift >0) {
			expires-=expiresDrift;
		}				
		sbbContext.getTimerFacility().setTimer(naAci,null,System.currentTimeMillis()+expires*1000,TIMER_OPTIONS);
	}

	/**
	 * 
	 */
	private void cancelExpiresTimer(boolean detachActivity) {
		ActivityContextInterface naAci = getTimerACI();
		if (naAci != null) {
			ActivityContextInterfaceExt naAciExt = (ActivityContextInterfaceExt) naAci;
			TimerID[] timerIDs = naAciExt.getTimers();
			if (timerIDs.length > 0) {
				sbbContext.getTimerFacility().cancelTimer(timerIDs[0]);
			}
			if (detachActivity) {
				naAci.detach(sbbContext.getSbbLocalObject());
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
	protected Request createPublishRequest(String entity) throws ParseException, TransportNotSupportedException, InvalidArgumentException{
		
		URI entityURI = addressFactory.createURI(entity);
		Address entityAddress = addressFactory.createAddress(entityURI);
		
		FromHeader fromHeader = headerFactory.createFromHeader(entityAddress,null);
		ToHeader toHeader = headerFactory.createToHeader(entityAddress, null);

		List<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
		viaHeaders.add(sleeSipProvider.getLocalVia("UDP", null));
		
		// Create a new CallId header
		CallIdHeader callIdHeader = sleeSipProvider.getNewCallId();

		// Create a new Cseq header
		CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(1L, Request.PUBLISH);

		// Create a new MaxForwardsHeader
		MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70); //leave it as 70?

		// Create the request.
		Request request = messageFactory.createRequest(entityURI, Request.PUBLISH, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);

		//add route header?
		if(this.ecsAddress!=null) {
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
	protected Request createNewPublishRequest(String entity, String eventPackage, int expires, String contentType, String contentSubType, String document) throws ParseException, TransportNotSupportedException, InvalidArgumentException {

		Request request = this.createPublishRequest(entity);
		//if expires is >0 add it, if not, skip;
		if(getExpiresCMP()>0)
		{
			ExpiresHeader expiresHeader = this.headerFactory.createExpiresHeader(expires);
			request.addHeader(expiresHeader);
		}
		
		//add custom header
		EventHeader eventHeader = this.headerFactory.createEventHeader(eventPackage);
		request.addHeader(eventHeader);
		
		//add content
		ContentTypeHeader contentTypeHeader = this.headerFactory.createContentTypeHeader(contentType, contentSubType);
		request.setContent(document, contentTypeHeader);
		
		return request;
	}
	/**
	 * @param contentType
	 * @param contentSubType
	 * @param document
	 * @return
	 */
	protected Request createUpdatePublishRequest(String contentType, String contentSubType, String document)throws ParseException, TransportNotSupportedException, InvalidArgumentException {
		
		Request request = this.createPublishRequest(getEntity());
		
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
		request.setContent(document, contentTypeHeader);
		
		return request;
	}
	/**
	 * @return
	 */
	protected Request createRemovePublishRequest() throws ParseException, TransportNotSupportedException, InvalidArgumentException{
		
		Request request = this.createPublishRequest(getEntity());
		
		//expires always here
		ExpiresHeader expiresHeader = this.headerFactory.createExpiresHeader(0);
		request.addHeader(expiresHeader);
		
		//add SIP-If-Match
		SIPIfMatchHeader sipIfMatch = this.headerFactory.createSIPIfMatchHeader(getETagCMP());
		request.addHeader(sipIfMatch);
		
		//add custom header
		EventHeader eventHeader = this.headerFactory.createEventHeader(getEventPackageCMP());
		request.addHeader(eventHeader);
		
		return request;
	}
	
	protected Request createRefreshPublishRequest() throws ParseException, TransportNotSupportedException, InvalidArgumentException{
		
		Request request = this.createPublishRequest(getEntity());
		
		//expires always here
		ExpiresHeader expiresHeader = this.headerFactory.createExpiresHeader(getExpiresCMP());
		request.addHeader(expiresHeader);
		
		//add SIP-If-Match
		SIPIfMatchHeader sipIfMatch = this.headerFactory.createSIPIfMatchHeader(getETagCMP());
		request.addHeader(sipIfMatch);
		
		//add custom header
		EventHeader eventHeader = this.headerFactory.createEventHeader(getEventPackageCMP());
		request.addHeader(eventHeader);
				
		return request;
	}
	/**
	 * @param statusCode
	 */
	protected void handleFailure(int statusCode,ActivityContextInterface ac) {
		//fill data we have.
		PublishRequestType type = getPublishRequestTypeCMP();
		if (type != null) {
			setPublishRequestTypeCMP(null);
		}
		try{
			switch (type) {
			case NEW:
				getParent().newPublicationFailed(statusCode, (PublicationClientChildSbbLocalObject) this.sbbContext.getSbbLocalObject());
				break;
			case REFRESH:
				getParent().refreshPublicationFailed(statusCode, (PublicationClientChildSbbLocalObject) this.sbbContext.getSbbLocalObject());
				break;
			case UPDATE:
				getParent().modifyPublicationFailed(statusCode, (PublicationClientChildSbbLocalObject) this.sbbContext.getSbbLocalObject());
				break;
			case REMOVE:
				getParent().removePublicationFailed(statusCode, (PublicationClientChildSbbLocalObject) this.sbbContext.getSbbLocalObject());
				break;
			}
		}
		catch(Exception e) {
			tracer.severe("Exception in publication parent!", e);			
		}
	}
		
	/**
	 * 
	 */
	protected void doRefresh() {
		//issue request.
		SbbLocalObject sbbLocalObject = sbbContext.getSbbLocalObject();
		try {
			final Request r = createRefreshPublishRequest();			
			final ClientTransaction ctx = this.sleeSipProvider.getNewClientTransaction(r);
			ActivityContextInterface ctxAci = this.sipActivityContextInterfaceFactory.getActivityContextInterface(ctx);
			ctxAci.attach(sbbLocalObject);
			ctx.sendRequest();
			setPublishRequestTypeCMP(PublishRequestType.REFRESH);			
		} catch (Throwable e) {
			tracer.severe("Failed to refresh publication", e);
			getParent().refreshPublicationFailed(Response.SERVER_INTERNAL_ERROR, (PublicationClientChildSbbLocalObject) sbbLocalObject);
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
		this.sbbContext = (SbbContextExt) sbbContext;
		if (tracer == null) {
			tracer = sbbContext.getTracer(PublicationClientChildSbb.class.getSimpleName());
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
