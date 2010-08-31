/*
 * EchoSbb.java
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
import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionDescriptor;
import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionMode;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;
import java.text.ParseException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
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
import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

/**
 * 
 * @author Oleg Kulikov
 * @author amit bhayani
 */
public abstract class EchoSbb implements Sbb {

    public final static String JBOSS_BIND_ADDRESS = System.getProperty("jboss.bind.address", "127.0.0.1");
    public final static String ENDPOINT_NAME = "/mobicents/media/echo/$";
    private SbbContext sbbContext;    
    
    // SIP
    private SleeSipProvider provider;
    private AddressFactory addressFactory;
    private HeaderFactory headerFactory;
    private MessageFactory messageFactory;
    private SipActivityContextInterfaceFactory acif;
    private Tracer logger;
    
    // MGCP
    private JainMgcpProvider mgcpProvider;
    private MgcpActivityContextInterfaceFactory mgcpAcif;
    public static final int MGCP_PEER_PORT = 2427;
    public static final int MGCP_PORT = 2727;

    /** Creates a new instance of CallSbb */
    public EchoSbb() {
    }

    public abstract String getCallIdentifier();
    public abstract void setCallIdentifier(String CallIdentifier);

    public abstract String getEndpointName();
    public abstract void setEndpointName(String endpointName);

    public abstract String getConnectionIdentifier();
    public abstract void setConnectionIdentifier(String connectionIdentifier);

