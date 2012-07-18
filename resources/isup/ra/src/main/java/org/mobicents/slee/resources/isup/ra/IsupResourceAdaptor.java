/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.slee.resources.isup.ra;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.InitialContext;
import javax.slee.Address;
import javax.slee.SLEEException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.EventFlags;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorContext;
import javax.slee.resource.SleeEndpoint;
import javax.slee.resource.StartActivityException;
import javax.slee.resource.ConfigProperties.Property;

import org.mobicents.protocols.ss7.isup.ISUPListener;
import org.mobicents.protocols.ss7.isup.ISUPMessageFactory;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ISUPProvider;
import org.mobicents.protocols.ss7.isup.ISUPStack;
import org.mobicents.protocols.ss7.isup.ISUPEvent;
import org.mobicents.protocols.ss7.isup.ISUPTimeoutEvent;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.ISUPStackImpl;
import org.mobicents.protocols.ss7.isup.message.AddressCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.AnswerMessage;
import org.mobicents.protocols.ss7.isup.message.ApplicationTransportMessage;
import org.mobicents.protocols.ss7.isup.message.BlockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.BlockingMessage;
import org.mobicents.protocols.ss7.isup.message.CallProgressMessage;
import org.mobicents.protocols.ss7.isup.message.ChargeInformationMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupBlockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupBlockingMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupQueryMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupQueryResponseMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupUnblockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupUnblockingMessage;
import org.mobicents.protocols.ss7.isup.message.ConfusionMessage;
import org.mobicents.protocols.ss7.isup.message.ConnectMessage;
import org.mobicents.protocols.ss7.isup.message.ContinuityCheckRequestMessage;
import org.mobicents.protocols.ss7.isup.message.ContinuityMessage;
import org.mobicents.protocols.ss7.isup.message.FacilityAcceptedMessage;
import org.mobicents.protocols.ss7.isup.message.FacilityRejectedMessage;
import org.mobicents.protocols.ss7.isup.message.ForwardTransferMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.IdentificationRequestMessage;
import org.mobicents.protocols.ss7.isup.message.IdentificationResponseMessage;
import org.mobicents.protocols.ss7.isup.message.InitialAddressMessage;
import org.mobicents.protocols.ss7.isup.message.LoopPreventionMessage;
import org.mobicents.protocols.ss7.isup.message.LoopbackAckMessage;
import org.mobicents.protocols.ss7.isup.message.NetworkResourceManagementMessage;
import org.mobicents.protocols.ss7.isup.message.OverloadMessage;
import org.mobicents.protocols.ss7.isup.message.PassAlongMessage;
import org.mobicents.protocols.ss7.isup.message.PreReleaseInformationMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseMessage;
import org.mobicents.protocols.ss7.isup.message.ResetCircuitMessage;
import org.mobicents.protocols.ss7.isup.message.ResumeMessage;
import org.mobicents.protocols.ss7.isup.message.SubsequentAddressMessage;
import org.mobicents.protocols.ss7.isup.message.SubsequentDirectoryNumberMessage;
import org.mobicents.protocols.ss7.isup.message.SuspendMessage;
import org.mobicents.protocols.ss7.isup.message.UnblockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.UnblockingMessage;
import org.mobicents.protocols.ss7.isup.message.UnequippedCICMessage;
import org.mobicents.protocols.ss7.isup.message.User2UserInformationMessage;
import org.mobicents.protocols.ss7.isup.message.UserPartAvailableMessage;
import org.mobicents.protocols.ss7.isup.message.UserPartTestMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.slee.resources.ss7.isup.events.TimeoutEvent;
import org.mobicents.slee.resources.ss7.isup.events.BlockedEvent;
import org.mobicents.slee.resources.ss7.isup.ratype.RAISUPProvider;
import org.mobicents.slee.resources.ss7.isup.ratype.CircuitActivity;


/**
 *
 * @author kulikov
 * @author baranowb
 */
