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
package org.mobicents.examples.media;

import java.text.ParseException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipProvider;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.ChildRelation;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import org.apache.log4j.Logger;
import org.mobicents.mscontrol.MsConnection;
import org.mobicents.mscontrol.MsConnectionEvent;
import org.mobicents.mscontrol.MsProvider;
import org.mobicents.mscontrol.MsSession;
import org.mobicents.slee.resource.media.ratype.MediaRaActivityContextInterfaceFactory;
import org.mobicents.slee.resource.sip.SipActivityContextInterfaceFactory;
import org.mobicents.slee.resource.sip.SipResourceAdaptorSbbInterface;

/**
 *
 * @author Oleg Kulikov
 */
public abstract class CallSbb implements Sbb {

    public final static String ENDPOINT_NAME = "media/trunk/PacketRelay/$";
    public final static String LOOP_DEMO = "1010";
    public final static String DTMF_DEMO = "1011";
    public final static String CONF_DEMO = "1012";
    public final static String RECORDER_DEMO = "1013";
    private SbbContext sbbContext;
    private SipResourceAdaptorSbbInterface fp;
    private SipProvider sipProvider;
    private AddressFactory addressFactory;
    private HeaderFactory headerFactory;
    private MessageFactory messageFactory;
    private SipActivityContextInterfaceFactory acif;
    //Media server
    private MsProvider msProvider;
    private MediaRaActivityContextInterfaceFactory msActivityFactory;
    private Logger logger = Logger.getLogger(CallSbb.class);

    /** Creates a new instance of CallSbb */
    public CallSbb() {
    }

    public void onCallCreated(RequestEvent evt, ActivityContextInterface aci) {
        Request request = evt.getRequest();
        

        CallIdHeader callID = (CallIdHeader) request.getHeader(CallIdHeader.NAME);
        FromHeader from = (FromHeader) request.getHeader(FromHeader.NAME);
        ToHeader to = (ToHeader) request.getHeader(ToHeader.NAME);

        logger.info("Incoming call " + from + " " + to.getName());
        
        String destination =  to.toString();
		if ((destination.indexOf(LOOP_DEMO) > 0) || (destination.indexOf(DTMF_DEMO) > 0) || 
                (destination.indexOf(CONF_DEMO) >0) || (destination.indexOf(RECORDER_DEMO) >0)){
			respond(evt, Response.TRYING);
		}
		else{
			logger.info("MMS Demo can understand only "+LOOP_DEMO+", "+DTMF_DEMO+" and "+CONF_DEMO+" dialed numbers");
			return;
		}
        this.setDestination(destination);

        //create Dialog and attach SBB to the Dialog Activity
        ActivityContextInterface daci = null;
        try {
            Dialog dialog = sipProvider.getNewDialog(evt.getServerTransaction());
            dialog.terminateOnBye(true);
            daci = acif.getActivityContextInterface(dialog);
            daci.attach(sbbContext.getSbbLocalObject());
        } catch (Exception e) {
            logger.error("Error during dialog creation", e);
            respond(evt, Response.SERVER_INTERNAL_ERROR);
            return;
        }

        respond(evt, Response.RINGING);
        
        MsSession session = msProvider.createSession();
        MsConnection msConnection = session.createNetworkConnection(ENDPOINT_NAME);

        ActivityContextInterface msAci = null;
        try {
            msAci = msActivityFactory.getActivityContextInterface(msConnection);
            msAci.attach(sbbContext.getSbbLocalObject());
        } catch (Exception ex) {
            logger.error("Internal server error", ex);
            respond(evt, Response.SERVER_INTERNAL_ERROR);
            return;
        }

        logger.info("Creating RTP connection [" + ENDPOINT_NAME + "]");
        String sdp = new String(request.getRawContent());
        msConnection.modify("$", sdp);
    }

