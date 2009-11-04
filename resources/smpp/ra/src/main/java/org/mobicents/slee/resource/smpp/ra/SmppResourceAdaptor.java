/*
 * SmppResourceAdaptor.java
 *
 * Created on 6 ������� 2006 �., 13:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.slee.resource.smpp.ra;

import ie.omk.smpp.BadCommandIDException;
import ie.omk.smpp.Connection;
import ie.omk.smpp.NotBoundException;
import ie.omk.smpp.event.ConnectionObserver;
import ie.omk.smpp.event.SMPPEvent;
import ie.omk.smpp.message.BindResp;
import ie.omk.smpp.message.EnquireLink;
import ie.omk.smpp.message.SMPPPacket;
import ie.omk.smpp.message.UnbindResp;
import ie.omk.smpp.version.SMPPVersion;
import ie.omk.smpp.version.VersionException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityAlreadyExistsException;
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

import net.java.slee.resource.smpp.ActivityContextInterfaceFactory;
import net.java.slee.resource.smpp.ClientTransaction;
import net.java.slee.resource.smpp.Dialog;
import net.java.slee.resource.smpp.RequestEvent;
import net.java.slee.resource.smpp.ResponseEvent;
import net.java.slee.resource.smpp.ServerTransaction;
import net.java.slee.resource.smpp.SmppEvent;
import net.java.slee.resource.smpp.Transaction;

/**
 *
 * @author Oleg Kulikov
 */
public class SmppResourceAdaptor implements ResourceAdaptor, ConnectionObserver {
        
    /**
     * The SLEE endpoint defines the contract between the SLEE and the resource adaptor that enables
     * the resource adaptor to deliver events asynchronously to SLEE endpoints residing in the SLEE.
     * This contract serves as a generic contract that allows a wide range of resources to be plugged
     * into a SLEE environment via the resource adaptor architecture.
     * For further information see JSLEE v1.1 Specification, Early Draft Review Page 307
     * The sleeEndpoint will be initialized in entityCreated() method.
     */
    private transient SleeEndpoint sleeEndpoint = null;
    
    /** the EventLookupFacility is used to look up the event id of incoming events */
    private transient EventLookupFacility eventLookup = null;
    
    protected SmppProviderImpl smppProvider;
    
    private ConcurrentHashMap<SmppActivityHandle, SmppActivityImpl> activities = new ConcurrentHashMap<SmppActivityHandle, SmppActivityImpl>();
    
    /** connection to sms center */
    protected Connection smscConnection = null;
    
    boolean isBound = false;
    private int bindStatus;
    
    /** liveness query interval */
    private long enquireLinkInterval = 30000;
    
    /** last time of activity */
    private long lastActivityTime;
    
    /** last time of liveness query response */
    private long lastEnquireLinkResp;
    
    private Semaphore semaphore = new Semaphore(0);
    
    private String host = "localhost";
    private int port = 2727;
    private String systemID = "1";
    private String systemType = "ESME";
    private String password = "1";
    private int addressTON = 0;
    private int addressNPI = 1;
    private String addressRange = "0020";
    private int enquareLinkTimeout = 300000;
    
    private transient ActivityContextInterfaceFactory acif = null;
    private Thread linkMonitorThread;
    
    private Tracer tracer;

	private ResourceAdaptorContext raContext;
    
	private FireableEventTypeCache eventTypeCache;
	private FireableEventTypeFilter eventTypeFilter;
	
	/**
	 * for all events we are interested in knowing when the event failed to be processed
	 */
	private static final int EVENT_FLAGS = getEventFlags();
	private static int getEventFlags() {
		int eventFlags = EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK;
		EventFlags.setRequestProcessingFailedCallback(eventFlags);
		return eventFlags;
	}

