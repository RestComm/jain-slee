/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mobicents.slee.demo.ivr.sip;

import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.message.CreateConnection;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.ModifyConnection;
import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConflictingParameterException;
import jain.protocol.ip.mgcp.message.parms.ConnectionDescriptor;
import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionMode;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import javax.sip.Dialog;
import javax.slee.ActivityContextInterface;

import javax.sip.RequestEvent;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.ChildRelation;
import javax.slee.SbbLocalObject;
import net.java.slee.resource.mgcp.MgcpConnectionActivity;

import net.java.slee.resource.sip.CancelRequestEvent;
import net.java.slee.resource.sip.DialogActivity;
import org.mobicents.slee.demo.ivr.CallConnectedEvent;
import org.mobicents.slee.demo.ivr.media.ConnectionState;

/**
 *
 * @author kulikov
 */
public abstract class SipSbb extends BaseSbb {

    
    private Request request;
    private SbbLocalObject crcxResponseHandler;
    
    public void onInvite(RequestEvent event, ActivityContextInterface aci) {
        request = event.getRequest();
                
        //sending provisional response to the UA which indiactes that initial request 
        //successfuly reach call controller and is going to be handled
        try {
            Response response = messageFactory.createResponse(Response.TRYING, request);
            event.getServerTransaction().sendResponse(response);
        } catch (Exception e) {
            //Can not send provisional response? Forget about this request.
            return;
        }
        
        //Provisional response sent so possible to obtain SIP Dialog activity and attach this
        //SBB to the Dialog activity. Dialog activity can be used to maintain current state too.
        ActivityContextInterface callActivity = null;
        try {
            Dialog dialog = sipProvider.getNewDialog(event.getServerTransaction());
            dialog.terminateOnBye(false);
            callActivity = acif.getActivityContextInterface((DialogActivity) dialog);
            callActivity.attach(sbbContext.getSbbLocalObject());
        } catch (Exception e) {            
            //oops, this is unexpected core problem. there is only one way - terminate call
            tracer.severe("Unexpected error", e);
            try {
                Response response = messageFactory.createResponse(Response.SERVER_INTERNAL_ERROR, request);
                sipProvider.sendResponse(response);
            } catch (Exception ex) {
            }
            return;
        }

        //generate call identifier
        callIdentifier = Integer.toHexString(CALL_ID_GEN++);
        this.setCallID(callIdentifier);
        
        setState(ConnectionState.NULL);
        tracer.info("CallID=" + callIdentifier + ", State=" + getState() + ", Receive incoming call");
        
        
        //now we are going to create connection. 
        //preparing MGCP create connection request
        
        CallIdentifier callID = new CallIdentifier(callIdentifier);
        EndpointIdentifier endpointID = new EndpointIdentifier(ENDPOINT_NAME, JBOSS_BIND_ADDRESS + ":2427");

        CreateConnection crcx = new CreateConnection(this, callID, endpointID, ConnectionMode.SendRecv);

        int txID = GEN++;
        crcx.setTransactionHandle(txID);
        
        //SDP present?
        if (event.getRequest().getContent() != null) {
            String sdp = new String(event.getRequest().getRawContent());
            try {
                crcx.setRemoteConnectionDescriptor(new ConnectionDescriptor(sdp));
            } catch (ConflictingParameterException e) {
                //never happen
            }
        }
        

        
        //creating media connection activity context interface
        ActivityContextInterface activityContextInterface = null;
        try {
            MgcpConnectionActivity connectionActivity = mgcpProvider.getConnectionActivity(txID, endpointID);
            activityContextInterface = mgcpAcif.getActivityContextInterface(connectionActivity);
        } catch (Exception ex) {
            setState(ConnectionState.CONNECTION_FAILED);
            tracer.severe(String.format("CallID=%s, State=%, Unexpected internal error", callIdentifier, getState()), ex);
            try {
                Response response = messageFactory.createResponse(Response.SERVER_INTERNAL_ERROR, request);
                sipProvider.sendResponse(response);
            } catch (Exception e) {
            }
            return;
        } 

        asSbbActivityContextInterface(activityContextInterface).setRequest(event);
        //attaching sbb to the connection activity
        activityContextInterface.attach(sbbContext.getSbbLocalObject());

        //update response handler and state
        crcxResponseHandler = this.getSbbLocalObject("ConnectionCreated");        
        setState(ConnectionState.CREATING_CONNECTION);
        
        //sending create connection request 
        tracer.info(String.format("CallID = %s, State=%s, Sending request to %s",callIdentifier, getState(), endpointID));
        mgcpProvider.sendMgcpEvents(new JainMgcpEvent[]{crcx}); 
    }