    public void onConnectionCreated(MsConnectionEvent evt, ActivityContextInterface aci) {
        MsConnection connection = evt.getConnection();
        logger.info("Created RTP connection [" + connection.getEndpoint() + "]");

        MsConnection msConnection = evt.getConnection();
        String sdp = msConnection.getLocalDescriptor();

        ServerTransaction txn = getServerTransaction();
        if (txn == null) {
            logger.error("SIP activity lost, close RTP connection");
            msConnection.release();
            return;
        }

        Request request = txn.getRequest();

        ContentTypeHeader contentType = null;
        try {
            contentType = headerFactory.createContentTypeHeader("application", "sdp");
        } catch (ParseException ex) {
        }

        String localAddress = sipProvider.getListeningPoints()[0].getIPAddress();
        int localPort = sipProvider.getListeningPoints()[0].getPort();

        Address contactAddress = null;
        try {
            contactAddress = addressFactory.createAddress("sip:" + localAddress + ":" + localPort);
        } catch (ParseException ex) {
        }
        ContactHeader contact = headerFactory.createContactHeader(contactAddress);

        Response response = null;
        try {
            response = messageFactory.createResponse(Response.OK, request, contentType, sdp.getBytes());
        } catch (ParseException ex) {
        }

        response.setHeader(contact);
        try {
            txn.sendResponse(response);
        } catch (InvalidArgumentException ex) {
        } catch (SipException ex) {
        }

        Demo demo = null;
        try {
            demo = getDemo(getDestination());
        } catch (CreateException e) {
            logger.error("Unexpected error", e);
            return;
        }

        if (demo == null) {
            logger.info("Unknown destination:");
            return;
        }

        aci.attach(demo);
        demo.startDemo(evt.getConnection().getEndpoint());
    }

    public void onCallTerminated(RequestEvent evt, ActivityContextInterface aci) {
        logger.info("---- BYE-----");
        
        MsConnection connection = this.getMediaConnection();
        if (connection != null) {
            logger.info("Deleting media conection");
            connection.release();
        }
        
        ServerTransaction tx = evt.getServerTransaction();
        Request request = evt.getRequest();
        
        try {
            Response response = messageFactory.createResponse(Response.OK, request);
            tx.sendResponse(response);
        } catch (Exception e) {
        }
    }

    private MsConnection getMediaConnection() {
        ActivityContextInterface[] activities = sbbContext.getActivities();
        for (ActivityContextInterface aci: activities) {
            if (aci.getActivity() instanceof MsConnection) {
                return (MsConnection) aci.getActivity();
            }
        }
        return null;
    }
    
    private void respond(RequestEvent evt, int cause) {
        Request request = evt.getRequest();
        ServerTransaction tx = evt.getServerTransaction();
        try {
            Response response = messageFactory.createResponse(cause, request);
            tx.sendResponse(response);
        } catch (Exception e) {
            logger.warn("Unexpected error: ", e);
        }
    }

    public Demo getDemo(String destination) throws CreateException {
        if (destination.indexOf(LOOP_DEMO) > 0) {
            return (Demo) this.getLoopDemoSbb().create();
        } else if (destination.indexOf(CONF_DEMO) > 0) {
            return (Demo) this.getConfDemoSbb().create();
        } else if (destination.indexOf(DTMF_DEMO) > 0) {
            return (Demo) this.getDtmfDemoSbb().create();
        } else if (destination.indexOf(RECORDER_DEMO) > 0) {
            return (Demo) this.getRecorderDemoSbb().create();
        }
        return null;
    }

    public abstract String getDestination();

    public abstract void setDestination(String destionation);

    public abstract ChildRelation getLoopDemoSbb();
    public abstract ChildRelation getConfDemoSbb();
    public abstract ChildRelation getDtmfDemoSbb();
    public abstract ChildRelation getRecorderDemoSbb();

    private ServerTransaction getServerTransaction() {
        ActivityContextInterface[] activities = sbbContext.getActivities();
        for (ActivityContextInterface activity: activities) {
            if (activity.getActivity() instanceof ServerTransaction) {
                return (ServerTransaction) activity.getActivity();
            }
        }
        return null;
    }
    
    public void setSbbContext(SbbContext sbbContext) {
        this.sbbContext = sbbContext;
        try {
            Context ctx = (Context) new InitialContext().lookup("java:comp/env");

            //initialize SIP API
            fp = (SipResourceAdaptorSbbInterface) ctx.lookup("slee/resources/jainsip/1.2/provider");
            sipProvider = fp.getSipProvider();
            addressFactory = fp.getAddressFactory();
            headerFactory = fp.getHeaderFactory();
            messageFactory = fp.getMessageFactory();
            acif = (SipActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainsip/1.2/acifactory");

            //initialize media api
            msProvider = (MsProvider) ctx.lookup("slee/resources/media/1.0/provider");
            msActivityFactory = (MediaRaActivityContextInterfaceFactory) ctx.lookup("slee/resources/media/1.0/acifactory");
        } catch (Exception ne) {
            logger.error("Could not set SBB context:", ne);
        }
    }

    public void unsetSbbContext() {
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
