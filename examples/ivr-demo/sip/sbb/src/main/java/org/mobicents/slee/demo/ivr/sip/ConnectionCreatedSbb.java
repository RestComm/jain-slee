/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.slee.demo.ivr.sip;

import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.parms.ConnectionDescriptor;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;
import java.text.ParseException;
import javax.sip.RequestEvent;
import javax.sip.address.Address;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;

import org.mobicents.slee.demo.ivr.media.ConnectionState;

/**
 *
 * @author kulikov
 */
public abstract class ConnectionCreatedSbb extends BaseSbb {

    public void onCreateConnectionResponse(CreateConnectionResponse response, ActivityContextInterface aci) {
        ReturnCode returnCode = response.getReturnCode();
        tracer.info(String.format("CallID = %s, State=%s, Receive ACK request ", getCallID(), getState()));
        switch (returnCode.getValue()) {
            case ReturnCode.TRANSACTION_BEING_EXECUTED :
                //provisional response received, awaitng for final response
                break;
            case ReturnCode.TRANSACTION_EXECUTED_NORMALLY :
                String endpoint = response.getSpecificEndpointIdentifier().getLocalEndpointName();
                
                ActivityContextInterface dialogActivity = getDialogActivity();                
                asSbbActivityContextInterface(dialogActivity).setEndpoint(endpoint);
                
                RequestEvent event = this.asSbbActivityContextInterface(aci).getRequest();
                Request request = event.getRequest();
                
                ConnectionDescriptor descriptor = response.getLocalConnectionDescriptor();
                byte[] content = descriptor.toString().getBytes();
                
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
                
                setState(ConnectionState.WAITING_FOR_ACKNOWLEDGEMENT);
                try {
                    Response ok = messageFactory.createResponse(Response.OK, request, contentType, content);
                    ok.setHeader(contact);
                    event.getServerTransaction().sendResponse(ok);
                    //provider.sendResponse(ok);
                } catch (Exception e) {
                    tracer.info("Can not send SIP response: ", e);
                }
                break;
            default :
                setState(ConnectionState.CONNECTION_FAILED);
                event = this.asSbbActivityContextInterface(aci).getRequest();
                request = event.getRequest();
                try {
                    Response cancel = messageFactory.createResponse(Response.SERVER_INTERNAL_ERROR, request);
                    sipProvider.sendResponse(cancel);
                } catch (Exception e) {
                    tracer.info("Can not send SIP response: ", e);
                }
        }
    }
    
}
