package net.java.slee.resource.diameter;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.HashMap;
import java.util.Set;

import javax.naming.NamingException;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.FacilityException;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.SleeEndpoint;
import javax.slee.facilities.EventLookupFacility;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;

import dk.i1.diameter.AVP;
import dk.i1.diameter.AVP_UTF8String;
import dk.i1.diameter.AVP_Unsigned32;
import dk.i1.diameter.InvalidAVPLengthException;
import dk.i1.diameter.Message;
import dk.i1.diameter.ProtocolConstants;
import dk.i1.diameter.node.Capability;
import dk.i1.diameter.node.ConnectionKey;
import dk.i1.diameter.node.EmptyHostNameException;
import dk.i1.diameter.node.InvalidSettingException;
import dk.i1.diameter.node.Node;
import dk.i1.diameter.node.NodeSettings;
import dk.i1.diameter.node.Peer;
import dk.i1.diameter.node.UnsupportedTransportProtocolException;
import static dk.i1.diameter.ProtocolConstants.*;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.FailureReason;
import net.java.slee.resource.diameter.activities.ConfigureCommonOptions;
import net.java.slee.resource.diameter.activities.ShInterfaceActivity;
import net.java.slee.resource.diameter.activities.ShInterfaceActivityImpl;
import net.java.slee.resource.diameter.message.MessageEvent;
import net.java.slee.resource.diameter.message.MessageEventImpl;
import net.java.slee.resource.diameter.utils.EventHandle;
import static net.java.slee.resource.diameter.ShProtocolConstants.*;

