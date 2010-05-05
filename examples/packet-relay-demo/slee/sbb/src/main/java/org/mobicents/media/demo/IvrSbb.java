/*
 * CallSbb.java
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */
package org.mobicents.media.demo;

import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.message.CreateConnection;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.DeleteConnection;
import jain.protocol.ip.mgcp.message.NotificationRequest;
import jain.protocol.ip.mgcp.message.NotificationRequestResponse;
import jain.protocol.ip.mgcp.message.Notify;
import jain.protocol.ip.mgcp.message.NotifyResponse;
import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionMode;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.EventName;
import jain.protocol.ip.mgcp.message.parms.NotifiedEntity;
import jain.protocol.ip.mgcp.message.parms.RequestedAction;
import jain.protocol.ip.mgcp.message.parms.RequestedEvent;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;
import jain.protocol.ip.mgcp.pkg.MgcpEvent;
import jain.protocol.ip.mgcp.pkg.PackageName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sip.RequestEvent;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.FactoryException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.Tracer;

import net.java.slee.resource.mgcp.JainMgcpProvider;
import net.java.slee.resource.mgcp.MgcpActivityContextInterfaceFactory;
import net.java.slee.resource.mgcp.MgcpConnectionActivity;
import net.java.slee.resource.mgcp.MgcpEndpointActivity;

/**
 * This SBB just acts as decision maker. For 1010 the INVITE event is routed to CRCXSbb
 * 
 * @author Oleg Kulikov
 * @author amit bhayani
 */
public abstract class IvrSbb implements Sbb {

    public final static String JBOSS_BIND_ADDRESS = System.getProperty("jboss.bind.address", "127.0.0.1");
    public final static String ENDPOINT_NAME = "/mobicents/media/IVR/$";
    
    public final static String WELCOME = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/RQNT-ULAW.wav";
    private final static String DTMF_0 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf0.wav";
    private final static String DTMF_1 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf1.wav";
    private final static String DTMF_2 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf2.wav";
    private final static String DTMF_3 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf3.wav";
    private final static String DTMF_4 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf4.wav";
    private final static String DTMF_5 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf5.wav";
    private final static String DTMF_6 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf6.wav";
    private final static String DTMF_7 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf7.wav";
    private final static String DTMF_8 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf8.wav";
    private final static String DTMF_9 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf9.wav";
    private final static String STAR = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-star.wav";
    private final static String POUND = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-pound.wav";
    private final static String A = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-A.wav";
    private final static String B = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-B.wav";
    private final static String C = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-C.wav";
    private final static String D = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-D.wav";
    private SbbContext sbbContext;
    private Tracer logger;
    // MGCP
    private JainMgcpProvider mgcpProvider;
    private MgcpActivityContextInterfaceFactory mgcpAcif;
    public static final int MGCP_PEER_PORT = 2427;
    public static final int MGCP_PORT = 2727;

    /** Creates a new instance of CallSbb */
    public IvrSbb() {
    }

    public abstract String getCallIdentifier();

    public abstract void setCallIdentifier(String CallIdentifier);

    public abstract String getEndpointName();

    public abstract void setEndpointName(String endpointName);

    public abstract String getRelayName();

    public abstract void setRelayName(String endpointName);
    
    public abstract String getConnectionIdentifier();

    public abstract void setConnectionIdentifier(String connectionIdentifier);

