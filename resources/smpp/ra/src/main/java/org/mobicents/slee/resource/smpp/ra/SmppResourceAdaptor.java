/*
 * SmppResourceAdaptor.java
 *
 * Created on 6 Декабрь 2006 г., 13:11
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
import java.io.Serializable;

import java.io.Serializable;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

import java.util.HashMap;
import java.util.Properties;

import javax.naming.NamingException;

import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.ActivityContextInterface;

import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.SleeEndpoint;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.FailureReason;
import javax.slee.resource.Marshaler;

import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.FacilityException;
import net.java.slee.resource.smpp.ClientTransaction;
import net.java.slee.resource.smpp.Dialog;
import net.java.slee.resource.smpp.RequestEvent;
import net.java.slee.resource.smpp.ResponseEvent;
import net.java.slee.resource.smpp.ServerTransaction;
import net.java.slee.resource.smpp.SmppEvent;
import net.java.slee.resource.smpp.Transaction;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;

import net.java.slee.resource.smpp.ActivityContextInterfaceFactory;

import org.apache.log4j.Logger;
import EDU.oswego.cs.dl.util.concurrent.Semaphore;

/**
 *
 * @author Oleg Kulikov
 */
public class SmppResourceAdaptor implements ResourceAdaptor, Serializable, ConnectionObserver   {
    
    /**
     * The BootstrapContext provides the resource adaptor with the required capabilities in the
     * SLEE to execute its work. The bootstrap context is implemented by the SLEE. The BootstrapContext
     * object holds references to a number of objects that are of interest to many resource adaptors.
     * For further information see JSLEE v1.1 Specification, Early Draft Review Page 305.
     * The bootstrapContext will be set in entityCreated() method.
     */
    private transient BootstrapContext bootstrapContext = null;
    
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
    
    private HashMap activities = new HashMap();
    private HashMap handlers = new HashMap();
    
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
    
    private Logger logger = Logger.getLogger(SmppResourceAdaptor.class);
    
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
    
    public void entityCreated(BootstrapContext bootstrapContext) throws ResourceException {
        this.bootstrapContext = bootstrapContext;
        this.sleeEndpoint = bootstrapContext.getSleeEndpoint();
        this.eventLookup = bootstrapContext.getEventLookupFacility();
        smppProvider = new SmppProviderImpl(this);
    }
    
    public void entityRemoved() {
    }
    
    public void entityActivated() throws ResourceException {
        try {
            logger.debug("Binding to SMSC");
            
            bindSMSC();
            logger.debug("Bound sucssefully");
            
            initializeNamingContext();
            
            linkMonitorThread = new Thread(new LinkMonitor());
            linkMonitorThread.start();
        } catch (IOException e) {
            logger.error("Could not load ra properties. Caused by", e);
            throw new ResourceException(e.getMessage());
        }
    }
    
    public void entityDeactivating() {
        isBound = false;
        linkMonitorThread.interrupt();
    }
    
    public void entityDeactivated() {
        unbindSMSC();
        clearNamingContext();
    }
    
    public void eventProcessingSuccessful(ActivityHandle activityHandle,
            Object object, int i, Address address, int i0) {
    }
    
    public void eventProcessingFailed(ActivityHandle activityHandle,
            Object object, int i, Address address, int i0, FailureReason failureReason) {
    }
    
    public void activityEnded(ActivityHandle activityHandle) {
        // remove the handle from the list of activities
        Object activity = activities.remove(activityHandle);
        handlers.remove(activity.toString());
    }
    
    public void activityUnreferenced(ActivityHandle activityHandle) {
    }
    
    public void queryLiveness(ActivityHandle activityHandle) {
    }
    
    public Object getActivity(ActivityHandle activityHandle) {
        return activities.get(activityHandle);
    }
    
    public ActivityHandle getActivityHandle(Object activity) {
        return (ActivityHandle) handlers.get(activity.toString());
    }
    
    public Object getSBBResourceAdaptorInterface(String string) {
        return smppProvider;
    }
    
    public Marshaler getMarshaler() {
        return null;
    }
    
    public void serviceInstalled(String string, int[] i, String[] string0) {
    }
    
    public void serviceUninstalled(String string) {
    }
    
    public void serviceActivated(String string) {
    }
    
    public void serviceDeactivated(String string) {
    }
    
