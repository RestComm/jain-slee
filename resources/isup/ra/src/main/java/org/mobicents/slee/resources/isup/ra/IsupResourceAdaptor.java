/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.slee.resources.isup.ra;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.slee.Address;
import javax.slee.AddressPlan;
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

import org.mobicents.protocols.ss7.isup.ISUPClientTransaction;
import org.mobicents.protocols.ss7.isup.ISUPListener;
import org.mobicents.protocols.ss7.isup.ISUPMessageFactory;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ISUPProvider;
import org.mobicents.protocols.ss7.isup.ISUPServerTransaction;
import org.mobicents.protocols.ss7.isup.ISUPStack;
import org.mobicents.protocols.ss7.isup.ISUPTransaction;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.TransactionAlredyExistsException;
import org.mobicents.protocols.ss7.isup.TransactionKey;
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
import org.mobicents.protocols.ss7.stream.tcp.M3UserConnector;
import org.mobicents.protocols.ss7.stream.tcp.StartFailedException;
import org.mobicents.slee.resources.ss7.isup.events.TransactionEnded;
import org.mobicents.slee.resources.ss7.isup.ratype.RAISUPProvider;

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
    private Map<ActivityHandle, ISUPTransaction> activities = new HashMap<ActivityHandle, ISUPTransaction>();
    
    /////////////////
    // RA Provider //
    /////////////////
    private RAISUPProviderImpl raProvider;
    //tmp sollution
    private boolean transportUp = false;
    /////////////////
    // ISUP stack  //
    /////////////////
    private ISUPStack stack;
    private ISUPProvider isupProvider;
    
    //////////////////
    // MTP provider //
    //////////////////
    private M3UserConnector mtpProvider;
    //config for provider
    private static final String _DEFAULT_serverAddress = "localhost";
    private static final String _DEFAULT_serverPort = "1345";
    
    private String serverAddress = _DEFAULT_serverAddress;
    private String serverPort = _DEFAULT_serverPort;

	
    
    
    ////////////////////////
    // MTP Provider props //
    ////////////////////////
    private static final String PROPERTY_OPC = "isup.opc";
    private static final String PROPERTY_DPC = "isup.dpc";
    private static final String PROPERTY_SLS = "isup.sls";
    private static final String PROPERTY_SSI = "isup.ssi";

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
        this.tracer = context.getTracer(getClass().getSimpleName());
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

    	if(tracer.isInfoEnabled())
		{
    		tracer.info("Configuring ISUP RA: "+this.raContext.getEntityName());
		}
    	//ignore data.... ech why two methods...
    	Properties props = new Properties();
    	props.put(M3UserConnector._PROPERTY_IP, this.serverAddress);
    	props.put(M3UserConnector._PROPERTY_PORT, this.serverPort);
    	this.mtpProvider = new M3UserConnector(props);
    	
    	
    	props = new Properties();
    	Property p = configProperties.getProperty(PROPERTY_DPC);
    	props.put(PROPERTY_DPC, p.getValue());
    	p = configProperties.getProperty(PROPERTY_OPC);
    	props.put(PROPERTY_OPC, p.getValue());
    	p = configProperties.getProperty(PROPERTY_SLS);
    	props.put(PROPERTY_SLS, p.getValue());
    	p = configProperties.getProperty(PROPERTY_SSI);
    	props.put(PROPERTY_SSI, p.getValue());
    	
    	this.stack = new ISUPStackImpl(this.mtpProvider, props);
    	this.isupProvider = this.stack.getIsupProvider();
    	this.raProvider = new RAISUPProviderImpl();
    }

    public void raUnconfigure() {
      
    }

    public void raActive() {
    	//this.stack  = new ISUPMtpProviderImpl();    	
    	this.stack.start();
    	try {
			this.mtpProvider.start();
		} catch (Exception e) {
			throw new RuntimeException();
		}
		this.stack.getIsupProvider().addListener(this);
  
    }

    public void raStopping() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void raInactive() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void raVerifyConfiguration(ConfigProperties configProperties) throws InvalidConfigurationException {

    	//FIXME: for B1 its hardcoded with M3UserConnector as provider
    	Property p =configProperties.getProperty(M3UserConnector._PROPERTY_IP);
    	if(p!=null)
    	{
    		this.serverAddress = (String) p.getValue();
    	}
    	p =configProperties.getProperty(M3UserConnector._PROPERTY_PORT);
    	if(p!=null)
    	{
    		this.serverPort = (String) p.getValue();
    	}
    	  try{
          	InetAddress.getByName(this.serverAddress);
          }catch(Exception e)
          {
          	tracer.severe("Error on checking server address due to some error, picking up default. "+e.getMessage());
          	e.printStackTrace();
          	this.serverAddress = _DEFAULT_serverAddress;
          	
          }
          
          try
          {
          	int x = Integer.parseInt(this.serverPort);
          	if(x<=0 || x> 65535)
          	{
          		this.serverPort = ""+this._DEFAULT_serverPort;
          	}
          }catch(Exception e)
          {
          	tracer.severe("Error on checking server port due to some error, picking up default. "+e.getMessage());
          	e.printStackTrace();
          	this.serverPort = _DEFAULT_serverPort;
          }
          
          if(configProperties.getProperty(PROPERTY_DPC) == null)
          {
        	  throw new InvalidConfigurationException("No DPC set!");
          }
          
          if(configProperties.getProperty(PROPERTY_OPC) == null)
          {
        	  throw new InvalidConfigurationException("No OPC set!");
          }
          if(configProperties.getProperty(PROPERTY_SSI) == null)
          {
        	  throw new InvalidConfigurationException("No SSI set!");
          }
          if(configProperties.getProperty(PROPERTY_SLS) == null)
          {
        	  throw new InvalidConfigurationException("No SLS set!");
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

    public void serviceActive(ReceivableService arg0) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void serviceStopping(ReceivableService arg0) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void serviceInactive(ReceivableService arg0) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void queryLiveness(ActivityHandle arg0) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getActivity(ActivityHandle key) {
       return this.activities.get(key);
    }

    public ActivityHandle getActivityHandle(Object act) {
    	if(act instanceof ISUPTransaction)
    	{
    		ISUPTransaction activity = (ISUPTransaction) act;
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

    protected void fireEvent(String eventName, Object activity, Object event) {
        final ActivityHandle handle = this.getActivityHandle(activity);        
        final FireableEventType eventType = eventTypeCache.getEventType(eventLookupFacility, eventName);
//        if (eventTypeFilter.filterEvent(eventType)) {
//            if (tracer.isFineEnabled()) {
//                tracer.fine("event " + eventName + " filtered");
//            }
//            return;
//        }

        //TODO insert global title.
        //this causes failure
       // final Address address = new Address(AddressPlan.GT, "");
        try {
            sleeEndpoint.fireEvent(handle, eventType, event, address, null, EVENT_FLAGS);
            tracer.info("Firde event: " + eventName);
        } catch (Throwable e) {
            tracer.severe("Failed to fire event", e);
        }
    }
    
    //////////////////
    // Some private //
    //////////////////
    
    private ActivityHandle createActivityHandle(ISUPTransaction tx)
    {
    	TransactionKey txKey = tx.getTransactionKey();
    	return new ISUPActivityHandle(txKey);
    	
    }
    
    ///////////////////////////
    // ISUP Listener methods //
    ///////////////////////////

    /* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.ISUPListener#onMessage(org.mobicents.protocols.ss7.isup.message.ISUPMessage)
	 */
	public void onMessage(ISUPMessage message) {
		
		if(tracer.isInfoEnabled())
		{
			tracer.info("Received Message, code: "+message.getMessageType().getCode());
		}
		//if there is no TX, lets create STX
		ISUPTransaction tx = message.getTransaction();
		if(tx == null)
		{
			//FIXME: determine in Qs if there is error msg to be sent.... can be extremly complciated since there is 40+ messages
			try {
				tx = this.raProvider.createServerTransaction(message);
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
			} catch (TransactionAlredyExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (StartActivityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//we have activity, now:
		// 1. get handle
		// 2. get event type
		// 3. fire
		//ActivityHandle handle = createActivityHandle(tx);
		String eventName = null;
		switch(message.getMessageType().getCode())
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
				tracer.severe("Received unkown event code: "+message.getMessageType().getCode());
				return;
			
		}
		
		
		this.fireEvent(eventName, tx, message);
	
		
	}
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.ISUPListener#onTransactionEnded(org.mobicents.protocols.ss7.isup.ISUPClientTransaction)
	 */
	public void onTransactionEnded(ISUPClientTransaction ctx) {
		ActivityHandle handle = createActivityHandle(ctx);
		if(this.activities.containsKey(handle))
		{
			TransactionEnded te = new TransactionEnded(ctx,false);
			this.fireEvent("TRANSACTION_ENDED", ctx, te);
			this.sleeEndpoint.endActivity(handle);
		}
		
	}
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.ISUPListener#onTransactionEnded(org.mobicents.protocols.ss7.isup.ISUPServerTransaction)
	 */
	public void onTransactionEnded(ISUPServerTransaction stx) {
		ActivityHandle handle = createActivityHandle(stx);
		if(this.activities.containsKey(handle))
		{
			TransactionEnded te = new TransactionEnded(stx,false);
			this.fireEvent("TRANSACTION_ENDED", stx, te);
			this.sleeEndpoint.endActivity(handle);
		}
		
	}
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.ISUPListener#onTransactionTimeout(org.mobicents.protocols.ss7.isup.ISUPClientTransaction)
	 */
	public void onTransactionTimeout(ISUPClientTransaction ctx) {
		ActivityHandle handle = createActivityHandle(ctx);
		if(this.activities.containsKey(handle))
		{
			TransactionEnded te = new TransactionEnded(ctx,true);
			this.fireEvent("TRANSACTION_ENDED", ctx, te);
			this.sleeEndpoint.endActivity(handle);
		}
		
	}
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.ISUPListener#onTransactionTimeout(org.mobicents.protocols.ss7.isup.ISUPServerTransaction)
	 */
	public void onTransactionTimeout(ISUPServerTransaction stx) {
		ActivityHandle handle = createActivityHandle(stx);
		if(this.activities.containsKey(handle))
		{
			TransactionEnded te = new TransactionEnded(stx,true);
			this.fireEvent("TRANSACTION_ENDED", stx, te);
			this.sleeEndpoint.endActivity(handle);
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

		
		public ISUPClientTransaction createClientTransaction(ISUPMessage arg0) throws TransactionAlredyExistsException,
				IllegalArgumentException, ActivityAlreadyExistsException, NullPointerException, IllegalStateException, SLEEException, StartActivityException {
			
			ISUPClientTransaction ctx = isupProvider.createClientTransaction(arg0);
			ActivityHandle handle = createActivityHandle(ctx);
			sleeEndpoint.startActivity(handle, ctx,ACTIVITY_FLAGS);
			activities.put(handle,ctx);
			return ctx;
		}

		public ISUPServerTransaction createServerTransaction(ISUPMessage arg0) throws TransactionAlredyExistsException,
				IllegalArgumentException, ActivityAlreadyExistsException, NullPointerException, IllegalStateException, SLEEException, StartActivityException {
			ISUPServerTransaction stx = isupProvider.createServerTransaction(arg0);
			ActivityHandle handle = createActivityHandle(stx);
			sleeEndpoint.startActivity(handle, stx,ACTIVITY_FLAGS);
			activities.put(handle,stx);
			return stx;
		}

		public ISUPMessageFactory getMessageFactory() {
			return isupProvider.getMessageFactory();
		}

		public ISUPParameterFactory getParameterFactory() {
			return isupProvider.getParameterFactory();
		}


		public void sendMessage(ISUPMessage arg0) throws ParameterRangeInvalidException, IOException {
			isupProvider.sendMessage(arg0);
			
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.resources.ss7.isup.ratype.RAISUPProvider#isTransportUp()
		 */
		public boolean isTransportUp() {
			return transportUp;
		}
    	
    }
}