    /** Creates a new instance of SmppResourceAdaptor */
    public SmppResourceAdaptor() {
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setPort(Integer port) {
        this.port = port.intValue();
    }
    
    public Integer getPort() {
        return new Integer(port);
    }
    
    public String getSystemId() {
        return systemID;
    }
    
    public void setSystemId(String systemID) {
        this.systemID = systemID;
    }
    
    public String getSystemType() {
        return systemType;
    }
    
    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getAddressTon() {
        return addressTON;
    }
    
    public void setAddressTon(Integer addressTON) {
        this.addressTON = addressTON.intValue();
    }
    
    public Integer getAddressNpi() {
        return new Integer(addressNPI);
    }
    
    public void setAddressNpi(Integer addressNPI) {
        this.addressNPI = addressNPI.intValue();
    }
    
    public String getAddressRange() {
        return addressRange;
    }
    
    public void setAddressRange(String addressRange) {
        this.addressRange = addressRange;
    }
    
    public Integer getEnquireLinkTimeout() {
        return new Integer(enquareLinkTimeout/1000);
    }
    
    public void setEnquireLinkTimeout(Integer enquireLinkTimeout) {
        this.enquareLinkTimeout = enquireLinkTimeout.intValue() * 1000;
    }
    
    /*
    public void entityCreated(BootstrapContext bootstrapContext) throws ResourceException {
        this.bootstrapContext = bootstrapContext;
        this.sleeEndpoint = bootstrapContext.getSleeEndpoint();
        this.eventLookup = bootstrapContext.getEventLookupFacility();
        smppProvider = new SmppProviderImpl(this);
    }
    */
    /*
    public void entityRemoved() {
    }
    */
    
    /*
    public void entityActivated() throws ResourceException {
        try {
            //logger.info("Binding to SMSC");
        	tracer.info("Binding to SMSC");
        	
            bindSMSC();
            //logger.info("Bound successfully");
            tracer.info("Bound successfully");
            
            initializeNamingContext();
            
            linkMonitorThread = new Thread(new LinkMonitor());
            linkMonitorThread.start();
        } catch (IOException e) {
            //logger.error("Could not load ra properties. Caused by", e);
        	tracer.finer("Could not load ra properties. Caused by", e);
            throw new ResourceException(e.getMessage());
        }
    }
    */
    
    /*
    public void entityDeactivating() {
        isBound = false;
        linkMonitorThread.interrupt();
    }
    
    public void entityDeactivated() {
        unbindSMSC();
        //clearNamingContext();
    }
    */
    
    public void eventProcessingSuccessful(ActivityHandle activityHandle,
            Object object, int i, Address address, int i0) {
    }
    
    public void eventProcessingFailed(ActivityHandle activityHandle,
            Object object, int i, Address address, int i0, FailureReason failureReason) {
    }
    
    public void activityEnded(ActivityHandle activityHandle) {
        // remove the handle from the list of activities
        activities.remove(activityHandle);
    }
    
    public void activityUnreferenced(ActivityHandle activityHandle) {
    }
    
    public void queryLiveness(ActivityHandle activityHandle) {
    }
    
    public Object getActivity(ActivityHandle activityHandle) {
        return activities.get(activityHandle);
    }
    
    public ActivityHandle getActivityHandle(Object activity) {
        return ((SmppActivityImpl)activity).getActivityHandle();
    }
        
    public Marshaler getMarshaler() {
        return null;
    }
    
    protected void fireEvent(String eventName, SmppActivityImpl activity, SmppEvent event) {
        
    	final ActivityHandle handle = activity.getActivityHandle();
        
        final FireableEventType eventType = eventTypeCache.getEventType(eventLookup, eventName);
        if (eventTypeFilter.filterEvent(eventType)) {
        	if (tracer.isFineEnabled()) 
        		tracer.fine("event "+eventName+" filtered");
        	return;
        }
        
        final Address address = new Address(AddressPlan.E164_MOBILE, event.getMessage().getOriginator());
        
        try {
            sleeEndpoint.fireEvent(handle, eventType, event, address, null,EVENT_FLAGS);
            tracer.info("Firde event: " + eventName);
        } catch (Throwable e) {
        	tracer.severe("Failed to fire event",e);
		}
    }
    
    
    /**
     * Create smpp link.
     *
     * @param properties the provisioned parameters of the link.
     */
    private synchronized void bindSMSC() throws UnknownHostException, IOException {
        
        // Printing SMSC connection information
        tracer.info("SMSC HOST    : " + host);
        tracer.info("SMSC PORT    : " + port);
        tracer.info("SYSTEM-ID    : " + systemID);
        tracer.info("SYSTEM-TYPE  : " + systemType);
        tracer.info("ADDRESS-TON  : " + addressTON);
        tracer.info("ADDRESS-NPI  : " + addressNPI);
        tracer.info("ADDRESS-RANGE: " + addressRange);
    	
        lastActivityTime = System.currentTimeMillis();
        
        smscConnection = new Connection(host, port, true);
        smscConnection.addObserver(this);
        
        smscConnection.setVersion(SMPPVersion.V34);
        smscConnection.autoAckLink(true);
        //smscConnection.autoAckMessages(true);
        
        
        // Bind the short way:
        try {
            BindResp resp = smscConnection.bind(smscConnection.TRANSCEIVER,
                    systemID,password,systemType,addressTON,addressNPI,addressRange);
            semaphore.acquire(5000);
        } catch (InterruptedException e) {
            bindStatus = -1;
        }
        
        if (bindStatus != Transaction.STATUS_OK) {
            throw new IOException("Could not bind to SMSC. The reason is " +
                    statusMessage(bindStatus));
        }
        tracer.info("Successfully bound to SMSC. ");
		//soowk: bug fix
		isBound = true;
    }
    
    /**
     * Shutdown smpp link.
     */
    private void unbindSMSC() {
        try {
            UnbindResp ubr = smscConnection.unbind();
            tracer.info(raContext.getEntityName() + ": unbinding from SMSC");
			//soowk: bug fix
			isBound = false;
        } catch (Exception e) {
        	tracer.severe("There was an error unbinding. ", e);
        }
    }
    
    public String statusMessage(int status) {
        return "";
    }
    
    protected void terminate(Transaction tx) {
        ActivityHandle handle = getActivityHandle(tx);
        try {
        	sleeEndpoint.endActivity(handle);
        } catch (Exception ex) {
        	tracer.warning("Could not end activity: " + handle);
        } 
    }

    protected void terminate(Dialog dialog) {
        ActivityHandle handle = getActivityHandle(dialog);
        try {
        	sleeEndpoint.endActivity(handle);
        } catch (Exception ex) {
        	tracer.warning("Could not end activity: " + handle);
        } 
    }
    
    public void packetReceived(Connection connection, SMPPPacket packet) {
        lastActivityTime = System.currentTimeMillis();
        String entityName = raContext.getEntityName();
        
        switch (packet.getCommandId()) {
            // A connected ESME has requested to bind as an ESME Transceiver
            //(by issuing a bind_transceiver PDU) and has received a response from
            // the SMSC authorising its Bind request. An ESME bound as a Transceiver
            // supports the complete set of operations supported by a Transmitter
            // ESME and a Receiver ESME. Thus an ESME bound as a transceiver may
            // send short messages to an SMSC for onward delivery to a Mobile Station
            // or to another ESME. The ESME may also receive short messages from an
            // SMSC which may be originated by a mobile station, by another ESME or
            // by the SMSC itself (for example an SMSC delivery receipt).
            case SMPPPacket.BIND_TRANSCEIVER_RESP:
            	tracer.info(entityName + " receive bind_transaceiver_resp");
                
                bindStatus = packet.getCommandStatus();
                semaphore.release();
                break;
                
                // An ESME has unbound from the SMSC and has closed the network
                // connection. The SMSC may also unbind from the ESME.
            case SMPPPacket.UNBIND_RESP: {
            	tracer.info(raContext.getEntityName() + " unbound successfuly");
                break;
            }
            
            // This message can be sent by either the ESME or SMSC and is used 
            // to provide a confidence-check of the communication path between 
            // an ESME and an SMSC. On receipt of this request the receiving 
            // party should respond with an enquire_link_resp, thus verifying 
            // that the application level connection between the SMSC and the 
            // ESME is functioning. The ESME may also respond by sending any 
            // valid SMPP primitive.
            case SMPPPacket.ENQUIRE_LINK:
                lastEnquireLinkResp = System.currentTimeMillis();
                //@todo reply with enquire_link_resp
                tracer.info("Enquare link packet received");
                break;
                
            case SMPPPacket.ENQUIRE_LINK_RESP:
                lastEnquireLinkResp = System.currentTimeMillis();
                tracer.info("Enquare link response packet received");
                break;
            
            case SMPPPacket.DELIVER_SM : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ServerTransaction tx = dialog.createDeliverSmServerTransaction(
                        packet.getSequenceNum(), msg);
                // TODO optimize this (and others) event firing to not require the cast
                RequestEvent requestEvent = new RequestEventImpl(tx, msg);
                fireEvent("net.java.slee.resource.smpp.DELIVER_SM", (SmppActivityImpl) tx, requestEvent);
                break;
            }
            // The command acknowledges deliver_sm message.    
            case SMPPPacket.DELIVER_SM_RESP : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ClientTransaction tx = getTransaction(packet);
                ResponseEvent responseEvent = new ResponseEventImpl(tx, msg);
                
                fireEvent("net.java.slee.resource.smpp.DELIVER_SM_RESP", (SmppActivityImpl) tx, responseEvent);
                dialog.terminate(tx);
                break;
            }
            
            case SMPPPacket.DATA_SM : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ServerTransaction tx = new ServerDataSmTransactionImpl(packet.getSequenceNum(), dialog, msg);
                RequestEvent requestEvent = new RequestEventImpl(tx, msg);
                
                fireEvent("net.java.slee.resource.smpp.DATA_SM", (SmppActivityImpl) tx, requestEvent);
                break;
            }                
            case SMPPPacket.DATA_SM_RESP : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ClientTransaction tx = getTransaction(packet);
                ResponseEvent responseEvent = new ResponseEventImpl(tx, msg);
                