public class DiameterResourceAdaptor implements ResourceAdaptor, Serializable,
        Diameter2SLEERAInterface {

    private static transient Logger logger = Logger
            .getLogger(DiameterResourceAdaptor.class);

    /**
     * The BootstrapContext provides the resource adaptor with the required
     * capabilities in the SLEE to execute its work. The bootstrap context is
     * implemented by the SLEE. The BootstrapContext object holds references to
     * a number of objects that are of interest to many resource adaptors. For
     * further information see JSLEE v1.1 Specification Page 305. The
     * bootstrapContext will be set in entityCreated() method.
     */
    private transient BootstrapContext bootstrapContext = null;

    /**
     * The SLEE endpoint defines the contract between the SLEE and the resource
     * adaptor that enables the resource adaptor to deliver events
     * asynchronously to SLEE endpoints residing in the SLEE. This contract
     * serves as a generic contract that allows a wide range of resources to be
     * plugged into a SLEE environment via the resource adaptor architecture.
     * For further information see JSLEE v1.1 Specification Page 307 The
     * sleeEndpoint will be initialized in entityCreated() method.
     */
    private transient SleeEndpoint sleeEndpoint = null;

    // the EventLookupFacility is used to look up the event id of incoming
    // events
    private transient EventLookupFacility eventLookup = null;

    // The list of activites stored in this resource adaptor. If this resource
    // adaptor were a distributed
    // and highly available solution, this storage were one of the candidates
    // for distribution.
    private transient HashMap activities = null;

    // the activity context interface factory defined in
    // DiameterRAActivityContextInterfaceFactoryImpl
    private transient DiameterRAActivityContextInterfaceFactory acif = null;

    // a link to the DiameterProvider which then will be exposed to Sbbs
    private transient DiameterResourceAdaptorSbbInterface raProvider = null;

    // extended Nodemanager class
    private transient Diameter2SLEENodeManager nodeManager = null;

    // Capabilities of our node, this objects holds apps that are supported,
    // according to conf file
    private transient Capability capabilities = null;

    
    //MAP GIVING MEANS TO DECODE for instance 274R into "Abort-Session-Request" name
    //AVPS like 274R=Abort-Session-Request 
    // and      274A=Abort-Session-Answer 
    // are placed in MessagesMap.properties file
    HashMap<EventHandle,String> associationMap=null;
    
    //SET OF PEERS TO WHICH WE HAVE ESTABILISHED CONNECTIONS 
    Set<Peer> peers=new HashSet<Peer>();
    //SET OF PEERS FROM WHICH WE RECEIVE REQUESTS
    Set<Peer> connectedPeers=new HashSet<Peer>();
    
    // private int[] serverResponses = null;

    public DiameterResourceAdaptor() {
        logger.info("============== CREATED DIAMETERRA ENTITY ===============");
    }

    /**
     * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
     * Specification Page 298 for further information. <br>
     * This method is called by the SLEE when a resource adaptor object instance
     * is bootstrapped, either when a resource adaptor entity is created or
     * during SLEE startup. The SLEE implementation will construct the resource
     * adaptor object and then invoke the entityCreated method before any other
     * operations can be invoked on the resource adaptor object.
     */
    public void entityCreated(BootstrapContext bootstrapContext)
            throws ResourceException {
        logger.debug("RAFrameResourceAdaptor.entityCreated() called.");
        this.bootstrapContext = bootstrapContext;
        this.sleeEndpoint = bootstrapContext.getSleeEndpoint();
        this.eventLookup = bootstrapContext.getEventLookupFacility();

    }

    /**
     * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
     * Specification Page 299 for further information. <br>
     * This method is called by the SLEE when a resource adaptor object instance
     * is being removed, either when a resource adaptor entity is deleted or
     * during SLEE shutdown. When receiving this invocation the resource adaptor
     * object is expected to close any system resources it has allocated.
     */
    public void entityRemoved() {
        logger
                .info("=================== DiameterRA entityRemoved METHOD CALLED ===================");
    }

    /**
     * implements javax.slee.resource.ResourceAdaptor The JSLEE v1.1
     * Specification does not include entityActivated(). However, the API
     * description of JSLEE v1.1 does include this method already. So, the
     * documentation follows the code. <br>
     * This method is called in context of project Mobicents in context of
     * resource adaptor activation. More precisely,
     * org.mobicents.slee.resource.ResourceAdaptorEntity.activate() calls this
     * method entityActivated(). This method signals the resource adaptor the
     * transition from state "INACTIVE" to state "ACTIVE".
     */
    public void entityActivated() throws javax.slee.resource.ResourceException {
        logger.debug("RAFrameResourceAdaptor.entityActivated() called.");
        try {
            logger
                    .info("=============== ACTIVATING DiameterRA ====================");

            //serverRequests = getServerRequestsCodes();
            //LETS CREATE ASSOCIATION MAP
            createEvents2NamesAssociationMap();
            //READ RA PROPS FILE, CONTAINS DEFINITIONS OF SUPPORTED APSS , VENDORS, 
            //NODESETTINGS AND PEERS TO WHICH WE SHOULD CONNECT
            Properties props = new Properties();
            try {
                props.load(getClass().getResourceAsStream(
                        "diameterRA.properties"));
            } catch (IOException IOE) {
                logger
                        .error("^^^^   FAILED TO LOAD: diameterRA.properties ^^^^^");
                throw IOE;
            }
            // WE NEED NodeSettings
            NodeSettings NS = readNodeConfiguration(props);
            capabilities = NS.capabilities();
            logger
                    .info("=============== CREATING NODE MANAGER ====================");
            nodeManager = new Diameter2SLEENodeManager(NS, this);

            logger
                    .info("=============== STARTING NODE MANAGER ====================");
            nodeManager.start();
            logger
                    .info("=============== CREATING CONNECTIONS ====================");
            // LETS CREATE CONNECTIONS
            Node node = nodeManager.node();
            Peer[] peers = getPeers(props);
            for (int i = 0; i < peers.length; i++) {
                // INITIATE PERSISTENT CONNECTION
                node.initiateConnection(peers[i], true);
                this.peers.add(peers[i]);
            }
            // WE SHOULD WAIT FOR CONNECTIONS, SHOULDNT WE? JUST A LITTLE BIT
            nodeManager.waitForConnection(5000);
            
            // SO EACH ACTIVITY CAN HAVE ACCESS TO SOME NEEDED METHODS DEFINED
            // BY THIS INTERFACE, THIS WAY WE DOENT HAVE TO WORRY ABOUT PASSING IT TO CONSTRUCTOR.
            ConfigureCommonOptions.doConfigure(raProvider);
            initializeNamingContext();

            logger
            .info("=============== CREATING PROVIDER ====================");
    raProvider = new DiameterRAProvider(this, nodeManager, acif);
            activities = new HashMap();

        } catch (InvalidSettingException e) {

            throw new javax.slee.resource.ResourceException(
                    "============================== DiameterResourceadaptor.entityActivated(): Failed to activate DiameterRA! WRONG NODE SETTINGS ============================",
                    e);
        } catch (IOException e) {
            throw new javax.slee.resource.ResourceException(
                    "============================== DiameterResourceadaptor.entityActivated(): Failed to activate DiameterRA! COULDNT READ NODE SETTINGS ============================",
                    e);
        } catch (InterruptedException e) {
            throw new javax.slee.resource.ResourceException(
                    "============================== DiameterResourceadaptor.entityActivated(): Failed to activate DiameterRA! CONNECTION FAILURE? ============================",
                    e);
        } catch (NamingException e) {
            throw new javax.slee.resource.ResourceException(
                    "============================== DiameterResourceadaptor.entityActivated(): Failed to activate DiameterRA! ============================",
                    e);
        } catch (UnsupportedTransportProtocolException e) {
            throw new javax.slee.resource.ResourceException(
                    "============================== DiameterResourceadaptor.entityActivated(): Failed to activate DiameterRA! ============================",
                    e);
		}
    }

    /**
     * implements javax.slee.resource.ResourceAdaptor The JSLEE v1.1
     * Specification does not include entityDeactivated(). However, the API
     * description of JSLEE v1.1 does include this method already. So, the
     * documentation follows the code. <br>
     * This method is called in context of project Mobicents in context of
     * resource adaptor deactivation. More precisely,
     * org.mobicents.slee.resource.ResourceAdaptorEntity.deactivate() calls this
     * method entityDeactivated(). The method call is done AFTER the call to
     * entityDeactivating(). This method signals the resource adaptor the
     * transition from state "STOPPING" to state "INACTIVE".
     */
    public void entityDeactivated() {
        logger
                .info("======================== DiameterRA entityDeactivated METHOD CALLED ===================");
        try {
            cleanNamingContext();
        } catch (NamingException e) {
            logger.error("^^^ DiameterRA Cannot unbind naming context ^^^");
        }
        logger
                .info("========================== DiameterRA stopped. ========================");
    }

    /**
     * This method is called in context of project Mobicents in context of
     * resource adaptor deactivation. More precisely,
     * org.mobicents.slee.resource.ResourceAdaptorEntity.deactivate() calls this
     * method entityDeactivating() PRIOR to invoking entityDeactivated(). This
     * method signals the resource adaptor the transition from state "ACTIVE" to
     * state "STOPPING".
     */
    public void entityDeactivating() {
        logger
                .info("================== DiameterRA entityDeactivating() METHOD CALLED ===================");

    }

    /**
     * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
     * Specification Page 300 for further information. <br>
     * The SLEE calls this method to inform the resource adaptor object that the
     * specified event was processed successfully by the SLEE. An event is
     * considered to be processed successfully if the SLEE has attempted to
     * deliver the event to all interested SBBs.
     */
    public void eventProcessingSuccessful(ActivityHandle activityHandle,
            Object obj, int param, Address address, int flags) {
        logger
                .info(" ===================== DiameterRA eventProcessingSuccessful METHOD CALLED ===================");
    }

    /**
     * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
     * Specification Page 300 for further information. <br>
     * The SLEE calls this method to inform the resource adaptor object that the
     * specified event was processed unsuccessfully by the SLEE. Event
     * processing can fail if, for example, the SLEE doesn’t have enough
     * resource to process the event, a SLEE node fails during event processing
     * or a system level failure prevents the SLEE from committing transactions.
     */
    public void eventProcessingFailed(ActivityHandle activityHandle,
            Object obj, int param, Address address, int flags,
            FailureReason failureReason) {
        logger
                .info(" ===================== DiameterRA eventProcessingFailed METHOD CALLED ===================");
    }

    /**
     * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
     * Specification Page 301 for further information. <br>
     * The SLEE calls this method to inform the resource adaptor that the SLEE
     * has completed activity end processing for the activity represented by the
     * activity handle. The resource adaptor should release any resource related
     * to this activity as the SLEE will not ask for it again.
     */
    public void activityEnded(javax.slee.resource.ActivityHandle activityHandle) {
        // remove the activity from the list of activities
        activities.remove(activityHandle);
        logger
                .info(" ===================== DiameterRA activityEnded METHOD CALLED ===================");
    }

    /**
     * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
     * Specification Page 301 for further information. <br>
     * The SLEE calls this method to inform the resource adaptor that the
     * activity’s Activity Context object is no longer attached to any SBB
     * entities and is no longer referenced by any SLEE Facilities. This enables
     * the resource adaptor to implicitly end the Activity object.
     */
    public void activityUnreferenced(
            javax.slee.resource.ActivityHandle activityHandle) {
//      remove the activity from the list of activities
        activities.remove(activityHandle);
        logger
                .info(" ===================== DiameterRA activityUnreferenced METHOD CALLED ===================");
    }

    /**
     * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
     * Specification Page 301 for further information. <br>
     * The SLEE calls this method to query if a specific activity belonging to
     * this resource adaptor object is alive.
     */
    public void queryLiveness(javax.slee.resource.ActivityHandle activityHandle) {
        logger
                .info(" ===================== DiameterRA queryLiveness METHOD CALLED ===================");
    }

    public Object getActivity(ActivityHandle activityHandle) {
        Object act = activities.get(activityHandle);
        logger
                .info(" ===================== DiameterRA getActivity METHOD CALLED ===================");
        return act;
    }

    /**
     * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
     * Specification Page 301 for further information. <br>
     * The SLEE calls this method to get an activity handle for an activity
     * created by the underlying resource. This method is invoked by the SLEE
     * when it needs to construct an activity context for an activity via an
     * activity context interface factory method invoked by an SBB.
     */
    public javax.slee.resource.ActivityHandle getActivityHandle(Object obj) {
        logger
                .info(" ===================== DiameterRA getActivityHandle METHOD CALLED ===================");
        return null;
    }

    /**
     * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
     * Specification Page 302 for further information. <br>
     * The SLEE calls this method to get access to the underlying resource
     * adaptor interface that enables the SBB to invoke the resource adaptor, to
     * send messages for example.
     */
    public Object getSBBResourceAdaptorInterface(String str) {
        logger
                .info(" ===================== DiameterRA getSBBResourceAdaptorInterface METHOD CALLED: "
                        + str + " ===================");
        return raProvider;
    }

    /**
     * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
     * Specification Page 302 for further information. <br>
     * The SLEE calls this method to get reference to the Marshaler object. The
     * resource adaptor implements the Marshaler interface. The Marshaler is
     * used by the SLEE to convert between object and distributable forms of
     * events and event handles.
     */
    public javax.slee.resource.Marshaler getMarshaler() {
        logger
                .info(" ===================== DiameterRA getMarshaler METHOD CALLED ===================");
        return null;
    }

    /**
     * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
     * Specification Page 302 for further information. <br>
     * The SLEE calls this method to signify to the resource adaptor that a
     * service has been installed and is interested in a specific set of events.
     * The SLEE passes an event filter which identifies a set of event types
     * that services in the SLEE are interested in. The SLEE calls this method
     * once a service is installed.
     */
    public void serviceInstalled(String str, int[] values, String[] str2) {
        logger
                .info(" ===================== DiameterRA serviceInstalled METHOD CALLED ===================");
    }

    /**
     * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
     * Specification Page 303 for further information. <br>
     * The SLEE calls this method to signify that a service has been
     * un-installed in the SLEE. The event types associated to the service key
     * are no longer of interest to a particular application.
     */
    public void serviceUninstalled(String str) {
        logger
                .info(" ===================== DiameterRA serviceUninstalled METHOD CALLED: "
                        + str + " ===================");
    }

    /**
     * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
     * Specification Page 303 for further information. <br>
     * The SLEE calls this method to inform the resource adaptor that a service
     * has been activated and is interested in the event types associated to the
     * service key. The service must be installed with the resource adaptor via
     * the serviceInstalled method before it can be activated.
     */
    public void serviceActivated(String str) {
        logger
                .info(" ===================== DiameterRA serviceActivated METHOD CALLED: "
                        + str + " ===================");
    }

    /**
     * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
     * Specification Page 304 for further information. <br>
     * The SLEE calls this method to inform the SLEE that a service has been
     * deactivated and is no longer interested in the event types associated to
     * the service key.
     */
    public void serviceDeactivated(String str) {
        logger
                .info(" ===================== DiameterRA serviceDeactivated METHOD CALLED: "
                        + str + "===================");
    }

    // set up the JNDI naming context
    private void initializeNamingContext() throws NamingException {
        // get the reference to the SLEE container from JNDI
        SleeContainer container = SleeContainer.lookupFromJndi();
        // get the entities name
        String entityName = bootstrapContext.getEntityName();

       ResourceAdaptorEntity resourceAdaptorEntity = ((ResourceAdaptorEntity) container
                .getResourceAdaptorEnitity(entityName));
        ResourceAdaptorTypeID raTypeId = resourceAdaptorEntity
                .getInstalledResourceAdaptor().getRaType()
                .getResourceAdaptorTypeID();

        acif = new DiameterRAActivityContextInterfaceFactoryImpl(
                resourceAdaptorEntity.getServiceContainer(), entityName);
        // set the ActivityContextInterfaceFactory
        resourceAdaptorEntity.getServiceContainer()
                .getActivityContextInterfaceFactories().put(raTypeId, acif);

        try {
            if (this.acif != null) {
                // parse the string =
                // java:slee/resources/diametera/diameterraacif
                String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) acif)
                        .getJndiName();
                int begind = jndiName.indexOf(':');
                int toind = jndiName.lastIndexOf('/');
                String prefix = jndiName.substring(begind + 1, toind);
                String name = jndiName.substring(toind + 1);
                logger.info("========================== jndiName prefix ="
                        + prefix + "; jndiName = " + name
                        + " ; ACIF: "+acif+"=========================");
                SleeContainer.registerWithJndi(prefix, name, this.acif);
            }
        } catch (IndexOutOfBoundsException e) {
            // not register with JNDI
            logger.info(e);
        }
    }

    // clean the JNDI naming context
    private void cleanNamingContext() throws NamingException {
        try {
            if (this.acif != null) {
                // parse the string =
                // java:slee/resources/DiameterRA/diameterraacif
                String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.acif)
                        .getJndiName();
                // remove "java:" prefix
                int begind = jndiName.indexOf(':');
                String javaJNDIName = jndiName.substring(begind + 1);
                logger.info("=================== JNDI name to unregister: "
                        + javaJNDIName + " ====================");
                SleeContainer.unregisterWithJndi(javaJNDIName);
                logger
                        .info("====================== JNDI name unregistered.==========================");
            }
        } catch (IndexOutOfBoundsException e) {
            logger.info(e);
        }
    }

    /**
     * Called by Diameter2SLEERANodeManager class when request is received.
     */
    public void onRequest(Message msg, ConnectionKey key, Peer peer) {
        
        logger.info("============= DiameterRA onRequest METHOD CALLED ================");
        if(!this.connectedPeers.contains(peer))
        {
            this.connectedPeers.add(peer);
        }
        DiameterRAActivityHandle DAH = null;
        
        //LETS CHECK IF REQUIRED FIELD IS PRESENT.
        AVP avpSID =  msg.find(ProtocolConstants.DI_SESSION_ID);
        AVP_UTF8String SIDAvp=null;
        if (avpSID == null) {
            logger.info("============== GOT REQUEST WITHOUT SESSION ID!!!! DROPPING !!!!! ===========");
            return;
        } else {
            SIDAvp=new AVP_UTF8String(avpSID);
            DAH = new DiameterRAActivityHandle(SIDAvp.queryValue());
        }

        if (activities.get(DAH) == null) {
            // UPS WE HAVE TO CREATE NEW ACTIVITY :/

            // SHOULD RA CHECK THIS? OR JUST CREATE ACTIviTIES ??
            int applicationId = msg.hdr.application_id;
            /*
             * if(!capabilities.isAllowedAcctApp(applicationId) &&
             * !capabilities.isAllowedAuthApp(applicationId)) {
             * logger.info("======== APPLICATION: "+applicationId+" IS NOT
             * SUPPORTED, SKIPPING MESSAGE ======="); return; }
             */
            
            //PAY ATTENTION, ITS TRICKY HERE
            //LOOK AT VARIABLE NAME AND WHAT IS LOOKED UP IN MESSAGE
            AVP_Unsigned32 authSessionState=null;
            try {
                authSessionState = new AVP_Unsigned32(msg.find(DI_AUTH_SESSION_STATE));
            } catch (InvalidAVPLengthException e) {
                logger.error("^^^^ SOMETHING WRONG HAS HAPPENED WITH AUTH SESSION STATE AVP ^^^");
                e.printStackTrace();
                return;
            }
            AVP_UTF8String destinationHost=new AVP_UTF8String(msg.find(DI_ORIGIN_HOST));
            AVP_UTF8String destinationRealm=new AVP_UTF8String(msg.find(DI_ORIGIN_REALM));
            //AVP_UTF8String sessionID=new AVP_UTF8String(msg.find(DI_SESSION_ID));
            //NEED DAH AND OF WE GO, LATTER
            //DAH=new DiameterRAActivityHandle(sessionID.queryValue());
            
            //HERE ARE APPS THAT WE SUPPORT
            //LETS BROWSE
            switch (applicationId) {
            case DIAMETER_APPLICATION_SH:
                
                ShInterfaceActivity activity=new ShInterfaceActivityImpl(destinationHost.queryValue(),destinationRealm.queryValue(),SIDAvp.queryValue(),authSessionState.queryValue(),key,DAH,raProvider);
                activities.put(DAH,activity);
                break;

                //APPLICATION NOT SUPPORTED...
            default:
                logger.info("===== APPLICATION NOT RECOGNIZED: "
                        + applicationId + " ======");
                return;
                
            }
            // END SWITCH
        }
        
        //WE HAVE ACTIVITY LETS SEND EVENT TO SLEE...
        int eventID=-1;
        MessageEvent msgEvent = new MessageEventImpl(msg, this, peer, key);

        //LETS RETRIEVE NAME OF EVENT.
        //TREU == Request
        String name=associationMap.get(new EventHandle(true,msg.hdr.command_code));
        logger.info("============ NAME OF EVENT: "+name+" ============");
        //AND FIRE
        fireEventToSLEE(msgEvent,name,DAH);
        
    }

    /**
     * Called by Diameter2SLEERANodeManager class when answer is received.
     */
    public void onAnswer(Message msg, ConnectionKey key, Object state) {
        logger.info("============== onAnswer CALLED ============");
        String name=null;
        MessageEvent msgEvent=null;
        //SHORT BALL - LETS CHECK IF SOME ACTIVITY METHOD HAS SENT THIS MESSAGE, 
        //IF SO, STATE OBJECT SHOULD BE SET TO SOME "DAH"
        logger.info("============ STATE OBJECT IS: "+state+" =================");
        if(state instanceof DiameterRAActivityHandle)
        {
            
//          WE HAVE ACTIVITY LETS SEND EVENT TO SLEE...
            //WE NEED NAME OF IT
            name=associationMap.get(new EventHandle(false,msg.hdr.command_code));
            logger.info("============ NAME OF EVENT: "+name+" ============");
            msgEvent = new MessageEventImpl(msg, this, nodeManager.node().connectionKey2Peer(key), key);
  
            fireEventToSLEE(msgEvent,name,state);
            return;
        }
        
        //SOMETHING ELSE HAS BEEN PASSED AS STATE OBJECT, HMM, PITTY
        AVP sid= msg.find(DI_SESSION_ID);
        if(sid==null)
        {
            //NOT GOOD NO SESSION ID....
            logger.info("========== FOUND NO SESSION ID IN ANSWER!!! ============");
            return;
        }
       DiameterRAActivityHandle DAH=new DiameterRAActivityHandle(new AVP_UTF8String(sid).queryValue());
       
       if(activities.containsKey(DAH))
       {
//         WE HAVE ACTIVITY LETS SEND EVENT TO SLEE...
           //WE NEED NAME OF IT
           name=associationMap.get(new EventHandle(false,msg.hdr.command_code));
           logger.info("============ NAME OF EVENT: "+name+" ============");
           msgEvent = new MessageEventImpl(msg, this, nodeManager.node().connectionKey2Peer(key), key);
 
           fireEventToSLEE(msgEvent,name,DAH);
       }  
       else
       {
           //UU NOT OUR ANSWER?? SOMEONES IS TOYING WITH US
           logger.info("============= GOT STRANGE ANSWER, WE HAVE NO ONGOING ACTIVITY FOR IT(SID:"+new AVP_UTF8String(sid).queryValue()+")!!! =========");
           return;
       }

    }

    /**
     * Reads protperties file: "diameterRA.properties" - should contain diameter
     * node settings.
     * Enumarated settings start with name and are followed by number, from "1" to infinity ( int ),: <br>
     * name1=xx<br>
     * name2=xx<br>
     * name3=xxx<br>
     * But name1, and following name3 are bad example, number3 will not be read...<br>
     * It takes under consideration props with keys as follows:<br>
     * <ul>
     * <li>dk.i1.diameter.node.Capability.AuthApp#
     * <li>dk.i1.diameter.node.Capability.AccApp#
     * <li>dk.i1.diameter.node.Capability.SupportedVendor#
     * <ul>
     * <li>dk.i1.diameter.node.Capability.SupportedVendorAuthApp#
     * <li>dk.i1.diameter.node.Capability.SupportedVendorAccApp#
     * </ul>
     * <li>dk.i1.diameter.node.Capability.HostId
     * <li>dk.i1.diameter.node.Capability.Realm
     * <li>dk.i1.diameter.node.Capability.VendorID
     * <li>dk.i1.diameter.node.Capability.ListenPort
     * <li>dk.i1.diameter.node.Capability.ProductName
     * <li>dk.i1.diameter.node.Capability.FirmWareRevision
     * </ul>
     * @return NodeSetting contained in props.
     * @throws InvalidSettingException
     * @throws IOException
     */
    private NodeSettings readNodeConfiguration(Properties props)
            throws InvalidSettingException, IOException {

        //OUR PEER LiST AND NODE SETTINGS
        Peer peers[] = null;
        NodeSettings node_settings = null;
        
        // TODO: set some local values?
        String host_id = null;
        String realm = null;
        String product = null;
        int port = 3868;
        int firmWare = 0, vendorID = 0;
        boolean error = false;

        // LETS GET SOME PROPS OUT
        String tmp = props.getProperty("dk.i1.diameter.node.Capability.HostId");
        if (tmp == null) {
            logger
                    .info("^^^ PROPERTY \"dk.i1.diameter.node.Capability.HostId\" cant be null. Check  diameterRA.properties^^^");
            error = true;
        } else {
            logger.info("================== SETTING: host_id TO: " + tmp
                    + " ===================");
            host_id = tmp;
        }

        tmp = props.getProperty("dk.i1.diameter.node.Capability.Realm");
        if (tmp == null) {
            logger
                    .info("^^^ PROPERTY \"dk.i1.diameter.node.Capability.Realm\" cant be null. Check  diameterRA.properties^^^");
            error = true;
        } else {
            logger.info("================== SETTING: realm TO: " + tmp
                    + " ===================");
            realm = tmp;
        }

        tmp = props.getProperty("dk.i1.diameter.node.Capability.VendorID");
        if (tmp == null) {
            logger
                    .info("^^^ PROPERTY \"dk.i1.diameter.node.Capability.VendorID\" cant be null. Check  diameterRA.properties^^^");
            error = true;
        } else {
            logger.info("================== SETTING: vendor_id TO: " + tmp
                    + " ===================");
            vendorID = Integer.parseInt(tmp);
        }

        tmp = props
                .getProperty("dk.i1.diameter.node.Capability.FirmWareRevision");
        if (tmp == null) {
            logger
                    .info("^^^ PROPERTY \"dk.i1.diameter.node.Capability.FirmWareRevision\" cant be null. Check  diameterRA.properties^^^");
            error = true;
        } else {
            logger.info("================== SETTING: firmWare TO: " + tmp
                    + " ===================");
            firmWare = Integer.parseInt(tmp);
        }
        tmp = props.getProperty("dk.i1.diameter.node.Capability.ProductName");
        if (tmp == null) {
            logger
                    .info("^^^ PROPERTY \"dk.i1.diameter.node.Capability.ProductName\" cant be null. Check  diameterRA.properties^^^");
            error = true;
        } else {
            logger.info("================== SETTING: product TO: " + tmp
                    + " ===================");
            product = tmp;
        }
        tmp = props.getProperty("dk.i1.diameter.node.Capability.ListenPort");
        if (tmp == null) {
            logger
                    .info("^^^ PROPERTY \"dk.i1.diameter.node.Capability.ListenPort\" can be null. DEFAULT VALUE=3868. Set to 0 if node should not listen to connections.^^^");

        } else {
            logger.info("================== SETTING: port TO: " + tmp
                    + " ===================");
            port = Integer.parseInt(tmp);
        }
        if (error) {
            throw new InvalidSettingException(" ++ Lack of node settings ++");
        }

        // LETS CREATE CApABILITIES FOR OUR NODE
        Capability capability = new Capability();
        String authApp = null;
        String accApp = null;
        String vendorId = null;

        // DIAMETER APPS ??
        String diamAuthAppPrefix = "dk.i1.diameter.node.Capability.AuthApp";
        String diamAccAppPrefix = "dk.i1.diameter.node.Capability.AccApp";

        for (int i = 1;; i++) {
            authApp = (String) props.get(diamAuthAppPrefix + i);
            accApp = (String) props.get(diamAccAppPrefix + i);
            if (authApp != null)
                capability.addAuthApp(Integer.parseInt(authApp));
            if (accApp != null)
                capability.addAcctApp(Integer.parseInt(accApp));
            // TODO: ?? Or just one could be null to break this loop ??
            if (accApp == null && authApp == null) {
                logger.info("========== SUPPORTED GENERIC APPS ADDED: "
                        + (i - 1) + " ==========");
                break;
            }

            logger.info("======================= ADDED AUTH APP: " + authApp
                    + "; ACC APP: " + accApp + "; ==================");
        }

        String vendorPrefix = "dk.i1.diameter.node.Capability.SupportedVendor";
        String supVendorAuthAppPrefix = "dk.i1.diameter.node.Capability.SupportedVendorAuthApp";
        String supVendorAccAppPrefix = "dk.i1.diameter.node.Capability.SupportedVendorAccApp";
        int suppVendor = 0;
        for (int i = 1;; i++) {
            vendorId = (String) props.get(vendorPrefix + i);
            if (vendorId == null) {
                logger.info("========== SUPPORTED VENDORS ADDED: " + (i - 1)
                        + " ==========");
                break;
            }
            suppVendor = Integer.parseInt(vendorId);
            capability.addSupportedVendor(suppVendor);
            authApp = (String) props.get(supVendorAuthAppPrefix + i);
            accApp = (String) props.get(supVendorAccAppPrefix + i);

            // TODO: IS THIS CONDITION ACCEPTABLE?
            if (authApp != null )  {
                capability.addVendorAuthApp(suppVendor, Integer
                        .parseInt(authApp));
            }
            if(accApp != null)
            {
                capability.addVendorAcctApp(suppVendor, Integer
                        .parseInt(accApp));
            }
            logger.info("======================= SUPP VENDOR: " + suppVendor
                    + "; AUTH APP: " + authApp + "; ACC APP: " + accApp
                    + "; ==================");
        }

        // TODO: CHECK THIS - CAN WE ASSUME THAT ??
        //capability.addAuthApp(ProtocolConstants.DIAMETER_APPLICATION_COMMON);
        //capability.addAcctApp(ProtocolConstants.DIAMETER_APPLICATION_COMMON);
        // LETS CREATE NODE STTINGS
        try {
            node_settings = new NodeSettings(host_id, realm, vendorID,
                    capability, port, product, firmWare);
            
        } catch (InvalidSettingException e) {
            logger
                    .error("============ COULD NOT CREATE NODE SETTINGS ================");
            throw e;
        }


        logger.info("============ SETTINGS: host: " + node_settings.hostId()
                + "; realm: " + node_settings.realm() + "; port: "
                + node_settings.port() + "; product: "
                + node_settings.productName() + "; firmWare: "
                + node_settings.firmwareRevision() + "; vendor: "
                + node_settings.vendorId()
                + " ====================================");

        return node_settings;

    }
    /**
     * Reads peers from passed properties.<br>
     * Peer is stored under key: <b>dk.i1.diameter.node.Capability.PeerConnection#</b>, where "<b>#</b>" number from 1- inf with no gaps like 1,3,4,... .
     * @param props
     * @return
     */
    private Peer[] getPeers(Properties props) {
        Peer[] peers = null;
        // LETS READ SOME PEERS WE WILL CONNECT TO
        ArrayList<Peer> lst = new ArrayList<Peer>();
        String peerPrefix = "dk.i1.diameter.node.Capability.PeerConnection";
        String peer = null;

        for (int i = 1;; i++) {
            peer = (String) props.get(peerPrefix + i);
            if (peer == null)
                break;
            logger
                    .info("================ ADDING "
                            + peer
                            + " TO PEERS LIST, WILL TRY TO CONNECT IN PERSISTENT MODE ==================");
            if (peer.contains(":")) {
                String tmpStr[] = peer.split(":");
                try {
                    lst.add(new Peer(tmpStr[0], Integer.parseInt(tmpStr[1])));
                } catch (NumberFormatException e) {
                    logger
                            .error("^^^ Wrong port format: " + tmpStr[1]
                                    + " ^^^");
                    e.printStackTrace();
                } catch (EmptyHostNameException e) {
                    logger.error("^^^ Wrong host name format: " + tmpStr[0]
                            + " ^^^");
                    e.printStackTrace();
                }
                continue;
            }
            try {
                lst.add(new Peer(peer));
            } catch (EmptyHostNameException e) {
                logger.error("^^^ Wrong host name format: " + peer + " ^^^");
                e.printStackTrace();
            }

        }
        peers = new Peer[lst.size()];
        // ECLIPSE has funny way of dealing with that kind of code....Why it has
        // to be cast, have no idea, under NB it deosnt...
        peers = (Peer[]) lst.toArray(peers);
        return peers;
    }

    /*
    private List<Integer> getServerRequestsCodes() {
        Integer[] ret = { ProtocolConstants.DIAMETER_COMMAND_ABORT_SESSION,
                ProtocolConstants.DIAMETER_COMMAND_REAUTH };

        return Arrays.asList(ret);
    }*/
    private void createEvents2NamesAssociationMap() throws IOException
    {
        HashMap<EventHandle,String> associationMap=new HashMap<EventHandle,String>();
        Properties props=new Properties();
        try {
            props.load(getClass().getResourceAsStream(
                    "MessagesMap.properties"));
        } catch (IOException IOE) {
            logger
                    .error("^^^^   FAILED TO LOAD: MessagesMap.properties ^^^^^");
            throw IOE;
        }
        Iterator it=props.keySet().iterator();
        while(it.hasNext())
        {
            String key=(String)it.next();
            boolean req=key.endsWith("R");
            //LETS LOOSE LAST LETTER ( A || R )
            int code=Integer.parseInt(key.substring(0,key.length()-1));
            associationMap.put(new EventHandle(req,code),props.getProperty(key));
        }
        this.associationMap=associationMap;
    }
    
    boolean registerActivity(DiameterRAActivityHandle DAH, Object activity) {
        if (activities.containsKey(DAH))
            return false;
        activities.put(DAH, activity);
        return true;
    }
    
    protected void fireEventToSLEE(MessageEvent msgEvent,String name, Object state)
    {
        int eventID=-1;
        try {
            //"dk.i1.diameter.Message."+nameOfCommand
            eventID = eventLookup.getEventID(name, "dk.i1.diameter", "1.0");
            
            
            
            if (eventID == -1) {
                // Silently drop the message because this is not a registered event type.
                logger.debug("===== COULDNT FIND EVENT MAPPING CHECK xml/slee-events.xml OR MessagesMap.properties. EVENT NAME:"+name+" ======");
                return;
            }
            
            logger.info("==== FIRING EVENT TO LOCAL SLEE, CMD CODE: "+msgEvent.getMessage().hdr.command_code+" ====");
            //CAN IT BE NULL?
            sleeEndpoint.fireEvent((DiameterRAActivityHandle)state,(Object)msgEvent,eventID,null);
            
        } 
        catch (FacilityException fe) {
            logger.error("===== GOT FACILITY EXCEPTION: ======");
            fe.printStackTrace();
            throw new RuntimeException("<><><> EXCPETION IN DiameterRA.onXX(...) <><><>", fe);
        } 
        catch (UnrecognizedEventException uee) {
            logger.error("==== GOT UnrecognizedEventException ==== ");
            uee.printStackTrace();
            throw new RuntimeException("<><><> EXCPETION IN DiameterRA.onXX(...) <><><>", uee);
        } catch (ActivityIsEndingException aiee) {
            logger.error("==== GOT ActivityIsEndingException ==== ");
            aiee.printStackTrace();
            throw new RuntimeException("<><><> EXCPETION IN DiameterRA.onXX(...) <><><>", aiee);
        } catch (NullPointerException npe) {
            logger.error("==== GOT NullPointerException ==== ");
            npe.printStackTrace();
            throw new RuntimeException("<><><> EXCPETION IN DiameterRA.onXX(...) <><><>", npe);
        } catch (IllegalArgumentException iae) {
            logger.error("==== GOT IllegalArgumentException ==== ");
            iae.printStackTrace();
            throw new RuntimeException("<><><> EXCPETION IN DiameterRA.onXX(...) <><><>", iae);
        } catch (IllegalStateException ise) {
            logger.error("==== GOT IllegalStateException ==== ");
            ise.printStackTrace();
            throw new RuntimeException("<><><> EXCPETION IN DiameterRA.onXX(...) <><><>", ise);
        } catch (UnrecognizedActivityException uae) {
            logger.error("==== GOT UnrecognizedActivityException ==== ");
            uae.printStackTrace();
            throw new RuntimeException("<><><> EXCPETION IN DiameterRA.onXX(...) <><><>", uae);
        }
        
    }

    public Set<Peer> getConnectedPeers() {
        return connectedPeers;
    }

    public Set<Peer> getPeers() {
        return peers;
    }
   
}
