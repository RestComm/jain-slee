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

import org.mobicents.slee.services.sip.location.LocationSbbLocalObject;
import org.mobicents.slee.services.sip.location.RegistrationBinding;

public abstract class WakeUpSbb implements javax.slee.Sbb {
	
	private static TimerOptions TIMER_OPTIONS = new TimerOptions();
	
	private static final String FIRST_TOKEN = "WAKE UP IN ";
	private static final String MIDDLE_TOKEN = "s! MSG: ";
	private static final String LAST_TOKEN = "!";
	private static final int FIRST_TOKEN_LENGTH = FIRST_TOKEN.length();
	private static final int MIDDLE_TOKEN_LENGTH = MIDDLE_TOKEN.length();
	
	private static List<ViaHeader> VIA_HEADERS = null;
	private static FromHeader FROM_HEADER = null;
	private static MaxForwardsHeader MAX_FORWARDS_HEADER = null;
	private static ContentTypeHeader CONTENT_TYPE_HEADER = null;
	private static CSeqHeader CSEQ_HEADER = null;
	
	// the Sbb's context
	private SbbContext sbbContext; 
	// the Sbb's tracer
	private static Tracer logger = null; 
		
	// cached Sbb's environment stuff
	private MessageFactory messageFactory;
	private SleeSipProvider provider;
	private TimerFacility timerFacility;
	private NullActivityContextInterfaceFactory nullACIFactory;
	private NullActivityFactory nullActivityFactory;
	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	
	// SbbObject LIFECYCLE METHODS
	
	public void setSbbContext(SbbContext context) { 
		// save the sbb context in a field
		this.sbbContext = context;
		// get the tracer if needed 
		if (logger == null) 
			logger = context.getTracer(WakeUpSbb.class.getSimpleName());
		// get jndi environment stuff
		try {
            final Context myEnv = (Context) new InitialContext();
            // slee facilities
            timerFacility = (TimerFacility) myEnv.lookup(TimerFacility.JNDI_NAME);
            nullACIFactory = (NullActivityContextInterfaceFactory)myEnv.
            				lookup(NullActivityContextInterfaceFactory.JNDI_NAME);
            nullActivityFactory = (NullActivityFactory)myEnv.lookup(NullActivityFactory.JNDI_NAME);
            // sip ra provider            
            provider = (SleeSipProvider) myEnv.lookup("java:comp/env/slee/resources/jainsip/1.2/provider");
            addressFactory = provider.getAddressFactory();
            headerFactory = provider.getHeaderFactory();
            messageFactory = provider.getMessageFactory();           
        } catch (Exception ne) {
           logger.severe("Failed to set sbb context",ne);
        }
	}
	