    protected void fireEvent(String eventName, Object activity, SmppEvent event) {
        ActivityHandle handle = getActivityHandle(activity);
        if (handle == null) {
            logger.warn("Unknown activity " + activity);
            return;
        }
        
        int eventID = -1;
        try {
            eventID = eventLookup.getEventID(eventName, "net.java","3.4");
        } catch (FacilityException fe) {
            logger.error("Caught a FacilityException: ");
            fe.printStackTrace();
            throw new RuntimeException("SmppResourceAdaptor.fireEvent(): FacilityException caught. ", fe);
        } catch (UnrecognizedEventException ue) {
            logger.error("Caught an UnrecognizedEventException: ");
            ue.printStackTrace();
            throw new RuntimeException("SmppResourceAdaptor.fireEvent(): UnrecognizedEventException caught.", ue);
        }
        
        if (eventID == -1) {
            logger.warn("Unknown event type: " + eventName);
            return;
        }
        
        try {
            Address address = new Address(AddressPlan.E164_MOBILE, event.getMessage().getOriginator());
            sleeEndpoint.fireEvent(handle, event, eventID, address);
            logger.info("Fire event: " + eventName);
        } catch (IllegalStateException ise) {
            logger.error("Caught an IllegalStateException: ");
            ise.printStackTrace();
        } catch (ActivityIsEndingException aiee) {
            logger.error("Caught an ActivityIsEndingException: ");
            aiee.printStackTrace();
        } catch (UnrecognizedActivityException uaee) {
            logger.error("Caught an UnrecognizedActivityException: ");
            uaee.printStackTrace();
        }
    }
    
    
    /**
     * Create smpp link.
     *
     * @param properties the provisioned parameters of the link.
     */
    private synchronized void bindSMSC() throws UnknownHostException, IOException {
        
        // Printing SMSC connection information
        logger.debug("SMSC HOST    : " + host);
        logger.debug("SMSC PORT    : " + port);
        logger.debug("SYSTEM-ID    : " + systemID);
        logger.debug("SYSTEM-TYPE  : " + systemType);
        logger.debug("ADDRESS-TON  : " + addressTON);
        logger.debug("ADDRESS-NPI  : " + addressNPI);
        logger.debug("ADDRESS-RANGE: " + addressRange);
        
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
            semaphore.attempt(5000);
        } catch (InterruptedException e) {
            bindStatus = -1;
        }
        
