package org.mobicents.slee.examples.wakeup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sip.ClientTransaction;
import javax.sip.ServerTransaction;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
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
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import net.java.slee.resource.sip.SleeSipProvider;

import org.apache.log4j.Logger;
import org.mobicents.slee.services.sip.common.SipSendErrorResponseException;
import org.mobicents.slee.services.sip.location.LocationSbbLocalObject;
import org.mobicents.slee.services.sip.location.LocationServiceException;
import org.mobicents.slee.services.sip.location.RegistrationBinding;

public abstract class WakeUpSbb implements javax.slee.Sbb {
	
	/**
	 * Child relation to the location service
	 * @return
	 */
	public abstract ChildRelation getLocationChildRelation();
	
	/**
	 * creates (if not created yet) and retrieves the child sbb instance for the
	 * location service
	 * 
	 * @return
	 */
	public LocationSbbLocalObject getLocationChildSbb() {
		ChildRelation childRelation = getLocationChildRelation();
		Iterator childRelationIterator =  childRelation.iterator();
		if (childRelationIterator.hasNext()) {
			return (LocationSbbLocalObject) childRelationIterator.next();
		}
		else {
			try {
				return (LocationSbbLocalObject) childRelation.create();
			} catch (Exception e) {
				logger.error( "failed to create child sbb", e);
				return null;
			}
		}		
	}
	
	/**
	 * Event handler for the SIP MESSAGE from the UA
	 * @param event
	 * @param aci
	 */
	public void onMessageEvent(javax.sip.RequestEvent event, ActivityContextInterface aci) {
		Request request = event.getRequest();

		try {
        	// Notifiy the client that we received the SIP MESSAGE request
			ServerTransaction st = (ServerTransaction) aci.getActivity();
			Response response = messageFactory.createResponse(Response.OK, request);
    		st.sendResponse(response);
    		
			// PARSING THE MESSAGE BODY should be *WAKE UP IN <timer value in
			// seconds>s! MSG: <msg to send back to UA>!*
			String body = new String(request.getRawContent());
			int i = body.indexOf("WAKE UP IN ");
			int j = body.indexOf("s! MSG: ",i+11);
			int k = body.indexOf("!",j+8);
			if (i >-1 && j>-1 && k >-1) {
				String timerValue = body.substring(i+11,j);
				int timer = Integer.parseInt(timerValue);
				String bodyMessage = body.substring(j+8,k);

				//   CREATE A NEW NULL ACTIVITIY
				NullActivity timerBus = this.nullActivityFactory.createNullActivity();

				// ATTACH ITSELF TO THE NULL ACTIVITY 
				// BY USING THE ACTIVITY CONTEXT INTERFACE
				ActivityContextInterface timerBusACI = 
					this.nullACIFactory.getActivityContextInterface(timerBus);
				timerBusACI.attach(sbbContext.getSbbLocalObject());

				// SETTING VALUES ON THE ACTIVITY CONTEXT
				// USING THE SBB CUSTOM ACI
				WakeUpSbbActivityContextInterface myViewOfTimerBusACI = 
					this.asSbbActivityContextInterface(timerBusACI);
				myViewOfTimerBusACI.setBody(bodyMessage);
				// The From field of each SIP MESSAGE has the UA Address of Record (logical address), 
				// which can be mapped to a current physical contact address. The mapping is provided by the LocationService,
				// which works together with the SIP Registrar service.
				FromHeader fromHeader = (FromHeader)request.getHeader(FromHeader.NAME);
				logger.info("Received a valid message from " + fromHeader.getAddress()+" requesting a reply containing '"+bodyMessage+"' after "+timerValue+"s");
				URI contactURI = findLocalTarget(fromHeader.getAddress().getURI(),getLocationChildSbb());
				Address contactAddress = addressFactory.createAddress(contactURI);
				ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);

				myViewOfTimerBusACI.setContact(contactHeader);

				// SETTING THE TIMER BY USING THE VALUE 
				// IN THE SIP MESSAGE BODY
				TimerOptions options = new TimerOptions();
				options.setPersistent(true);
				this.timerFacility.setTimer(timerBusACI, 
						null, 
						System.currentTimeMillis()+timer*1000, 
						options);
			}
			else {
				logger.warn("Ignoring invalid msg "+body);
			}
			
		} catch (Exception e) {
			logger.error( 
					"Exception while processing MESSAGE: ", e);
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
		// DETACHING SO NULL ACI IS CLAIMED WHEN THE TRANSACTION ENDS
		aci.detach(sbbContext.getSbbLocalObject());
		// RETRIEVING STORED VALUE FROM THE ACTIVITY CONTEXT INTERFACE
		WakeUpSbbActivityContextInterface myViewOfACI = 
			this.asSbbActivityContextInterface(aci);
		// GET DATA FROM ACI
		Header contact = myViewOfACI.getContact();
		String body = myViewOfACI.getBody();
		// SENDING BACK THE WAKE UP CALL
		sendWakeUpCall(contact, body);
	}
	
	
	/*
	 * constructs and sends a SIP MESSAGE back to the UA
	 */
	private void sendWakeUpCall(Header toContact, String body) {
		String strContact = toContact.toString();
		int beginIndex = strContact.indexOf('<');
		int endIndex = strContact.indexOf('>');
		String toAddressStr = strContact.substring(beginIndex+1, endIndex);
		try {
		SipURI fromAddress =
			addressFactory.createSipURI("wakeup", System.getProperty("bind.address","127.0.0.1"));

		javax.sip.address.Address fromNameAddress = addressFactory.createAddress(fromAddress);
		fromNameAddress.setDisplayName("WakeUp");
		FromHeader fromHeader =
			headerFactory.createFromHeader(fromNameAddress, "12345SomeTagID6789");
		
		javax.sip.address.Address toNameAddress = addressFactory.createAddress(toAddressStr);
		toNameAddress.setDisplayName("Some Sleepy User");
		
		ToHeader toHeader =
			headerFactory.createToHeader(toNameAddress, null);
		
		ArrayList viaHeaders = new ArrayList();
		ViaHeader viaHeader =
			headerFactory.createViaHeader(
				provider.getListeningPoints()[0].getIPAddress(),
				provider.getListeningPoints()[0].getPort(),
				provider.getListeningPoints()[0].getTransport(),
				null);

		// add via headers
		viaHeaders.add(viaHeader);
		
		MaxForwardsHeader maxForwards =
			this.headerFactory.createMaxForwardsHeader(70);
		
		
			URI uri = provider.getAddressFactory().createURI(toAddressStr);
			Request req = messageFactory.createRequest(uri, 
					Request.MESSAGE,
					this.provider.getNewCallId(),
					headerFactory.createCSeqHeader(1, Request.MESSAGE),
					fromHeader,
					toHeader,
					viaHeaders,
					maxForwards);
			ContentTypeHeader contentType = headerFactory.createContentTypeHeader("text", "plain");
			req.setContent(body,contentType);
			ClientTransaction ct = provider.getNewClientTransaction(req);
			ct.sendRequest();
			
		} catch (Exception e) {
			logger.error(e);
		}
		
		
	}
	