public class IsupResourceAdaptor implements ResourceAdaptor, ISUPListener {
	
	
    private Tracer tracer;
    private transient SleeEndpoint sleeEndpoint = null;
    /** the EventLookupFacility is used to look up the event id of incoming events */
    private transient EventLookupFacility eventLookupFacility = null;
    
    private FireableEventTypeCache eventTypeCache;
    private FireableEventTypeFilter eventTypeFilter;
    
    private ResourceAdaptorContext raContext;
    ////////////
    // Flafgs //
    ////////////
    private static final int EVENT_FLAGS = getEventFlags();
    private static final int ACTIVITY_FLAGS = getActivityFlags();
    private ConcurrentHashMap<ActivityHandle, CircuitActivity> activities = new ConcurrentHashMap<ActivityHandle, CircuitActivity>();
        
    /////////////////
    // RA Provider //
    /////////////////
    private RAISUPProviderImpl raProvider;

    // ////////////////////////////
    // Configuration parameters //
    // ////////////////////////////
    private static final String CONF_ISUP_JNDI = "isupJndi";
    private String isupJndi = null;

    /////////////////
    // ISUP stack  //
    /////////////////    
    private ISUPProvider isupProvider;
    private boolean transportUp = false;
    
    private int localspc,ni;
    
    private static int getEventFlags() {
        int eventFlags = EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK;
        EventFlags.setRequestProcessingFailedCallback(eventFlags);
        return eventFlags;
    }
    private static int getActivityFlags() {
    	
        return ActivityFlags.REQUEST_ENDED_CALLBACK;
    }
    
    public void setResourceAdaptorContext(ResourceAdaptorContext context) {
        this.raContext = context;
        this.tracer = context.getTracer("IsupResourceAdaptor");
        this.sleeEndpoint = context.getSleeEndpoint();
        this.eventLookupFacility = context.getEventLookupFacility();
        this.eventTypeCache = new FireableEventTypeCache(tracer);
        this.eventTypeFilter = new FireableEventTypeFilter();
    }

    public void unsetResourceAdaptorContext() {
        this.tracer = null;
        this.raContext = null;
        this.sleeEndpoint = null;
        this.eventLookupFacility = null;
        this.eventTypeCache = null;
        this.eventTypeFilter = null;
    }

    public void raConfigure(ConfigProperties configProperties) {
    	try {
            if (tracer.isInfoEnabled()) {
                    tracer.info("Configuring ISUPRA: " + this.raContext.getEntityName());
            }
            this.isupJndi = (String) configProperties.getProperty(CONF_ISUP_JNDI).getValue();
    	} catch (Exception e) {
            tracer.severe("Configuring of ISUP RA failed ", e);
    	}

    	this.raProvider = new RAISUPProviderImpl();
    }

    public void raUnconfigure() {
      
    }