    public void onCreateConnectionResponse(CreateConnectionResponse response, ActivityContextInterface aci) {
        tracer.info(String.format("CallID = %s, State=%s, Receive CRCX response",getCallID(), getState()));
        System.out.println("handler=" + crcxResponseHandler);
        attach(crcxResponseHandler);
    }
    
    public void onCallCanceled(CancelRequestEvent event, ActivityContextInterface aci) {
        tracer.info(String.format("CallID = %s, State=%s, Receive Cancel request ", getCallID(), getState()));
        setState(ConnectionState.CALL_CANCELED);
        
        crcxResponseHandler = this.getSbbLocalObject("ConnectionDeleted");
        tracer.info(String.format("CallID = %s, State=%s", getCallID(), getState()));
    }
    
    public void onAck(RequestEvent event, ActivityContextInterface aci) {
        tracer.info(String.format("CallID = %s, State=%s, Receive ACK request ", getCallID(), getState()));

        request = event.getRequest();
        
        //SDP present? Update connection
        if (request.getContent() == null) {
            setState(ConnectionState.CONNECTION_CONNECTED);
            tracer.info(String.format("CallID = %s, State=%s", getCallID(), getState()));
            
            CallConnectedEvent evt = new CallConnectedEvent("79023629581", this.getEndpointID(), this.getConnectionID());
            Address address = new Address(AddressPlan.UNDEFINED, "IVR");
            this.fireCallConnected(evt, this.getConnectionActivity(), address);
        } else {
            setState(ConnectionState.MODIFYING_CONNECTION);
            tracer.info(String.format("CallID = %s, State=%s, Sending Modify request", getCallID(), getState()));
            
            String sdp = new String(event.getRequest().getRawContent());
            
            CallIdentifier callID = new CallIdentifier(callIdentifier);
            ConnectionIdentifier connectionID = new ConnectionIdentifier(this.getConnectionID());
            EndpointIdentifier endpointID = new EndpointIdentifier(this.getEndpointID(), JBOSS_BIND_ADDRESS + ":2427");
            
            ModifyConnection mdcx = new ModifyConnection(mgcpProvider, callID, endpointID, connectionID);
            mdcx.setRemoteConnectionDescriptor(new ConnectionDescriptor(sdp));
            
            SbbLocalObject handler = this.getSbbLocalObject("ModifiedConnection");
            attach(handler);
            
            mgcpProvider.sendMgcpEvents(new JainMgcpEvent[]{mdcx});            
        }
    }
    
    public void onBye(RequestEvent event, ActivityContextInterface aci) {
        SbbLocalObject handler = this.getSbbLocalObject("ConnectionDeleted");
        attach(handler);
    }  

    private void attach(SbbLocalObject sbbLocalObject) {
        this.getDialogActivity().attach(sbbLocalObject);
        this.getConnectionActivity().attach(sbbLocalObject);
    }
    
    
    private SbbLocalObject getSbbLocalObject(String name) {
        ChildRelation relation = null;
        if (name.equals("ConnectionCreated")) {
            relation = this.getConnectionCreatedSbb();
        } else if (name.equals("ModifiedConnection")) {
            relation = this.getConnectionModifiedSbb();
        } else if (name.equals("ConnectionDeleted")) {
            relation = this.getConnectionDeletedSbb();
        }
        try {
            return relation.create();
        } catch (Exception e) {
            return null;
        }
    }
    
    
        
    public abstract ChildRelation getConnectionCreatedSbb();
    public abstract ChildRelation getConnectionDeletedSbb();
    public abstract ChildRelation getConnectionModifiedSbb();
    
    public abstract void fireCallConnected(CallConnectedEvent event, ActivityContextInterface aci, javax.slee.Address address);
    
}