                fireEvent("net.java.slee.resource.smpp.DATA_SM_RESP", (SmppActivityImpl) tx, responseEvent);
                dialog.terminate(tx);
                break;
            }
                
            case SMPPPacket.SUBMIT_SM : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ServerTransaction tx = new ServerSubmitSmTransactionImpl(packet.getSequenceNum(), dialog, msg);
                RequestEvent requestEvent = new RequestEventImpl(tx, msg);
                
                fireEvent("net.java.slee.resource.smpp.SUBMIT_SM", (SmppActivityImpl) tx, requestEvent);
                break;
            }
                
            case SMPPPacket.SUBMIT_SM_RESP : {
                SmppMessageImpl msg = encodeRespMessage(packet);
                ClientTransaction tx = getTransaction(packet);
                //soowk: bug fix
				//msg.setOriginator(((AbstractTransaction)tx).dialog.getRemoteAddress());
                //msg.setRecipient(((AbstractTransaction)tx).dialog.getLocalAddress());
				msg.setOriginator(((AbstractTransaction)tx).dialog.getLocalAddress());
				msg.setRecipient(((AbstractTransaction)tx).dialog.getRemoteAddress());
                ResponseEvent responseEvent = new ResponseEventImpl(tx, msg);
                
                fireEvent("net.java.slee.resource.smpp.SUBMIT_SM_RESP", (SmppActivityImpl) tx, responseEvent);
                ((AbstractTransaction)tx).dialog.terminate(tx);
                break;
            }
                
            case SMPPPacket.QUERY_SM : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ServerTransaction tx = new ServerDeliverSmTransactionImpl(packet.getSequenceNum(), dialog, msg);
                RequestEvent requestEvent = new RequestEventImpl(tx, msg);
                
                fireEvent("net.java.slee.resource.smpp.QUERY_SM", (SmppActivityImpl) tx, requestEvent);
                break;
            }
            
            case SMPPPacket.QUERY_SM_RESP : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ClientTransaction tx = getTransaction(packet);
                ResponseEvent responseEvent = new ResponseEventImpl(tx, msg);
                
                fireEvent("net.java.slee.resource.smpp.QUERY_SM_RESP", (SmppActivityImpl) tx, responseEvent);
                dialog.terminate(tx);
                break;
            }
            
            case SMPPPacket.REPLACE_SM : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ServerTransaction tx = new ServerDeliverSmTransactionImpl(packet.getSequenceNum(), dialog, msg);
                RequestEvent requestEvent = new RequestEventImpl(tx, msg);
                
                fireEvent("net.java.slee.resource.smpp.REPLACE_SM", (SmppActivityImpl) tx, requestEvent);
                break;
            }
            
            case SMPPPacket.REPLACE_SM_RESP : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ClientTransaction tx = getTransaction(packet);
                ResponseEvent responseEvent = new ResponseEventImpl(tx, msg);

                fireEvent("net.java.slee.resource.smpp.REPLACE_SM_RESP", (SmppActivityImpl) tx, responseEvent);
                dialog.terminate(tx);
                break;
            }
            default :
            	tracer.warning("Unexpected packet received! Id = 0x" +
                                Integer.toHexString(packet.getCommandId()));
        }
    }
    
    private SmppDialogImpl getDialog(SMPPPacket packet) {
        tracer.info("Source: " + packet.getSource());
        tracer.info("Destination: " + packet.getDestination());
        tracer.info("ESM class : " + packet.getEsmClass());
    	
		//soowk: bug fix
        //String remoteAddress = packet.getSource().getAddress();
        //String localAddress = packet.getDestination().getAddress();
		String localAddress = packet.getSource().getAddress();
		String remoteAddress = packet.getDestination().getAddress();
        
		//soowk: bug fix
		//SmppDialogImpl dialog = (SmppDialogImpl)smppProvider.getDialog(remoteAddress, localAddress);
		SmppDialogImpl dialog = (SmppDialogImpl)smppProvider.getDialog(localAddress, remoteAddress);
		//

        return dialog;
    }
    
    private ClientTransaction getTransaction(SMPPPacket packet) {
        int id = packet.getSequenceNum();
        return smppProvider.getClientTransaction(id);
    }
    
    private SmppMessageImpl encodeMessage(SMPPPacket packet) {
		//soowk: bug fix
		//String remoteAddress = packet.getSource().getAddress();
		//String localAddress = packet.getDestination().getAddress();
		String localAddress = packet.getSource().getAddress();
		String remoteAddress = packet.getDestination().getAddress();
        
        SmppMessageImpl msg = new SmppMessageImpl(localAddress, remoteAddress);
        msg.setEncoding(packet.getDataCoding());
        msg.setData(packet.getMessage());
        
        return msg;
    }
    
    public SmppMessageImpl encodeRespMessage(SMPPPacket packet) {
        SmppMessageImpl msg = new SmppMessageImpl(packet.getCommandStatus());
        msg.setEncoding(packet.getDataCoding());
        msg.setData(packet.getMessage());
        return msg;
    }
    
    public void update(Connection connection, SMPPEvent event) {
    	tracer.info("Update called " + event);
    }
    
    protected Dialog getDialogHandle(String origination, String destination) {
    	SmppDialogImpl dialog = new SmppDialogImpl(this, origination, destination);
    	SmppActivityImpl anotherDialog = activities.putIfAbsent(dialog.getActivityHandle(),dialog);
    	if (anotherDialog != null) {
    		dialog = (SmppDialogImpl) anotherDialog;
    	}
    	else {
    		try {
				sleeEndpoint.startActivitySuspended(dialog.getActivityHandle(), dialog);
    		} catch (ActivityAlreadyExistsException e) {
    			tracer.warning("Dialog "+dialog+" already exists in SLEE, may be acceptable in a cluster env",e);        		
    		} catch (Throwable e) {
				tracer.severe("Failed to add dialog "+dialog+" to SLEE activities",e);
				return null;
			}
    		dialog.init();    		
    	}
    	return dialog;
    }
    
    /**
     * 
     * @param tx
     */
    protected boolean createTransactionHandle(AbstractTransaction tx) {
        activities.put(tx.getActivityHandle(), tx);
        try {
			sleeEndpoint.startActivitySuspended(tx.getActivityHandle(), tx);
		} catch (ActivityAlreadyExistsException e) {
			tracer.warning("Transaction "+tx+" already exists in SLEE, may be acceptable in a cluster env",e);        		
		} catch (Throwable e) {
			tracer.severe("Failed to add tx "+tx+" to SLEE activities",e);
			return false;
		}        
		return true;
    }
    
    private void reconnect() {
        try {
            //logger.warn("Lost connection! Reconnect to SMSC");
        	tracer.warning("Lost connection! Reconnect to SMSC");
            unbindSMSC();
            bindSMSC();
        } catch (Exception e) {
            //logger.error("Reconnect error: " + e.getMessage());
            tracer.severe("Reconnect error: " + e.getMessage());
        }
    }

    private class LinkMonitor implements Runnable {
        public void run() {
			//soowk
        	//logger.info("In LinkMonitor, isBound = " + isBound);
        	tracer.info("In LinkMonitor, isBound = " + isBound);
            while (isBound) {
                long currentTime = System.currentTimeMillis();
                try {
                    EnquireLink sm = (EnquireLink) smscConnection.newInstance(SMPPPacket.ENQUIRE_LINK);
                    smscConnection.sendRequest(sm);
                    
                    //if (logger.isDebugEnabled()) {
                    //    logger.debug("Send enquire link for " + bootstrapContext.getEntityName());
                    //}
                    
                    tracer.info("Send enquire link for " + raContext.getEntityName());
                    
                    Thread.currentThread().sleep(enquireLinkInterval);
                } catch (NotBoundException nbe) {
                    //logger.warn("Connection lost! Reconnecting...");
                	tracer.warning("Connection lost! Reconnecting...");
                    reconnect();
                } catch (IOException ie) {
                    //logger.error("Connection lost! Communication failed", ie);
                	tracer.severe("Connection lost! Communication failed", ie);
                } catch (InterruptedException e) {
                    //logger.info("Terminate link monitor: " + bootstrapContext.getEntityName());
                	tracer.info("Terminate link monitor: " + raContext.getEntityName());
                } catch (BadCommandIDException ex) {
                    //should never happen
                } catch (VersionException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // LifeCycle methods for slee1.1
    
	public void administrativeRemove(ActivityHandle arg0) {
		// TODO Auto-generated method stub
		
	}

	public void eventProcessingFailed(ActivityHandle arg0,
			FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5, FailureReason arg6) {
		// TODO Auto-generated method stub
		
	}

	public void eventProcessingSuccessful(ActivityHandle arg0,
			FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5) {
		// TODO Auto-generated method stub
		
	}

	public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1,
			Object arg2, Address arg3, ReceivableService arg4, int arg5) {
		// TODO Auto-generated method stub
		
	}

	public Object getResourceAdaptorInterface(String raTypeSbbInterfaceclassName) { 
		return smppProvider;
	}

	public void raActive() 
	{
		try {
            //logger.info("Binding to SMSC");
        	tracer.info("Binding to SMSC");
        	
            bindSMSC();
            //logger.info("Bound successfully");
            tracer.info("Bound successfully");
                        
            linkMonitorThread = new Thread(new LinkMonitor());
            linkMonitorThread.start();
        } catch (IOException e) {
            //logger.error("Could not load ra properties. Caused by", e);
        	tracer.finer("Could not load ra properties. Caused by", e);
            throw new RuntimeException(e.getMessage(),e);
        }
		
	}

	public void raConfigurationUpdate(ConfigProperties arg0) {
		// not supported
	}

	public void raConfigure(ConfigProperties properties) {
        // TODO properties need to be validated in raVerifyConfiguration
		setAddressNpi((Integer) properties.getProperty("addressNpi").getValue());
		setAddressRange((String) properties.getProperty("addressRange").getValue());
		setAddressTon((Integer) properties.getProperty("addressTon").getValue());
		setEnquireLinkTimeout((Integer) properties.getProperty("enquireLinkTimeout").getValue());
		setHost((String) properties.getProperty("host").getValue());
		setPassword((String) properties.getProperty("password").getValue());
		setPort((Integer) properties.getProperty("port").getValue());
		setSystemId((String) properties.getProperty("systemId").getValue());
		setSystemType((String) properties.getProperty("systemType").getValue());
	}

	public void raInactive() 
	{
		unbindSMSC();		
	}

	public void raStopping() 
	{
		isBound = false;
        linkMonitorThread.interrupt();		
	}

	public void raUnconfigure() {		
	}

	public void raVerifyConfiguration(ConfigProperties arg0)
			throws InvalidConfigurationException {		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#serviceActive(javax.slee.resource
	 * .ReceivableService)
	 */
	public void serviceActive(ReceivableService arg0) {
		eventTypeFilter.serviceActive(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#serviceStopping(javax.slee.resource
	 * .ReceivableService)
	 */
	public void serviceStopping(ReceivableService arg0) {
		eventTypeFilter.serviceStopping(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#serviceInactive(javax.slee.resource
	 * .ReceivableService)
	 */
	public void serviceInactive(ReceivableService arg0) {
		eventTypeFilter.serviceInactive(arg0);
	}

	public void setResourceAdaptorContext(ResourceAdaptorContext raContext) 
	{
		this.tracer = raContext.getTracer(getClass().getSimpleName());
		this.raContext = raContext;		
		this.sleeEndpoint = raContext.getSleeEndpoint();
        this.eventLookup = raContext.getEventLookupFacility();
        this.smppProvider = new SmppProviderImpl(this);	
        this.eventTypeCache = new FireableEventTypeCache(tracer);
		this.eventTypeFilter = new FireableEventTypeFilter();
		
	}

	public void unsetResourceAdaptorContext() {
		this.tracer = null;
		this.raContext = null;		
		this.sleeEndpoint = null;
        this.eventLookup = null;
        this.smppProvider = null;
        this.eventTypeCache = null;
		this.eventTypeFilter = null;
	}       
}