        if (bindStatus != Transaction.STATUS_OK) {
            throw new IOException("Could not bind to SMSC. The reason is " +
                    statusMessage(bindStatus));
        }
        logger.info("Successfully bound to SMSC. ");
    }
    
    /**
     * Shutdown smpp link.
     */
    private void unbindSMSC() {
        try {
            UnbindResp ubr = smscConnection.unbind();
            logger.info(bootstrapContext.getEntityName() + ": unbinding from SMSC");
        } catch (Exception e) {
            logger.error("There was an error unbinding. ", e);
        }
    }
    
    public String statusMessage(int status) {
        return "";
    }
    
    protected void terminate(Transaction tx) {
        ActivityHandle handle = getActivityHandle(tx);
        try {
            sleeEndpoint.activityEnding(handle);
        } catch (Exception ex) {
            logger.warn("Could not end activity: " + handle);
        } 
    }

    protected void terminate(Dialog dialog) {
        ActivityHandle handle = getActivityHandle(dialog);
        try {
            sleeEndpoint.activityEnding(handle);
        } catch (Exception ex) {
            logger.warn("Could not end activity: " + handle);
        } 
    }
    
    public void packetReceived(Connection connection, SMPPPacket packet) {
        lastActivityTime = System.currentTimeMillis();
        String entityName = bootstrapContext.getEntityName();
        
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
                if (logger.isDebugEnabled()) {
                    logger.debug(entityName + " receive bind_transaceiver_resp");
                }
                
                bindStatus = packet.getCommandStatus();
                semaphore.release();
                break;
                
                // An ESME has unbound from the SMSC and has closed the network
                // connection. The SMSC may also unbind from the ESME.
            case SMPPPacket.UNBIND_RESP: {
                logger.info(bootstrapContext.getEntityName() + " unbound succsefuly");
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
                logger.debug("Enquare link packet received");
                break;
                
            case SMPPPacket.ENQUIRE_LINK_RESP:
                lastEnquireLinkResp = System.currentTimeMillis();
                logger.debug("Enquare link response packet received");
                break;
            
            case SMPPPacket.DELIVER_SM : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ServerTransaction tx = dialog.createDeliverSmServerTransaction(
                        packet.getSequenceNum(), msg);
                
                RequestEvent requestEvent = new RequestEventImpl(tx, msg);
                fireEvent("net.java.slee.resource.smpp.DELIVER_SM", tx, requestEvent);
                break;
            }
            // The command acknowledges deliver_sm message.    
            case SMPPPacket.DELIVER_SM_RESP : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ClientTransaction tx = getTransaction(packet);
                ResponseEvent responseEvent = new ResponseEventImpl(tx, msg);
                
                fireEvent("net.java.slee.resource.smpp.DELIVER_SM_RESP", tx, responseEvent);
                dialog.terminate(tx);
                break;
            }
            
            case SMPPPacket.DATA_SM : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ServerTransaction tx = new ServerDataSmTransactionImpl(packet.getSequenceNum(), dialog, msg);
                RequestEvent requestEvent = new RequestEventImpl(tx, msg);
                
                fireEvent("net.java.slee.resource.smpp.DATA_SM", tx, requestEvent);
                break;
            }                
            case SMPPPacket.DATA_SM_RESP : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ClientTransaction tx = getTransaction(packet);
                ResponseEvent responseEvent = new ResponseEventImpl(tx, msg);
                
                fireEvent("net.java.slee.resource.smpp.DATA_SM_RESP", tx, responseEvent);
                dialog.terminate(tx);
                break;
            }
                
            case SMPPPacket.SUBMIT_SM : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ServerTransaction tx = new ServerSubmitSmTransactionImpl(packet.getSequenceNum(), dialog, msg);
                RequestEvent requestEvent = new RequestEventImpl(tx, msg);
                
                fireEvent("net.java.slee.resource.smpp.SUBMIT_SM", tx, requestEvent);
                break;
            }
                
            case SMPPPacket.SUBMIT_SM_RESP : {
                SmppMessageImpl msg = encodeRespMessage(packet);
                ClientTransaction tx = getTransaction(packet);
                msg.setOriginator(((AbstractTransaction)tx).dialog.getRemoteAddress());
                msg.setRecipient(((AbstractTransaction)tx).dialog.getLocalAddress());
                ResponseEvent responseEvent = new ResponseEventImpl(tx, msg);
                
                fireEvent("net.java.slee.resource.smpp.SUBMIT_SM_RESP", tx, responseEvent);
                ((AbstractTransaction)tx).dialog.terminate(tx);
                break;
            }
                
            case SMPPPacket.QUERY_SM : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ServerTransaction tx = new ServerDeliverSmTransactionImpl(packet.getSequenceNum(), dialog, msg);
                RequestEvent requestEvent = new RequestEventImpl(tx, msg);
                
                fireEvent("net.java.slee.resource.smpp.QUERY_SM", tx, requestEvent);
                break;
            }
            
            case SMPPPacket.QUERY_SM_RESP : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ClientTransaction tx = getTransaction(packet);
                ResponseEvent responseEvent = new ResponseEventImpl(tx, msg);
                
                fireEvent("net.java.slee.resource.smpp.QUERY_SM_RESP", tx, responseEvent);
                dialog.terminate(tx);
                break;
            }
            
            case SMPPPacket.REPLACE_SM : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ServerTransaction tx = new ServerDeliverSmTransactionImpl(packet.getSequenceNum(), dialog, msg);
                RequestEvent requestEvent = new RequestEventImpl(tx, msg);
                
                fireEvent("net.java.slee.resource.smpp.REPLACE_SM", tx, requestEvent);
                break;
            }
            
            case SMPPPacket.REPLACE_SM_RESP : {
                SmppDialogImpl dialog = getDialog(packet);
                SmppMessageImpl msg = encodeMessage(packet);
                
                ClientTransaction tx = getTransaction(packet);
                ResponseEvent responseEvent = new ResponseEventImpl(tx, msg);

                fireEvent("net.java.slee.resource.smpp.REPLACE_SM_RESP", tx, responseEvent);
                dialog.terminate(tx);
                break;
            }
            default :
                logger.warn("Unexpected packet received! Id = 0x" +
                        Integer.toHexString(packet.getCommandId()));
        }
    }
    
    private SmppDialogImpl getDialog(SMPPPacket packet) {
        logger.info("Source: " + packet.getSource());
        logger.info("Destination: " + packet.getDestination());
        logger.info("ESM class : " + packet.getEsmClass());
        
        String remoteAddress = packet.getSource().getAddress();
        String localAddress = packet.getDestination().getAddress();
        
        SmppDialogImpl dialog = (SmppDialogImpl)smppProvider.getDialog(remoteAddress, localAddress);
        return dialog;
    }
    
    private ClientTransaction getTransaction(SMPPPacket packet) {
        int id = packet.getSequenceNum();
        return smppProvider.getClientTransaction(id);
    }
    
    private SmppMessageImpl encodeMessage(SMPPPacket packet) {
        String remoteAddress = packet.getSource().getAddress();
        String localAddress = packet.getDestination().getAddress();
        
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
        logger.debug("Update called " + event);
    }
    
    private void initializeNamingContext() {
        logger.info("Initialize naming context");
        
        SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
        logger.debug("SLEE container: " + sleeContainer);
        
        String entityName = bootstrapContext.getEntityName();
        logger.debug("Entity name: " + entityName);
        
        ResourceAdaptorEntity resourceAdaptorEntity =
                (ResourceAdaptorEntity) sleeContainer.getResourceAdaptorEnitity(entityName);
        logger.debug("Resource Adaptor Entity: " + resourceAdaptorEntity);
        
        ResourceAdaptorTypeID resourceAdaptorTypeId =
                resourceAdaptorEntity.getInstalledResourceAdaptor().getRaType().getResourceAdaptorTypeID();
        logger.debug("Resource Adaptor Type ID: " + resourceAdaptorTypeId);
        
        acif = new SmppActivityContextInterfaceFactoryImpl(
                resourceAdaptorEntity.getServiceContainer(), this, entityName);
        sleeContainer.getActivityContextInterfaceFactories().put(resourceAdaptorTypeId, acif);
        
        String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory)acif).getJndiName();
        
        int i = jndiName.indexOf(':');
        int j = jndiName.lastIndexOf('/');
        
        String prefix = jndiName.substring(i + 1, j);
        String name = jndiName.substring(j + 1);
        
        sleeContainer.registerWithJndi(prefix, name, acif);
    }
    
    private void clearNamingContext() {
        String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory)
        acif).getJndiName();
        int i = jndiName.indexOf(':');
        String name = jndiName.substring(i + 1);
        
        SleeContainer.unregisterWithJndi(name);
    }
    
    protected void createDialogHandle(Dialog dialog) {
        DialogHandle handle = new DialogHandle(dialog);
        handlers.put(dialog.toString(), handle);
        activities.put(handle, dialog);
    }
    
    protected void createTransactionHandle(Transaction tx) {
        TransactionHandle handle = new TransactionHandle(tx);
        handlers.put(tx.toString(), handle);
        activities.put(handle, tx);
    }
    
    private void reconnect() {
        try {
            logger.warn("Lost connection! Reconnect to SMSC");
            unbindSMSC();
            bindSMSC();
        } catch (Exception e) {
            logger.error("Reconnect error: " + e.getMessage());
        }
    }

    private class LinkMonitor implements Runnable {
        public void run() {
            while (isBound) {
                long currentTime = System.currentTimeMillis();
                try {
                    EnquireLink sm = (EnquireLink) smscConnection.newInstance(SMPPPacket.ENQUIRE_LINK);
                    smscConnection.sendRequest(sm);
                    
                    if (logger.isDebugEnabled()) {
                        logger.debug("Send enquire link for " + bootstrapContext.getEntityName());
                    }
                    
                    Thread.currentThread().sleep(enquireLinkInterval);
                } catch (NotBoundException nbe) {
                    logger.warn("Connection lost! Reconnecting...");
                    reconnect();
                } catch (IOException ie) {
                    logger.error("Connection lost! Communication failed", ie);
                } catch (InterruptedException e) {
                    logger.info("Terminate link monitor: " + bootstrapContext.getEntityName());
                } catch (BadCommandIDException ex) {
                    //should never happen
                } catch (VersionException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