	/**
	 *  Initialize the component
	 */
	public void setSbbContext(SbbContext context) { 
		this.sbbContext = context;
		try {
            Context myEnv = (Context) new InitialContext().lookup("java:comp/env");
                        
            // Getting SLEE Factility
            timerFacility = (TimerFacility) myEnv.lookup("slee/facilities/timer");
            nullACIFactory = (NullActivityContextInterfaceFactory)myEnv.
            				lookup("slee/nullactivity/activitycontextinterfacefactory");
            nullActivityFactory = (NullActivityFactory)myEnv.lookup("slee/nullactivity/factory");
            
            // Getting JAIN SIP Resource Adaptor interfaces            
            provider = (SleeSipProvider) myEnv.lookup("slee/resources/jainsip/1.2/provider");
            
           
            
            addressFactory = provider.getAddressFactory();
            headerFactory = provider.getHeaderFactory();
            messageFactory = provider.getMessageFactory();
            
        } catch (Exception ne) {
           logger.error("Failed to set sbb context",ne);
        }
		
	}
    public void unsetSbbContext() { this.sbbContext = null; }
    
    // TODO: Implement the lifecycle methods if required
    public void sbbCreate() throws javax.slee.CreateException {}
    public void sbbPostCreate() throws javax.slee.CreateException {}
    public void sbbActivate() {}
    public void sbbPassivate() {}
    public void sbbRemove() {}
    public void sbbLoad() {}
    public void sbbStore() {}
    public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface activity) {}
    public void sbbRolledBack(RolledBackContext context) {}
	
	public abstract org.mobicents.slee.examples.wakeup.WakeUpSbbActivityContextInterface asSbbActivityContextInterface(ActivityContextInterface aci);

    /**
     * Attempts to find a locally registered contact address for the given URI,
     * using the location service interface.
     */
    public URI findLocalTarget(URI uri, LocationSbbLocalObject locationSbbLocalObject) throws SipSendErrorResponseException {
        String addressOfRecord = uri.toString();

        Map bindings = null;
        try {
            bindings = locationSbbLocalObject.getBindings(addressOfRecord);
        } catch (LocationServiceException lse) {
            lse.printStackTrace();
        }

        if (bindings == null) {
            throw new SipSendErrorResponseException("User not found",
                    Response.NOT_FOUND);
        }
        if (bindings.isEmpty()) {
            throw new SipSendErrorResponseException(
                    "User temporarily unavailable",
                    Response.TEMPORARILY_UNAVAILABLE);
        }

        Iterator it = bindings.values().iterator();
        URI target = null;
        while (it.hasNext()) {
            RegistrationBinding binding = (RegistrationBinding) it.next();
			try {
				target = addressFactory.createURI(binding.getContactAddress());
		         break;
			} catch (Exception e) {
				logger.warn("Failed to create sip uri",e);
			}
        }
        if (target == null) {
            logger.error("findLocalTarget: No contacts for "
                    + addressOfRecord + " found.");
            throw new SipSendErrorResponseException(
                    "User temporarily unavailable",
                    Response.TEMPORARILY_UNAVAILABLE);
        }
        return target;
    }

    
	private SbbContext sbbContext; // This SBB's SbbContext
	
	private MessageFactory messageFactory;

	private SleeSipProvider provider;
	private TimerFacility timerFacility;
	private NullActivityContextInterfaceFactory nullACIFactory;
	private NullActivityFactory nullActivityFactory;
	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	
	private static final Logger logger = Logger.getLogger(WakeUpSbb.class); 
	
}