    public void sbbCreate() throws javax.slee.CreateException {}
    public void sbbPostCreate() throws javax.slee.CreateException {}
    public void sbbActivate() {}
    public void sbbPassivate() {}
    public void sbbRemove() {}
    public void sbbLoad() {}
    public void sbbStore() {}
    public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface activity) {}
    public void sbbRolledBack(RolledBackContext context) {}
    
    public void unsetSbbContext() { 
    	this.sbbContext = null;
    }
    
	/**
	 * Child relation to the location service
	 * @return
	 */
	public abstract ChildRelation getLocationChildRelation();
	
	/**
	 * Retrieves the Sbb's view of the specified ACI.
	 * @param aci
	 * @return
	 */
	public abstract org.mobicents.slee.examples.wakeup.WakeUpSbbActivityContextInterface asSbbActivityContextInterface(ActivityContextInterface aci);

	// EVENT HANDLERS
	
	/**
	 * Event handler for the SIP MESSAGE from the UA
	 * @param event
	 * @param aci
	 */
	public void onMessageEvent(javax.sip.RequestEvent event, ActivityContextInterface aci) {

		final Request request = event.getRequest();
		try {
        	// message body should be *FIRST_TOKEN<timer value in seconds>MIDDLE_TOKEN<msg to send back to UA>LAST_TOKEN*
			final String body = new String(request.getRawContent());
			final int firstTokenStart = body.indexOf(FIRST_TOKEN);
			final int timerDurationStart = firstTokenStart + FIRST_TOKEN_LENGTH;
			final int middleTokenStart = body.indexOf(MIDDLE_TOKEN,timerDurationStart);
			final int bodyMessageStart = middleTokenStart + MIDDLE_TOKEN_LENGTH;	
			final int lastTokenStart = body.indexOf(LAST_TOKEN,bodyMessageStart);
			if (firstTokenStart >-1 && middleTokenStart>-1 && lastTokenStart>-1) {
				// extract the timer duration
				final int timerDuration = Integer.parseInt(body.substring(timerDurationStart,middleTokenStart));
				// create a null AC and attach the sbb local object
				final ActivityContextInterface timerACI = 
					this.nullACIFactory.getActivityContextInterface(this.nullActivityFactory.createNullActivity());
				timerACI.attach(sbbContext.getSbbLocalObject());
				// set the timer on the null AC
				this.timerFacility.setTimer(timerACI, null, System.currentTimeMillis()+(timerDuration*1000),TIMER_OPTIONS);
				// extract the body message
				final String bodyMessage = body.substring(bodyMessageStart,lastTokenStart);
				// store it in sbb view of the null aci
				final WakeUpSbbActivityContextInterface myViewOfTimerACI = 
					this.asSbbActivityContextInterface(timerACI);
				myViewOfTimerACI.setBody(bodyMessage);
				myViewOfTimerACI.setCallId((CallIdHeader) request.getHeader(CallIdHeader.NAME));
				// also store the sender's uri, so we can send the wake up message
				final FromHeader fromHeader = (FromHeader)request.getHeader(FromHeader.NAME);
				if (logger.isInfoEnabled()){
					logger.info("Received a valid message from " + fromHeader.getAddress()+" requesting a reply containing '"+bodyMessage+"' after "+timerDuration+"s");
				}
				myViewOfTimerACI.setSender(fromHeader.getAddress());
				// finally reply to the SIP message request
	    		sendResponse(event,Response.OK);
			}
			else {
				// parsing failed
				logger.warning("Invalid msg '"+body+"' received");
	    		sendResponse(event,Response.BAD_REQUEST);
			}
		} catch (Throwable e) {
			// oh oh something wrong happened
			logger.severe("Exception while processing MESSAGE", e);
			try {
				sendResponse(event,Response.SERVER_INTERNAL_ERROR);
			} catch (Exception f) {
				logger.severe("Exception while sending SERVER INTERNAL ERROR", f);	
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
	public void onTimerEvent(TimerEvent event, WakeUpSbbActivityContextInterface aci) {
		// detaching so the null AC is claimed after the event handling
		aci.detach(sbbContext.getSbbLocalObject());
		// get message body from aci
		final String body = aci.getBody();
		// get sender from aci
		final Address sender = aci.getSender();
		try {
			// get from, via, content, cseq and max forwards headers
			final FromHeader fromHeader = getFromHeader();
			final List<ViaHeader> viaHeaders = getViaHeaders();
			final ContentTypeHeader contentTypeHeader = getContentTypeHeader();
			final CSeqHeader cSeqHeader = getCSeqHeader();
			final MaxForwardsHeader maxForwardsHeader = getMaxForwardsHeader();
			// create location service child sbb
			final LocationSbbLocalObject locationChildSbb = (LocationSbbLocalObject) getLocationChildRelation().create();
			// get sender bindings and send a message to each
			URI requestURI = null;
			Request request = null;
			ClientTransaction clientTransaction = null;
			//Address toNameAddress = null;
			ToHeader toHeader = null;
			for (RegistrationBinding registration : locationChildSbb.getBindings(sender.getURI().toString()).values()){
				try {
					// create request uri
					requestURI = addressFactory.createURI(registration.getContactAddress());
					// create to header
					//toNameAddress = addressFactory.createAddress(sender.clone());
					//toNameAddress.setDisplayName("Some Sleepy User");
					toHeader = headerFactory.createToHeader((Address) sender.clone(),null);
					// create request and send it
					request = messageFactory.createRequest(requestURI, 
							Request.MESSAGE,
							aci.getCallId(),
							cSeqHeader,
							fromHeader,
							toHeader,
							viaHeaders,
							maxForwardsHeader,contentTypeHeader,body);
					clientTransaction = provider.getNewClientTransaction(request);
					clientTransaction.sendRequest();
				} catch (Throwable f) {
					logger.severe(f.getMessage(),f);
				}
			}
		} catch (Throwable e) {
			logger.severe(e.getMessage(),e);
		}
	}
	
	// HELPERS
	
	private void sendResponse(RequestEvent event, int responseCode) throws SipException, InvalidArgumentException, ParseException {
		event.getServerTransaction().sendResponse(messageFactory.createResponse(responseCode, event.getRequest()));
	}
		
	private ContentTypeHeader getContentTypeHeader() throws ParseException {
		if (CONTENT_TYPE_HEADER == null)  {
			CONTENT_TYPE_HEADER = headerFactory.createContentTypeHeader("text", "plain");
		}
		return CONTENT_TYPE_HEADER;
	}	
	
	private CSeqHeader getCSeqHeader() throws ParseException, InvalidArgumentException {
		if (CSEQ_HEADER == null)  {
			CSEQ_HEADER = headerFactory.createCSeqHeader(2L, Request.MESSAGE);
		}
		return CSEQ_HEADER;
	}
	
	private FromHeader getFromHeader() throws ParseException {
		if (FROM_HEADER == null)  {
			final Address fromNameAddress = addressFactory.createAddress("sip:wakeup@mobicents.org");
			fromNameAddress.setDisplayName("Wake Up Service");
			FROM_HEADER = headerFactory.createFromHeader(fromNameAddress, "1");
		}
		return FROM_HEADER;
	}
	
	private List<ViaHeader> getViaHeaders() throws ParseException, InvalidArgumentException {
		if (VIA_HEADERS == null)  {
			final ListeningPoint listeningPoint = provider.getListeningPoints()[0];
			VIA_HEADERS = new ArrayList<ViaHeader>(1);
			final ViaHeader viaHeader = headerFactory.createViaHeader(listeningPoint.getIPAddress(),listeningPoint.getPort(),listeningPoint.getTransport(),null);
			VIA_HEADERS.add(viaHeader);
		}
		return VIA_HEADERS;
	}
	
	private MaxForwardsHeader getMaxForwardsHeader() throws InvalidArgumentException {
		if (MAX_FORWARDS_HEADER == null)  {
			MAX_FORWARDS_HEADER = this.headerFactory.createMaxForwardsHeader(70);
		}
		return MAX_FORWARDS_HEADER;
	}	   
	
}