    public void start(CallIdentifier callID, String packetRelay) {
        this.setRelayName(packetRelay);
        setCallIdentifier(callID.toString());

        //selecting echo endpoint
        EndpointIdentifier relay = new EndpointIdentifier(packetRelay, JBOSS_BIND_ADDRESS + ":" + MGCP_PEER_PORT);
        EndpointIdentifier endpointID = new EndpointIdentifier(ENDPOINT_NAME, JBOSS_BIND_ADDRESS + ":" + MGCP_PEER_PORT);

        CreateConnection createConnection = new CreateConnection(this, callID, endpointID, ConnectionMode.SendRecv);

        try {
            createConnection.setSecondEndpointIdentifier(relay);
        } catch (Exception e) {
        }

        int txID = mgcpProvider.getUniqueTransactionHandler();
        createConnection.setTransactionHandle(txID);

        //Create Connection is ready. Sending and awaiting response on connection activity
        MgcpConnectionActivity connectionActivity = null;
        try {
            connectionActivity = mgcpProvider.getConnectionActivity(txID, endpointID);
            ActivityContextInterface epnAci = mgcpAcif.getActivityContextInterface(connectionActivity);
            epnAci.attach(sbbContext.getSbbLocalObject());
        } catch (FactoryException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (UnrecognizedActivityException ex) {
            ex.printStackTrace();
        }

        //sending
        mgcpProvider.sendMgcpEvents(new JainMgcpEvent[]{createConnection});
    }

    public void onCreateConnectionResponse(CreateConnectionResponse event, ActivityContextInterface aci) {
        logger.info("Receive CRCX response: " + event.getTransactionHandle());
        ReturnCode status = event.getReturnCode();
        switch (status.getValue()) {
            case ReturnCode.TRANSACTION_EXECUTED_NORMALLY:

                this.setEndpointName(event.getSpecificEndpointIdentifier().getLocalEndpointName());
                logger.info("***&& " + this.getEndpointName());

                ConnectionIdentifier connectionIdentifier = event.getConnectionIdentifier();

                this.setConnectionIdentifier(connectionIdentifier.toString());
                this.sendRQNT(WELCOME, true);
                
                logger.info("Sending request to " + this.getEndpointName());
                break;
            default:
                logger.warning("Unexpected response ");
        }
    }

    private void sendRQNT(String mediaPath, boolean createActivity) {
        EndpointIdentifier endpointID = new EndpointIdentifier(this.getEndpointName(), JBOSS_BIND_ADDRESS + ":" + MGCP_PEER_PORT);

        NotificationRequest notificationRequest = new NotificationRequest(this, endpointID, mgcpProvider.getUniqueRequestIdentifier());

        ConnectionIdentifier connectionIdentifier = new ConnectionIdentifier(this.getConnectionIdentifier());

        EventName[] signalRequests = {new EventName(PackageName.Announcement, MgcpEvent.ann.withParm(mediaPath), connectionIdentifier)};
        notificationRequest.setSignalRequests(signalRequests);

        RequestedAction[] actions = new RequestedAction[]{RequestedAction.NotifyImmediately};

        RequestedEvent[] requestedEvents = {
            new RequestedEvent(new EventName(PackageName.Announcement, MgcpEvent.oc, connectionIdentifier), actions),
            new RequestedEvent(new EventName(PackageName.Announcement, MgcpEvent.of, connectionIdentifier), actions),
            new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf0, connectionIdentifier), actions),
            new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf1, connectionIdentifier), actions),
            new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf2, connectionIdentifier), actions),
            new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf3, connectionIdentifier), actions),
            new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf4, connectionIdentifier), actions),
            new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf5, connectionIdentifier), actions),
            new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf6, connectionIdentifier), actions),
            new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf7, connectionIdentifier), actions),
            new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf8, connectionIdentifier), actions),
            new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf9, connectionIdentifier), actions),
            new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmfA, connectionIdentifier), actions),
            new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmfB, connectionIdentifier), actions),
            new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmfC, connectionIdentifier), actions),
            new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmfD, connectionIdentifier), actions),
            new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmfStar, connectionIdentifier), actions),
            new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmfHash, connectionIdentifier), actions)
        };

        notificationRequest.setRequestedEvents(requestedEvents);
        notificationRequest.setTransactionHandle(mgcpProvider.getUniqueTransactionHandler());

        NotifiedEntity notifiedEntity = new NotifiedEntity(JBOSS_BIND_ADDRESS, JBOSS_BIND_ADDRESS, MGCP_PORT);
        notificationRequest.setNotifiedEntity(notifiedEntity);

        if (createActivity) {
            MgcpEndpointActivity endpointActivity = null;
            try {
                endpointActivity = mgcpProvider.getEndpointActivity(endpointID);
                ActivityContextInterface epnAci = mgcpAcif.getActivityContextInterface(endpointActivity);
                epnAci.attach(sbbContext.getSbbLocalObject());
            } catch (FactoryException ex) {
                ex.printStackTrace();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            } catch (UnrecognizedActivityException ex) {
                ex.printStackTrace();
            }
        } // if (createActivity)

        mgcpProvider.sendMgcpEvents(new JainMgcpEvent[]{notificationRequest});

        logger.info(" NotificationRequest sent");
    }

    public void onNotificationRequestResponse(NotificationRequestResponse event, ActivityContextInterface aci) {
        logger.info("onNotificationRequestResponse");

        ReturnCode status = event.getReturnCode();

        switch (status.getValue()) {
            case ReturnCode.TRANSACTION_EXECUTED_NORMALLY:
                logger.info("The Announcement should have been started");
                break;
            default:
                ReturnCode rc = event.getReturnCode();
                logger.severe("RQNT failed. Value = " + rc.getValue() + " Comment = " + rc.getComment());

                // TODO : Send DLCX to MMS. Send BYE to UA
                break;
        }

    }

    public void onNotifyRequest(Notify event, ActivityContextInterface aci) {
        logger.info("onNotifyRequest");

        NotifyResponse response = new NotifyResponse(event.getSource(),
                ReturnCode.Transaction_Executed_Normally);
        response.setTransactionHandle(event.getTransactionHandle());

        mgcpProvider.sendMgcpEvents(new JainMgcpEvent[]{response});

        EventName[] observedEvents = event.getObservedEvents();

        for (EventName observedEvent : observedEvents) {
            switch (observedEvent.getEventIdentifier().intValue()) {
                case MgcpEvent.REPORT_ON_COMPLETION:
                    logger.info("Announcemnet Completed NTFY received");
                    break;
                case MgcpEvent.REPORT_FAILURE:
                    logger.info("Announcemnet Failed received");
                    // TODO : Send DLCX and Send BYE to UA
                    break;
                case MgcpEvent.DTMF_0:
                    logger.info("You have pressed 0");
                    sendRQNT(DTMF_0, false);
                    break;
                case MgcpEvent.DTMF_1:
                    logger.info("You have pressed 1");
                    sendRQNT(DTMF_1, false);
                    break;
                case MgcpEvent.DTMF_2:
                    logger.info("You have pressed 2");
                    sendRQNT(DTMF_2, false);
                    break;
                case MgcpEvent.DTMF_3:
                    logger.info("You have pressed 3");
                    sendRQNT(DTMF_3, false);
                    break;
                case MgcpEvent.DTMF_4:
                    logger.info("You have pressed 4");
                    sendRQNT(DTMF_4, false);
                    break;
                case MgcpEvent.DTMF_5:
                    logger.info("You have pressed 5");
                    sendRQNT(DTMF_5, false);
                    break;
                case MgcpEvent.DTMF_6:
                    logger.info("You have pressed 6");
                    sendRQNT(DTMF_6, false);
                    break;
                case MgcpEvent.DTMF_7:
                    logger.info("You have pressed 7");
                    sendRQNT(DTMF_7, false);
                    break;
                case MgcpEvent.DTMF_8:
                    logger.info("You have pressed 8");
                    sendRQNT(DTMF_8, false);
                    break;
                case MgcpEvent.DTMF_9:
                    logger.info("You have pressed 9");
                    sendRQNT(DTMF_9, false);
                    break;
                case MgcpEvent.DTMF_A:
                    logger.info("You have pressed A");
                    sendRQNT(A, false);
                    break;
                case MgcpEvent.DTMF_B:
                    logger.info("You have pressed B");
                    sendRQNT(B, false);
                    break;
                case MgcpEvent.DTMF_C:
                    logger.info("You have pressed C");
                    sendRQNT(C, false);
                    break;
                case MgcpEvent.DTMF_D:
                    logger.info("You have pressed D");
                    sendRQNT(D, false);

                    break;
                case MgcpEvent.DTMF_STAR:
                    logger.info("You have pressed *");
                    sendRQNT(STAR, false);

                    break;
                case MgcpEvent.DTMF_HASH:
                    logger.info("You have pressed C");
                    sendRQNT(POUND, false);

                    break;
            }
        }
    }

    public void onCallTerminated(RequestEvent evt, ActivityContextInterface aci) {
        logger.info("============ IVR terminated ===========");
        EndpointIdentifier endpointID = new EndpointIdentifier(this.getEndpointName(), JBOSS_BIND_ADDRESS + ":" + MGCP_PEER_PORT);
        DeleteConnection deleteConnection = new DeleteConnection(this, endpointID);

        deleteConnection.setTransactionHandle(mgcpProvider.getUniqueTransactionHandler());
        mgcpProvider.sendMgcpEvents(new JainMgcpEvent[]{deleteConnection});

        endpointID = new EndpointIdentifier(this.getRelayName(), JBOSS_BIND_ADDRESS + ":" + MGCP_PEER_PORT);
        deleteConnection = new DeleteConnection(this, endpointID);

        deleteConnection.setTransactionHandle(mgcpProvider.getUniqueTransactionHandler());
        mgcpProvider.sendMgcpEvents(new JainMgcpEvent[]{deleteConnection});
    }
    
    public void setSbbContext(SbbContext sbbContext) {
        this.sbbContext = sbbContext;
        this.logger = sbbContext.getTracer(IvrSbb.class.getSimpleName());

        try {
            Context ctx = (Context) new InitialContext().lookup("java:comp/env");

            // initialize media api
            mgcpProvider = (JainMgcpProvider) ctx.lookup("slee/resources/jainmgcp/2.0/provider/demo");
            mgcpAcif = (MgcpActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainmgcp/2.0/acifactory/demo");
        } catch (Exception ne) {
            logger.severe("Could not set SBB context:", ne);
        }
    }

    public void unsetSbbContext() {
        this.sbbContext = null;
        this.logger = null;
    }

    public void sbbCreate() throws CreateException {
    }

    public void sbbPostCreate() throws CreateException {
    }

    public void sbbActivate() {
    }

    public void sbbPassivate() {
    }

    public void sbbLoad() {
    }

    public void sbbStore() {
    }

    public void sbbRemove() {
    }

    public void sbbExceptionThrown(Exception exception, Object object, ActivityContextInterface activityContextInterface) {
    }

    public void sbbRolledBack(RolledBackContext rolledBackContext) {
    }
}