    public void raActive() {
    	try {
    		InitialContext ic = new InitialContext();
            this.isupProvider = (ISUPProvider) ic.lookup(this.isupJndi);
            tracer.info("Sucssefully connected to ISUP service[" + this.isupJndi + "]");
            
    		this.isupProvider.addListener(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.tracer.severe("Failed to activate ISUP RA ", e);
		}
    }

    public void raStopping() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void raInactive() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	public void raVerifyConfiguration(ConfigProperties configProperties) throws InvalidConfigurationException {
		try {

            if (tracer.isInfoEnabled()) {
                    tracer.info("Verifying configuring ISUPA: " + this.raContext.getEntityName());
            }

            this.isupJndi = (String) configProperties.getProperty(CONF_ISUP_JNDI).getValue();
            if (this.isupJndi == null) {
                    throw new InvalidConfigurationException("ISUP JNDI lookup name cannot be null");
            }

		} catch (Exception e) {
            throw new InvalidConfigurationException("Failed to test configuration options!", e);
		}
	}

    public void raConfigurationUpdate(ConfigProperties arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getResourceAdaptorInterface(String arg0) {
        return this.raProvider;
    }

    public Marshaler getMarshaler() {
        return null;
    }

    public void serviceActive(ReceivableService receivableService) {
    	eventTypeFilter.serviceActive(receivableService);        
    }

    public void serviceStopping(ReceivableService receivableService) {
    	eventTypeFilter.serviceStopping(receivableService);
    }

    public void serviceInactive(ReceivableService receivableService) {
    	eventTypeFilter.serviceInactive(receivableService);
    }

    public void queryLiveness(ActivityHandle arg0) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getActivity(ActivityHandle key) {
       return this.activities.get(key);
    }

    public ActivityHandle getActivityHandle(Object act) {
    	if(act instanceof CircuitActivity)
    	{
    		CircuitActivity activity = (CircuitActivity) act;
    		return createActivityHandle(activity);
    	}
    	return null;
    }

    public void administrativeRemove(ActivityHandle handle) {
    	if(this.activities.containsKey(handle))
    	{
    		//this.activities.remove(handle);
    	}
    }

    public void eventProcessingSuccessful(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5) {
    	//throw new UnsupportedOperationException("Not supported yet.");
    }

    public void eventProcessingFailed(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5, FailureReason arg6) {
    	//throw new UnsupportedOperationException("Not supported yet.");
    }

    public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5) {
    	//throw new UnsupportedOperationException("Not supported yet.");
    }

    public void activityEnded(ActivityHandle handle) {
        //throw new UnsupportedOperationException("Not supported yet.");
    	this.activities.remove(handle);
    }

    public void activityUnreferenced(ActivityHandle handle) {
    	//throw new UnsupportedOperationException("Not supported yet.");    	
    }

    protected void fireEvent(FireableEventType eventType, Object activity, Object event) {
        final ActivityHandle handle = this.getActivityHandle(activity);                

        //TODO insert global title.
        //this causes failure
       // final Address address = new Address(AddressPlan.GT, "");
        try {
            sleeEndpoint.fireEvent(handle, eventType, event, null, null, EVENT_FLAGS);
            tracer.info("Fired event: " + eventType);
        } catch (Throwable e) {
            tracer.severe("Failed to fire event", e);
        }
    }    
    
    //////////////////
    // Some private //
    //////////////////
    
    private ActivityHandle createActivityHandle(CircuitActivity ca)
    {
    	long txKey = ca.getTransactionKey();
    	return new ISUPActivityHandle(txKey);
    	
    }
    
    ///////////////////////////
    // ISUP Listener methods //
    ///////////////////////////

    /* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.ISUPListener#onMessage(org.mobicents.protocols.ss7.isup.message.ISUPMessage)
	 */
	public void onEvent(ISUPEvent event) {
		
		if(tracer.isInfoEnabled())
		{
			tracer.info("Received Message, code: "+event.getMessage().getMessageType().getCode());
		}
		
		//we have activity, now:
		// 1. get handle
		// 2. get event type
		// 3. fire
		//ActivityHandle handle = createActivityHandle(tx);
		String eventName = null;
		switch(event.getMessage().getMessageType().getCode())
		{
		case AnswerMessage.MESSAGE_CODE:
			eventName = "ANSWER";
			break;
		case ApplicationTransportMessage.MESSAGE_CODE:
			eventName = "APPLICATION_TRANSPORT";
			break;
		case AddressCompleteMessage.MESSAGE_CODE:
			eventName = "ADDRESS_COMPLETE";
			break;
		case BlockingMessage.MESSAGE_CODE:
			eventName = "BLOCKING";
			break;
			
		case BlockingAckMessage.MESSAGE_CODE:
			eventName = "BLOCKING_ACK";
			break;
		case CallProgressMessage.MESSAGE_CODE:
			eventName = "CALL_PROGRESS";
			break;
		case ChargeInformationMessage.MESSAGE_CODE:
			eventName = "CHARGE_INFORMATION";
			break;
		case CircuitGroupBlockingMessage.MESSAGE_CODE:
			eventName = "CIRCUIT_GROUP_BLOCKING";
			break;
		case CircuitGroupBlockingAckMessage.MESSAGE_CODE:
			eventName = "CIRCUIT_GROUP_BLOCKING_ACK";
			break;
		case CircuitGroupQueryMessage.MESSAGE_CODE:
			eventName = "CIRCUIT_GROUP_QUERY";
			break;
		case CircuitGroupQueryResponseMessage.MESSAGE_CODE:
			eventName = "CIRCUIT_GROUP_QUERY_RESPONSE";
			break;
		case CircuitGroupResetMessage.MESSAGE_CODE:
			eventName = "CIRCUIT_GROUP_RESET";
			break;
		case CircuitGroupResetAckMessage.MESSAGE_CODE:
			eventName = "CIRCUIT_GROUP_RESET_ACK";
			break;
		case CircuitGroupUnblockingMessage.MESSAGE_CODE:
			eventName = "CIRCUIT_GROUP_UNBLOCKING";
			break;
		case CircuitGroupUnblockingAckMessage.MESSAGE_CODE:
			eventName = "CIRCUIT_GROUP_UNBLOCKING_ACK";
			break;
		case ConnectMessage.MESSAGE_CODE:
			eventName = "CONNECT";
			break;
		case ContinuityCheckRequestMessage.MESSAGE_CODE:
			eventName = "CONTINUITY_CHECK_REQUEST";
			break;
		case ContinuityMessage.MESSAGE_CODE:
			eventName = "CONTINUITY";
			break;
		case FacilityAcceptedMessage.MESSAGE_CODE:
			eventName = "FACILITY_ACCPETED";
			break;
		case FacilityRejectedMessage.MESSAGE_CODE:
			eventName = "FACILITY_REJECTED";
			break;
		case ForwardTransferMessage.MESSAGE_CODE:
			eventName = "FORWARD_TRANSFER";
			break;
		case IdentificationRequestMessage.MESSAGE_CODE:
			eventName = "IDENTIFICATION_REQUEST";
			break;
		case IdentificationResponseMessage.MESSAGE_CODE:
			eventName = "IDENTIFICATION_RESPONSE";
			break;
		case InitialAddressMessage.MESSAGE_CODE:
			eventName = "INITIAL_ADDRESS_MESSAGE";
			break;
		case LoopPreventionMessage.MESSAGE_CODE:
			eventName = "LOOP_PREVENTION";
			break;
		case LoopbackAckMessage.MESSAGE_CODE:
			eventName = "LOOPBACK_ACK";
			break;
		case NetworkResourceManagementMessage.MESSAGE_CODE:
			eventName = "NETWORK_RESOURCE_MANAGEMENT";
			break;
		case OverloadMessage.MESSAGE_CODE:
			eventName = "OVERLOAD";
			break;
		case PassAlongMessage.MESSAGE_CODE:
			eventName = "PASS_ALONG";
			break;
		case PreReleaseInformationMessage.MESSAGE_CODE:
			eventName = "PRERELEASE_INFORMATION";
			break;
		case ReleaseCompleteMessage.MESSAGE_CODE:
			eventName = "RELEASE_COMPLETE";
			break;
		case ReleaseMessage.MESSAGE_CODE:
			eventName = "RELEASE";
			break;
		case ResetCircuitMessage.MESSAGE_CODE:
			eventName = "RESET_CIRCUIT";
			break;
		case ResumeMessage.MESSAGE_CODE:
			eventName = "RESUME";
			break;
		case SubsequentAddressMessage.MESSAGE_CODE:
			eventName = "SUBSEQUENT_ADDRESS";
			break;
		case SubsequentDirectoryNumberMessage.MESSAGE_CODE:
			eventName = "SUBSEQUENT_DIRECTORY_NUMBER";
			break;
		case SuspendMessage.MESSAGE_CODE:
			eventName = "SUSPEND";
			break;
		case UnblockingMessage.MESSAGE_CODE:
			eventName = "UNBLOCKING";
			break;
		case UnblockingAckMessage.MESSAGE_CODE:
			eventName = "UNBLOCKING_ACK";
			break;
		case UnequippedCICMessage.MESSAGE_CODE:
			eventName = "UNEQUIPPED_CIC";
			break;
		case User2UserInformationMessage.MESSAGE_CODE:
			eventName = "USER_TO_USER_INFORMATION";
			break;
		case UserPartAvailableMessage.MESSAGE_CODE:
			eventName = "USER_PART_AVAILABLE";
			break;
		case UserPartTestMessage.MESSAGE_CODE:
			eventName = "USER_PART_TEST";
			break;
		default:
			tracer.severe("Received unkown event code: "+event.getMessage().getMessageType().getCode());			
			return;
			
		}		
		
		final FireableEventType eventType = eventTypeCache.getEventType(eventLookupFacility, eventName);        
        
        //if there is no TX, lets create STX
		long tx=CircuitActivity.generateTransactionKey(event.getMessage().getCircuitIdentificationCode().getCIC(),event.getDpc());
		
		CircuitActivity ca = (CircuitActivity)getActivity(new ISUPActivityHandle(tx));
      	if(ca == null)
		{
      		if (eventTypeFilter.filterInitialEvent(eventType)) {
            	tracer.info("event " + eventName + " filtered");
            	
            	ConfusionMessage cm=this.raProvider.getMessageFactory().createCNF(event.getMessage().getCircuitIdentificationCode().getCIC());
            	CauseIndicators ci=this.raProvider.getParameterFactory().createCauseIndicators();
    			ci.setLocation(CauseIndicators._LOCATION_NETWORK_BEYOND_IP);
    			ci.setCauseValue(CauseIndicators._CV_INVALID_MESSAGE_UNSPECIFIED);
    			cm.setCauseIndicators(ci);
    			
    			try
    			{
    				this.raProvider.sendMessage(cm,event.getDpc());
    			}
    			catch(Exception ex)
    			{}
    			
                return;
            }
      		
			//FIXME: determine in Qs if there is error msg to be sent.... can be extremly complciated since there is 40+ messages
			try {
				ca = this.raProvider.createCircuitActivity(event.getMessage(),event.getDpc());
			} catch (ActivityAlreadyExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SLEEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (StartActivityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
      	else
      	{
      		//may be also initial event , for example for blocking
      		if (eventTypeFilter.filterEvent(eventType) && eventTypeFilter.filterInitialEvent(eventType)) {
            	tracer.info("event " + eventName + " filtered");  
            	
            	ConfusionMessage cm=this.raProvider.getMessageFactory().createCNF(event.getMessage().getCircuitIdentificationCode().getCIC());
            	CauseIndicators ci=this.raProvider.getParameterFactory().createCauseIndicators();
    			ci.setLocation(CauseIndicators._LOCATION_NETWORK_BEYOND_IP);
    			ci.setCauseValue(CauseIndicators._CV_INVALID_MESSAGE_UNSPECIFIED);
    			cm.setCauseIndicators(ci);
    			
    			try
    			{
    				this.raProvider.sendMessage(cm,event.getDpc());
    			}
    			catch(Exception ex)
    			{}
    			
                return;
            }
      	}
      	
		this.fireEvent(eventType, ca, event.getMessage());			
	}
	
	public void onTimeout(ISUPTimeoutEvent event) {		
		ActivityHandle handle=new ISUPActivityHandle(CircuitActivity.generateTransactionKey(event.getMessage().getCircuitIdentificationCode().getCIC(),event.getDpc()));
        if(this.activities.containsKey(handle))
        {        		
                TimeoutEvent te = new TimeoutEvent(event.getMessage(),event.getTimerId());
                final FireableEventType eventType = eventTypeCache.getEventType(eventLookupFacility, "TRANSACTION_TIMEOUT");
                if (eventTypeFilter.filterEvent(eventType)) {
                	tracer.info("event TRANSACTION_TIMEOUT filtered");            
                    return;
                }
                this.fireEvent(eventType, getActivity(handle), te);                
        }
	}	
	
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.ISUPListener#onTransportDown()
	 */
	public void onTransportDown() {
		this.transportUp = false;
		
	}
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.ISUPListener#onTransportUp()
	 */
	public void onTransportUp() {
		// TODO Auto-generated method stub
		this.transportUp = true;
	}

	///////////////////////
    // RA Provider Class //
    ///////////////////////
    private class RAISUPProviderImpl implements RAISUPProvider
    {	
		public CircuitActivity createCircuitActivity(ISUPMessage arg0,int dpc) throws IllegalArgumentException, ActivityAlreadyExistsException, 
			NullPointerException, IllegalStateException, SLEEException, StartActivityException {
				
			ActivityHandle handle=new ISUPActivityHandle(CircuitActivity.generateTransactionKey(arg0.getCircuitIdentificationCode().getCIC(),dpc));
	        if(activities.containsKey(handle))
	        	throw new ActivityAlreadyExistsException("Circuit activity already exists");
	        
			CircuitActivity activity = new CircuitActivity(arg0,dpc,this);
			handle = createActivityHandle(activity);
			sleeEndpoint.startActivity(handle, activity,ACTIVITY_FLAGS);
			activities.putIfAbsent(handle,activity);
			return activity;
		}

		public ISUPMessageFactory getMessageFactory() {
			return isupProvider.getMessageFactory();
		}

		public ISUPParameterFactory getParameterFactory() {
			return isupProvider.getParameterFactory();
		}


		public void sendMessage(ISUPMessage arg0,int dpc) throws ParameterException, IOException {
			isupProvider.sendMessage(arg0,dpc);
			
		}

		public void notifyBlockedChannel(int cic, int dpc)
		{
			ActivityHandle handle=new ISUPActivityHandle(CircuitActivity.generateTransactionKey(cic,dpc));
	        if(activities.containsKey(handle))
	        {        		
	                BlockedEvent be = new BlockedEvent(cic,dpc);
	                final FireableEventType eventType = eventTypeCache.getEventType(eventLookupFacility, "BLOCKED");
	                if (eventTypeFilter.filterEvent(eventType)) {
	                	tracer.info("event BLOCKED filtered");            
	                    return;
	                }
	                fireEvent(eventType, getActivity(handle), be);                
	        }
		}
		
		public void notifyResetChannel(int cic, int dpc)
		{
			ActivityHandle handle=new ISUPActivityHandle(CircuitActivity.generateTransactionKey(cic,dpc));
	        if(activities.containsKey(handle))
	        {        		
	                BlockedEvent be = new BlockedEvent(cic,dpc);
	                final FireableEventType eventType = eventTypeCache.getEventType(eventLookupFacility, "RESET");
	                if (eventTypeFilter.filterEvent(eventType)) {
	                	tracer.info("event RESET filtered");            
	                    return;
	                }
	                fireEvent(eventType, getActivity(handle), be);                
	        }
		}
		
		public void cancelTimer(int cic, int dpc, int timerId)
		{			
			isupProvider.cancelTimer(cic,dpc,timerId);
		}
		
		public void endActivity(CircuitActivity ac)
		{
			isupProvider.cancelAllTimers(ac.getCIC(),ac.getDPC());
			ActivityHandle handle=getActivityHandle(ac);
			sleeEndpoint.endActivity(handle);
			activityEnded(handle);			
		}
		
		/* (non-Javadoc)
		 * @see org.mobicents.slee.resources.ss7.isup.ratype.RAISUPProvider#isTransportUp()
		 */
		public boolean isTransportUp() {
			return transportUp;
		}
    	
    }
}