    public void onInvite(RequestEvent evt, ActivityContextInterface aci) {
        Request request = evt.getRequest();
        respond(evt, Response.TRYING);

        FromHeader from = (FromHeader) request.getHeader(FromHeader.NAME);
        ToHeader to = (ToHeader) request.getHeader(ToHeader.NAME);

        logger.info("Incoming call " + from + " " + to);

        // create Dialog and attach SBB to the Dialog Activity
        ActivityContextInterface daci = null;
        try {
            Dialog dialog = provider.getNewDialog(evt.getServerTransaction());
            dialog.terminateOnBye(true);
            daci = acif.getActivityContextInterface((DialogActivity) dialog);
            daci.attach(sbbContext.getSbbLocalObject());
        } catch (Exception e) {
            logger.severe("Error during dialog creation", e);
            respond(evt, Response.SERVER_INTERNAL_ERROR);
            return;
        }

        //sending ringing event back to UA
        respond(evt, Response.RINGING);

        //creating media connection
        //we are expecting SDP in received invite
        String sdp = new String(evt.getRequest().getRawContent());
        ConnectionDescriptor descriptor = new ConnectionDescriptor(sdp);

        //generate call identifier
        CallIdentifier callID = mgcpProvider.getUniqueCallIdentifier();
        setCallIdentifier(callID.toString());

        //selecting echo endpoint
        EndpointIdentifier endpointID = new EndpointIdentifier(ENDPOINT_NAME, JBOSS_BIND_ADDRESS + ":" + MGCP_PEER_PORT);

        //Create new CRCX for Echo Endpoint
        CreateConnection createConnection = new CreateConnection(this, callID, endpointID, ConnectionMode.SendRecv);
        
        //assign unique Transaction
        int txID = mgcpProvider.getUniqueTransactionHandler();
        createConnection.setTransactionHandle(txID);
        try {
            createConnection.setRemoteConnectionDescriptor(descriptor);
        } catch (Exception e) {
        	e.printStackTrace();
        }


        //Create Connection is ready. Sending and awaiting response on connection activity
        MgcpConnectionActivity connectionActivity = null;
        try {
        	//Create a new Connection Activity
            connectionActivity = mgcpProvider.getConnectionActivity(txID, endpointID);
            
            //Get ACI for newly created Connection Activity
            ActivityContextInterface epnAci = mgcpAcif.getActivityContextInterface(connectionActivity);
            
            //Attach SBB to ACI
            epnAci.attach(sbbContext.getSbbLocalObject());
        } catch (FactoryException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (UnrecognizedActivityException ex) {
            ex.printStackTrace();
        }

        //Send CRCX to MGW
        mgcpProvider.sendMgcpEvents(new JainMgcpEvent[]{createConnection});
    }

    public void onCreateConnectionResponse(CreateConnectionResponse event, ActivityContextInterface aci) {
        logger.info("Receive CRCX response: " + event.getTransactionHandle());

        ServerTransaction txn = getServerTransaction();
        if (txn == null) {
            logger.info("SIP activity lost:");
            
            //TODO : Clean the media server resources
            return;
        }
        
        Request request = txn.getRequest();

        ReturnCode status = event.getReturnCode();
        switch (status.getValue()) {
            case ReturnCode.TRANSACTION_EXECUTED_NORMALLY:

                this.setEndpointName(event.getSpecificEndpointIdentifier().getLocalEndpointName());
                logger.info("Received CRCX Response. Endpoint = " + this.getEndpointName());

                ConnectionIdentifier connectionIdentifier = event.getConnectionIdentifier();

                this.setConnectionIdentifier(connectionIdentifier.toString());
                String sdp = event.getLocalConnectionDescriptor().toString();

                ContentTypeHeader contentType = null;
                try {
                    contentType = headerFactory.createContentTypeHeader("application", "sdp");
                } catch (ParseException ex) {
                	ex.printStackTrace();
                }

                String localAddress = provider.getListeningPoints()[0].getIPAddress();
                int localPort = provider.getListeningPoints()[0].getPort();

                Address contactAddress = null;
                try {
                    contactAddress = addressFactory.createAddress("sip:" + localAddress + ":" + localPort);
                } catch (ParseException ex) {
                	ex.printStackTrace();
                }
                ContactHeader contact = headerFactory.createContactHeader(contactAddress);

                Response response = null;
                try {
                    response = messageFactory.createResponse(Response.OK, request, contentType, sdp.getBytes());
                } catch (ParseException ex) {
                    logger.severe("ParseException while trying to create OK Response", ex);
                }

                response.setHeader(contact);
                try {
                    txn.sendResponse(response);
                } catch (InvalidArgumentException ex) {
                    logger.severe("InvalidArgumentException while trying to send OK Response", ex);
                } catch (SipException ex) {
                    logger.severe("SipException while trying to send OK Response", ex);
                }
                break;
            default:
                try {
                    response = messageFactory.createResponse(Response.SERVER_INTERNAL_ERROR, request);
                    txn.sendResponse(response);
                } catch (Exception ex) {
                    logger.severe("Exception while trying to send SERVER_INTERNAL_ERROR Response", ex);
                }
        }
    }

    public void onCallTerminated(RequestEvent evt, ActivityContextInterface aci) {
        EndpointIdentifier endpointID = new EndpointIdentifier(this.getEndpointName(), JBOSS_BIND_ADDRESS + ":" + MGCP_PEER_PORT);
        DeleteConnection deleteConnection = new DeleteConnection(this, endpointID);

        deleteConnection.setTransactionHandle(mgcpProvider.getUniqueTransactionHandler());
        mgcpProvider.sendMgcpEvents(new JainMgcpEvent[]{deleteConnection});

        ServerTransaction tx = evt.getServerTransaction();
        Request request = evt.getRequest();

        try {
            Response response = messageFactory.createResponse(Response.OK, request);
            tx.sendResponse(response);
        } catch (Exception e) {
            logger.severe("Error while sending DLCX ", e);
        }
    }

    private void respond(RequestEvent evt, int cause) {
        Request request = evt.getRequest();
        ServerTransaction tx = evt.getServerTransaction();
        try {
            Response response = messageFactory.createResponse(cause, request);
            tx.sendResponse(response);
        } catch (Exception e) {
            logger.warning("Unexpected error: ", e);
        }
    }

    private ServerTransaction getServerTransaction() {
        ActivityContextInterface[] activities = sbbContext.getActivities();
        for (ActivityContextInterface activity : activities) {
            if (activity.getActivity() instanceof ServerTransaction) {
                return (ServerTransaction) activity.getActivity();
            }
        }
        return null;
    }

    public void setSbbContext(SbbContext sbbContext) {
        this.sbbContext = sbbContext;
        this.logger = sbbContext.getTracer(EchoSbb.class.getSimpleName());

        try {
            Context ctx = (Context) new InitialContext().lookup("java:comp/env");

            // initialize SIP API
            provider = (SleeSipProvider) ctx.lookup("slee/resources/jainsip/1.2/provider");

            addressFactory = provider.getAddressFactory();
            headerFactory = provider.getHeaderFactory();
            messageFactory = provider.getMessageFactory();
            acif = (SipActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainsip/1.2/acifactory");

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
