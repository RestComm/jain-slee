package org.mobicents.slee.examples.wakeup;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipProvider;
import javax.sip.TransactionUnavailableException;
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
import javax.slee.ComponentID;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.Level;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TraceFacility;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import org.mobicents.slee.resource.sip.SipActivityContextInterfaceFactory;
import org.mobicents.slee.resource.sip.SipFactoryProvider;
import org.mobicents.slee.services.sip.common.LocationSbbLocalObject;
import org.mobicents.slee.services.sip.common.LocationServiceException;
import org.mobicents.slee.services.sip.common.RegistrationBinding;
import org.mobicents.slee.services.sip.common.SipSendErrorResponseException;

public abstract class WakeUpSbb implements javax.slee.Sbb {
	
	
	public abstract ChildRelation getLocationChildRelation();
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
				this.trace(Level.SEVERE, "failed to create child sbb", e);
				return null;
			}
		}		
	}
	
	public void onMessageEvent(javax.sip.RequestEvent event, ActivityContextInterface aci) {
		Request request = event.getRequest();

		try {
        	// Notifiy the client that we received the SIP MESSAGE request
			ServerTransaction st = (ServerTransaction) aci.getActivity();
			Response response = messageFactory.createResponse(Response.OK, request);
    		st.sendResponse(response);
    		
    		//   CREATE A NEW NULL ACTIVITIY
    		NullActivity timerBus = this.nullActivityFactory.createNullActivity();
    		
        	// ATTACH ITSELF TO THE NULL ACTIVITY 
        	// BY USING THE ACTIVITY CONTEXT INTERFACE
        	ActivityContextInterface timerBusACI = 
        		this.nullACIFactory.getActivityContextInterface(timerBus);
			timerBusACI.attach(sbbContext.getSbbLocalObject());
			
			// PARSING THE MESSAGE BODY
			String body = new String(request.getRawContent());
			int i = body.indexOf("WAKE UP IN ");
			int j = body.indexOf("s! MSG: ",i+11);
			int k = body.indexOf("!",j+8);
			String timerValue = body.substring(i+11,j);
			int timer = Integer.parseInt(timerValue);
			String bodyMessage = body.substring(j+8,k);
			
			// SETTING VALUES ON THE ACTIVITY CONTEXT
			// USING THE SBB CUSTOM ACI
			WakeUpSbbActivityContextInterface myViewOfTimerBusACI = 
				this.asSbbActivityContextInterface(timerBusACI);
			myViewOfTimerBusACI.setBody(bodyMessage);
			// The From field of each SIP MESSAGE has the UA Address of Record (logical address), 
			// which can be mapped to a current physical contact address. The mapping is provided by the LocationService,
			// which works together with the SIP Registrar service.
			FromHeader fromHeader = (FromHeader)request.getHeader(FromHeader.NAME);
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
			
		} catch (Exception e) {
			this.trace(Level.WARNING, 
					"Exception while processing MESSAGE: ", e);
		}
	}
		
	
	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		// RETRIEVING STORED VALUE FROM THE ACTIVITY CONTEXT INTERFACE
		WakeUpSbbActivityContextInterface myViewOfACI = 
			this.asSbbActivityContextInterface(aci);
		Header contact = myViewOfACI.getContact();
		String body = myViewOfACI.getBody();
		
		// SENDING BACK THE WAKE UP CALL
		sendWakeUpCall(contact, body);
	}
	
	
	
	private void sendWakeUpCall(Header toContact, String body) {
		String strContact = toContact.toString();
		int beginIndex = strContact.indexOf('<');
		int endIndex = strContact.indexOf('>');
		String toAddressStr = strContact.substring(beginIndex+1, endIndex);
		try {
		SipURI fromAddress =
			addressFactory.createSipURI("wakeup", "nist.gov");

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
		
		
			URI uri = fp.getAddressFactory().createURI(toAddressStr);
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
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransactionUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public MessageFactory getMessageFactory() { return messageFactory; }

	
	/**
	 *  Initialize the component
	 */
	public void setSbbContext(SbbContext context) { 
		this.sbbContext = context;
		try {
            Context myEnv = (Context) new InitialContext().lookup("java:comp/env");
            // Storing Sbb Component ID
            id = sbbContext.getSbb();
            
            // Getting SLEE Factility
            traceFacility = (TraceFacility) myEnv.lookup("slee/facilities/trace");
            timerFacility = (TimerFacility) myEnv.lookup("slee/facilities/timer");
            alarmFacility = (AlarmFacility) myEnv.lookup("slee/facilities/alarm");
            nullACIFactory = (NullActivityContextInterfaceFactory)myEnv.
            				lookup("slee/nullactivity/activitycontextinterfacefactory");
            nullActivityFactory = (NullActivityFactory)myEnv.lookup("slee/nullactivity/factory");
            
            // Getting JAIN SIP Resource Adaptor interfaces            
            fp = (SipFactoryProvider) myEnv.lookup("slee/resources/jainsip/1.2/provider");
            
            provider = fp.getSipProvider();
            
            addressFactory = fp.getAddressFactory();
            headerFactory = fp.getHeaderFactory();
            messageFactory = fp.getMessageFactory();
            
            acif = (SipActivityContextInterfaceFactory) myEnv.
            		lookup("slee/resources/jainsip/1.2/acifactory");
            
        } catch (NamingException ne) {
            this.trace(Level.WARNING, "Exception During setSbbContext", ne);
            
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
            bindings = locationSbbLocalObject.getUserBindings(addressOfRecord);
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
            System.out.println("BINDINGS: " + binding);
            ContactHeader header = binding.getContactHeader(addressFactory, headerFactory);
            System.out.println("CONTACT HEADER: " + header);
            if (header == null) { // entry expired
                continue; // see if there are any more contacts...
            }
            Address na = header.getAddress();
            System.out.println("Address: " + na);
            target = na.getURI();
            break;
        }
        if (target == null) {
            System.err.println("findLocalTarget: No contacts for "
                    + addressOfRecord + " found.");
            throw new SipSendErrorResponseException(
                    "User temporarily unavailable",
                    Response.TEMPORARILY_UNAVAILABLE);
        }
        return target;
    }
    
	
	/**
	 * Convenience method to retrieve the SbbContext object stored in setSbbContext.
	 * 
	 * TODO: If your SBB doesn't require the SbbContext object you may remove this 
	 * method, the sbbContext variable and the variable assignment in setSbbContext().
	 *
	 * @return this SBB's SbbContext object
	 */
	 
	protected SbbContext getSbbContext() {
		
		return sbbContext;
	}

	private SbbContext sbbContext; // This SBB's SbbContext
	
	protected final void trace(Level level, String message) {
        try {
            traceFacility.createTrace(id, level, "WakeUpSbb", message, System.currentTimeMillis());
       } catch (Exception e) { }
    }
	
	protected final void trace(Level level, String message, Throwable t) {
        try {
            traceFacility.createTrace(id, level, "WakeUpSbb", message, t, System.currentTimeMillis());
        } catch (Exception e) { }
    }
	
	private MessageFactory messageFactory;
	private SipProvider provider;
	private SipFactoryProvider fp;
	private SipActivityContextInterfaceFactory acif;
	private TraceFacility traceFacility;
	private TimerFacility timerFacility;
	private AlarmFacility alarmFacility;
	private NullActivityContextInterfaceFactory nullACIFactory;
	private NullActivityFactory nullActivityFactory;
	private ComponentID id;
	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	
}
